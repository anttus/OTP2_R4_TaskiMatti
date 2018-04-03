package com.example.ryhma4.taskimatti.Controller;

import android.content.Context;
import android.content.res.Resources;
import android.telecom.Call;
import android.util.Log;

import com.example.ryhma4.taskimatti.R;
import com.example.ryhma4.taskimatti.activity.MainActivity;
import com.example.ryhma4.taskimatti.model.Routine;
import com.example.ryhma4.taskimatti.model.Task;
import com.example.ryhma4.taskimatti.model.Type;
import com.example.ryhma4.taskimatti.model.User;
import com.example.ryhma4.taskimatti.utility.CallbackHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by mikae on 5.2.2018.
 * no longer default to get rid of useless warning.
 */

public class Database extends MainActivity {
    private static Database instance = null;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private SimpleDateFormat weekSdf = new SimpleDateFormat("yyyy-w", Locale.getDefault());
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private Database() {
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    public static Database getInstance() {
        if (instance == null) {
            synchronized(Database.class) {
                if (instance == null) {
                    instance = new Database();
                }
            }
        }
        return instance;
    }

    /**
     * Writes a new Routine object to the database
     * @param routine Routine Object
     */
    public void setRoutine(Routine routine) {
        mDatabase.child("routines").child(routine.getRoutineId()).setValue(routine);
        mDatabase.child("users").child(mAuth.getUid()).child("routines").child(routine.getRoutineId()).setValue(true);
    }

    /**
     * Reads a specific Routine object with the passed routineId from the database
     * @param routineId String form UUID of the routine id
     */
    public void getRoutine(String routineId, final CallbackHandler callback) {
        mDatabase.child("routines").child(routineId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Routine routine = dataSnapshot.getValue(Routine.class);
//                Log.w("GR_ROUTINE", routine.getRoutineId());
                callback.passObject((Routine)routine);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("GET_ROUTINE_FAIL", "THIS FAILED");
            }
        });
    }

    /**
     * Returns all the users routines one by one as Routine objects.
     */
    public void getUserRoutines(final CallbackHandler callback) {
        mDatabase.child("users").child(mAuth.getUid()).child("routines/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userRoutineSnapshot: dataSnapshot.getChildren()) {
                    mDatabase.child("routines").child(userRoutineSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot routineSnapshot) {
                            Routine routine = routineSnapshot.getValue(Routine.class);
                            callback.passObject((Routine)routine);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * Updates the passed Routine objects changeable values to the database
     * @param routine Routine object
     */
    public void updateRoutine(Routine routine) {
        DatabaseReference routineRef = mDatabase.child("routines").child(routine.getRoutineId());
        routineRef.child("name").setValue(routine.getName());
        routineRef.child("description").setValue(routine.getDescription());
        routineRef.child("repeat").setValue(routine.getRepeat());
        routineRef.child("type").setValue(routine.getType());
        routineRef.child("hours").setValue(routine.getHours());
        routineRef.child("minutes").setValue(routine.getMinutes());
    }

    /**
     * Removes the routine and tasks related to that routine from the database.
     * @param routineId String form UUID of the routine
     */
    public void removeRoutine(String routineId) {
        final String fRoutineId = routineId;
        //Retrieve taskId's related to the routine.
        mDatabase.child("routines").child(routineId).child("tasks/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Remove tasks from the database and from the users task list.
                for (DataSnapshot taskSnapshot: dataSnapshot.getChildren()) {
                    String taskId = taskSnapshot.getKey();
                    mDatabase.child("users").child(mAuth.getUid()).child("tasks").child(taskId).removeValue();
                    mDatabase.child("tasks").child(taskId).removeValue();
                }
                //Remove the routine from the database and from the users routine list.
                mDatabase.child("users").child(mAuth.getUid()).child("routines").child(fRoutineId).removeValue();
                mDatabase.child("routines").child(fRoutineId).removeValue();
              }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.wtf("removeRoutine", "Failed.");
            }
        });
    }

    /**
     * Lists all the different routine types the user has created.
     */
    public void listTypes(final CallbackHandler callback){
        //Get all the users routines.
        mDatabase.child("users").child(mAuth.getUid()).child("routines/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<Type> types = new ArrayList<>();
                //Iterate through the routines
                for(DataSnapshot routineSnap : dataSnapshot.getChildren()) {
                    mDatabase.child("routines").child(routineSnap.getKey()).child("type").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot){
                            Type type = dataSnapshot.getValue(Type.class);
                            if(types.indexOf(type) < 0) {
                                types.add(type);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    callback.successHandler(types);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    /**
     * Read a specific task from the database.
     * @param taskId String form UUID of the task.
     */
    public void getTask(String taskId, final CallbackHandler callback) {
        mDatabase.child("tasks").child(taskId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Task task = dataSnapshot.getValue(Task.class);
                callback.passObject((Task)task);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    /**
     * Lists all of the users taskId's.
     */
    public void listTaskIds(final CallbackHandler callback) {
        mDatabase.child("users").child(mAuth.getUid()).child("tasks/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> taskIds = new ArrayList<>();
                for(DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                    if(taskSnapshot.child("state").getValue().equals("waiting")) {
                        taskIds.add(taskSnapshot.getKey());
                    }
                }
                callback.successHandler(taskIds);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    /**
     * Lists all of the set taskId's for a given day.
     */
    public void listSetTaskIds(final String date, final CallbackHandler callback) {
        mDatabase.child("users").child(mAuth.getUid()).child("tasks/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> taskIds = new ArrayList<>();
                for(DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                    if(taskSnapshot.child("state").getValue().equals("set") && taskSnapshot.child("date").getValue().equals(date)) {
                        taskIds.add(taskSnapshot.getKey());
                    }
                }
                callback.successHandler(taskIds);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    /**
     * Gets the users tasks that are set to a specific date and time.
     * @param date
     */
    public void getTasksForDay(final String date, final CallbackHandler callback) {
        mDatabase.child("users").child(mAuth.getUid()).child("tasks/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(final DataSnapshot userTaskSnapshot : dataSnapshot.getChildren()) {
                    if(userTaskSnapshot.child("state").getValue().equals("set") && userTaskSnapshot.child("date").getValue().equals(date)) {
                        final String time = (String)userTaskSnapshot.child("time").getValue();
                        mDatabase.child("tasks").child(userTaskSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot taskSnapshot) {
                                Task task = taskSnapshot.getValue(Task.class);
                                task.setTime(time);
                                task.setDate(date);
                                callback.passObject(task);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) { }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    /**
     * Finds waiting tasks that should be activated.
     */
    public void findTasksToActivate() {
        Date day = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.add(Calendar.WEEK_OF_MONTH, 1);
        day = calendar.getTime();
        final String week = weekSdf.format(day);

        mDatabase.child("users").child(mAuth.getUid()).child("tasks/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot userTaskSnapshot : dataSnapshot.getChildren()) {
                    if(userTaskSnapshot.child("state").getValue().equals("waiting") && userTaskSnapshot.child("date").getValue().equals(week)) {
                        setTaskActive(userTaskSnapshot.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * Sets the state of the task to Waiting.
     * @param task the task object to change.
     */
    public void setTaskWaiting(final Task task) {
        mDatabase.child("routines").child(task.getRoutineID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Date currentDate = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                Routine routine = dataSnapshot.getValue(Routine.class);
                if(routine.getRepeat().equals(globalRes.getString(R.string.time_year))) {
                    calendar.add(Calendar.YEAR,1);
                }
                else if(routine.getRepeat().equals(globalRes.getString(R.string.time_month))) {
                    calendar.add(Calendar.WEEK_OF_MONTH, 4);
                }
                else calendar.add(Calendar.WEEK_OF_MONTH, 1);

                currentDate = calendar.getTime();
                String date = weekSdf.format(currentDate);
                mDatabase.child("users").child(mAuth.getUid()).child("tasks").child(task.getTaskID()).child("date").setValue(date);
                mDatabase.child("users").child(mAuth.getUid()).child("tasks").child(task.getTaskID()).child("state").setValue("waiting");
                mDatabase.child("users").child(mAuth.getUid()).child("tasks").child(task.getTaskID()).child("time").setValue("");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    /**
     * Sets the state of the task to Active.
     * @param taskId String form UUID of the task,
     */
    public void setTaskActive(String taskId) {
        mDatabase.child("users").child(mAuth.getUid()).child("tasks").child(taskId).child("state").setValue("active");
        mDatabase.child("users").child(mAuth.getUid()).child("tasks").child(taskId).child("date").setValue("");
        mDatabase.child("users").child(mAuth.getUid()).child("tasks").child(taskId).child("time").setValue("");
    }

    /**
     * Sets the state of the task to Set.
     * @param taskId id of the specific task
     * @param date yyyy-MM-dd formatted date the task is set to.
     * @param time HH:mm formatted time.
     */
    public void setTaskSet(String taskId, String date, String time) {
        mDatabase.child("users").child(mAuth.getUid()).child("tasks").child(taskId).child("state").setValue("set");
        mDatabase.child("users").child(mAuth.getUid()).child("tasks").child(taskId).child("date").setValue(date);
        mDatabase.child("users").child(mAuth.getUid()).child("tasks").child(taskId).child("time").setValue(time);
    }

    /**
     * Write the User object to the database.
     * @param user User object to be written to the database.
     */
    public void setUser(User user) {
        mDatabase.child("users").child(user.getUserID()).setValue(user);
    }

    /**
     * Not implemented yet.
     * @param userId String form UUID of the users Id.
     */
    public void getUser(String userId) {

    }

    /**
     * Not implemented yet.
     * @param userId String form UUID of the users Id.
     */
    public void updateUser(String userId) {

    }

    /**
     * Writes the given Task object to the database.
     * @param tasks ArrayList of Task objects to be written to the database.
     */
    public void setTask(final ArrayList<Task> tasks) {
        String routineId = tasks.get(0).getRoutineID();
        mDatabase.child("routines").child(routineId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Routine routine = dataSnapshot.getValue(Routine.class);
                String repeat = routine.getRepeat();
                int times = routine.getTimes();
                System.out.println(repeat);

                int interval;
                if(repeat.equalsIgnoreCase(globalRes.getString(R.string.time_year))) {
                    interval = 52 / times;
                }
                else if(repeat.equalsIgnoreCase(globalRes.getString(R.string.time_month))) {
                    interval  = 4 / times;
                }
                else interval = 0;

                Date day = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(day);
                String date;

                for(Task task : tasks) {
                    day = calendar.getTime();
                    date = weekSdf.format(day);
                    mDatabase.child("routines").child(task.getRoutineID()).child("tasks").child(task.getTaskID()).setValue(true);
                    mDatabase.child("users").child(mAuth.getUid()).child("tasks").child(task.getTaskID()).child("state").setValue(task.getState());
                    mDatabase.child("users").child(mAuth.getUid()).child("tasks").child(task.getTaskID()).child("date").setValue(date);
                    mDatabase.child("users").child(mAuth.getUid()).child("tasks").child(task.getTaskID()).child("time").setValue(task.getTime());
                    mDatabase.child("tasks").child(task.getTaskID()).setValue(task);
                    calendar.add(Calendar.WEEK_OF_MONTH, interval);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * Checks if user exists, if not, creates a new user to the the database.
     * @param user FirebaseUser, current logged in user.
     */
    public void userExists(final FirebaseUser user) {

        mDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    User newUser = new User(user.getUid(), user.getDisplayName(), user.getEmail());
                    setUser(newUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Lists all of the users routineId's.
     */
    public void listRoutineIds(final CallbackHandler callback) {
        final ArrayList<String> userRoutineIds = new ArrayList<>();
        DatabaseReference routineRef = mDatabase.child("users").child(mAuth.getUid()).child("routines/");
        routineRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String value = userSnapshot.getKey();
//                    Log.d("User id value ", value);
                    userRoutineIds.add(value);
                }
//                Log.d("userRoutineIds ", userRoutineIds.toString());
                callback.successHandler(userRoutineIds);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.errorHandler();
            }
        });
    }
}

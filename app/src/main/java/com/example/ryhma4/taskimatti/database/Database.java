package com.example.ryhma4.taskimatti.database;

import android.util.Log;

import com.example.ryhma4.taskimatti.activity.MainActivity;
import com.example.ryhma4.taskimatti.model.Routine;
import com.example.ryhma4.taskimatti.model.Task;
import com.example.ryhma4.taskimatti.model.Type;
import com.example.ryhma4.taskimatti.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by mikae on 5.2.2018.
 */

public class Database extends MainActivity{
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private CallbackHandler callback;
    private String userID;
    private SimpleDateFormat dateFormat, timeFormat;

    public Database() {
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getUid();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        timeFormat = new SimpleDateFormat("HH:mm");
    }

    /**
     * Constructor to use with callback methods.
     * @param cb object implementing the CallbackHandler interface.
     */
    public Database(CallbackHandler cb) {
        callback = cb;
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getUid();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        timeFormat = new SimpleDateFormat("HH:mm");
    }

    /**
     * Writes a new Routine object to the database
     * @param routine Routine Object
     */
    public void setRoutine(Routine routine) {
        mDatabase.child("routines").child(routine.getRoutineId()).setValue(routine);
        mDatabase.child("users").child(userID).child("routines").child(routine.getRoutineId()).setValue(true);
    }

    /**
     * Reads a specific Routine object with the passed routineId from the database
     * @param routineId String form UUID of the routine id
     */
    public void getRoutine(String routineId) {
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
                    mDatabase.child("users").child(userID).child("tasks").child(taskId).removeValue();
                    mDatabase.child("tasks").child(taskId).removeValue();
                }
                //Remove the routine from the database and from the users routine list.
                mDatabase.child("users").child(userID).child("routines").child(fRoutineId).removeValue();
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
    public void listTypes(){
        //Get all the users routines.
        mDatabase.child("users").child(userID).child("routines/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<Type> types = new ArrayList<>();
                //Iterate through the routines
                for(DataSnapshot routineSnap : dataSnapshot.getChildren()) {
                    mDatabase.child("routines").child(routineSnap.getKey()).child("type").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot){
                            Type type = dataSnapshot.getValue(Type.class);
                            types.add(type);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    //Callback function goes here.
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
    public void getTask(String taskId) {
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
    public void listTaskIds() {
        mDatabase.child("users").child(userID).child("tasks/").addListenerForSingleValueEvent(new ValueEventListener() {
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
     * Sets the state of the task to Waiting.
     * @param taskId String form UUID of the task.
     */
    public void setTaskWaiting(String taskId) {
        Date currentDate = new Date();
        String date = dateFormat.format(currentDate);
        mDatabase.child("users").child(userID).child("tasks").child(taskId).child("state").setValue("waiting");
        mDatabase.child("users").child(userID).child("tasks").child(taskId).child("date").setValue(date);
    }

    /**
     * Sets the state of the task to Active.
     * @param taskId String form UUID of the task,
     */
    public void setTaskActive(String taskId) {
        mDatabase.child("users").child(userID).child("tasks").child(taskId).child("state").setValue("active");
        mDatabase.child("users").child(userID).child("tasks").child(taskId).child("date").removeValue();
        mDatabase.child("users").child(userID).child("tasks").child(taskId).child("time").removeValue();
    }

    /**
     * Sets the state of the task to Set.
     * @param taskId id of the specific task
     * @param date yyyy-MM-dd formatted date the task is set to.
     * @param time HH:mm formatted time.
     */
    public void setTaskSet(String taskId, String date, String time) {
        mDatabase.child("users").child(userID).child("tasks").child(taskId).child("state").setValue("set");
        mDatabase.child("users").child(userID).child("tasks").child(taskId).child("date").setValue(date);
        mDatabase.child("users").child(userID).child("tasks").child(taskId).child("time").setValue(time);
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
     * @return
     */
    public User getUser(String userId) {
        return null;
    }

    /**
     * Not implemented yet.
     * @param userId String form UUID of the users Id.
     */
    public void updateUser(String userId) {

    }

    /**
     * Writes the given Task object to the database.
     * @param task Object to be written to the database.
     */
    public void setTask(Task task) {
        mDatabase.child("routines").child(task.getRoutineID()).child("tasks").child(task.getTaskID()).setValue(true);
        mDatabase.child("users").child(userID).child("tasks").child(task.getTaskID()).child("state").setValue(task.getState());
        mDatabase.child("users").child(userID).child("tasks").child(task.getTaskID()).child("date").setValue(task.getDate());
        mDatabase.child("users").child(userID).child("tasks").child(task.getTaskID()).child("time").setValue(task.getTime());
        mDatabase.child("tasks").child(task.getTaskID()).setValue(task);
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
    public void listRoutineIds() {
        final ArrayList<String> userRoutineIds = new ArrayList<>();
        DatabaseReference routineRef = mDatabase.child("users").child(userID).child("routines/");
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

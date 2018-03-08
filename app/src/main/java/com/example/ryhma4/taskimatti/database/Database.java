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

    public Database(CallbackHandler cb) {
        callback = cb;
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getUid();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        timeFormat = new SimpleDateFormat("HH:mm");
    }

    public void setRoutine(Routine routine) {
        mDatabase.child("routines").child(routine.getRoutineId()).setValue(routine);
        mDatabase.child("users").child(userID).child("routines").child(routine.getRoutineId()).setValue(true);
    }

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

    public void updateRoutine(Routine routine) {
        DatabaseReference routineRef = mDatabase.child("routines").child(routine.getRoutineId());
        routineRef.child("name").setValue(routine.getName());
        routineRef.child("description").setValue(routine.getDescription());
        routineRef.child("repeat").setValue(routine.getRepeat());
        routineRef.child("type").setValue(routine.getType());
        routineRef.child("hours").setValue(routine.getHours());
        routineRef.child("minutes").setValue(routine.getMinutes());
    }

    public void removeRoutine(String routineId) {
        final String fRoutineId = routineId;
        mDatabase.child("routines").child(routineId).child("tasks/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot taskSnapshot: dataSnapshot.getChildren()) {
                    String taskId = taskSnapshot.getKey();
                    mDatabase.child("users").child(userID).child("tasks").child(taskId).removeValue();
                    mDatabase.child("tasks").child(taskId).removeValue();
                }
                mDatabase.child("users").child(userID).child("routines").child(fRoutineId).removeValue();
                mDatabase.child("routines").child(fRoutineId).removeValue();
              }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.wtf("removeRoutine", "Failed.");
            }
        });
    }

    public void listTypes(){
        mDatabase.child("users").child(userID).child("routines/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<Type> types = new ArrayList<>();
                for(DataSnapshot routineSnap : dataSnapshot.getChildren()) {
                    mDatabase.child("routines").child(routineSnap.getKey()).child("type").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
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
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getTask(String taskId) {
        mDatabase.child("tasks").child(taskId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Task task = dataSnapshot.getValue(Task.class);
                callback.passObject((Task)task);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

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
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setTaskWaiting(String taskId) {
        Date currentDate = new Date();
        String date = dateFormat.format(currentDate);
        mDatabase.child("users").child(userID).child("tasks").child(taskId).child("state").setValue("waiting");
        mDatabase.child("users").child(userID).child("tasks").child(taskId).child("date").setValue(date);
    }

    public void setTaskActive(String taskId) {
        mDatabase.child("users").child(userID).child("tasks").child(taskId).child("state").setValue("active");
        mDatabase.child("users").child(userID).child("tasks").child(taskId).child("date").removeValue();
        mDatabase.child("users").child(userID).child("tasks").child(taskId).child("time").removeValue();
    }

    public void setTaskSet(String taskId, String date, String time) {
        mDatabase.child("users").child(userID).child("tasks").child(taskId).child("state").setValue("set");
        mDatabase.child("users").child(userID).child("tasks").child(taskId).child("date").setValue(date);
        mDatabase.child("users").child(userID).child("tasks").child(taskId).child("time").setValue(time);
    }

    public void setUser(User user) {
        mDatabase.child("users").child(user.getUserID()).setValue(user);
    }

    public User getUser(String userID) {
        return null;
    }

    public void updateUser(String userID) {

    }

    public void setTask(Task task) {
        mDatabase.child("routines").child(task.getRoutineID()).child("tasks").child(task.getTaskID()).setValue(true);
        mDatabase.child("users").child(userID).child("tasks").child(task.getTaskID()).child("state").setValue(task.getState());
        mDatabase.child("users").child(userID).child("tasks").child(task.getTaskID()).child("date").setValue(task.getDate());
        mDatabase.child("users").child(userID).child("tasks").child(task.getTaskID()).child("time").setValue(task.getTime());
        mDatabase.child("tasks").child(task.getTaskID()).setValue(task);
    }

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

    public void listRoutineIds() {
        final ArrayList<String> userRoutineIds = new ArrayList<>();
        DatabaseReference routineRef = mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("routines/");
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

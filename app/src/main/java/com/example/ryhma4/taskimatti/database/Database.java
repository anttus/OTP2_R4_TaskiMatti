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

import java.util.ArrayList;


/**
 * Created by mikae on 5.2.2018.
 */

public class Database extends MainActivity{
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private CallbackHandler callback;
    private String userID;

    public Database() {
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getUid();
    }

    public Database(CallbackHandler cb) {
        callback = cb;
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        mAuth = FirebaseAuth.getInstance();
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
                callback.passRoutine(routine);
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

    // UNCLEAR IF NEEDED
    public void getType(String routineId){
        mDatabase.child("routines").child(routineId).child("type").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Type type = dataSnapshot.getValue(Type.class);
                // TODO callback for returning type.
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.wtf("getType", "Retrieving type failed");
            }
        });
    }

    public void getTask(String taskId) {
        mDatabase.child("tasks").child(taskId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Task task = dataSnapshot.getValue(Task.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        mDatabase.child("users").child(userID).child("tasks").child(task.getTaskID()).child("setTo").setValue(task.getSetTo());
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

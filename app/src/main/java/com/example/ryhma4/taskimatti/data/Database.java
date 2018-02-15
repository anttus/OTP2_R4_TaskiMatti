package com.example.ryhma4.taskimatti.data;

import com.example.ryhma4.taskimatti.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


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
        mDatabase.child("routines").child(routine.getID()).setValue(routine);
        mDatabase.child("users").child(userID).child("routines").child(routine.getID()).setValue(true);
    }

    public Routine getRoutine(String ID) {
        DatabaseReference routineRef = database.getReference("routines/" + ID);
        routineRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               Routine routine = dataSnapshot.getValue(Routine.class);
               callback.passRoutine(routine);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.errorHandler();
            }
        });
        return null;
    }

    public void removeRoutine(String routineID) {
        mDatabase.child("routines").child(routineID).removeValue();
        mDatabase.child("users").child(userID).child("routines").child(routineID).removeValue();
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

}

package com.example.ryhma4.taskimatti.data;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by mikae on 5.2.2018.
 */

public class Database {
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;

    public Database() {
    database = FirebaseDatabase.getInstance();
    mDatabase = database.getReference();

    }

    public void setRoutine( Routine routine) {
        mDatabase.child("routines").child(routine.getID()).setValue(routine);
    }

    public Routine getRoutine(String ID) {
        DatabaseReference routineRef = database.getReference("routines/" + ID);

        ValueEventListener routineLister = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               Routine routine = dataSnapshot.getValue(Routine.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        routineRef.addListenerForSingleValueEvent(routineLister);
        return null;
    }

    public void setUser(User user) {
        mDatabase.child("users").child(user.getUserID()).setValue(user);
    }

    public User getUser(String userID) {
        return null;
    }

    public void updateUser(String userID) {

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

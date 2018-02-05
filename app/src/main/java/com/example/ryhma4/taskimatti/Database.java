package com.example.ryhma4.taskimatti;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


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
        return null;
    }




}

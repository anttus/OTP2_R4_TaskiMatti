package com.example.ryhma4.taskimatti.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.ryhma4.taskimatti.database.CallbackHandler;
import com.example.ryhma4.taskimatti.database.Database;
import com.example.ryhma4.taskimatti.model.Routine;
import com.example.ryhma4.taskimatti.utility.ExapandableListAdapter;
import com.example.ryhma4.taskimatti.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowRoutinesActivity extends MainActivity implements CallbackHandler {
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader, allRoutines;
    private HashMap<String, List<String>> listHashMap;
    private Database db;
    private ArrayList<ArrayList<String>> routinesByType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_routines);

        db = new Database(this);
        db.listRoutineIds();

        listView = findViewById(R.id.lvExp);
        listDataHeader = new ArrayList<>();
        listDataHeader.add("KAIKKI RUTIINIT");
        listHashMap = new HashMap<>();
        routinesByType = new ArrayList<>();
        routinesByType.add(new ArrayList<String>());
        allRoutines = new ArrayList<>();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRoutineActivity();
            }
        });
    }

    private void initData(Routine routine) {

        int index = listDataHeader.indexOf(routine.getType().getName());

        if (index < 0) {
            listDataHeader.add(routine.getType().getName());
            index = listDataHeader.indexOf(routine.getType().getName());
            routinesByType.add(new ArrayList<String>());
        }

        routinesByType.get(0).add(routine.getName());
        routinesByType.get(index).add(routine.getName());

        for (int i = 0; i < listDataHeader.size(); i++) {
            listHashMap.put(listDataHeader.get(i), routinesByType.get(i));
        }

        listAdapter = new ExapandableListAdapter(this,listDataHeader,listHashMap);
        listView.setAdapter(listAdapter);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void successHandler(ArrayList<String> list) {
        Database newDb = new Database(this);
        for(String routineId : list) {
            Log.w("SUCCESS_HANDLER", routineId);
            newDb.getRoutine(routineId);
        }
    }

    @Override
    public void errorHandler() {

    }

    @Override
    public void passRoutine(Routine routine) {
//        listOfTypes.add(new ArrayList());

        initData(routine);

        Log.w("PASS_ROUTINE", routine.getType().getName());
    }

}

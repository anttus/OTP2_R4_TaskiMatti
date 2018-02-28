package com.example.ryhma4.taskimatti.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private ExpandableListView listView, listViewAllRoutines;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader, listAllRoutines, allRoutines;
    private HashMap<String, List<String>> listHashMap;
    private Database db;
    private ArrayList<List> listOfTypes;
    private ArrayList<ArrayList<String>> routinesByType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_routines);

        db = new Database(this);
        db.listRoutineIds();

        listOfTypes = new ArrayList<>();
        listView = findViewById(R.id.lvExp);
        listViewAllRoutines = findViewById(R.id.lvExp2);
        listDataHeader = new ArrayList<>();
        listAllRoutines = new ArrayList<>();
        listHashMap = new HashMap<>();
        routinesByType = new ArrayList<>();
        routinesByType.add(new ArrayList<String>());
        allRoutines = new ArrayList<>();
    }

    private void initData(Routine routine) {
        listDataHeader.clear();
        listAllRoutines.clear();

        listAllRoutines.add("KAIKKI RUTIINIT");

//        List<List<String>> allRoutines = new ArrayList<List<String>>();

//        ArrayList<String> temp = new ArrayList<>();

//        for (int i = 0; i < allRoutines.size(); i++){
//            for(int j = 0; j < allRoutines.get(i).size(); j++){
//                temp.add(allRoutines.get(i).get(j));
//            }
//        }
        int index = listDataHeader.indexOf(routine.getType().getName());

        if (index < 0) {
            listDataHeader.add(routine.getType().getName());
            index = listDataHeader.indexOf(routine.getType().getName());
        }

        if(routinesByType.get(index) == null) {
            routinesByType.add(new ArrayList<String>());
        }

        allRoutines.add(routine.getName());
        routinesByType.get(index).add(routine.getName());
        Log.w("HEADER",listDataHeader.get(index));
        Log.w("LIST", routinesByType.get(index).get(0));

//        listAllRoutines.get(0).add(routine.getName());


        listHashMap.put(listAllRoutines.get(0), allRoutines);
        Log.w("ALLROUTINES", allRoutines.toString());

        for (int i = 0; i < listDataHeader.size(); i++) {
            listHashMap.put(listDataHeader.get(i), routinesByType.get(i));
        }
        listAdapter = new ExapandableListAdapter(this,listAllRoutines,listHashMap);
        listViewAllRoutines.setAdapter(listAdapter);
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

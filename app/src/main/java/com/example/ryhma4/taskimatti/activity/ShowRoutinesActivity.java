package com.example.ryhma4.taskimatti.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryhma4.taskimatti.database.CallbackHandler;
import com.example.ryhma4.taskimatti.database.Database;
import com.example.ryhma4.taskimatti.model.Routine;
import com.example.ryhma4.taskimatti.utility.ExapandableListAdapter;
import com.example.ryhma4.taskimatti.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowRoutinesActivity extends MainActivity implements CallbackHandler {
    private ExpandableListView listView;
    private ArrayList<Routine> listDataHeader;
    private HashMap<Routine, ArrayList<Routine>> listHashMap;
    private ArrayList<ArrayList<Routine>> routinesByType;
    private int scene = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_routines);

        Database db = new Database(this);
        db.listRoutineIds();

        listView = findViewById(R.id.lvExp);
        listDataHeader = new ArrayList<Routine>();
        listDataHeader.add(new Routine());
        listDataHeader.get(0).setName("KAIKKI");
        listHashMap = new HashMap<>();
        routinesByType = new ArrayList<>();
        routinesByType.add(new ArrayList<Routine>());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRoutineActivity();
            }
        });
    }

    private void initData(Routine routine) {

        int index = listDataHeader.indexOf(routine);

        if (index < 0) {
            listDataHeader.add(routine);
            index = listDataHeader.indexOf(routine);
            routinesByType.add(new ArrayList<Routine>());
        }

        routinesByType.get(0).add(routine);
        routinesByType.get(index).add(routine);



        for (int i = 0; i < listDataHeader.size(); i++) {
            listHashMap.put(listDataHeader.get(i), routinesByType.get(i));
        }

        final ExpandableListAdapter listAdapter = new ExapandableListAdapter(this, listDataHeader, listHashMap);
        listView.setAdapter(listAdapter);

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                scene = 1;
                Routine myRoutine = routinesByType.get(i).get(i1);

                Toast.makeText(ShowRoutinesActivity.this, myRoutine.getName(), Toast.LENGTH_SHORT).show();

                listView.removeAllViewsInLayout();
                LinearLayout ll = new LinearLayout(ShowRoutinesActivity.this);
                TextView routineName = new TextView(ShowRoutinesActivity.this);

                routineName.setText(myRoutine.getName());
                routineName.setGravity(Gravity.START);

                ll.addView(routineName);
                return false;
}
        });
                }

    @Override
    public void onBackPressed() {
        if (scene == 0) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, ShowRoutinesActivity.class));
            scene = 0;
        }
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

package com.example.ryhma4.taskimatti.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryhma4.taskimatti.R;
import com.example.ryhma4.taskimatti.database.CallbackHandler;
import com.example.ryhma4.taskimatti.database.Database;
import com.example.ryhma4.taskimatti.model.Routine;
import com.example.ryhma4.taskimatti.model.Type;
import com.example.ryhma4.taskimatti.utility.ExapandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowRoutinesActivity extends MainActivity implements CallbackHandler {
    private ExpandableListView listView;
    private ArrayList<Type> listDataHeader;
    private HashMap<Type, ArrayList<Routine>> listHashMap;
    private ArrayList<ArrayList<Routine>> routinesByType;
    private Button btnDeleteRoutine;
    private LinearLayout ll;
    private int scene = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_routines);

        Database db = new Database(this);
        db.listRoutineIds();

        listView = findViewById(R.id.lvExp);
        listDataHeader = new ArrayList<>();
        listDataHeader.add(new Type());
        listDataHeader.get(0).setName("KAIKKI");
        listDataHeader.get(0).setColor("#ffffff");
        listHashMap = new HashMap<>();
        routinesByType = new ArrayList<>();
        routinesByType.add(new ArrayList<Routine>());
        btnDeleteRoutine = new Button(this);
        ll = new LinearLayout(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRoutineActivity();
            }
        });
    }

    private void initData(Routine routine) {

        int index = listDataHeader.indexOf(routine.getType());

        if (index < 0) {
            listDataHeader.add(routine.getType());
            index = listDataHeader.indexOf(routine.getType());
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
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                scene = 1;
                Routine myRoutine = routinesByType.get(i).get(i1);

                Toast.makeText(ShowRoutinesActivity.this, myRoutine.getName(), Toast.LENGTH_SHORT).show();

                listView.removeAllViewsInLayout();

                createRoutineMenu(myRoutine);

                return false;
            }
        });
    }

    // Menu for inspecting routines
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void createRoutineMenu(final Routine routine) {
        setContentView(ll);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
        ll.setMinimumHeight(1200);

        // TÄHÄN PAREMPI SYSTEEMI
        ArrayList<TextView> routineData = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            TextView data = new TextView(this);
            data.setPadding(30,30,30,10);
            data.setTextSize(15);
            routineData.add(data);
        }

        routineData.get(0).setText("Rutiinin nimi: " + routine.getName());
        routineData.get(1).setText("Tyyppi: " + routine.getType().getName());
        routineData.get(2).setText("Tyyppiväri: " + routine.getType().getColor());
        routineData.get(3).setText("Tekijä: " + routine.getAuthor());
        routineData.get(4).setText("Päiväys: " + routine.getDate());
        routineData.get(5).setText("Kuvaus: " + routine.getDescription());
        routineData.get(6).setText("Kesto: " + routine.getHours() + " tuntia, " + routine.getMinutes() + " minuuttia.");
        routineData.get(7).setText("Toistoväli: " + routine.getRepeat());
        routineData.get(8).setText("Toistokerrat: " + routine.getTimes());
        routineData.get(9).setText("ID: " + routine.getRoutineId());

        for (int i = 0; i < 10; i++) {
            ll.addView(routineData.get(i));
        }

        btnDeleteRoutine.setText("Poista rutiini");
        btnDeleteRoutine.setHeight(150);
        btnDeleteRoutine.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        btnDeleteRoutine.setTextColor(Color.parseColor("#f5f5f5"));
        btnDeleteRoutine.setBackgroundColor(Color.RED);
        btnDeleteRoutine.setPadding(50, 20, 50, 20);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(60,100,60,100);

        ll.addView(btnDeleteRoutine, layoutParams);

        btnDeleteRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w("btnDeleteRoutine", "Clicked.");
                Database db = new Database();
                db.removeRoutine(routine.getRoutineId());

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
//            Log.w("SUCCESS_HANDLER", routineId);
            newDb.getRoutine(routineId);
        }
    }

    @Override
    public void errorHandler() {

    }

    @Override
    public void passRoutine(Routine routine) {
        initData(routine);
//        Log.w("PASS_ROUTINE", routine.getType().getName());
    }

}

package com.example.ryhma4.taskimatti.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
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
    private int scene = 0;
    private EditText name, type, repeatTimes, hours, minutes, desc;
    private Spinner repeatInterval, typeDropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_routines);

        Database db = new Database(this);
        db.listRoutineIds();

        listView = findViewById(R.id.lvExp);
        listDataHeader = new ArrayList<>();
        listDataHeader.add(new Type("KAIKKI", "#ffffff"));
        listHashMap = new HashMap<>();
        routinesByType = new ArrayList<>();
        routinesByType.add(new ArrayList<Routine>());
        btnDeleteRoutine = new Button(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRoutineActivity();
            }
        });
    }

    // Lists and shows types as expandable lists with routines as their children
    private void initData(Routine routine) {
        String typeName = routine.getType().getName();
        int index = findIndex(typeName);

        if (index < 0) {
            listDataHeader.add(routine.getType());
            index = findIndex(typeName);
            routinesByType.add(new ArrayList<Routine>());
        }

        routinesByType.get(0).add(routine);
        routinesByType.get(index).add(routine);

        for (int i = 0; i < listDataHeader.size(); i++) {
            listHashMap.put(listDataHeader.get(i), routinesByType.get(i));
        }

        final ExpandableListAdapter listAdapter = new ExapandableListAdapter(this, listDataHeader, listHashMap);
        listView.setAdapter(listAdapter);

        // OnClick listener for the routine elements. Opens the routine inspection window.
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                scene = 1;
                Routine myRoutine = routinesByType.get(i).get(i1);

                listView.removeAllViewsInLayout();

                createRoutineMenu(myRoutine);

                return false;
            }
        });
    }

    // Finds the index of the type name headers
    public int findIndex(String typeName) {
        int index = -1;
        for(int i = 0; i < listDataHeader.size(); i++) {
            if(typeName.equals(listDataHeader.get(i).getName())) {
                index = i;
            }
        }
        return index;
    }

    // Menu for inspecting routines
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void createRoutineMenu(final Routine routine) {
        LinearLayout ll = new LinearLayout(this);
        ScrollView sv = new ScrollView(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.content_create_routine, null);
        ll.addView(v);

        // Editing routine items
        name = ll.findViewById(R.id.inputRoutineName);
        name.setText(routine.getName());
        type = ll.findViewById(R.id.inputRoutineType);
        type.setText(routine.getType().getName());

        // Adding type names to an ArrayList and using it in the dropdown
        ArrayList<String> types = new ArrayList<>();
        for (int i = 1; i < listDataHeader.size(); i++) {
            types.add(listDataHeader.get(i).getName());
        }
        ArrayAdapter adapterTypes = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapterTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeDropdown = ll.findViewById(R.id.dropdownType);
        typeDropdown.setAdapter(adapterTypes);

        // Interval dropdown
        ArrayList<String> spinnerArray =  new ArrayList<>();
        spinnerArray.add("Viikko");
        spinnerArray.add("Kuukausi");
        spinnerArray.add("Vuosi");
        ArrayAdapter<String> adapterInterval = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);

        adapterInterval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatInterval = ll.findViewById(R.id.dropdownInterval);
        repeatInterval.setAdapter(adapterInterval);

        repeatTimes = ll.findViewById(R.id.numTimes);
        repeatTimes.setText(String.valueOf(routine.getTimes()));
        hours = ll.findViewById(R.id.inputHours);
        hours.setText(String.valueOf(routine.getHours()));
        minutes = ll.findViewById(R.id.inputMinutes);
        minutes.setText(String.valueOf(routine.getMinutes()));
        desc = ll.findViewById(R.id.inputDescription);
        desc.setText(routine.getDescription());

        // Buttons for editing and deleting routine
        Button btnEditRoutine = new Button(ShowRoutinesActivity.this);
        btnEditRoutine.setText("Tallenna muutokset");
        btnEditRoutine.setBackgroundColor(Color.parseColor("#34a853"));
        btnEditRoutine.setTextColor(Color.parseColor("#f5f5f5"));
        btnEditRoutine.setPadding(50, 0, 50, 0);
        btnEditRoutine.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        btnDeleteRoutine.setText("Poista rutiini");
        btnDeleteRoutine.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        btnDeleteRoutine.setTextColor(Color.parseColor("#f5f5f5"));
        btnDeleteRoutine.setBackgroundColor(Color.parseColor("#ea4335"));
        btnDeleteRoutine.setPadding(50, 0, 50, 0);

        LinearLayout.LayoutParams layoutParamsBtnDelete = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsBtnDelete.setMargins(10,30,10,20);
        LinearLayout.LayoutParams layoutParamsBtnEdit = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsBtnEdit.setMargins(10,30,10,0);

        ll.addView(btnEditRoutine, layoutParamsBtnEdit);
        ll.addView(btnDeleteRoutine, layoutParamsBtnDelete);

        sv.addView(ll);

        setContentView(sv);

        // Removing the routine
        btnDeleteRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ShowRoutinesActivity.this)
                        .setTitle("Rutiinin poisto")
                        .setMessage("Haluatko varmasti poistaa rutiinin?")
//                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Database db = new Database();
                                db.removeRoutine(routine.getRoutineId());
                                startActivity(new Intent(ShowRoutinesActivity.this, ShowRoutinesActivity.class));
                                Toast.makeText(ShowRoutinesActivity.this, "Rutiini poistettu.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });

        // Editing the routine
        btnEditRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ShowRoutinesActivity.this)
                        .setTitle("Rutiinin muokkaus")
                        .setMessage("Tallennetaanko muutokset?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Database db = new Database();
                                routine.setName(name.getText().toString());
                                routine.getType().setName((type.getText().toString()));
                                routine.setRepeat(repeatInterval.getSelectedItem().toString());
                                routine.setTimes(Integer.parseInt(repeatTimes.getText().toString()));
                                routine.setHours(Integer.parseInt(hours.getText().toString()));
                                routine.setMinutes(Integer.parseInt(minutes.getText().toString()));
                                routine.setDescription(desc.getText().toString());
                                db.updateRoutine(routine);
                                startActivity(new Intent(ShowRoutinesActivity.this, ShowRoutinesActivity.class));
                                Toast.makeText(ShowRoutinesActivity.this, "Muokkaus onnistui.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
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
            newDb.getRoutine(routineId);
        }
    }

    @Override
    public void errorHandler() {

    }

    // When a routine is added or edited, this method's listener activates and runs the initData method
    @Override
    public void passRoutine(Routine routine) {
        initData(routine);
    }

}

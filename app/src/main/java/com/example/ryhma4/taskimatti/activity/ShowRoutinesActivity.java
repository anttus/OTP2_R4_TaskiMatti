package com.example.ryhma4.taskimatti.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ryhma4.taskimatti.Controller.Database;
import com.example.ryhma4.taskimatti.Controller.RoutineController;
import com.example.ryhma4.taskimatti.R;
import com.example.ryhma4.taskimatti.model.Routine;
import com.example.ryhma4.taskimatti.model.Type;
import com.example.ryhma4.taskimatti.utility.ExapandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowRoutinesActivity extends MainActivity {
    private ExpandableListView listView;
    private ArrayList<Type> listDataHeader;
    private HashMap<Type, ArrayList<Routine>> listHashMap;
    private ArrayList<ArrayList<Routine>> routinesByType;
    private int scene = 0;
    private EditText name, type, repeatTimes, hours, minutes, desc;
    private Spinner repeatInterval;
    private LayoutInflater inflater;
    private ProgressDialog pd;
    private RoutineController rc;
    private ExapandableListAdapter listAdapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_routines);

        rc = RoutineController.getInstance();

        listView = findViewById(R.id.lvExp);
        listView.setEmptyView(findViewById(R.id.emptyView));

        listDataHeader = rc.getTypes();
        listHashMap = rc.getRoutinesByHeader();
        routinesByType = rc.getRoutinesByType();

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRoutineActivity();
            }
        });
        pd = new ProgressDialog(ShowRoutinesActivity.this);
        pd.setMessage(getResources().getString(R.string.prompt_loading));
        pd.show();
        initData();
    }

    /**
     * Lists and shows types as expandable lists with routines as their children
     */
    public void initData() {

        listAdapter = new ExapandableListAdapter(this, listDataHeader, listHashMap);
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
        pd.hide();
    }


    /**
     * Menu for inspecting routines
     *
     * @param routine The routine passed from the database
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void createRoutineMenu(final Routine routine) {
        LinearLayout ll = new LinearLayout(this);
        View v = inflater.inflate(R.layout.content_create_routine, null);
        ll.addView(v);
        setContentView(ll);

        // Buttons for editing and deleting routine
        FloatingActionButton btnDeleteRoutine = v.findViewById(R.id.btnDeleteRoutine);
        FloatingActionButton btnEditRoutine = v.findViewById(R.id.btnEditRoutine);

        // Editing routine items
        name = ll.findViewById(R.id.inputRoutineName);
        name.setText(routine.getName());
        type = ll.findViewById(R.id.inputRoutineType);
        type.setText(routine.getType().getName());

        // Adding type names to an ArrayList and using it in the dropdown
        final Spinner typeDropdown = ll.findViewById(R.id.dropdownType);
        rc.createFillTypeSpinner(type, this, typeDropdown);

        // Interval dropdown
        ArrayList<String> spinnerArray = new ArrayList<>();
        spinnerArray.add(getResources().getString(R.string.time_week));
        spinnerArray.add(getResources().getString(R.string.time_month));
        spinnerArray.add(getResources().getString(R.string.time_year));
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
        rc.routineDescriptionTouchListener(desc, this);

        // Removing the routine
        btnDeleteRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ShowRoutinesActivity.this)
                        .setTitle(getResources().getString(R.string.prompt_routine_removal))
                        .setMessage(getResources().getString(R.string.prompt_routine_removal_confirm))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Database db = Database.getInstance();
                                db.removeRoutine(routine.getRoutineId());
                                rc.removeRoutine(routine);
                                listAdapter.notifyDataSetChanged();
                                startActivity(new Intent(ShowRoutinesActivity.this, ShowRoutinesActivity.class));
                                Toast.makeText(ShowRoutinesActivity.this, getResources().getString(R.string.prompt_routine_removal_success), Toast.LENGTH_SHORT).show();
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
                        .setTitle(getResources().getString(R.string.prompt_routine_edit))
                        .setMessage(getResources().getString(R.string.prompt_routine_edit_confirm))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Database db = Database.getInstance();
                                routine.setName(name.getText().toString());
                                routine.getType().setName((type.getText().toString()));
                                routine.setRepeat(repeatInterval.getSelectedItem().toString());
                                routine.setTimes(Integer.parseInt(repeatTimes.getText().toString()));
                                routine.setHours(Integer.parseInt(hours.getText().toString()));
                                routine.setMinutes(Integer.parseInt(minutes.getText().toString()));
                                routine.setDescription(desc.getText().toString());
                                db.updateRoutine(routine);
                                startActivity(new Intent(ShowRoutinesActivity.this, ShowRoutinesActivity.class));
                                Toast.makeText(ShowRoutinesActivity.this, getResources().getString(R.string.prompt_routine_edit_success), Toast.LENGTH_SHORT).show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (scene == 0) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else {
                startActivity(new Intent(this, ShowRoutinesActivity.class));
                scene = 0;
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}

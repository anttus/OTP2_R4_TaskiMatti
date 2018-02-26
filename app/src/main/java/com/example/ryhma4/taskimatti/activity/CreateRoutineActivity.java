package com.example.ryhma4.taskimatti.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryhma4.taskimatti.Controller.CreateRoutineController;
import com.example.ryhma4.taskimatti.R;
import com.example.ryhma4.taskimatti.database.Database;
import com.example.ryhma4.taskimatti.model.Routine;
import com.example.ryhma4.taskimatti.model.Task;
import com.example.ryhma4.taskimatti.model.Type;
import com.example.ryhma4.taskimatti.utility.Validate;

import java.util.ArrayList;

public class CreateRoutineActivity extends MainActivity {

    private TextView routineNameView;
    private TextView routineTypeView;
    private EditText routineIntervalNumberView;
    private Spinner routineIntervalView;
    private EditText routineDurationHoursView;
    private EditText routineDurationMinutesView;
    private TextView routineDescriptionView;
    private FloatingActionButton btnSaveRoutine;
    private FloatingActionButton btnSaveAll;
    private CheckBox checkSameTasks;
    private ArrayList<Integer> taskIdList, taskIdDescList;
    private Routine routine;
    private CreateRoutineController rc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_routine);
        taskIdList = new ArrayList<>();
        taskIdDescList = new ArrayList<>();
        rc = new CreateRoutineController();

        // List for the routine intervals
        Spinner dropdownInterval = findViewById(R.id.dropdownInterval);
        String[] intervals = new String[]{"Viikko", "Kuukausi", "Vuosi"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, intervals);
        dropdownInterval.setAdapter(adapter);

        routineNameView = findViewById(R.id.inputRoutineName);
        routineTypeView = findViewById(R.id.inputRoutineType);
        routineIntervalNumberView = findViewById(R.id.numTimes);
        routineIntervalView = findViewById(R.id.dropdownInterval);
        routineDurationHoursView = findViewById(R.id.inputHours);
        routineDurationMinutesView = findViewById(R.id.inputMinutes);
        routineDescriptionView = findViewById(R.id.inputDescription);

        checkSameTasks = findViewById(R.id.checkSameTasks);
        btnSaveRoutine = findViewById(R.id.btnSaveRoutine);

        btnSaveRoutine.setOnClickListener(saveRoutineButtonListener);

    }

    public void drawNewRows(int id, LinearLayout ll, EditText tv) {
        ll.addView(tv);

        EditText tvDescription = new EditText(this);
        tvDescription.setHint("Kuvaus");
        tvDescription.setId(id);
        tvDescription.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        tvDescription.setHeight(200);
//            tvDescription.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        tvDescription.setGravity(Gravity.TOP);
        tvDescription.setBackgroundResource(android.R.drawable.editbox_background);
        tvDescription.setSingleLine(false);
        ll.setPadding(0, 0, 0, 10);

        ll.addView(tvDescription);
    }

    public void createNewRows(int numberOfTasks, View v) {
        Toast.makeText(CreateRoutineActivity.this, "Tehtävät luotu", Toast.LENGTH_SHORT).show();

        // Find the ScrollView
        LinearLayout linearRoutines = v.findViewById(R.id.createRoutineLinearLayout);
        linearRoutines.removeAllViews();
        linearRoutines.setMinimumWidth(1000); // Possibly needs a better solution?

        // Create a LinearLayout element
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        // Create multiple (or one) of the same task
        if (checkSameTasks.isChecked()) {
            int id = 1;
            EditText tv = new EditText(this);
            tv.setHint("Tehtävä ");
            tv.setId(id);
            taskIdList.add(id);
            taskIdDescList.add(id + 1000);
            drawNewRows(id + 1000, ll, tv);

        // Create multiple (or one) different tasks
        } else {
            for (int i = 1; i < numberOfTasks + 1; i++) {
                EditText tv = new EditText(this);
                tv.setHint("Tehtävä " + i);
                tv.setId(i);
                taskIdList.add(i);
                taskIdDescList.add(i + 1000);
                drawNewRows(i + 1000, ll, tv);
            }
        }

        // Add the LinearLayout element to the ScrollView
        linearRoutines.addView(ll);

    }

    public boolean validateEditText(ArrayList<Integer> ids) {
        boolean isNotEmpty = true;

        for(int id: ids) {
            EditText et = findViewById(id);

            if(TextUtils.isEmpty(et.getText().toString())) {
                et.setError("Vaaditaan");
                isNotEmpty = false;
            }
        }
        return isNotEmpty;
    }

    public boolean validateNumbers(ArrayList<Integer> ids) {
        boolean isNotEmpty = true;

        for(int id: ids) {
            EditText et = findViewById(id);
            if(TextUtils.isEmpty(et.getText().toString())) {
                et.setError("Vaaditaan");
                isNotEmpty = false;
            }
            else {
                if(Integer.parseInt(et.getText().toString()) <= 0) {
                    et.setError("Arvo alle 1");
                    isNotEmpty = false;
                }
            }
        }
        return isNotEmpty;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private View.OnClickListener saveRoutineButtonListener = new View.OnClickListener() {
        public void onClick(View v) {

            ArrayList<Integer> ids = new ArrayList<>();
            ids.add(R.id.inputRoutineName);
            ids.add(R.id.inputRoutineType);
            ids.add(R.id.inputHours);
            ids.add(R.id.inputMinutes);
            ids.add(R.id.inputDescription);

            ArrayList<Integer> numTimes = new ArrayList<>();
            numTimes.add(R.id.numTimes);

            if (validateEditText(ids) && validateNumbers(numTimes)) {
                //Creating the routine
                String routineName = routineNameView.getText().toString();
                Type routineType = new Type(routineTypeView.getText().toString(), "#FFFFFF");
                int routineIntervalNumber = Integer.parseInt(routineIntervalNumberView.getText().toString());
                String routineInterval = routineIntervalView.getSelectedItem().toString();
                int routineDurationHours = Integer.parseInt(routineDurationHoursView.getText().toString());
                int routineDurationMinutes = Integer.parseInt(routineDurationMinutesView.getText().toString());
                String routineDescription = routineDescriptionView.getText().toString();

                routine = new Routine(routineName, routineType, routineIntervalNumber, routineInterval, routineDurationHours, routineDurationMinutes, routineDescription);

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                inflater.inflate(R.layout.activity_create_routine, null);

                // Display the view
                View v2 = inflater.inflate(R.layout.activity_create_routine, null);
                setContentView(v2);

                rc.setRoutine(routine);

                btnSaveAll = findViewById(R.id.btnSaveRoutine);
                btnSaveAll.setImageResource(R.drawable.ic_check_black_24dp);
                createNewRows(routineIntervalNumber, v2);
                View.OnClickListener saveAllListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (validateEditText(taskIdList) && validateEditText(taskIdDescList)) {
                            createTasks();
                            startActivity(new Intent(CreateRoutineActivity.this, MainActivity.class));
//                            Snackbar mySnackbar = Snackbar.make(view,
//                                    "Tehtävät lisätty.", Snackbar.LENGTH_SHORT);
//                            mySnackbar.show();
                        }
                    }
                };
                btnSaveAll.setOnClickListener(saveAllListener);
            }
        }
    };

    public void createTasks() {
        String name, description;
        for(int taskId: taskIdList) {
            EditText etName = findViewById(taskId);
            EditText etDescription = findViewById(taskId + 1000);

            name = etName.getText().toString();
            description = etDescription.getText().toString();

            Task task = new Task(routine.getID(), name, description);
            rc.setTask(task);
        }
    }

}
package com.example.ryhma4.taskimatti.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryhma4.taskimatti.Controller.RoutineController;
import com.example.ryhma4.taskimatti.R;
import com.example.ryhma4.taskimatti.model.Routine;
import com.example.ryhma4.taskimatti.model.Task;
import com.example.ryhma4.taskimatti.model.Type;
import com.example.ryhma4.taskimatti.model.TypeColor;
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
    private CheckBox checkSameTasks;
    private ArrayList<Integer> taskIdList, taskIdDescList;
    private Routine routine;
    private RoutineController rc;
    private int taskRepeatAmount;
    private ArrayList<Type> listDataHeader;
    private Validate validate;
    private Activity activity;
    private LayoutInflater inflater;
    private View routineView;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_routine);
        taskIdList = new ArrayList<>();
        taskIdDescList = new ArrayList<>();
        rc = RoutineController.getInstance();

        listDataHeader = rc.getTypes();

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        routineView = inflater.inflate(R.layout.activity_create_routine, null);

        // List for the routine intervals
        Spinner dropdownInterval = findViewById(R.id.dropdownInterval);
        String[] intervals = new String[]{getResources().getString(R.string.time_week), getResources().getString(R.string.time_month), getResources().getString(R.string.time_year)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, intervals);
        dropdownInterval.setAdapter(adapter);

        routineNameView = findViewById(R.id.inputRoutineName);
        routineTypeView = findViewById(R.id.inputRoutineType);
        routineIntervalNumberView = findViewById(R.id.numTimes);
        routineIntervalView = dropdownInterval;
        routineDurationHoursView = findViewById(R.id.inputHours);
        routineDurationMinutesView = findViewById(R.id.inputMinutes);
        routineDescriptionView = findViewById(R.id.inputDescription);
        rc.routineDescriptionTouchListener(routineDescriptionView, this);
        checkSameTasks = findViewById(R.id.checkSameTasks);
        FloatingActionButton btnSaveRoutine = findViewById(R.id.btnSaveRoutine);
        btnSaveRoutine.setOnClickListener(saveRoutineButtonListener);

        FrameLayout editDeleteBtnLayout = findViewById(R.id.editDeleteBtnLayout);
        editDeleteBtnLayout.setVisibility(View.GONE);

        final Spinner typeDropdown = findViewById(R.id.dropdownType);

        rc.createFillTypeSpinner(routineTypeView, this, typeDropdown);

        validate = new Validate();
        activity = this;
    }

    /**
     * Draws the tasks' name and description fields
     *
     * @param id Locally used id for the TextView element
     * @param ll The current layout where the element will be created
     */
    public void drawNewRows(int id, LinearLayout ll) {
        View taskView = inflater.inflate(R.layout.task_field, ll);
        EditText tvDescription = taskView.findViewById(R.id.taskDescription);
        EditText tvTaskName = taskView.findViewById(R.id.taskName);
        tvTaskName.setId(id);
        taskIdList.add(id);
        taskIdDescList.add(id + 1000);
        tvDescription.setId(id + 1000);
    }

    /**
     * Creates the amount of task name and description fields specified by the routine's interval number
     *
     * @param numberOfTasks Number of the tasks, received from routine creation window's routine interval value
     * @param v             The view where the TextViews will be created
     */
    public void createNewRows(int numberOfTasks, View v) {
        Toast.makeText(CreateRoutineActivity.this, getResources().getString(R.string.text_tasks_created), Toast.LENGTH_SHORT).show();

        // Find the ScrollView
        LinearLayout linearRoutines = v.findViewById(R.id.createRoutineLinearLayout);
        linearRoutines.removeAllViews();

        TextView title = new TextView(this);
        title.setText("LUO TEHTÄVÄT");
        title.setTextSize(15);
        title.setPadding(20, 20, 20, 30);

        FrameLayout editDeleteBtnLayout = findViewById(R.id.editDeleteBtnLayout);
        editDeleteBtnLayout.setVisibility(View.GONE);

        // Create a LinearLayout element
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        taskRepeatAmount = routine.getTimes();
        if (checkSameTasks.isChecked()) { // Create multiple (or one) of the same task
            int id = 1;
            drawNewRows(id, ll);
        } else { // Create multiple (or one) different tasks
            for (int i = 1; i < numberOfTasks + 1; i++) {
                drawNewRows(i, ll);
            }
        }

        // Add the LinearLayout element to the ScrollView
        linearRoutines.addView(title);
        linearRoutines.addView(ll);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            super.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.finish();
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

            if (validate.validateEditText(ids, activity) && validate.validateNumbers(numTimes, activity)) {
                //Creating the routine
                String routineName = routineNameView.getText().toString();
                Type routineType = new Type(routineTypeView.getText().toString(), TypeColor.randomColor());
                int routineIntervalNumber = Integer.parseInt(routineIntervalNumberView.getText().toString());
                String routineInterval = routineIntervalView.getSelectedItem().toString();
                int routineDurationHours = Integer.parseInt(routineDurationHoursView.getText().toString());
                int routineDurationMinutes = Integer.parseInt(routineDurationMinutesView.getText().toString());
                String routineDescription = routineDescriptionView.getText().toString();

                routine = new Routine(routineName, routineType, routineIntervalNumber, routineInterval, routineDurationHours, routineDurationMinutes, routineDescription);

                setContentView(routineView);

                FloatingActionButton btnSaveAll = findViewById(R.id.btnSaveRoutine);
                btnSaveAll.setImageResource(R.drawable.ic_check_black_24dp);
                createNewRows(routineIntervalNumber, routineView);

                View.OnClickListener saveAllListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (validate.validateEditText(taskIdList, activity) && validate.validateEditText(taskIdDescList, activity)) {
                            rc.setRoutine(routine);
                            createTasks();
                            startActivity(new Intent(CreateRoutineActivity.this, MainActivity.class));
                        }
                    }
                };
                btnSaveAll.setOnClickListener(saveAllListener);
            }
        }
    };

    /**
     * Creates the routines in RoutineController
     */
    public void createTasks() {
        String name, description;
        EditText etName, etDescription;
        ArrayList<Task> tasks = new ArrayList<>();
        for (int i = 0; i < taskRepeatAmount; i++) {
            if (checkSameTasks.isChecked()) {
                etName = findViewById(taskIdList.get(0));
                etDescription = findViewById(taskIdList.get(0) + 1000);
            } else {
                etName = findViewById(taskIdList.get(i));
                etDescription = findViewById(taskIdList.get(i) + 1000);
            }
            name = etName.getText().toString();
            description = etDescription.getText().toString();

            Task task = new Task(routine.getRoutineId(), name, description, routine.getHours(), routine.getMinutes());
            tasks.add(task);
        }

        rc.setTask(tasks);
        rc.fetchRoutines();
    }

}

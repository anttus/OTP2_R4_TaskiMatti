package com.example.ryhma4.taskimatti;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryhma4.taskimatti.data.Database;
import com.example.ryhma4.taskimatti.data.Routine;
import com.example.ryhma4.taskimatti.data.Type;

public class CreateRoutineActivity extends MainActivity {

    private TextView routineNameView;
    private TextView routineTypeView;
    private EditText routineIntervalNumberView;
    private Spinner routineIntervalView;
    private EditText routineDurationHoursView;
    private EditText routineDurationMinutesView;
    private TextView routineDescriptionView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_routine);

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

        FloatingActionButton btnSaveRoutine = findViewById(R.id.btnSaveRoutine);
        btnSaveRoutine.setOnClickListener(buttonListener);

        routineIntervalNumberView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Toast.makeText(CreateRoutineActivity.this, charSequence, Toast.LENGTH_SHORT).show();
                createNewRows(Integer.parseInt(charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    public void createNewRows(int numberOfTasks) {
        Toast.makeText(CreateRoutineActivity.this, "Tehtävät luotu", Toast.LENGTH_SHORT).show();

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.activity_create_routine, null);

        // Find the ScrollView
        LinearLayout linear = v.findViewById(R.id.createRoutineLinearLayout);

        // Create a LinearLayout element
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < numberOfTasks; i++) {
            // Add the task creation fields
            EditText tv = new EditText(this);
            tv.setHint("Tehtävä " + (i+1));
            tv.setId(i+1);
            Log.d("ID: ", String.valueOf(tv.getId()));
            ll.addView(tv);

            EditText tvDescription = new EditText(this);
            tvDescription.setHint("Kuvaus");
            tvDescription.setId(i+1 + 1000);
            tvDescription.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            tvDescription.setHeight(200);
            tvDescription.setGravity(Gravity.TOP);
            tvDescription.setBackgroundResource(android.R.drawable.editbox_background);
            tvDescription.setSingleLine(false);

            ll.addView(tvDescription);
        }

        // Add the LinearLayout element to the ScrollView
        linear.addView(ll);

        // Display the view
        setContentView(v);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private View.OnClickListener buttonListener = new View.OnClickListener() {
        public void onClick(View v) {

            String routineName = routineNameView.getText().toString();
            Type routineType = new Type(routineTypeView.getText().toString(), "#FFFFFF");
            int routineIntervalNumber = Integer.parseInt(routineIntervalNumberView.getText().toString());
            String routineInterval = routineIntervalView.getSelectedItem().toString();
            int routineDurationHours = Integer.parseInt(routineDurationHoursView.getText().toString());
            int routineDurationMinutes = Integer.parseInt(routineDurationMinutesView.getText().toString());
            String routineDescription = routineDescriptionView.getText().toString();

            Routine routine = new Routine(routineName, routineType, routineIntervalNumber, routineInterval, routineDurationHours, routineDurationMinutes, routineDescription);
            Database db = new Database();
            db.setRoutine(routine);

            Toast.makeText(CreateRoutineActivity.this, "Routine set.", Toast.LENGTH_LONG).show();

        }
    };


}

package com.example.ryhma4.taskimatti;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

        Button btnSaveRoutine = findViewById(R.id.btnSaveRoutine);
        btnSaveRoutine.setOnClickListener(buttonListener);

        routineIntervalNumberView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Toast.makeText(CreateRoutineActivity.this, charSequence, Toast.LENGTH_SHORT).show();
                createNewRows();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    public void createNewRows() {
        Toast.makeText(CreateRoutineActivity.this, "asdf", Toast.LENGTH_SHORT).show();
        LinearLayout linearLayout = new LinearLayout(this);

        TextView textView1 = new TextView(this);
        textView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        textView1.setText("programmatically created TextView1");
        textView1.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
        textView1.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
        linearLayout.addView(textView1);
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

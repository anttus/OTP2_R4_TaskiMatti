package com.example.ryhma4.taskimatti;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CreateRoutineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_routine);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        // List for the routine intervals
        Spinner dropdownInterval = findViewById(R.id.dropdownInterval);
        String[] intervals = new String[]{"Viikko", "Kuukausi", "Vuosi"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, intervals);
        dropdownInterval.setAdapter(adapter);

        Button btnSaveRoutine = findViewById(R.id.btnSaveRoutine);
        btnSaveRoutine.setOnClickListener(buttonListener);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private View.OnClickListener buttonListener = new View.OnClickListener() {
        public void onClick(View v) {
            TextView routineNameView = findViewById(R.id.inputRoutineName);
            TextView routineTypeView = findViewById(R.id.inputRoutineType);
            EditText routineIntervalNumberView = findViewById(R.id.numTimes);
            Spinner routineIntervalView = findViewById(R.id.dropdownInterval);
            EditText routineDurationHoursView = findViewById(R.id.inputHours);
            EditText routineDurationMinutesView = findViewById(R.id.inputMinutes);
            TextView routineDescriptionView = findViewById(R.id.inputDescription);

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

        }
    };

}

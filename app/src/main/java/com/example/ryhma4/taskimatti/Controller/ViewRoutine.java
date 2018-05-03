package com.example.ryhma4.taskimatti.Controller;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.ryhma4.taskimatti.R;
import com.example.ryhma4.taskimatti.activity.MainActivity;
import com.example.ryhma4.taskimatti.model.Routine;

import java.util.ArrayList;


public class ViewRoutine {

    private View v;

    public View getView() {
        return v;
    }

    /**
     * Menu for inspecting and editing routines
     * @param routine The routine passed from the database
     * @param context Current context
     * @param inflater The inflater of the current activity
     * @param activity Current activity
     * @param rc RoutineController
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void createRoutineMenu(Routine routine, Context context, LayoutInflater inflater, Activity activity, RoutineController rc) {
        LinearLayout ll = new LinearLayout(context);
        View v = inflater.inflate(R.layout.activity_create_routine, null);

        ll.addView(v);
        activity.setContentView(ll);

        // Editing routine items
        EditText name = ll.findViewById(R.id.inputRoutineName);
        name.setText(routine.getName());
        EditText type = ll.findViewById(R.id.inputRoutineType);
        type.setText(routine.getType().getName());

        // Adding type names to an ArrayList and using it in the dropdown
        Spinner typeDropdown = ll.findViewById(R.id.dropdownType);
        rc.createFillTypeSpinner(type, context, typeDropdown);

        // Interval dropdown
        ArrayList<String> spinnerArray = new ArrayList<>();
        spinnerArray.add(MainActivity.globalRes.getString(R.string.time_week));
        spinnerArray.add(MainActivity.globalRes.getString(R.string.time_month));
        spinnerArray.add(MainActivity.globalRes.getString(R.string.time_year));
        ArrayAdapter<String> adapterInterval = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, spinnerArray);
        adapterInterval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner repeatInterval = ll.findViewById(R.id.dropdownInterval);
        repeatInterval.setAdapter(adapterInterval);
        int spinnerPosition = adapterInterval.getPosition(repeatInterval.getSelectedItem().toString());
        repeatInterval.setSelection(spinnerPosition);

        EditText repeatTimes = ll.findViewById(R.id.numTimes);
        repeatTimes.setText(String.valueOf(routine.getTimes()));
        EditText hours = ll.findViewById(R.id.inputHours);
        hours.setText(String.valueOf(routine.getHours()));
        EditText minutes = ll.findViewById(R.id.inputMinutes);
        minutes.setText(String.valueOf(routine.getMinutes()));
        EditText desc = ll.findViewById(R.id.inputDescription);
        desc.setText(routine.getDescription());
        rc.routineDescriptionTouchListener(desc, context);

        this.v = v;

    }

    /**
     * Hides the save button used when creating a routine
     */
    public void hideSaveButton() {
        FloatingActionButton btnSaveRoutine = v.findViewById(R.id.btnSaveRoutine);
        btnSaveRoutine.setVisibility(View.GONE);
    }

}

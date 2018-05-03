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
import com.example.ryhma4.taskimatti.model.Type;

import java.util.ArrayList;


public class ViewRoutine {

    private View v;
    private EditText name, type, repeatTimes, hours, minutes, desc;
    private Spinner typeDropdown, repeatInterval;

    public View getView() {
        return v;
    }

    private EditText getName() {
        return name;
    }

    private EditText getType() {
        return type;
    }

    private EditText getRepeatTimes() {
        return repeatTimes;
    }

    private EditText getHours() {
        return hours;
    }

    private EditText getMinutes() {
        return minutes;
    }

    private EditText getDesc() {
        return desc;
    }

    private Spinner getRepeatInterval() {
        return repeatInterval;
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
        name = ll.findViewById(R.id.inputRoutineName);
        name.setText(routine.getName());
        type = ll.findViewById(R.id.inputRoutineType);
        type.setText(routine.getType().getName());

        // Adding type names to an ArrayList and using it in the dropdown
        typeDropdown = ll.findViewById(R.id.dropdownType);
        rc.createFillTypeSpinner(type, context, typeDropdown);

        // Interval dropdown
        ArrayList<String> spinnerArray = new ArrayList<>();
        spinnerArray.add(MainActivity.globalRes.getString(R.string.time_week));
        spinnerArray.add(MainActivity.globalRes.getString(R.string.time_month));
        spinnerArray.add(MainActivity.globalRes.getString(R.string.time_year));
        ArrayAdapter<String> adapterInterval = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, spinnerArray);
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

    public Routine getRoutine() {
        Routine routine = new Routine();
        routine.setType(new Type());
        routine.setName(getName().getText().toString());
        routine.getType().setName((getType().getText().toString()));
        routine.setRepeat(getRepeatInterval().getSelectedItem().toString());
        routine.setTimes(Integer.parseInt(getRepeatTimes().getText().toString()));
        routine.setHours(Integer.parseInt(getHours().getText().toString()));
        routine.setMinutes(Integer.parseInt(getMinutes().getText().toString()));
        routine.setDescription(getDesc().getText().toString());

        return routine;
    }

}

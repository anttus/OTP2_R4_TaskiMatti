package com.example.ryhma4.taskimatti.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryhma4.taskimatti.R;

import org.w3c.dom.Text;

/**
 * Created by mikae on 6.3.2018.
 */

public class DayFragment extends Fragment {
    private TextView mainRoutineText, mainTimeText;
    private LinearLayout mainTimesLayout, mainRoutinesLayout;

    public DayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day, container, false);
        mainRoutineText = view.findViewById(R.id.mainRoutineText);
        mainTimeText = view.findViewById(R.id.mainTimeText);
        mainRoutinesLayout = view.findViewById(R.id.mainRoutinesLayout);
        mainTimesLayout = view.findViewById(R.id.mainTimesLayout);
        mainRoutineText.setText("Tehtävä:");
        mainTimeText.setText("Aika:");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        int day = args.getInt("day");

        switch (day) {
            case 0:
                // Needs implementation for fetching user's tasks
                createTaskElement("7:30", "Tehtävä 13");
                createTaskElement("7:00", "Tehtävä 12");
                createTaskElement("7:00", "Tehtävä 11");
                createTaskElement("7:00", "Tehtävä 10");
                createTaskElement("7:00", "Tehtävä 9");
                break;
            case 1:
                // Needs implementation for fetching user's tasks
                break;
            case 2:
                // Needs implementation for fetching user's tasks
                createTaskElement("7:00", "Tehtävä 1");
                break;
            case 3:
                // Needs implementation for fetching user's tasks
                break;
            case 4:
                // Needs implementation for fetching user's tasks
                break;
            case 5:
                // Needs implementation for fetching user's tasks
                break;
            case 6:
                // Needs implementation for fetching user's tasks
                break;
        }

    }

    public void createTaskElement(String time, String task) {
        final TextView tvTime = new TextView(getActivity());
        tvTime.setText(time);
        tvTime.setTextSize(20);
        tvTime.setClickable(true);
        tvTime.setTypeface(null, Typeface.BOLD);
        tvTime.setPadding(30, 30, 30, 30);
        tvTime.setBackgroundResource(R.drawable.border_straight);
        mainTimesLayout.addView(tvTime);

        final TextView tvTask = new TextView(getActivity());
        tvTask.setText(task);
        tvTask.setTextSize(20);
        tvTask.setClickable(true);
        tvTask.setTypeface(null, Typeface.BOLD);
        tvTask.setPadding(30, 30, 30, 30);
        tvTask.setBackgroundResource(R.drawable.border_straight);
        mainRoutinesLayout.addView(tvTask);
        tvTask.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Tehtävän poisto")
                        .setMessage("Haluatko varmasti poistaa tehtävän " + tvTask.getText() + "?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                tvTask.setVisibility(View.GONE);
                                tvTime.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Tehtävä poistettu.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                return false;
            }
        });
    }

}

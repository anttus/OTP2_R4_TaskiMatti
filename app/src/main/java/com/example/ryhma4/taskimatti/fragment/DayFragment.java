package com.example.ryhma4.taskimatti.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryhma4.taskimatti.R;
import com.example.ryhma4.taskimatti.utility.CallbackHandler;
import com.example.ryhma4.taskimatti.Controller.Database;
import com.example.ryhma4.taskimatti.model.Task;

import java.util.ArrayList;

/**
 * Created by mikae on 6.3.2018.
 */

public class DayFragment extends Fragment implements CallbackHandler {
    private TextView mainRoutineText, mainTimeText;
    private LinearLayout mainTimesLayout, mainRoutinesLayout;
    private Database db;
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
        mainRoutineText.setText(getResources().getString(R.string.param_task));
        mainTimeText.setText(getResources().getString(R.string.time_time));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        String date = args.getString("weekDate");
        db = new Database(this);
//        db.listSetTaskIds(date);
        db.findTasksToActivate();
        db.getTasksForDay(date);

    }

    /**
     * Creates the task elements that are shown in the main window
     * @param task the task object used to create the element
     */
    public void createTaskElement(final Task task) {
        String time = task.getTime();
        String name = task.getName();
        String taskDesc = task.getDescription();

        final TextView tvTime = new TextView(getActivity());
        tvTime.setText(time);
        tvTime.setTextSize(20);
        tvTime.setClickable(true);
        tvTime.setTypeface(null, Typeface.BOLD);
        tvTime.setPadding(30, 30, 30, 30);
        tvTime.setBackgroundResource(R.drawable.border_straight);
        tvTime.setHeight(150);
        mainTimesLayout.addView(tvTime);

        final TextView tvTask = new TextView(getActivity());
        tvTask.setText(name);
        tvTask.setTextSize(20);
        tvTask.setClickable(true);
        tvTask.setTypeface(null, Typeface.BOLD);
        tvTask.setPadding(30, 30, 30, 30);
        tvTask.setBackgroundResource(R.drawable.border_straight);
        tvTask.setHeight(150);
        tvTask.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_down_black_24dp, 0, 0, 0);

        final TextView tvDesc = new TextView(getActivity());
        tvDesc.setText(taskDesc);
        tvDesc.setVisibility(View.GONE);
        tvDesc.setTextSize(15);
        tvDesc.setClickable(true);
        tvDesc.setPadding(30, 30, 30, 30);
        tvDesc.setBackgroundResource(R.drawable.border_straight);

        tvTask.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (tvDesc.getVisibility() == View.GONE) {
                    tvDesc.setVisibility(View.VISIBLE);
                    tvTask.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_up_black_24dp, 0, 0, 0);
                } else {
                    tvDesc.setVisibility(View.GONE);
                    tvTask.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_down_black_24dp, 0, 0, 0);
                }
            }
        });

        mainRoutinesLayout.addView(tvTask);
        mainRoutinesLayout.addView(tvDesc);

        tvTask.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Tehtävän poisto")
                        .setMessage(getResources().getString(R.string.prompt_removal_confirm) + tvTask.getText() + "?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                tvTask.setVisibility(View.GONE);
                                tvTime.setVisibility(View.GONE);
                                db.setTaskWaiting(task);
                                Toast.makeText(getActivity(), getResources().getString(R.string.prompt_task_removal_success), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                return false;
            }
        });
    }

    @Override
    public void successHandler(ArrayList<?> list) {
        Database db = new Database(this);
        for(Object taskId : list) {
            db.getTask((String) taskId);
        }
    }

    @Override
    public void errorHandler() {

    }

    @Override
    public void passObject(Object object) {
        Task task = (Task) object;
        createTaskElement(task);
    }
}

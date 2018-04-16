package com.example.ryhma4.taskimatti.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ryhma4.taskimatti.Controller.Database;
import com.example.ryhma4.taskimatti.R;
import com.example.ryhma4.taskimatti.model.Task;
import com.example.ryhma4.taskimatti.recyclerview.RecyclerViewEmptySupport;
import com.example.ryhma4.taskimatti.recyclerview.TaskFragment;
import com.example.ryhma4.taskimatti.recyclerview.TaskRecyclerViewAdapter;
import com.example.ryhma4.taskimatti.utility.CallbackHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by mikae on 6.3.2018.
 */

public class DayFragment extends Fragment implements CallbackHandler, TaskFragment.OnListFragmentInteractionListener {
    private Database db;
    private RecyclerViewEmptySupport mRecyclerView;
    List<Task> tasks;

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
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        mRecyclerView = view.findViewById(R.id.taskRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setEmptyView(view.findViewById(R.id.emptyTaskView));
        RecyclerViewEmptySupport.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        tasks = new ArrayList<>();
        mRecyclerView.setAdapter(new TaskRecyclerViewAdapter(tasks, this));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        String date = args.getString("weekDate");
        db = Database.getInstance();
        db.getTasksForDay(date, this);
    }

    /**
     * Creates the task elements that are shown in the main window
     * @param task the task object used to create the element
     */
    public void createTaskItems(final Task task) {

        tasks.add(task);

        // Sorting the times in an ascending order
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return o1.getTime().compareTo(o2.getTime());
            }
        });

        RecyclerViewEmptySupport.Adapter mAdapter = new TaskRecyclerViewAdapter(tasks, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void successHandler(ArrayList<?> list) {
        Database db = Database.getInstance();
        for(Object taskId : list) {
            db.getTask((String) taskId, this);
        }
    }

    @Override
    public void errorHandler() {}

    @Override
    public void passObject(Object object) {
        createTaskItems((Task) object);
    }

    /**
     * When clicking on a specific tasks, an alert window opens and asks the user if the task is done
     * @param task The task object received from the CallBackHandler
     */
    @Override
    public void onListFragmentInteraction(final Task task) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.prompt_set_as_done))
                .setMessage(getResources().getString(R.string.prompt_set_task_as_done) + task.getName())
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        db.setTaskWaiting(task);
                        Toast.makeText(getActivity(), getResources().getString(R.string.prompt_task_set_done), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }
}

package com.example.ryhma4.taskimatti.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ryhma4.taskimatti.Controller.TaskController;
import com.example.ryhma4.taskimatti.R;
import com.example.ryhma4.taskimatti.calendar.DateTimeInterpreter;
import com.example.ryhma4.taskimatti.calendar.MonthLoader;
import com.example.ryhma4.taskimatti.calendar.WeekView;
import com.example.ryhma4.taskimatti.calendar.WeekViewEvent;
import com.example.ryhma4.taskimatti.model.Reminder;
import com.example.ryhma4.taskimatti.model.Task;
import com.example.ryhma4.taskimatti.notification.AlarmReceiver;
import com.example.ryhma4.taskimatti.notification.NotificationService;
import com.example.ryhma4.taskimatti.utility.CallbackHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by kurki on 7.3.2018.
 * This is a base activity which contains week view and all the codes necessary to initialize the
 * week view.
 * http://alamkanak.github.io
 */
public abstract class SetTaskAbstract extends MainActivity implements WeekView.EventClickListener,
        MonthLoader.MonthChangeListener,
        WeekView.EventLongPressListener,
        WeekView.EmptyViewLongPressListener,
        CallbackHandler {
    private WeekView mWeekView;
    private GridView tasksGrid;
    private ArrayAdapter<String> adapter;
    private ArrayList<Task> tasks;
    private ArrayList<String> taskNames;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Calendar date;
    private int year, mon, day, hour, minute;
    private TaskController tc;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_task);

        // Get a reference for the week view in the layout.
        mWeekView = findViewById(R.id.weekView);
        // Get a reference for the task grid in the layout.
        tasksGrid = findViewById(R.id.taskGrid);
        tasksGrid.setEmptyView(findViewById(R.id.taskGridEmpty));

        tc = TaskController.getInstance();
        tc.setTaskAbstract(this);
        tasks = tc.getActiveTasks();
        taskNames = tc.getActiveTaskNames();

        setupActionBar();

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        mWeekView.setHourHeight(90);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set long press listener for empty view
        mWeekView.setEmptyViewLongPressListener(this);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(true);

        date = Calendar.getInstance();
        mWeekView.goToHour(date.get(Calendar.HOUR_OF_DAY));

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        lastDate.add(Calendar.DATE, 13);

        Calendar firstDate = Calendar.getInstance();
        firstDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        firstDate.add(Calendar.DATE, -7);

        mWeekView.setMinDate(firstDate);
        mWeekView.setMaxDate(lastDate);

        updateTasksGrid();
    }

    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     *
     * @param shortDate True if the date values should be short.
     */
    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" d.M", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0) + String.valueOf(weekday.charAt(1)));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 0) + ".00" : (hour == 0 ? "00.00" : hour + ".00");
            }
        });
    }

    /**
     * Setting task's date
     */
    private void setTaskClickListener() {

        tasksGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                View vDate = LayoutInflater.from(SetTaskAbstract.this)
                        .inflate(R.layout.dialog_date, null);
                datePicker = vDate.findViewById(R.id.dialog_date_date_picker);
                datePicker.setMinDate(System.currentTimeMillis() - 1000);

                new AlertDialog.Builder(SetTaskAbstract.this)
                        .setView(vDate)
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        year = datePicker.getYear();
                                        mon = datePicker.getMonth();
                                        day = datePicker.getDayOfMonth();

                                        View vTime = LayoutInflater.from(SetTaskAbstract.this)
                                                .inflate(R.layout.dialog_time, null);
                                        timePicker = vTime.findViewById(R.id.dialog_time_picker);
                                        timePicker.setIs24HourView(true);
                                        timePicker.setMinute(0);

                                        new AlertDialog.Builder(SetTaskAbstract.this)
                                                .setView(vTime)
                                                .setPositiveButton(android.R.string.ok,
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int whichButton) {

                                                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                                                    hour = timePicker.getHour();
                                                                    minute = timePicker.getMinute();
                                                                } else {
                                                                    hour = timePicker.getCurrentHour();
                                                                    minute = timePicker.getCurrentMinute();
                                                                }
                                                                date.set(year, mon, day, hour, minute, 0);
                                                                Reminder reminder = new Reminder();
                                                                reminder.setDate(date);
                                                                reminder.setTitle(tasks.get(position).getName());
                                                                reminder.setContent(tasks.get(position).getDescription());
                                                                NotificationService.setReminder(getBaseContext(), AlarmReceiver.class, reminder);

                                                                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                                                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                                                String timeStr = timeFormat.format(date.getTime());
                                                                String dateStr = dateFormat.format(date.getTime());

                                                                Task setTask = tasks.get(position);
                                                                setTask.setState("set");
                                                                setTask.setDate(dateStr);
                                                                setTask.setTime(timeStr);

                                                                tc.setTaskStateToSet(setTask);
                                                                tc.removeTaskFromActive(setTask);
                                                                tc.addSetTask(setTask);

                                                                Snackbar snackbar = Snackbar.make(getWeekView(),
                                                                        MainActivity.globalRes.getString(R.string.text_task_set_to) + dateStr + ", " + timeStr
                                                                        , Snackbar.LENGTH_LONG);
                                                                snackbar.show();
                                                                updateAdapters();

                                                            }
                                                        })
                                                .setNegativeButton(android.R.string.no, null)
                                                .show();
                                    }
                                })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });
    }

    /**
     * Editing the task
     */
    private void editTaskLongClickListener() {
        tasksGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View taskEditView = inflater.inflate(R.layout.task_field, null, false);
                final EditText taskName = taskEditView.findViewById(R.id.taskName);
                final EditText taskDesc = taskEditView.findViewById(R.id.taskDescription);

                // Set the fields to match the task
                taskName.setText(tasks.get(position).getName());
                taskDesc.setText(tasks.get(position).getDescription());

                new AlertDialog.Builder(SetTaskAbstract.this)
                        .setView(taskEditView)
                        .setTitle(MainActivity.globalRes.getString(R.string.prompt_task_edit) + ": " + tasks.get(position).getName())
                        .setMessage(MainActivity.globalRes.getString(R.string.param_description) + ": " + tasks.get(position).getDescription())
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                String taskNameString = taskName.getText().toString();
                                String taskDescString = taskDesc.getText().toString();

                                Task taskToEdit = tc.getTaskByName(tasks.get(position).getName());
                                if (!taskNameString.isEmpty()) {
                                    taskToEdit.setName(taskNameString);
                                    Toast.makeText(SetTaskAbstract.this, MainActivity.globalRes.getString(R.string.prompt_task_edit_success), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SetTaskAbstract.this, MainActivity.globalRes.getString(R.string.error_cant_change_to_empty), Toast.LENGTH_SHORT).show();
                                }
                                if (!taskDescString.isEmpty()) {
                                    taskToEdit.setDescription(taskDescString);
                                    Toast.makeText(SetTaskAbstract.this, MainActivity.globalRes.getString(R.string.prompt_task_edit_success), Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(SetTaskAbstract.this, MainActivity.globalRes.getString(R.string.error_cant_change_to_empty), Toast.LENGTH_SHORT).show();
                                }

                                tc.updateTask(taskToEdit);

                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                return true;
            }
        });
    }

    /**
     * Updates the task grid view in the calendar view. Also creates alert dialogs for date and time pickers so that the task can be assigned to a specific time.
     */
    public void updateTasksGrid() {

        tc.fetchTasks();
        tasks = tc.getActiveTasks();
        taskNames = tc.getActiveTaskNames();

        adapter = new ArrayAdapter<String>(this, R.layout.task_grid_item, taskNames) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View row = super.getView(position, convertView, parent);

                row.setBackgroundColor(Color.parseColor(tc.getTypeOfTask(tasks.get(position)).getColor()));
                return row;
            }
        };

        tasksGrid.setAdapter(adapter);
        updateAdapters();

        setTaskClickListener();

        editTaskLongClickListener();

    }


    protected String getEventTitle(Calendar time) {
        return String.format(Locale.getDefault(), "Event of %02d:%02d %s.%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.DAY_OF_MONTH), time.get(Calendar.MONTH) + 1);
    }

    public void updateAdapters() {
        mWeekView.notifyDatasetChanged();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Snackbar snackbar = Snackbar.make(getWeekView(),
                event.getName() + ": " + event.getStartTime().getTime(), Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
//        Toast.makeText(this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
//        Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();
    }

    public WeekView getWeekView() {
        return mWeekView;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void successHandler(ArrayList<?> list) {}

    @Override
    public void errorHandler() {}

    @Override
    public void passObject(Object object) {}
}

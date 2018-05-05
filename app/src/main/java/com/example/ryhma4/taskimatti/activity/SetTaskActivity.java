package com.example.ryhma4.taskimatti.activity;

import android.graphics.Color;

import com.example.ryhma4.taskimatti.Controller.TaskController;
import com.example.ryhma4.taskimatti.R;
import com.example.ryhma4.taskimatti.calendar.WeekViewEvent;
import com.example.ryhma4.taskimatti.model.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by kurki on 7.3.2018.
 */

public class SetTaskActivity extends SetTaskAbstract {

    private static int id = 1;

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        // Populate the week view with some events.
        List<WeekViewEvent> events = new ArrayList<>();
        TaskController tc = TaskController.getInstance();
        tc.setTaskActivity(this);

//        EventLister eventLister = EventLister.getInstance();
        List<Task> tasks = tc.getSetTasks();
        System.out.println("TASKCONTROLLER SET TASKS SIZE: " + tasks.size());
        Calendar startTime;
        Calendar endTime;
        for (Task task : tasks) {
            startTime = Calendar.getInstance();

            String pattern = "yyyy-MM-dd HH:mm";
            SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
            SimpleDateFormat hourFormat = new SimpleDateFormat("HH", Locale.getDefault());
            SimpleDateFormat minuteFormat = new SimpleDateFormat("mm", Locale.getDefault());
            Date dateTime = null;
            try {
                dateTime = format.parse(task.getDate() + " " + task.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            startTime.setTime(dateTime);
            startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourFormat.format(dateTime)));
            startTime.set(Calendar.MINUTE, Integer.parseInt(minuteFormat.format(dateTime)));
            startTime.set(Calendar.MONTH, newMonth - 1);
            startTime.set(Calendar.YEAR, newYear);
            endTime = (Calendar) startTime.clone();
            endTime.add(Calendar.HOUR, task.getHours());
            endTime.add(Calendar.MINUTE, task.getMinutes());
            endTime.set(Calendar.MONTH, newMonth - 1);
            WeekViewEvent event = new WeekViewEvent(id++, task.getName(), startTime, endTime);
            event.setColor(Color.parseColor(tc.getTypeOfTask(task).getColor()));
            events.add(event);
        }
        return events;
    }

}


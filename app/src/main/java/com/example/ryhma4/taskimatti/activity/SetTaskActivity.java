package com.example.ryhma4.taskimatti.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import com.example.ryhma4.taskimatti.Controller.Database;
import com.example.ryhma4.taskimatti.R;
import com.example.ryhma4.taskimatti.calendar.WeekViewEvent;
import com.example.ryhma4.taskimatti.model.Task;
import com.example.ryhma4.taskimatti.utility.CallbackHandler;

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

public class SetTaskActivity extends SetTaskAbstract implements CallbackHandler {

    private List<WeekViewEvent> events;
    private Calendar startTime, endTime;

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        // Populate the week view with some events.

        events = new ArrayList<>();

//        Database db = Database.getInstance();
//        populateEventList(db);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR, 1);
        endTime.set(Calendar.MONTH, newMonth-1);
        WeekViewEvent event = new WeekViewEvent(1, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_01));
        events.add(event);

        return events;
    }

    public void addToEventList(Task task) {
        startTime = Calendar.getInstance();

        String pattern = "yyyy-MM-dd HH:mm";
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
        Date dateTime = null;
        try {
            dateTime = format.parse(task.getDate() + " " + task.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        startTime.setTime(dateTime);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, task.getHours());
        endTime.add(Calendar.HOUR_OF_DAY, task.getMinutes());

        WeekViewEvent event = new WeekViewEvent(Integer.parseInt(task.getTaskID()), task.getName(), startTime, endTime);
        events.add(event);
    }

    public void populateEventList(Database db) {
        db.getSetTasks(SetTaskActivity.this);
    }

    @Override
    public void successHandler(ArrayList<?> list) {}

    @Override
    public void errorHandler() {}

    @Override
    public void passObject(Object object) {
        addToEventList((Task) object);
    }

    /*@Override
    public double toWeekViewPeriodIndex(Calendar instance) {
        return 0;
    }

    @Override
    public List<? extends WeekViewEvent> onLoad(int periodIndex) {
       return null;
    }
    */
}


package com.example.ryhma4.taskimatti.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import com.example.ryhma4.taskimatti.Controller.Database;
import com.example.ryhma4.taskimatti.R;
import com.example.ryhma4.taskimatti.calendar.EventLister;
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
        EventLister eventLister = EventLister.getInstance();
        List<Task> tasks = eventLister.getTasks();

//        Database db = Database.getInstance();
//        populateEventList(db);
        int id = 1;
        for(Task task : tasks) {
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
            endTime.add(Calendar.HOUR, 1);
            endTime.add(Calendar.MINUTE, 30);
            endTime.set(Calendar.MONTH, newMonth - 1);
            WeekViewEvent event = new WeekViewEvent(id++, task.getName(), startTime, endTime);
            System.out.println("HOURS: " + task.getHours() + " MINUTES: " + task.getMinutes());
            event.setColor(getResources().getColor(R.color.event_color_02));
//            System.out.println("START: " + event.getStartTime() + " || END: " + event.getEndTime());
            events.add(event);
        }

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
//        System.out.println("EXAMPLE START: " + event.getStartTime() + "EXAMPLE END: " + event.getEndTime());
        events.add(event);

        eventLister.clearList();
        return events;
    }

    public void addToEventList(Task task) {
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


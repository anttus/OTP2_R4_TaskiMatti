package com.example.ryhma4.taskimatti.calendar;

import android.provider.ContactsContract;

import com.example.ryhma4.taskimatti.Controller.Database;
import com.example.ryhma4.taskimatti.model.Task;
import com.example.ryhma4.taskimatti.utility.CallbackHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class to store set tasks into an event list for the calendar view.
 */
public class EventLister implements CallbackHandler{
    private static EventLister instance;
    private List<Task> tasks;
    private Database database;

    private EventLister() {
        tasks = new ArrayList<>();
        database = Database.getInstance();
    }

    public static EventLister getInstance() {
        if (instance == null) {
            synchronized(EventLister.class) {
                if (instance == null) {
                    instance = new EventLister();
                }
            }
        }
        return instance;
    }

    public void setList() {
        database.getSetTasks(this);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void clearList() {
        tasks.clear();
    }

    @Override
    public void successHandler(ArrayList<?> list) {

    }

    @Override
    public void errorHandler() {

    }

    @Override
    public void passObject(Object object) {
        Task task = (Task) object;
        if(tasks.indexOf(task) < 0) {
            tasks.add(task);
        }
    }
}

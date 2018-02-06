package com.example.ryhma4.taskimatti.data;

import java.util.ArrayList;

/**
 * Created by mikae on 6.2.2018.
 */

public class User {

    private String name, email, userID;
    private ArrayList<Routine> routines;
    private ArrayList<Task> tasks;

    public User(String userID, String name, String email) {
        this.userID = userID;
    }

    public void addRoutine(Routine routine) {
        routines.add(routine);
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Routine> getRoutines() {
        return routines;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }
}

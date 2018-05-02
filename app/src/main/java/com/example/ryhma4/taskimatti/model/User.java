package com.example.ryhma4.taskimatti.model;

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
        this.name = name;
        this.email = email;
    }


    public String getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<Routine> getRoutines() {
        return routines;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void addRoutine(Routine routine) {
        routines.add(routine);
    }

    public void addTask(Task task) {
        tasks.add(task);
    }
}

package com.example.ryhma4.taskimatti.model;

import java.util.UUID;

/**
 * Created by mikae on 6.2.2018.
 */

public class Task {
    private String taskID, routineID, name, description, typeID, state, date, time;
    private int hours, minutes;

    public Task(String routineID, String name, String description, int hours, int minutes) {
        taskID = UUID.randomUUID().toString();
        this.routineID = routineID;
        this.name = name;
        this.description = description;
        this.hours = hours;
        this.minutes = minutes;
        state = "waiting";
        date = "";
        time = "";
    }

    public Task() {}

// START getters and setters

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getTaskID() { return taskID; }

    public void setTaskID(String taskID) { this.taskID = taskID; }

    public String getRoutineID() { return routineID; }

    public void setRoutineID(String routineID) { this.routineID = routineID; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getTypeID() { return typeID; }

    public void setTypeID(String typeID) { this.typeID = typeID; }

    public String getState() { return state; }

    public void setState(String state) { this.state = state; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

// END getters and setters
}

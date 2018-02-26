package com.example.ryhma4.taskimatti.model;

import java.sql.Time;
import java.util.UUID;

/**
 * Created by mikae on 6.2.2018.
 */

public class Task {
    private String taskID, routineID, name, description, typeID, state;
    private Time setTo;

    public Task(String routineID, String name, String description) {
        taskID = UUID.randomUUID().toString();
        this.routineID = routineID;
        this.name = name;
        this.description = description;
        state = "waiting";
        setTo = null;
    }

// START getters and setters
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

    public Time getSetTo() { return setTo; }

    public void setSetTo(Time setTo) { this.setTo = setTo; }
// END getters and setters
}

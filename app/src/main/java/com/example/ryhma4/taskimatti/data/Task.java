package com.example.ryhma4.taskimatti.data;

import java.sql.Time;
import java.util.UUID;

/**
 * Created by mikae on 6.2.2018.
 */

public class Task {
    private String taskID, routineID, name, description, typeID;
    private boolean waiting, active;
    private Time setTo;

    public Task(String routineID) {
        taskID = UUID.randomUUID().toString();
        this.routineID = routineID;
        waiting = true;
        setTo = null;
    }

    public void setWaiting() {
        waiting = true;
        active = false;
        setTo = null;
    }

    public void setActive() {
        active = true;
        waiting = false;
        setTo = null;
    }

    public void setSetTo() {

    }

}

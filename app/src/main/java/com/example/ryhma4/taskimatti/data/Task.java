package com.example.ryhma4.taskimatti.data;

import java.sql.Time;
import java.util.UUID;

/**
 * Created by mikae on 6.2.2018.
 */

public class Task {
    private String taskID, routineID;
    private boolean waiting, active, done;
    private Time setTo;

    public Task(String routineID) {
        taskID = UUID.randomUUID().toString();
        this.routineID = routineID;
        waiting = true;
        active = false;
        done = false;
        setTo = null;
    }

    public void setWaiting() {
        waiting = true;
        active = false;
        done = false;
        setTo = null;
    }

    public void setActive() {
        active = true;
        waiting = false;
        done = false;
        setTo = null;
    }

    public void setDone() {
        done = true;
        waiting = false;
        active = false;
        setTo = null;
    }

    public void setSetTo() {

    }

}

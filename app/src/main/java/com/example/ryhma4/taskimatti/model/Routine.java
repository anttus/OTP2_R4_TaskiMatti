package com.example.ryhma4.taskimatti.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by mikae on 5.2.2018.
 */

@IgnoreExtraProperties
public class Routine {

    private FirebaseAuth mAuth;
    private String routineId, name, author, description, repeat, date;
    private Type type;
    private int times, hours, minutes;

    public Routine() {
    }

    /**
     * @param name
     * @param type
     * @param times
     * @param repeat
     * @param hours
     * @param minutes
     * @param description
     */
    public Routine(String name, Type type, int times, String repeat, int hours, int minutes, String description) {
        routineId = UUID.randomUUID().toString();
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        mAuth = FirebaseAuth.getInstance();

        this.name = name;
        this.type = type;
        this.times = times;
        this.repeat = repeat;
        this.hours = hours;
        this.minutes = minutes;
        this.description = description;
        author = mAuth.getUid();
    }

    public String getRoutineId() {
        return routineId;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getRepeat() {
        return repeat;
    }

    public String getDate() {
        return date;
    }

    public Type getType() {
        return type;
    }

    public int getTimes() {
        return times;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setId(String routineId) {
        this.routineId = routineId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}

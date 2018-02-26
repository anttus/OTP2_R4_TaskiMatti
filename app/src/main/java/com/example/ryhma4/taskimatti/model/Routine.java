package com.example.ryhma4.taskimatti.model;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by mikae on 5.2.2018.
 */

public class Routine {

    private FirebaseAuth mAuth;
    private String ID, name, author, description, repeat, date;
    private Type type;
    private int times, hours, minutes;

    public Routine(String name, Type type, int times, String repeat, int hours, int minutes, String description) {
        ID = UUID.randomUUID().toString();
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

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

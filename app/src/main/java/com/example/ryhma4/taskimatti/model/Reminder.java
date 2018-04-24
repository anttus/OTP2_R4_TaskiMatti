package com.example.ryhma4.taskimatti.model;

import java.util.Calendar;

public class Reminder {
    private String title, content;
    private Calendar date;

    public Reminder() {}

    public Reminder(String title, String content, Calendar date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }
}

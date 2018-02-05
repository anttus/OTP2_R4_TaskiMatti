package com.example.ryhma4.taskimatti;

/**
 * Created by mikae on 5.2.2018.
 */

public class Type {
    private String name, color;

    public Type(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

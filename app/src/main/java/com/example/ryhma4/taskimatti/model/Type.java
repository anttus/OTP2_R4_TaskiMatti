package com.example.ryhma4.taskimatti.model;

import java.util.UUID;

/**
 * Created by mikae on 5.2.2018.
 */

public class Type {
    private String name, color, typeId;

    public Type() {
    }

    public Type(String name, String color) {
        this.name = name;
        this.color = color;typeId = UUID.randomUUID().toString();

    }
    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
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

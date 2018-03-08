package com.example.ryhma4.taskimatti.database;

import com.example.ryhma4.taskimatti.model.Routine;
import com.example.ryhma4.taskimatti.model.Task;

import java.util.ArrayList;

/**
 * Created by mikae on 12.2.2018.
 */

public interface CallbackHandler {

    public void successHandler(ArrayList<String> list);

    public void errorHandler();

    public void passObject(Object object);
}

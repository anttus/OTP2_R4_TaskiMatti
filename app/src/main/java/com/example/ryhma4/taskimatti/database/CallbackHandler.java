package com.example.ryhma4.taskimatti.database;

import com.example.ryhma4.taskimatti.model.Routine;

/**
 * Created by mikae on 12.2.2018.
 */

public interface CallbackHandler {

    public void successHandler();

    public void errorHandler();

    public void passRoutine(Routine routine);
}

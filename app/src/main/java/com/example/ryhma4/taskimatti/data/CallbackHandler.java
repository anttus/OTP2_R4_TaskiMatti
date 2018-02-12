package com.example.ryhma4.taskimatti.data;

/**
 * Created by mikae on 12.2.2018.
 */

public interface CallbackHandler {

    public void successHandler();

    public void errorHandler();

    public void passRoutine(Routine routine);
}

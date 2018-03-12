package com.example.ryhma4.taskimatti.utility;

import java.util.ArrayList;

/**
 * Created by mikae on 12.2.2018.
 */

public interface CallbackHandler {

    void successHandler(ArrayList<?> list);

    void errorHandler();

    void passObject(Object object);
}

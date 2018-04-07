package com.example.ryhma4.taskimatti.calendar;

import java.util.Calendar;

/**
 * Created by kurki on 7.4.2018.
 */

public interface DateTimeInterpreter {
    String interpretDate(Calendar date);
    String interpretTime(int hour);
}

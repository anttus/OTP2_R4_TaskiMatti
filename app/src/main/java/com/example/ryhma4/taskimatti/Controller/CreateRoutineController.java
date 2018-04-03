package com.example.ryhma4.taskimatti.Controller;

import com.example.ryhma4.taskimatti.model.Routine;
import com.example.ryhma4.taskimatti.model.Task;

import java.util.ArrayList;

/**
 * Created by anttu on 26.2.2018.
 */

public class CreateRoutineController {
    private Database db = Database.getInstance();

    public void setRoutine(Routine routine) {
        db.setRoutine(routine);
    }

    public void setTask(ArrayList<Task> tasks) {
        db.setTask(tasks);
    }

}

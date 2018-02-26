package com.example.ryhma4.taskimatti.Controller;

import com.example.ryhma4.taskimatti.database.Database;
import com.example.ryhma4.taskimatti.model.Routine;
import com.example.ryhma4.taskimatti.model.Task;

/**
 * Created by anttu on 26.2.2018.
 */

public class CreateRoutineController {
    private Database db = new Database();

    public void setRoutine(Routine routine) {
        db.setRoutine(routine);
    }

    public void setTask(Task task) {
        db.setTask(task);
    }

}

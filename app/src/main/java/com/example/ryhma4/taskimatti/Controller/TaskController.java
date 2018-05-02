package com.example.ryhma4.taskimatti.Controller;

import com.example.ryhma4.taskimatti.model.Task;
import com.example.ryhma4.taskimatti.model.Type;
import com.example.ryhma4.taskimatti.utility.CallbackHandler;

import java.util.ArrayList;

public class TaskController implements CallbackHandler {
    private static TaskController instance;
    private Database db;
    private ArrayList<Task> activeTasks, setTasks;
    private ArrayList<String> activeTaskNames;
    private RoutineController rc;


    private TaskController() {
        db = Database.getInstance();
        activeTasks = new ArrayList<>();
        setTasks = new ArrayList<>();
        activeTaskNames = new ArrayList<>();
        rc = RoutineController.getInstance();

        fetchTasks();
    }

    public static TaskController getInstance() {
        if (instance == null) {
            synchronized (TaskController.class) {
                if (instance == null) {
                    instance = new TaskController();
                }
            }
        }
        return instance;
    }

    public void fetchTasks() {
        fetchActiveTasks();
        fetchSetTasks();
    }

    public void fetchActiveTasks() {
        db.getActiveTasks(this);
    }

    public void fetchSetTasks() {
        db.getSetTasks(this);
    }

    public void removeTaskFromActive(Task task) {
        activeTasks.remove(findIndex(task, activeTasks));
        activeTaskNames.remove(activeTaskNames.indexOf(task.getName()));
    }

    public void removeTaskFromSet(Task task) {
        setTasks.remove(findIndex(task, setTasks));
    }

    public ArrayList<Task> getActiveTasks() {
        return activeTasks;
    }

    public ArrayList<Task> getSetTasks() {
        return setTasks;
    }

    public ArrayList<String> getActiveTaskNames() {
        return activeTaskNames;
    }

    public void updateSetTasks() {
        setTasks.clear();
        fetchSetTasks();
    }

    public void clearSetTasks() {
        setTasks.clear();
    }

    public void addSetTask(Task task) {
        if (findIndex(task, setTasks) < 0) {
            setTasks.add(task);
        }
    }
    public Type getTypeOfTask(Task task) {
        return rc.getTypeById(task.getTypeID());
    }

    public void setTaskStateToSet(Task task) {

        db.setTaskSet(task.getTaskID(), task.getDate(), task.getTime());
    }

    public int findIndex(Task task, ArrayList<Task> list) {
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if (task.getTaskID().equals(list.get(i).getTaskID())) {
                index = i;
            }
        }
        return index;
    }

    @Override
    public void successHandler(ArrayList<?> list) {

    }

    @Override
    public void errorHandler() {

    }

    @Override
    public void passObject(Object object) {
        Task task = (Task) object;

        switch (task.getState()) {
            case "set":
                if (findIndex(task, setTasks) < 0) {
                    setTasks.add(task);
                }
                break;
            case "active":
                if (findIndex(task, activeTasks) < 0) {
                    activeTasks.add(task);
                    activeTaskNames.add(task.getName());
                }
                break;
        }
    }
}

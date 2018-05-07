package com.example.ryhma4.taskimatti.Controller;

import com.example.ryhma4.taskimatti.activity.SetTaskAbstract;
import com.example.ryhma4.taskimatti.activity.SetTaskActivity;
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
    private SetTaskAbstract taskAbstract;
    private SetTaskActivity taskActivity;


    private TaskController() {
        db = Database.getInstance();
        activeTasks = new ArrayList<>();
        setTasks = new ArrayList<>();
        activeTaskNames = new ArrayList<>();
        rc = RoutineController.getInstance();
        taskAbstract = null;
        taskActivity = null;
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

    public void setTaskAbstract(SetTaskAbstract taskAbstract) {
        this.taskAbstract = taskAbstract;
    }

    public void setTaskActivity(SetTaskActivity taskActivity) {
        this.taskActivity = taskActivity;
    }

    /**
     * Fetches tasks from the database that have their state set to 'active' or 'set'.
     */
    public void fetchTasks() {
        fetchActiveTasks();
        fetchSetTasks();
    }

    /**
     * Fetch tasks from the database that have their state set as 'active'
     */
    public void fetchActiveTasks() {
        db.getActiveTasks(this);
    }

    /**
     * Fetch tasks from the database that have their state set as 'set'
     */
    public void fetchSetTasks() {
        db.getSetTasks(this);
    }

    /**
     * Removes the given task from activeTasks and activeTaskNames arrays
     * @param task object to be removed
     */
    public void removeTaskFromActive(Task task) {
        activeTasks.remove(findIndex(task, activeTasks));
        activeTaskNames.remove(activeTaskNames.indexOf(task.getName()));
    }

    /**
     * Removes the given task from the setTasks array
     * @param task
     */
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

    /**
     * Clears the setTasks list and triggers a new fetch for active and set tasks
     */
    public void updateSetTasks() {
        setTasks.clear();
        fetchSetTasks();
    }

    /**
     * Clears and updates the setTasks, activeTasks and activeTaskNames lists
     */
    public void updateTasks() {
        setTasks.clear();
        activeTaskNames.clear();
        activeTasks.clear();
        fetchTasks();
    }

    /**
     * Updates the calendar and task grids.
     */
    public void updateAdapters() {
        if(taskAbstract != null) {
            taskAbstract.updateAdapters();
        }
    }

    /**
     * Clears the setTasks array
     */
    public void clearSetTasks() {
        setTasks.clear();
    }

    /**
     * Adds a task object to the setTasks array
     * @param task object to be added
     */
    public void addSetTask(Task task) {
        if (findIndex(task, setTasks) < 0) {
            setTasks.add(task);
        }
    }

    /**
     * Gets the Type object associated with the given Task object from the RoutineController
     * @param task the Task object whose Type is wanted
     * @return Type object
     */
    public Type getTypeOfTask(Task task) {
        return rc.getTypeById(task.getTypeID());
    }

    /**
     * Changes the given task objects state to set in the database.
     * @param task
     */
    public void setTaskStateToSet(Task task) {
        db.setTaskSet(task.getTaskID(), task.getDate(), task.getTime());
    }

    /**
     * Finds the given Tasks index from the given ArrayList.
     * @param task object
     * @param list ArrayList of Task objects
     * @return int index of given task -1 if task not found.
     */
    public int findIndex(Task task, ArrayList<Task> list) {
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if (task.getTaskID().equals(list.get(i).getTaskID())) {
                index = i;
            }
        }
        return index;
    }

    /**
     * Returns a task
     * @param taskName The wanted task's name
     * @return Task
     */
    public Task getTaskByName(String taskName) {
        Task task = null;
        for (Task t : activeTasks) {
            if (t.getName().equals(taskName)) {
                task = t;
            }
        }
        return task;
    }

    public void updateTask(Task task) {
        int taskIndex = findIndex(task, activeTasks);
        activeTasks.set(taskIndex, task);
        activeTaskNames.set(taskIndex, task.getName());
        db.updateTask(task);
        updateAdapters();
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
//                    System.out.println("TC: passed set task updating adapters. Size: " + setTasks.size() );
                    updateAdapters();
                }
                break;
            case "active":
                if (findIndex(task, activeTasks) < 0) {
                    activeTasks.add(task);
                    activeTaskNames.add(task.getName());
//                    System.out.println("TC: passed active task updating adapters");
                    updateAdapters();
                }
                break;
        }
    }
}

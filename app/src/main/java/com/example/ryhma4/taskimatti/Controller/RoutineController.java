package com.example.ryhma4.taskimatti.Controller;

import android.content.Context;

import com.example.ryhma4.taskimatti.R;
import com.example.ryhma4.taskimatti.activity.MainActivity;
import com.example.ryhma4.taskimatti.activity.ShowRoutinesActivity;
import com.example.ryhma4.taskimatti.model.Routine;
import com.example.ryhma4.taskimatti.model.Task;
import com.example.ryhma4.taskimatti.model.Type;
import com.example.ryhma4.taskimatti.utility.CallbackHandler;
import com.example.ryhma4.taskimatti.utility.ExapandableListAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by anttu on 26.2.2018.
 */

public class RoutineController implements CallbackHandler{
    private static RoutineController instance;
    private Database db;
    private ArrayList<Routine> routines;
    private ArrayList<Type> types;
    private ArrayList<ArrayList<Routine>> routinesByType;
    private HashMap<Type, ArrayList<Routine>> routinesByHeader;
    private ExapandableListAdapter listAdapter;

    private RoutineController () {
        db = Database.getInstance();
        routines = new ArrayList<>();
        types = new ArrayList<>();
        routinesByType = new ArrayList<>();
        routinesByType.add(new ArrayList<Routine>());
        routinesByHeader = new HashMap<>();

        fetchRoutines();
    }

    public static RoutineController getInstance() {
        if (instance == null) {
            synchronized(RoutineController.class) {
                if (instance == null) {
                    instance = new RoutineController();
                }
            }
        }
        return instance;
    }

    public void fetchRoutines() {
        Type allType = new Type();
        allType.setColor("#ffffff");
        allType.setName(MainActivity.globalRes.getString(R.string.text_all));
        if(findTypeIndex(allType.getName()) < 0) {
            types.add(allType);
        }
        db.getUserRoutines(this);
    }

    public void removeRoutine(Routine routine) {
        int routineIndex = findRoutineIndex(routine.getName());
        int typeIndex = findTypeIndex(routine.getType().getName());
        if(routinesByType.get(typeIndex).size() <= 1) {
            types.remove(typeIndex);
        }
        routines.remove(routineIndex);
        getRoutinesByHeader();
    }

    public void updateRoutines() {
        routines.clear();
        types.clear();
        fetchRoutines();
    }

    public void setListAdapter(Context context) {
        listAdapter = new ExapandableListAdapter(context,types, getRoutinesByHeader() );
    }

    public ArrayList<Routine> getRoutines() {
        return routines;
    }

    public void setRoutinesByType() {
        routinesByType.clear();
        routinesByType.add(new ArrayList<Routine>());

        for(Routine routine : routines) {
            int index = findTypeIndex(routine.getType().getName());
            if(index > routinesByType.size() - 1) {
                routinesByType.add(new ArrayList<Routine>());
            }
            routinesByType.get(0).add(routine);
            routinesByType.get(index).add(routine);
        }
    }

    public ArrayList<ArrayList<Routine>> getRoutinesByType() {
        return routinesByType;
    }

    public HashMap<Type, ArrayList<Routine>> getRoutinesByHeader() {
        routinesByHeader.clear();
        setRoutinesByType();
        for (int i = 0; i < types.size(); i++) {
            routinesByHeader.put(types.get(i), routinesByType.get(i));
        }
        return routinesByHeader;
    }

    public ArrayList<Type> getTypes() {
        return types;
    }

    public void setRoutine(Routine routine) {
        db.setRoutine(routine);
    }

    /**
     * Finds the index of the type from the types list
     * @param name Name of the type
     * @return Returns the index of the type or -1 if not found
     */
    public int findTypeIndex(String name) {
        int index = -1;
        for(int i = 0; i < types.size(); i++) {
            if(name.equals(types.get(i).getName())) {
                index = i;
            }
        }
        return index;
    }

    /**
     * Finds the index of the routine from the routines list
     * @param name Name of the routine
     * @return Returns the index of the routine or -1 if not found
     */
    public int findRoutineIndex(String name) {
        int index = -1;
        for(int i = 0; i < routines.size(); i++) {
            if(name.equals(routines.get(i).getName())){
                index = i;
            }
        }
        return index;
    }

    @Override
    public void successHandler(ArrayList<?> list) { }

    @Override
    public void errorHandler() {

    }

    @Override
    public void passObject(Object object) {
        Routine routine = (Routine) object;
        if(findRoutineIndex(routine.getName()) < 0) {
            routines.add(routine);
        }

        Type type = routine.getType();
        if(findTypeIndex(type.getName()) < 0) {
            types.add(type);
        }
    }

    public void setTask(ArrayList<Task> tasks) {
        Database db = Database.getInstance();
        db.setTask(tasks);
    }

}

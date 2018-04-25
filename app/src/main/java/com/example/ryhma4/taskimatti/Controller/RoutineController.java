package com.example.ryhma4.taskimatti.Controller;

import com.example.ryhma4.taskimatti.R;
import com.example.ryhma4.taskimatti.activity.MainActivity;
import com.example.ryhma4.taskimatti.model.Routine;
import com.example.ryhma4.taskimatti.model.Task;
import com.example.ryhma4.taskimatti.model.Type;
import com.example.ryhma4.taskimatti.utility.CallbackHandler;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by anttu on 26.2.2018.
 */

public class RoutineController implements CallbackHandler{
    private Database db;
    private ArrayList<Routine> routines;
    private ArrayList<Type> types;
    private ArrayList<Type> listTypes;
    private ArrayList<ArrayList<Routine>> routinesByType;
    private HashMap<Type, ArrayList<Routine>> routinesByHeader;

    public RoutineController () {
        db = Database.getInstance();
        routines = new ArrayList<>();
        types = new ArrayList<>();
        routinesByType = new ArrayList<>();
        routinesByType.add(new ArrayList<Routine>());
        routinesByHeader = new HashMap<>();
        listTypes = new ArrayList<>();

        Type allType = new Type();
        allType.setColor("#ffffff");
        allType.setName(MainActivity.globalRes.getString(R.string.text_all));
        listTypes.add(allType);
    }

    public void fetchRoutines() {
        db.getUserRoutines(this);
    }

    public ArrayList<Routine> getRoutines() {
        return routines;
    }

    public ArrayList<ArrayList<Routine>> getRoutinesByType() {
        listTypes.clear();
        routinesByType.clear();
        routinesByType.add(new ArrayList<Routine>());

        for(int i = 0; i < routines.size(); i++) {
            int index = findIndex(types.get(i).getName(), listTypes);
            if(index < 0) {
                listTypes.add(types.get(i));
                index = findIndex(types.get(i).getName(), listTypes);
                routinesByType.add(new ArrayList<Routine>());
            }
            routinesByType.get(0).add(routines.get(i));
            routinesByType.get(index).add(routines.get(i));
        }
        return routinesByType;
    }

    public HashMap<Type, ArrayList<Routine>> getRoutinesByHeader() {
        routinesByHeader.clear();
        for (int i = 0; i < listTypes.size(); i++) {
            routinesByHeader.put(listTypes.get(i), routinesByType.get(i));
        }
        return routinesByHeader;
    }

    public ArrayList<Type> getTypes() {
        return types;
    }



    public void fetchTypes() {
        db.listTypes(this);
    }

    public void setRoutine(Routine routine) {
        Database db = Database.getInstance();
        System.out.println("RoutineController attempting to create routine.");
        db.setRoutine(routine);
    }

    /**
     * Finds the index of the types' headers
     * @param typeName Name of the type
     * @return Returns the index of the type
     */
    public int findIndex(String typeName, ArrayList<Type> listDataHeader) {
        int index = -1;
        for(int i = 0; i < listDataHeader.size(); i++) {
            if(typeName.equals(listDataHeader.get(i).getName())) {
                index = i;
            }
        }
        return index;
    }

    @Override
    public void successHandler(ArrayList<?> list) {
        for(Object item : list) {
            Type type = (Type) item;
            if(types.indexOf(type) < 0) {
                types.add(type);
            }
        }
    }

    @Override
    public void errorHandler() {

    }

    @Override
    public void passObject(Object object) {
        Routine routine = (Routine) object;
        routines.add(routine);
    }

    public void setTask(ArrayList<Task> tasks) {
        Database db = Database.getInstance();
        System.out.println("RoutineController attempting to create tasks");
        db.setTask(tasks);
    }

}

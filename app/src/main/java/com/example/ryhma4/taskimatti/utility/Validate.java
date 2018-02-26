package com.example.ryhma4.taskimatti.utility;

import android.text.TextUtils;
import android.widget.EditText;

import com.example.ryhma4.taskimatti.activity.MainActivity;

/**
 * Created by mikae on 14.2.2018.
 */

public class Validate extends MainActivity {

    private boolean isEmpty;

    public Validate() {
    }

    public boolean isTextEmpty(int id) {
        isEmpty = false;
        EditText view = findViewById(id);

        if(TextUtils.isEmpty(view.getText().toString())){
            view.setError("Vaaditaan");
            isEmpty = true;
        }
        return isEmpty;
    }

    public boolean validateEditText(int[] ids) {
        isEmpty = false;

        for(int id: ids) {
            EditText view = findViewById(id);

            if (TextUtils.isEmpty(view.getText().toString())) {
                view.setError("Vaaditaan");
                isEmpty = true;
            }
        }
        return isEmpty;
    }

    public boolean isNumberPositive(int id) {
        boolean isNumber = true;
        EditText view = findViewById(id);
        int number = Integer.parseInt(view.getText().toString());

        if(number <= 0) {
            view.setError("Vaaditaan");
            isNumber = false;
        }
        return isNumber;
    }

    public boolean areNumbersPositive(int[] ids) {
        boolean isNumber = true;

        for(int id: ids) {
            EditText view = findViewById(id);
            int number = Integer.parseInt(view.getText().toString());

            if(number <= 0) {
                view.setError("Vaaditaan");
                isNumber = false;
            }
        }
        return isNumber;
    }
}

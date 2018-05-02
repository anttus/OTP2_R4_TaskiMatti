package com.example.ryhma4.taskimatti.utility;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.EditText;

import com.example.ryhma4.taskimatti.R;
import com.example.ryhma4.taskimatti.activity.MainActivity;

import java.util.ArrayList;

/**
 * Created by mikae on 14.2.2018.
 */

public class Validate extends MainActivity {

    public Validate() {
    }

    /**
     * Validates the EditText input fields
     *
     * @param ids      Array of element ids
     * @param activity Current activity
     * @return Returns the boolean isNotEmpty
     */
    public boolean validateEditText(ArrayList<Integer> ids, Activity activity) {
        boolean isNotEmpty = true;

        for (int id : ids) {
            EditText et = activity.findViewById(id);

            if (TextUtils.isEmpty(et.getText().toString())) {
                et.setError(MainActivity.globalRes.getString(R.string.error_field_required_short));
                isNotEmpty = false;
            }
        }
        return isNotEmpty;
    }

    /**
     * Validates the EditText input fields
     *
     * @param ids      Array of element ids
     * @param activity Current activity
     * @return Returns the boolean isNotEmpty
     */
    public boolean validateNumbers(ArrayList<Integer> ids, Activity activity) {
        boolean isNotEmpty = true;

        for (int id : ids) {
            EditText et = activity.findViewById(id);
            if (TextUtils.isEmpty(et.getText().toString())) {
                et.setError(MainActivity.globalRes.getString(R.string.error_field_required_short));
                isNotEmpty = false;
            } else {
                if (Integer.parseInt(et.getText().toString()) <= 0) {
                    et.setError(MainActivity.globalRes.getString(R.string.error_less_than) + " 1");
                    isNotEmpty = false;
                }
            }
        }
        return isNotEmpty;
    }

}

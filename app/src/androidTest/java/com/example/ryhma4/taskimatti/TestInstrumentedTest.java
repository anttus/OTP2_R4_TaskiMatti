package com.example.ryhma4.taskimatti;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.example.ryhma4.taskimatti.activity.ShowRoutinesActivity;
import com.example.ryhma4.taskimatti.model.Routine;
import com.example.ryhma4.taskimatti.model.Type;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class TestInstrumentedTest {

    private ShowRoutinesActivity mShowRoutinesActivity;
    private ArrayList<Type> listDataHeader;
//    String name, Type type, int times, String repeat, int hours, int minutes, String description
    private static final Type testType = new Type("testType", "#ffffff");
    private static final Routine routine = new Routine("testRoutine", testType, 3, "Viikko", 1, 30, "testing routine");

    @Before
    public void createShowRoutinesActivity() {
        Looper.prepare();
        mShowRoutinesActivity = new ShowRoutinesActivity();
    }

    @Test
    public void showRoutinesActivity_DataHeaderList() {
        listDataHeader = new ArrayList<>();
        listDataHeader.add(new Type("KAIKKI", "#ffffff"));
        listDataHeader.add(routine.getType());

        assertThat(mShowRoutinesActivity.findIndex(routine.getType().getName(), listDataHeader), is(1));
    }



}

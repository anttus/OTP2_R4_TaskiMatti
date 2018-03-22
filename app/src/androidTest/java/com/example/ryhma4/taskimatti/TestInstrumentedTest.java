package com.example.ryhma4.taskimatti;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.AppCompatButton;
import android.widget.EditText;

import com.example.ryhma4.taskimatti.activity.LoginActivity;
import com.example.ryhma4.taskimatti.activity.ShowRoutinesActivity;
import com.example.ryhma4.taskimatti.model.Routine;
import com.example.ryhma4.taskimatti.model.Type;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
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
    private LoginActivity mLoginActivity;
    private ArrayList<Type> listDataHeader;
    private static final Type testType = new Type("testType", "#ffffff");
    private static final Routine routine = new Routine("testRoutine", testType, 3, "Viikko", 1, 30, "testing routine");

    @Before
    public void createShowRoutinesActivity() {
        if (Looper.myLooper()==null) Looper.prepare();
        mShowRoutinesActivity = new ShowRoutinesActivity();
        mLoginActivity = new LoginActivity();
    }

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    @Test
    public void LoginActivity_LoginTest() {
        onView(withId(R.id.inputEmail)).perform(typeText("asessori_95@hotmail.com")).check(matches(isDisplayed()));
        onView(withId(R.id.inputPassword)).perform(typeText("hunter2")).check(matches(isDisplayed()));

        onView(withId(R.id.btnLogin)).perform(scrollTo()).perform(click());
    }

    @Test
    public void showRoutinesActivity_DataHeaderList() {
        listDataHeader = new ArrayList<>();
        listDataHeader.add(new Type("KAIKKI", "#ffffff"));
        listDataHeader.add(routine.getType());

        assertThat(mShowRoutinesActivity.findIndex(routine.getType().getName(), listDataHeader), is(1));
    }


}

package com.example.ryhma4.taskimatti.activity;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.ryhma4.taskimatti.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class EmailLoginTest {

    /**
     * Tests logging in with email credentials
     */

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    @Test
    public void LoginActivity_LoginTest() {
        onView(ViewMatchers.withId(R.id.inputEmail)).perform(typeText("asessori_95@hotmail.com")).check(matches(isDisplayed()));
        onView(withId(R.id.inputPassword)).perform(typeText("hunter2")).check(matches(isDisplayed()));

        onView(withId(R.id.btnLogin)).perform(scrollTo()).perform(click());
    }

}

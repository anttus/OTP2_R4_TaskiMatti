package com.example.ryhma4.taskimatti.activity;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.ryhma4.taskimatti.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginRoutineAndTaskCreation {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void loginRoutineAndTaskCreation() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        floatingActionButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.inputRoutineName),
                        childAtPosition(
                                allOf(withId(R.id.createRoutineLinearLayout),
                                        childAtPosition(
                                                withId(R.id.createRoutineScrollView),
                                                0)),
                                0)));
        appCompatEditText.perform(scrollTo(), replaceText("test"), closeSoftKeyboard());

        ViewInteraction appCompatAutoCompleteTextView = onView(
                allOf(withId(R.id.inputRoutineType),
                        childAtPosition(
                                allOf(withId(R.id.typeLinearLayout),
                                        childAtPosition(
                                                withId(R.id.createRoutineLinearLayout),
                                                1)),
                                0)));
        appCompatAutoCompleteTextView.perform(scrollTo(), replaceText("Ty"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.numTimes),
                        childAtPosition(
                                allOf(withId(R.id.repeatLinearLayout),
                                        childAtPosition(
                                                withId(R.id.createRoutineLinearLayout),
                                                2)),
                                1)));
        appCompatEditText2.perform(scrollTo(), replaceText("2"), closeSoftKeyboard());

        ViewInteraction appCompatAutoCompleteTextView2 = onView(
                allOf(withId(R.id.inputRoutineType), withText("Ty"),
                        childAtPosition(
                                allOf(withId(R.id.typeLinearLayout),
                                        childAtPosition(
                                                withId(R.id.createRoutineLinearLayout),
                                                1)),
                                0)));
        appCompatAutoCompleteTextView2.perform(scrollTo(), replaceText("Type"));

        ViewInteraction appCompatAutoCompleteTextView3 = onView(
                allOf(withId(R.id.inputRoutineType), withText("Type"),
                        childAtPosition(
                                allOf(withId(R.id.typeLinearLayout),
                                        childAtPosition(
                                                withId(R.id.createRoutineLinearLayout),
                                                1)),
                                0),
                        isDisplayed()));
        appCompatAutoCompleteTextView3.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.inputHours),
                        childAtPosition(
                                allOf(withId(R.id.durationLinearLayout),
                                        childAtPosition(
                                                withId(R.id.createRoutineLinearLayout),
                                                4)),
                                1)));
        appCompatEditText3.perform(scrollTo(), replaceText("1"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.inputMinutes),
                        childAtPosition(
                                allOf(withId(R.id.durationLinearLayout),
                                        childAtPosition(
                                                withId(R.id.createRoutineLinearLayout),
                                                4)),
                                2)));
        appCompatEditText4.perform(scrollTo(), replaceText("0"), closeSoftKeyboard());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.dropdownInterval),
                        childAtPosition(
                                allOf(withId(R.id.repeatLinearLayout),
                                        childAtPosition(
                                                withId(R.id.createRoutineLinearLayout),
                                                2)),
                                3)));
        appCompatSpinner.perform(scrollTo(), click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(2);
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.inputDescription),
                        childAtPosition(
                                allOf(withId(R.id.createRoutineLinearLayout),
                                        childAtPosition(
                                                withId(R.id.createRoutineScrollView),
                                                0)),
                                5)));
        appCompatEditText5.perform(scrollTo(), replaceText("Description"), closeSoftKeyboard());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.btnSaveRoutine),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction editText = onView(
                childAtPosition(
                        childAtPosition(
                                withId(R.id.createRoutineLinearLayout),
                                0),
                        0));
        editText.perform(scrollTo(), replaceText("Task"), closeSoftKeyboard());

        ViewInteraction editText2 = onView(
                childAtPosition(
                        childAtPosition(
                                withId(R.id.createRoutineLinearLayout),
                                0),
                        1));
        editText2.perform(scrollTo(), replaceText("description"), closeSoftKeyboard());

        ViewInteraction editText3 = onView(
                childAtPosition(
                        childAtPosition(
                                withId(R.id.createRoutineLinearLayout),
                                0),
                        2));
        editText3.perform(scrollTo(), replaceText("Task "), closeSoftKeyboard());

        ViewInteraction editText4 = onView(
                childAtPosition(
                        childAtPosition(
                                withId(R.id.createRoutineLinearLayout),
                                0),
                        3));
        editText4.perform(scrollTo(), replaceText("d"), closeSoftKeyboard());

        ViewInteraction floatingActionButton3 = onView(
                allOf(withId(R.id.btnSaveRoutine),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton3.perform(click());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}

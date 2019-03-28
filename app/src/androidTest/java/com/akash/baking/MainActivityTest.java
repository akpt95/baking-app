package com.akash.baking;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.akash.baking.home.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);


    @Test
    public void testToolbar(){
        onView(withId(R.id.text_toolbar_title))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testRecyclerViewRecipe(){
        onView(withId(R.id.recycler_view_recipe))
                .perform(RecyclerViewActions.scrollToPosition(0))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

    }

    @Test
    public void testLaunchNewActivity(){
        onView(withId(R.id.recycler_view_recipe))
                .perform(RecyclerViewActions.scrollToPosition(1))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.text_toolbar_title))
                .check(matches(withText("Brownies")));
    }

    @Test
    public void testRecipeStepClick(){
        onView(withId(R.id.recycler_view_recipe))
                .perform(RecyclerViewActions.scrollToPosition(1))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.recycler_view_steps))
                .perform(RecyclerViewActions.scrollToPosition(0))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.text_recipe_step_description))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testNextButton(){
        onView(withId(R.id.recycler_view_recipe))
                .perform(RecyclerViewActions.scrollToPosition(1))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.recycler_view_steps))
                .perform(RecyclerViewActions.scrollToPosition(0))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.btn_next))
                .perform(click());

    }

}

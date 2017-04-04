package com.chikeandroid.debtmanager20;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.chikeandroid.debtmanager20.home.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Chike on 3/15/2017.
 */
@RunWith(AndroidJUnit4.class)
public class AddDebtTest {

    /**
     * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch your activity under test.
     * <p>
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void shouldBeAbleToSwipeBetweenFirstSecondAndThirdPage() {

        onView(withText("Fragment 1")).check(matches(isDisplayed()));
        onView(withId(R.id.view_pager_main)).perform(swipeLeft());
        onView(withText("Fragment 2")).check(matches(isDisplayed()));
        onView(withId(R.id.view_pager_main)).perform(swipeLeft());
        onView(withText("Fragment 3")).check(matches(isDisplayed()));
    }

    @Test
    public void shouldBeAbleTotestSwipeToTheEnd() {
        onView(withText("Fragment 1")).check(matches(isDisplayed()));
        onView(withId(R.id.view_pager_main)).perform(swipeLeft()).perform(swipeLeft()).perform(swipeLeft());
        onView(withText("Fragment 3")).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfFabIsDisplayed() {
        onView(withId(R.id.fab_main)).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfFabIsDisplayedInAllFragments() {
        onView(withId(R.id.fab_main)).check(matches(isDisplayed()));
        onView(withId(R.id.view_pager_main)).perform(swipeLeft());
        onView(withId(R.id.fab_main)).check(matches(isDisplayed()));
        onView(withId(R.id.view_pager_main)).perform(swipeLeft());
        onView(withId(R.id.fab_main)).check(matches(isDisplayed()));

    }

}

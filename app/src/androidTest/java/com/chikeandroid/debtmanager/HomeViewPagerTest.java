package com.chikeandroid.debtmanager;

import android.graphics.drawable.ColorDrawable;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.chikeandroid.debtmanager.data.Debt;
import com.chikeandroid.debtmanager.features.home.MainActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Chike on 3/15/2017.
 */
@RunWith(AndroidJUnit4.class)
public class HomeViewPagerTest {

    /**
     * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch your activity under test.
     * <p>
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class) {

        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();

            ((DebtManagerApplication) InstrumentationRegistry.getTargetContext()
                    .getApplicationContext()).getComponent()
                    .getDebtsRepository().deleteAllPersonDebtsByType(Debt.DEBT_TYPE_OWED);
        }};

    @Test
    public void testToolbarDesign() {
        onView(withId(R.id.toolbar_main_included)).check(matches(isDisplayed()));
        // see if there is some text that equals ‘R.string.app_name’ and has a parent whose id is R.id.toolbar.
        onView(withText(R.string.app_name)).check(matches(withParent(withId(R.id.toolbar_main_included))));

        onView(withId(R.id.toolbar_main_included)).check(matches(withToolbarBackGroundColor()));
    }

    @Test
    public void shouldBeAbleToSwipeToTheEndAndThenBackAgain() {

        onView(withId(R.id.view_pager_main)).perform(swipeLeft()).perform(swipeLeft()).perform(swipeLeft());
        onView(withId(R.id.view_pager_main)).perform(swipeRight()).perform(swipeRight()).perform(swipeRight());
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

    @Test
    public void fabShouldOpenAddDebtUIWhenClick() {

        onView(withId(R.id.fab_main)).perform(click());

        onView(withId(R.id.et_full_name)).check(matches(isDisplayed()));

    }

    private Matcher<? super View> withToolbarBackGroundColor() {
        return new BoundedMatcher<View, View>(View.class) {
            @Override
            public void describeTo(org.hamcrest.Description description) {
                // just do nothing for now
            }

            @Override
            public boolean matchesSafely(View view) {
                // Get the toolbar background as a ColorDrawable
                final ColorDrawable buttonColor = (ColorDrawable) view.getBackground();

                // Match the toolbar background to the apps primary color
                return ContextCompat
                        .getColor(mActivityTestRule.getActivity(), R.color.colorPrimary) ==
                        buttonColor.getColor();
            }
        };
    }

}

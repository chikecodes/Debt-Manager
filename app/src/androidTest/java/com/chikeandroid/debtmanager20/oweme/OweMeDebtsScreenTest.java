package com.chikeandroid.debtmanager20.oweme;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.chikeandroid.debtmanager20.DebtManagerApplication;
import com.chikeandroid.debtmanager20.R;
import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.data.source.DebtsDataSource;
import com.chikeandroid.debtmanager20.home.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Chike on 4/17/2017.
 * Tests for the OweMe fragment screen, in the main screen which contains a list of all debts owed.
 */
@RunWith(AndroidJUnit4.class)
public class OweMeDebtsScreenTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<MainActivity>(MainActivity.class) {

                /**
                 * To avoid a long list of debts and the need to scroll through the list to find a
                 * debt, we call {@link DebtsDataSource#deleteAllDebtsByType(int)} ()} before each test.
                 */
                @Override
                protected void beforeActivityLaunched() {
                    super.beforeActivityLaunched();

                    ((DebtManagerApplication) InstrumentationRegistry.getTargetContext()
                            .getApplicationContext()).getComponent()
                            .getDebtsRepository().deleteAllDebtsByType(Debt.DEBT_TYPE_OWED);
                }};

    @Test
    public void shouldOpenAddDebtUiWhenAddDebtFabButtonIsClicked() {

        onView(withId(R.id.fab_main)).perform(click());

        onView(withId(R.id.et_comment)).check(matches(isDisplayed()));
    }





}

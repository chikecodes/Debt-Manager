package com.chikeandroid.debtmanager20.features.persondetail;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.chikeandroid.debtmanager20.DebtManagerApplication;
import com.chikeandroid.debtmanager20.R;
import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.features.home.MainActivity;
import com.chikeandroid.debtmanager20.util.StringUtil;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.chikeandroid.debtmanager20.util.AndroidTestUtil.createDebt;
import static com.chikeandroid.debtmanager20.util.AndroidTestUtil.verifyDebtDetailUiViews;
import static com.chikeandroid.debtmanager20.util.AndroidTestUtil.withCollapsingToolbarLayoutTitle;
import static com.chikeandroid.debtmanager20.util.TestUtil.AMOUNT1;
import static com.chikeandroid.debtmanager20.util.TestUtil.AMOUNT2;
import static com.chikeandroid.debtmanager20.util.TestUtil.NAME1;
import static com.chikeandroid.debtmanager20.util.TestUtil.NOTE1;
import static com.chikeandroid.debtmanager20.util.TestUtil.NOTE2;
import static com.chikeandroid.debtmanager20.util.TestUtil.PHONE_NUMBER1;

/**
 * Created by Chike on 5/23/2017.
 * Tests for the PersonDetail fragment screen, in the main screen which contains debts by person.
 */
@RunWith(AndroidJUnit4.class)
public class PersonDetailScreenTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class) {

                /**
                 * To avoid a long list of debts and the need to scroll through the list to find a
                 * debt, we call {@link com.chikeandroid.debtmanager20.data.source.PersonDebtsDataSource ;#deleteAllPersonDebts()} ()} before each test.
                 */
                @Override
                protected void beforeActivityLaunched() {
                    super.beforeActivityLaunched();

                    ((DebtManagerApplication) InstrumentationRegistry.getTargetContext()
                            .getApplicationContext()).getComponent()
                            .getDebtsRepository().deleteAllPersonDebts();
                }};

    @Before
    public void registerIdlingResource() {
        Espresso.registerIdlingResources(mActivityTestRule.getActivity().getCountingIdlingResource());
    }

    @After
    public void unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(mActivityTestRule.getActivity().getCountingIdlingResource());
    }

    @Test
    public void shouldBeAbleToAddANewDebtToOweMeList() {

        createDebt(NAME1, PHONE_NUMBER1, AMOUNT1, NOTE1, Debt.DEBT_TYPE_OWED);

        createDebt(NAME1, PHONE_NUMBER1, AMOUNT2, NOTE2, Debt.DEBT_TYPE_IOWE);

        onView(withId(R.id.view_pager_main)).perform(swipeLeft()).perform(swipeLeft());

        onView(withId(R.id.rv_people)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(isAssignableFrom(CollapsingToolbarLayout.class)).check(matches(
                withCollapsingToolbarLayoutTitle(Matchers.<CharSequence>is(NAME1))));
        onView(withText(StringUtil.commaNumber(AMOUNT1))).check(matches(isDisplayed()));
        onView(withText(StringUtil.commaNumber(AMOUNT2))).check(matches(isDisplayed()));

        // open debt detail ui
        onView(withText(StringUtil.commaNumber(AMOUNT1))).perform(click());
        verifyDebtDetailUiViews();
    }




}

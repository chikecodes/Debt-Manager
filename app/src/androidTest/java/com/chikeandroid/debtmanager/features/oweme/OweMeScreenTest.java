package com.chikeandroid.debtmanager.features.oweme;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.chikeandroid.debtmanager.DebtManagerApplication;
import com.chikeandroid.debtmanager.R;
import com.chikeandroid.debtmanager.data.Debt;
import com.chikeandroid.debtmanager.features.home.MainActivity;
import com.chikeandroid.debtmanager.util.RecyclerViewItemCountAssertion;
import com.chikeandroid.debtmanager.util.StringUtil;
import com.chikeandroid.debtmanager.util.TimeUtil;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.chikeandroid.debtmanager.util.AndroidTestUtil.createDebt;
import static com.chikeandroid.debtmanager.util.AndroidTestUtil.withCollapsingToolbarLayoutTitle;
import static com.chikeandroid.debtmanager.util.TestUtil.AMOUNT1;
import static com.chikeandroid.debtmanager.util.TestUtil.AMOUNT2;
import static com.chikeandroid.debtmanager.util.TestUtil.AMOUNT3;
import static com.chikeandroid.debtmanager.util.TestUtil.CREATED_DAY_OF_MONTH;
import static com.chikeandroid.debtmanager.util.TestUtil.CREATED_MONTH;
import static com.chikeandroid.debtmanager.util.TestUtil.CREATED_YEAR;
import static com.chikeandroid.debtmanager.util.TestUtil.DUE_DAY_OF_MONTH;
import static com.chikeandroid.debtmanager.util.TestUtil.DUE_MONTH;
import static com.chikeandroid.debtmanager.util.TestUtil.DUE_YEAR;
import static com.chikeandroid.debtmanager.util.TestUtil.NAME1;
import static com.chikeandroid.debtmanager.util.TestUtil.NAME2;
import static com.chikeandroid.debtmanager.util.TestUtil.NAME3;
import static com.chikeandroid.debtmanager.util.TestUtil.NOTE1;
import static com.chikeandroid.debtmanager.util.TestUtil.NOTE2;
import static com.chikeandroid.debtmanager.util.TestUtil.NOTE3;
import static com.chikeandroid.debtmanager.util.TestUtil.PHONE_NUMBER1;
import static com.chikeandroid.debtmanager.util.TestUtil.PHONE_NUMBER2;
import static com.chikeandroid.debtmanager.util.TestUtil.PHONE_NUMBER3;

/**
 * Created by Chike on 4/17/2017.
 * Tests for the OweMe fragment screen, in the main screen which contains a list of all debts owed.
 */
@RunWith(AndroidJUnit4.class)
public class OweMeScreenTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class) {

                /**
                 * To avoid a long list of debts and the need to scroll through the list to find a
                 * debt, we call {@link com.chikeandroid.debtmanager.data.source.PersonDebtsDataSource ;#deleteAllPersonDebtsByType(int)} ()} before each test.
                 */
                @Override
                protected void beforeActivityLaunched() {
                    super.beforeActivityLaunched();

                    ((DebtManagerApplication) InstrumentationRegistry.getTargetContext()
                            .getApplicationContext()).getComponent()
                            .getDebtsRepository().deleteAllPersonDebtsByType(Debt.DEBT_TYPE_OWED);
                }};

    @Before
    public void registerIdlingResource() {
        Espresso.registerIdlingResources(mActivityTestRule.getActivity().getCountingIdlingResource());
    }

    @Test
    public void shouldOpenAddDebtUiWhenAddDebtFabButtonIsClicked() {

        onView(withId(R.id.fab_main)).perform(click());

        onView(withId(R.id.et_comment)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldBeAbleToAddANewDebtToOweMeList() {

        createDebt(NAME1, PHONE_NUMBER1, AMOUNT1, NOTE1, Debt.DEBT_TYPE_OWED);

        // onView(ViewMatchers.withId(R.id.rv_oweme)).perform(RecyclerViewActions.scrollToHolder(withTitle("Chike Mgbemena")));

        onView(withText(NAME1)).check(matches(isDisplayed()));

        // Click on the RecyclerView item at position 2
        // onView(withId(R.id.rv_oweme)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

    }

    @Test
    public void shouldOpenDebtDetailUiWhenDebtIsClicked() {

        createDebt(NAME1, PHONE_NUMBER1, AMOUNT1, NOTE1, Debt.DEBT_TYPE_OWED);

        onView(withText(NAME1)).perform(click());

        onView(withText(NAME1)).check(matches(isDisplayed()));
        onView(isAssignableFrom(CollapsingToolbarLayout.class)).check(matches(
                withCollapsingToolbarLayoutTitle(Matchers.<CharSequence>is(StringUtil.commaNumber(AMOUNT1)))));
        onView(withText(NOTE1)).check(matches(isDisplayed()));

        String dateCreated = TimeUtil.dateToString(CREATED_YEAR, CREATED_MONTH - 1, CREATED_DAY_OF_MONTH);
        String dateDue = TimeUtil.dateToString(DUE_YEAR, DUE_MONTH - 1, DUE_DAY_OF_MONTH);

        onView(withText(dateCreated + " (Created)")).check(matches(isDisplayed()));
        onView(withText(dateDue + " (Due Date)")).check(matches(isDisplayed()));
    }

    @Test
    public void shouldBeAbleToSelectAndDeleteMultipleDebtsListItemOnLongClick() {

        createDebt(NAME1, PHONE_NUMBER1, AMOUNT1, NOTE1, Debt.DEBT_TYPE_OWED);

        createDebt(NAME2, PHONE_NUMBER2, AMOUNT2, NOTE2, Debt.DEBT_TYPE_OWED);

        createDebt(NAME3, PHONE_NUMBER3, AMOUNT3, NOTE3, Debt.DEBT_TYPE_OWED);

        onView(withText(NAME1)).perform(longClick());

        onView(withText(NAME2)).perform(click());

        onView(withText(NAME3)).perform(click());

        onView(withId(R.id.action_delete)).perform(click());

        // confirm dialog
        onView(withId(android.R.id.message)).check(matches(isDisplayed()));

        onView(withId(android.R.id.button1)).perform(click());

        onView(withText(NAME1)).check(doesNotExist());
        onView(withText(NAME2)).check(doesNotExist());
        onView(withText(NAME3)).check(doesNotExist());
    }

    @Test
    public void shouldBeAbleToDeleteDebtOnDetailScreenAndThenNotShowInList() {

        createDebt(NAME1, PHONE_NUMBER1, AMOUNT1, NOTE1, Debt.DEBT_TYPE_OWED);

        createDebt(NAME3, PHONE_NUMBER2, AMOUNT2, NOTE2, Debt.DEBT_TYPE_OWED);

        createDebt(NAME3, PHONE_NUMBER3, AMOUNT3, NOTE3, Debt.DEBT_TYPE_OWED);

        onView(withText(NAME1)).perform(click());

        onView(withId(R.id.action_delete)).perform(click());

        onView(withId(android.R.id.message)).check(matches(isDisplayed()));

        onView(withId(android.R.id.button1)).perform(click());

        onView(withText(NAME1)).check(doesNotExist());

        onView(withId(R.id.rv_oweme)).check(new RecyclerViewItemCountAssertion(2));
    }

    @Test
    public void shouldNotShowActionModeWhenViewPagerIsSwiped() {

        createDebt(NAME1, PHONE_NUMBER1, AMOUNT1, NOTE1, Debt.DEBT_TYPE_OWED);

        onView(withText(NAME1)).perform(longClick());

        onView(withId(R.id.action_delete)).check(matches(isDisplayed()));

        onView(withId(R.id.view_pager_main)).perform(swipeLeft());

        //onView(withText("Selected")).check(matches(not(isDisplayed())));
        onView(withId(R.id.action_delete)).check(doesNotExist());
    }

    /**
     * Matches the OweMeAdapter.ViewHolders in the middle of the list.
     */
   /* private static Matcher<DebtsAdapter.ViewHolder> isInTheMiddle() {
        return new TypeSafeMatcher<DebtsAdapter.ViewHolder>() {
            @Override
            protected boolean matchesSafely(DebtsAdapter.ViewHolder customHolder) {
                return customHolder.getIsInTheMiddle();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("item in the middle");
            }
        };
    }*/

    @After
    public void unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(mActivityTestRule.getActivity().getCountingIdlingResource());
    }
}

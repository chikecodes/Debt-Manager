package com.chikeandroid.debtmanager20.iowe;

/**
 * Created by Chike on 5/3/2017.
 * Tests for the IOwe fragment screen, in the main screen which contains a list of all debts owed.
 */

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;

import com.chikeandroid.debtmanager20.DebtManagerApplication;
import com.chikeandroid.debtmanager20.R;
import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.home.MainActivity;
import com.chikeandroid.debtmanager20.util.RecyclerViewItemCountAssertion;
import com.chikeandroid.debtmanager20.util.StringUtil;
import com.chikeandroid.debtmanager20.util.TimeUtil;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.chikeandroid.debtmanager20.util.TestUtil.AMOUNT1;
import static com.chikeandroid.debtmanager20.util.TestUtil.AMOUNT2;
import static com.chikeandroid.debtmanager20.util.TestUtil.AMOUNT3;
import static com.chikeandroid.debtmanager20.util.TestUtil.NAME1;
import static com.chikeandroid.debtmanager20.util.TestUtil.NAME2;
import static com.chikeandroid.debtmanager20.util.TestUtil.NAME3;
import static com.chikeandroid.debtmanager20.util.TestUtil.NOTE1;
import static com.chikeandroid.debtmanager20.util.TestUtil.NOTE2;
import static com.chikeandroid.debtmanager20.util.TestUtil.NOTE3;
import static com.chikeandroid.debtmanager20.util.TestUtil.PHONE_NUMBER1;
import static com.chikeandroid.debtmanager20.util.TestUtil.PHONE_NUMBER2;
import static com.chikeandroid.debtmanager20.util.TestUtil.PHONE_NUMBER3;

@RunWith(AndroidJUnit4.class)
public class IOweScreenTest {

    private final static int CREATED_YEAR = 2017;
    private final static int CREATED_MONTH = 10;
    private final static int CREATED_DAY_OF_MONTH = 10;

    private final static int DUE_YEAR = 2017;
    private final static int DUE_MONTH = 12;
    private final static int DUE_DAY_OF_MONTH = 15;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class) {

                /**
                 * To avoid a long list of debts and the need to scroll through the list to find a
                 * debt, we call {@link com.chikeandroid.debtmanager20.data.source.PersonDebtsDataSource ;#deleteAllPersonDebtsByType(int)} ()} before each test.
                 */
                @Override
                protected void beforeActivityLaunched() {
                    super.beforeActivityLaunched();

                    ((DebtManagerApplication) InstrumentationRegistry.getTargetContext()
                            .getApplicationContext()).getComponent()
                            .getDebtsRepository().deleteAllPersonDebtsByType(Debt.DEBT_TYPE_IOWE);
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
    public void shouldBeAbleToAddANewDebtToIOweList() {

        createDebt(NAME1, PHONE_NUMBER1, AMOUNT1, NOTE1, Debt.DEBT_TYPE_IOWE);

        // onView(ViewMatchers.withId(R.id.rv_oweme)).perform(RecyclerViewActions.scrollToHolder(withTitle("Chike Mgbemena")));

        onView(withText(NAME1)).check(matches(isDisplayed()));

        // Click on the RecyclerView item at position 2
        // onView(withId(R.id.rv_oweme)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

    }

    @Test
    public void shouldOpenDebtDetailUiWhenAListIsClicked() {

        createDebt(NAME1, PHONE_NUMBER1, AMOUNT1, NOTE1, Debt.DEBT_TYPE_IOWE);

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

        createDebt(NAME1, PHONE_NUMBER1, AMOUNT1, NOTE1, Debt.DEBT_TYPE_IOWE);

        createDebt(NAME2, PHONE_NUMBER2, AMOUNT2, NOTE2, Debt.DEBT_TYPE_IOWE);

        createDebt(NAME3, PHONE_NUMBER3, AMOUNT3, NOTE3, Debt.DEBT_TYPE_IOWE);

        onView(withText(NAME1)).perform(longClick());

        onView(withText(NAME2)).perform(click());

        onView(withText(NAME3)).perform(click());

        // action mode delete
        onView(withId(R.id.action_delete)).perform(click());

        // confirm dialog
        onView(withId(android.R.id.message)).check(matches(isDisplayed()));

        onView(withId(android.R.id.button1)).perform(click());

        onView(withText(NAME1)).check(doesNotExist());
        onView(withText(NAME2)).check(doesNotExist());
        onView(withText(NAME3)).check(doesNotExist());
    }

    @Test
    public void shouldNotShowActionModeWhenViewPagerIsSwiped() {

        createDebt(NAME1, PHONE_NUMBER1, AMOUNT1, NOTE1, Debt.DEBT_TYPE_IOWE);

        onView(withText(NAME1)).perform(longClick());

        onView(withId(R.id.action_delete)).check(matches(isDisplayed()));

        onView(withId(R.id.view_pager_main)).perform(swipeLeft());

        //onView(withText("Selected")).check(matches(not(isDisplayed())));
        onView(withId(R.id.action_delete)).check(doesNotExist());
    }

    @Test
    public void shouldBeAbleToDeleteDebtOnDetailScreenAndThenNotShowInList() {

        createDebt(NAME1, PHONE_NUMBER1, AMOUNT1, NOTE1, Debt.DEBT_TYPE_IOWE);

        createDebt(NAME2, PHONE_NUMBER2, AMOUNT2, NOTE2, Debt.DEBT_TYPE_IOWE);

        createDebt(NAME3, PHONE_NUMBER2, AMOUNT3, NOTE3, Debt.DEBT_TYPE_IOWE);

        onView(withText(NAME1)).perform(click());

        onView(withId(R.id.action_delete)).perform(click());

        // confirm dialog
        onView(withId(android.R.id.message)).check(matches(isDisplayed()));

        onView(withId(android.R.id.button1)).perform(click());

        onView(withText(NAME1)).check(doesNotExist());

        onView(withId(R.id.rv_iowe)).check(new RecyclerViewItemCountAssertion(2));
    }

    private void createDebt(String name, String phoneNumber, double amount, String comment, int debtType) {

        onView(withId(R.id.fab_main)).perform(click());

        if(debtType == Debt.DEBT_TYPE_OWED) {
            onView(withId(R.id.rb_owed_to_me)).perform(click());
        }else if(debtType == Debt.DEBT_TYPE_IOWE) {
            onView(withId(R.id.rb_owed_by_me)).perform(click());
        }

        onView(withId(R.id.et_full_name)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.et_phone_number)).perform(typeText(phoneNumber), closeSoftKeyboard());
        onView(withId(R.id.et_amount)).perform(typeText(String.valueOf(amount)), closeSoftKeyboard());
        onView(withId(R.id.et_comment)).perform(typeText(comment), closeSoftKeyboard());

        onView(withId(R.id.btn_date_due)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(
                PickerActions.setDate(DUE_YEAR, DUE_MONTH, DUE_DAY_OF_MONTH));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.btn_date_created)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(
                PickerActions.setDate(CREATED_YEAR, CREATED_MONTH, CREATED_DAY_OF_MONTH));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.action_save_debt)).perform(click());
    }

    private static Matcher<Object> withCollapsingToolbarLayoutTitle(final Matcher<CharSequence> textMatcher) {
        return new BoundedMatcher<Object, CollapsingToolbarLayout>(CollapsingToolbarLayout.class) {
            @Override public boolean matchesSafely(CollapsingToolbarLayout toolbar) {
                return textMatcher.matches(toolbar.getTitle());
            }
            @Override public void describeTo(Description description) {
                description.appendText("with toolbar title: ");
                textMatcher.describeTo(description);
            }
        };
    }

    @After
    public void unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(mActivityTestRule.getActivity().getCountingIdlingResource());
    }
}

package com.chikeandroid.debtmanager20.oweme;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.test.InstrumentationRegistry;
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
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Chike on 4/17/2017.
 * Tests for the OweMe fragment screen, in the main screen which contains a list of all debts owed.
 */
@RunWith(AndroidJUnit4.class)
public class OweMeDebtsScreenTest {

    private final static String NAME = "chike mgbemena";
    private final static String PHONE_NUMBER = "070381115342";
    private final static double AMOUNT = 5000;
    private final static String COMMENT = "comment 123";

    private final static int CREATED_YEAR = 2017;
    private final static int CREATED_MONTH = 10;
    private final static int CREATED_DAY_OF_MONTH = 10;

    private final static int DUE_YEAR = 2017;
    private final static int DUE_MONTH = 12;
    private final static int DUE_DAY_OF_MONTH = 15;

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
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
                            .getDebtsRepository().deleteAllPersonDebtsByType(Debt.DEBT_TYPE_OWED);
                }};

    @Test
    public void shouldOpenAddDebtUiWhenAddDebtFabButtonIsClicked() {

        onView(withId(R.id.fab_main)).perform(click());

        onView(withId(R.id.et_comment)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldBeAbleToAddANewDebtToOweMeList() {

        createDebt(NAME, PHONE_NUMBER, AMOUNT, COMMENT, Debt.DEBT_TYPE_OWED);

        // onView(ViewMatchers.withId(R.id.rv_oweme)).perform(RecyclerViewActions.scrollToHolder(withTitle("Chike Mgbemena")));

        onView(withText(NAME)).check(matches(isDisplayed()));

        // Click on the RecyclerView item at position 2
        // onView(withId(R.id.rv_oweme)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

    }

    @Test
    public void shouldOpenDebtDetailUiWhenDebtIsClicked() {

        createDebt(NAME, PHONE_NUMBER, AMOUNT, COMMENT, Debt.DEBT_TYPE_OWED);

        onView(withText(NAME)).perform(click());

        onView(withText(NAME)).check(matches(isDisplayed()));
        onView(isAssignableFrom(CollapsingToolbarLayout.class)).check(matches(
                withCollapsingToolbarLayoutTitle(Matchers.<CharSequence>is(StringUtil.commaNumber(AMOUNT)))));
        onView(withText(COMMENT)).check(matches(isDisplayed()));

        String dateCreated = TimeUtil.dateToString(CREATED_YEAR, CREATED_MONTH - 1, CREATED_DAY_OF_MONTH);
        String dateDue = TimeUtil.dateToString(DUE_YEAR, DUE_MONTH - 1, DUE_DAY_OF_MONTH);

        onView(withText(dateCreated + " (Created)")).check(matches(isDisplayed()));
        onView(withText(dateDue + " (Due Date)")).check(matches(isDisplayed()));

    }

    @Test
    public void shouldBeAbleToDeleteDebtOnDetailScreenAndThenNotShowInList() {

        createDebt(NAME, PHONE_NUMBER, AMOUNT, COMMENT, Debt.DEBT_TYPE_OWED);

        createDebt("Mary Jane", "0124535476", 8000, "comment 098", Debt.DEBT_TYPE_OWED);

        createDebt("Chuka Smith", "10245784", 9000, "comment 4543", Debt.DEBT_TYPE_OWED);

        onView(withText(NAME)).perform(click());

        onView(withId(R.id.action_delete)).perform(click());

        onView(withId(android.R.id.message)).check(matches(isDisplayed()));

        onView(withId(android.R.id.button1)).perform(click());

        onView(withText(NAME)).check(doesNotExist());

        onView(withId(R.id.rv_oweme)).check(new RecyclerViewItemCountAssertion(2));
    }

    private void createDebt(String name, String phoneNumber, double amount, String comment, int debtType) {

        onView(withId(R.id.fab_main)).perform(click());

        onView(withId(R.id.rb_owed_to_me)).perform(click());

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

    /**
     * Matches the {@link DebtsAdapter.ViewHolder}s in the middle of the list.
     */
    private static Matcher<DebtsAdapter.ViewHolder> isInTheMiddle() {
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
}

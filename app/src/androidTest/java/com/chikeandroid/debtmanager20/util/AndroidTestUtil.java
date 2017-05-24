package com.chikeandroid.debtmanager20.util;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.widget.DatePicker;

import com.chikeandroid.debtmanager20.R;
import com.chikeandroid.debtmanager20.data.Debt;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.chikeandroid.debtmanager20.util.TestUtil.AMOUNT1;
import static com.chikeandroid.debtmanager20.util.TestUtil.CREATED_DAY_OF_MONTH;
import static com.chikeandroid.debtmanager20.util.TestUtil.CREATED_MONTH;
import static com.chikeandroid.debtmanager20.util.TestUtil.CREATED_YEAR;
import static com.chikeandroid.debtmanager20.util.TestUtil.DUE_DAY_OF_MONTH;
import static com.chikeandroid.debtmanager20.util.TestUtil.DUE_MONTH;
import static com.chikeandroid.debtmanager20.util.TestUtil.DUE_YEAR;
import static com.chikeandroid.debtmanager20.util.TestUtil.NOTE1;

/**
 * Created by Chike on 5/24/2017.
 */

public class AndroidTestUtil {

    public static Matcher<Object> withCollapsingToolbarLayoutTitle(final Matcher<CharSequence> textMatcher) {
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

    public static void verifyDebtDetailUiViews() {

        onView(isAssignableFrom(CollapsingToolbarLayout.class)).check(matches(
                withCollapsingToolbarLayoutTitle(Matchers.<CharSequence>is(StringUtil.commaNumber(AMOUNT1)))));
        onView(withText(NOTE1)).check(matches(isDisplayed()));

        String dateCreated = TimeUtil.dateToString(CREATED_YEAR, CREATED_MONTH - 1, CREATED_DAY_OF_MONTH);
        String dateDue = TimeUtil.dateToString(DUE_YEAR, DUE_MONTH - 1, DUE_DAY_OF_MONTH);

        onView(withText(dateCreated + " (Created)")).check(matches(isDisplayed()));
        onView(withText(dateDue + " (Due Date)")).check(matches(isDisplayed()));
    }

    public static void createDebt(String name, String phoneNumber, double amount, String comment, int debtType) {

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
}

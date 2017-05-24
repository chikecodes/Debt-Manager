package com.chikeandroid.debtmanager20.features.debtdetail;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.chikeandroid.debtmanager20.R;
import com.chikeandroid.debtmanager20.util.TestUtil;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Chike on 4/29/2017.
 */
@RunWith(AndroidJUnit4.class)
public class DebtDetailScreenTest {

    //private PersonDebtsRepository mDebtsRepository;

    //@Mock
    //private PersonDebtsDataSource mDebtsLocalDataSource;

    //private PersonDebt mPersonDebt;
    //private String mDebtId;

    @Rule
    public ActivityTestRule<DebtDetailActivity> mDebtDetailActivityActivityTestRule =
            new ActivityTestRule<>(DebtDetailActivity.class, true, false);


    private void loadDebt() {
        startActivityWithStubbedDebt();
    }

    @Test
    public void shouldBeAbleToOpenAddEditScreenWithFieldsFilled() {

        loadDebt();

        //onView(withId(R.id.action_edit)).perform(click());

        onView(withId(R.id.description1)).check(matches(withText(TestUtil.NAME1)));
        onView(withId(R.id.tv_comment)).check(matches(withText(TestUtil.NOTE1)));

    }

    private void startActivityWithStubbedDebt() {

        Intent startIntent = new Intent();
        startIntent.putExtra(DebtDetailFragment.EXTRA_DEBT_ID, "5t5t");
        mDebtDetailActivityActivityTestRule.launchActivity(startIntent);
    }

}

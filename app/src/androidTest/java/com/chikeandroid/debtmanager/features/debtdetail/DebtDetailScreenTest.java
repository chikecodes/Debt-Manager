package com.chikeandroid.debtmanager.features.debtdetail;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.chikeandroid.debtmanager.DebtManagerApplication;
import com.chikeandroid.debtmanager.features.home.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

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
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class) {

                /**
                 * To avoid a long list of debts and the need to scroll through the list to find a
                 * debt, we call {@link com.chikeandroid.debtmanager.data.source.PersonDebtsDataSource ;#deleteAllPersonDebts()} ()} before each test.
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

    /*private void loadDebt() {
        startActivityWithStubbedDebt();
    }

    @Test
    public void shouldBeAbleToOpenAddEditScreenWithFieldsFilled() {

        loadDebt();

        //onView(withId(R.id.action_edit)).perform(click());

        onView(withId(R.id.description1)).check(matches(withText(NAME1)));
        onView(withId(R.id.tv_comment)).check(matches(withText(NOTE1)));

    }

    private void startActivityWithStubbedDebt() {

        Intent startIntent = new Intent();
        startIntent.putExtra(DebtDetailFragment.EXTRA_DEBT_ID, "5t5t");
        mDebtDetailActivityActivityTestRule.launchActivity(startIntent);
    }*/

}

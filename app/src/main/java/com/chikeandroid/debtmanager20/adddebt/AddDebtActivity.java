package com.chikeandroid.debtmanager20.adddebt;

import android.support.v4.app.Fragment;

import com.chikeandroid.debtmanager20.DebtManagerApplication;
import com.chikeandroid.debtmanager20.base.SingleFragmentActivity;

import javax.inject.Inject;

/**
 * Created by Chike on 3/16/2017.
 */

public class AddDebtActivity extends SingleFragmentActivity {

        public static final int REQUEST_ADD_DEBT = 1;

    @Inject
    AddDebtPresenter mAddDebtPresenter;

    @Override
    protected Fragment createFragment() {

        AddDebtFragment fragment = AddDebtFragment.newInstance();

        // Create presenter
        DaggerAddDebtComponent.builder()
                .addDebtPresenterModule(new AddDebtPresenterModule(fragment))
                .applicationComponent(((DebtManagerApplication) getApplication()).getComponent()).build()
                .inject(this);

        return fragment;
    }
}

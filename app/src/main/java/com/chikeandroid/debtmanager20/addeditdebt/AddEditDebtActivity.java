package com.chikeandroid.debtmanager20.addeditdebt;

import android.support.v4.app.Fragment;

import com.chikeandroid.debtmanager20.DebtManagerApplication;
import com.chikeandroid.debtmanager20.base.SingleFragmentActivity;

import javax.inject.Inject;

/**
 * Created by Chike on 3/16/2017.
 */

public class AddEditDebtActivity extends SingleFragmentActivity {

    public static final int REQUEST_ADD_DEBT = 1;
    public static final int REQUEST_EDIT_DEBT = 2;

    @Inject
    AddEditDebtPresenter mAddEditDebtPresenter;

    @Override
    protected Fragment createFragment() {

        AddEditDebtFragment fragment;

        boolean editDebt = false;
        if(getIntent().hasExtra(AddEditDebtFragment.ARGUMENT_EDIT_DEBT)) {

            fragment = AddEditDebtFragment.newInstance(getIntent().getExtras());
            editDebt = true;
        }else {
            fragment = AddEditDebtFragment.newInstance(null);
        }

        // Create presenter
        DaggerAddEditDebtComponent.builder()
                .addEditDebtPresenterModule(new AddEditDebtPresenterModule(fragment, editDebt))
                .applicationComponent(((DebtManagerApplication) getApplication()).getComponent()).build()
                .inject(this);

        return fragment;
    }
}

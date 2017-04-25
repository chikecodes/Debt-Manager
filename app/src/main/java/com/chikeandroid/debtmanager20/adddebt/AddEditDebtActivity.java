package com.chikeandroid.debtmanager20.adddebt;

import android.support.v4.app.Fragment;

import com.chikeandroid.debtmanager20.DebtManagerApplication;
import com.chikeandroid.debtmanager20.base.SingleFragmentActivity;
import com.chikeandroid.debtmanager20.data.PersonDebt;

import javax.inject.Inject;

/**
 * Created by Chike on 3/16/2017.
 */

public class AddEditDebtActivity extends SingleFragmentActivity {

    public static final int REQUEST_ADD_DEBT = 1;

    @Inject
    AddEditDebtPresenter mAddEditDebtPresenter;

    @Override
    protected Fragment createFragment() {

        AddEditDebtFragment fragment;

        String debtId = null;
        String personId = null;
        if(getIntent().hasExtra(AddEditDebtFragment.ARGUMENT_EDIT_DEBT)) {
            fragment = AddEditDebtFragment.newInstance(getIntent().getExtras());

            PersonDebt personDebt = getIntent().getParcelableExtra(AddEditDebtFragment.ARGUMENT_EDIT_DEBT);

            debtId = personDebt.getDebt().getId();
            personId = personDebt.getPerson().getId();

        }else {
            fragment = AddEditDebtFragment.newInstance(null);
        }

        // Create presenter
        DaggerAddEditDebtComponent.builder()
                .addEditDebtPresenterModule(new AddEditDebtPresenterModule(fragment, debtId, personId))
                .applicationComponent(((DebtManagerApplication) getApplication()).getComponent()).build()
                .inject(this);

        return fragment;
    }
}

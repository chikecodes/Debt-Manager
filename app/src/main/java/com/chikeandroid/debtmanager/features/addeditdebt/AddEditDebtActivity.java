package com.chikeandroid.debtmanager.features.addeditdebt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.chikeandroid.debtmanager.DebtManagerApplication;
import com.chikeandroid.debtmanager.base.SingleFragmentActivity;
import com.chikeandroid.debtmanager.data.PersonDebt;

import javax.inject.Inject;

/**
 * Created by Chike on 3/16/2017.
 * Displays the AddEditDebt Screen
 * Might have extras:
 * - Boolean: ARGUMENT_EDIT_DEBT
 */

public class AddEditDebtActivity extends SingleFragmentActivity {

    public static final int REQUEST_ADD_DEBT = 1;
    public static final int REQUEST_EDIT_DEBT = 2;

    @Inject
    AddEditDebtPresenter mAddEditDebtPresenter;

    public static void start(@NonNull Activity context, @NonNull int requestCode) {

        Intent intent = new Intent(context, AddEditDebtActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    public static void startFromDebtDetailScreen(@NonNull Context context, @NonNull PersonDebt personDebt,
                                                 @NonNull Fragment fragment) {
        Intent intent = new Intent(context, AddEditDebtActivity.class);
        intent.putExtra(AddEditDebtFragment.ARGUMENT_EDIT_DEBT, personDebt);
        fragment.startActivityForResult(intent, AddEditDebtActivity.REQUEST_EDIT_DEBT);
    }

    @Override
    protected Fragment createFragment() {

        AddEditDebtFragment fragment;

        boolean editDebt = false;
        if (getIntent().hasExtra(AddEditDebtFragment.ARGUMENT_EDIT_DEBT)) {

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

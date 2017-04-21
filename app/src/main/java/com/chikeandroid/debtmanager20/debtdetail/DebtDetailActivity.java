package com.chikeandroid.debtmanager20.debtdetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.chikeandroid.debtmanager20.DebtManagerApplication;
import com.chikeandroid.debtmanager20.base.SingleFragmentActivity;

import javax.inject.Inject;

/**
 * Created by Chike on 4/20/2017.
 */

public class DebtDetailActivity extends SingleFragmentActivity {

    @Inject
    DebtDetailPresenter mDebtDetailPresenter;

    @Override
    protected Fragment createFragment() {

        Bundle b = getIntent().getExtras();

        DebtDetailFragment fragment = DebtDetailFragment.newInstance(b);

        DaggerDebtDetailComponent.builder()
                .debtDetailPresenterModule(new DebtDetailPresenterModule(fragment))
                .applicationComponent(((DebtManagerApplication) getApplication()).getComponent()).build()
                .inject(this);

        return fragment;
    }
}

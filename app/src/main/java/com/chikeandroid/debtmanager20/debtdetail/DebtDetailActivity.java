package com.chikeandroid.debtmanager20.debtdetail;

import android.support.v4.app.Fragment;

import com.chikeandroid.debtmanager20.base.SingleFragmentActivity;

/**
 * Created by Chike on 4/20/2017.
 */

public class DebtDetailActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {

        String debtId = getIntent().getStringExtra(DebtDetailFragment.EXTRA_DEBT_ID);

        return DebtDetailFragment.newInstance(debtId);
    }
}

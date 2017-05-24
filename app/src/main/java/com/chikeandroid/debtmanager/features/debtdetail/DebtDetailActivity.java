package com.chikeandroid.debtmanager.features.debtdetail;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.chikeandroid.debtmanager.base.SingleFragmentActivity;

/**
 * Created by Chike on 4/20/2017.
 * Displays the Debt Detail screen
 */
public class DebtDetailActivity extends SingleFragmentActivity {

    public static void start(Context context, String debtId, int debtType) {
        Intent intent = new Intent(context, DebtDetailActivity.class);
        intent.putExtra(DebtDetailFragment.EXTRA_DEBT_ID, debtId);
        intent.putExtra(DebtDetailFragment.EXTRA_DEBT_TYPE, debtType);
        context.startActivity(intent);
    }

    @Override
    protected Fragment createFragment() {

        String debtId = getIntent().getStringExtra(DebtDetailFragment.EXTRA_DEBT_ID);
        int debtType = getIntent().getIntExtra(DebtDetailFragment.EXTRA_DEBT_TYPE, -1);

        return DebtDetailFragment.newInstance(debtId, debtType);
    }
}

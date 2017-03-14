package com.chikeandroid.debtmanager20.OweMe;

import android.support.v4.app.Fragment;

import com.chikeandroid.debtmanager20.data.Debt;

import java.util.List;

/**
 * Created by Chike on 3/13/2017.
 */

public class OweMeFragment extends Fragment implements OweMeContract.View {


    @Override
    public void showDebts(List<Debt> debts) {

    }

    @Override
    public void showDebtDetailsUi(String debtId) {

    }

    @Override
    public void showNoDebts() {

    }

    @Override
    public void showLoadingDebtsError() {

    }

    @Override
    public boolean isActive() {
        return false;
    }
}

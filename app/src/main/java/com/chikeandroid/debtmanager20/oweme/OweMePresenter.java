package com.chikeandroid.debtmanager20.oweme;

import android.support.annotation.NonNull;

import com.chikeandroid.debtmanager20.base.BasePresenter;
import com.chikeandroid.debtmanager20.data.Debt;

/**
 * Created by Chike on 3/13/2017.
 */

public class OweMePresenter<V extends OweMeContract.View> extends BasePresenter<V> implements OweMeContract.Presenter<V> {

    @Override
    public void loadDebts(boolean forceUpdate) {

    }

    @Override
    public void openDebtDetails(@NonNull Debt debt) {

    }
}

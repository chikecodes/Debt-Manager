package com.chikeandroid.debtmanager20.iowe;

import android.support.annotation.NonNull;

import com.chikeandroid.debtmanager20.base.BasePresenter;
import com.chikeandroid.debtmanager20.data.Debt;

/**
 * Created by Chike on 3/14/2017.
 */

public class IOwePresenter<V extends IOweContract.View> extends BasePresenter<V> implements IOweContract.Presenter<V> {


    @Override
    public void loadDebts(boolean forceUpdate) {

    }

    @Override
    public void openDebtDetails(@NonNull Debt debt) {

    }
}

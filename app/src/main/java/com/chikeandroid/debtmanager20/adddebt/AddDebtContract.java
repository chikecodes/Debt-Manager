package com.chikeandroid.debtmanager20.adddebt;

import com.chikeandroid.debtmanager20.base.MvpPresenter;
import com.chikeandroid.debtmanager20.base.MvpView;

/**
 * Created by Chike on 3/16/2017.
 */

public interface AddDebtContract {

    interface View extends MvpView {

        boolean isActive();

        void showErroSavingDebt();

    }

    interface Presenter<V extends MvpView> extends MvpPresenter<V> {

        void save

    }
}

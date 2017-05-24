package com.chikeandroid.debtmanager20.features.addeditdebt;

import com.chikeandroid.debtmanager20.base.BasePresenter;
import com.chikeandroid.debtmanager20.base.BaseView;
import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.data.Person;

/**
 * Created by Chike on 3/16/2017.
 */

public interface AddEditDebtContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void showErroSavingDebt();

        void showDebts();

        void showEmptyDebtError();

    }

    interface Presenter extends BasePresenter {

        void saveDebt(Person person, Debt debt);
    }
}

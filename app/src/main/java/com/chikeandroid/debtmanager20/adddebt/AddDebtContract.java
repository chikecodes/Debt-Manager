package com.chikeandroid.debtmanager20.adddebt;

import com.chikeandroid.debtmanager20.base.BasePresenter;
import com.chikeandroid.debtmanager20.base.BaseView;

/**
 * Created by Chike on 3/16/2017.
 */

public interface AddDebtContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void showErroSavingDebt();

        void showEmptyDebtError();

    }

    interface Presenter extends BasePresenter {

        void saveDebt(String name, String phoneNumber, double amount, String note, long createdDate,
                      long dueDate, int debtType, int status);


    }
}

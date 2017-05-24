package com.chikeandroid.debtmanager.features.persondetail;

import com.chikeandroid.debtmanager.base.BasePresenter;
import com.chikeandroid.debtmanager.base.BaseView;
import com.chikeandroid.debtmanager.data.Debt;

import java.util.List;

/**
 * Created by Chike on 4/20/2017.
 * Contract for View and Presenter
 */

public interface PersonDetailContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void showPersonDebts(List<Debt> debts);

        void showMissingPersonDebts();
    }

    interface Presenter extends BasePresenter {

    }
}

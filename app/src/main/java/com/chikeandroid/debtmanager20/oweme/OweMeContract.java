package com.chikeandroid.debtmanager20.oweme;

import com.chikeandroid.debtmanager20.base.BasePresenter;
import com.chikeandroid.debtmanager20.base.BaseView;
import com.chikeandroid.debtmanager20.data.PersonDebt;

import java.util.List;

/**
 * Created by Chike on 3/13/2017.
 */

public interface OweMeContract {

    interface View extends BaseView<Presenter> {

        void showDebts(List<PersonDebt> debts);

        void showEmptyView();

        void showLoadingDebtsError();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

    }
}

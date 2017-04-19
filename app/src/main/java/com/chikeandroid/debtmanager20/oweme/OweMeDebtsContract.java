package com.chikeandroid.debtmanager20.oweme;

import android.support.annotation.NonNull;

import com.chikeandroid.debtmanager20.base.BasePresenter;
import com.chikeandroid.debtmanager20.base.BaseView;
import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.data.PersonDebt;

import java.util.List;

/**
 * Created by Chike on 3/13/2017.
 */

public interface OweMeDebtsContract {

    interface View extends BaseView<Presenter> {

        void showDebts(List<PersonDebt> debts);

        void showDebtDetailsUi(String debtId);

        void showEmptyView();

        void showAddDebtUI();

        void showLoadingDebtsError();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void openDebtDetails(@NonNull Debt debt);
    }
}

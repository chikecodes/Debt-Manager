package com.chikeandroid.debtmanager20.iowe;

import android.support.annotation.NonNull;

import com.chikeandroid.debtmanager20.base.BasePresenter;
import com.chikeandroid.debtmanager20.base.BaseView;
import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.data.PersonDebt;

import java.util.List;

/**
 * Created by Chike on 3/14/2017.
 * This specifies the contract between the view and the presenter.
 */

public interface IOweContract {

    interface View extends BaseView<Presenter> {

        void showDebts(List<PersonDebt> debts);

        void showDebtDetailsUi(String debtId);

        void showLoadingDebtsError();

        void showEmptyView();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void openDebtDetails(@NonNull Debt debt);

        void batchDeletePersonDebts(@NonNull List<PersonDebt> personDebts, @NonNull int debtType);
    }
}

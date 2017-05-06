package com.chikeandroid.debtmanager20.oweme;

import android.support.annotation.NonNull;

import com.chikeandroid.debtmanager20.base.BasePresenter;
import com.chikeandroid.debtmanager20.base.BaseView;
import com.chikeandroid.debtmanager20.data.PersonDebt;

import java.util.List;

/**
 * Created by Chike on 3/13/2017.
 * This specifies the contract between the view and the presenter.
 */

public interface OweMeContract {

    interface View extends BaseView<Presenter> {

        void showDebts(List<PersonDebt> debts);

        void showEmptyView();

        void showLoadingDebtsError();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void batchDeletePersonDebts(@NonNull List<PersonDebt> personDebts, @NonNull int debtType);
    }
}

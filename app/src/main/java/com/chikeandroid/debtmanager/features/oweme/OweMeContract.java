package com.chikeandroid.debtmanager.features.oweme;

import android.support.annotation.NonNull;

import com.chikeandroid.debtmanager.base.BasePresenter;
import com.chikeandroid.debtmanager.base.BaseView;
import com.chikeandroid.debtmanager.data.PersonDebt;

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

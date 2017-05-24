package com.chikeandroid.debtmanager.features.debtdetail;

import android.support.annotation.NonNull;

import com.chikeandroid.debtmanager.base.BasePresenter;
import com.chikeandroid.debtmanager.base.BaseView;
import com.chikeandroid.debtmanager.data.PersonDebt;

/**
 * Created by Chike on 4/20/2017.
 */

public interface DebtDetailContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void addPartialPayment();

        void showPersonDebt(@NonNull PersonDebt personDebt);

        void showMissingDebt();

        void showPersonDebtDeleted();
    }

    interface Presenter extends BasePresenter {

        void addAdditionalDebt();

        void deletePersonDebt(@NonNull PersonDebt personDebt);
    }
}

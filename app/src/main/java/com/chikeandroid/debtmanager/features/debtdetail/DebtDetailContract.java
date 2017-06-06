package com.chikeandroid.debtmanager.features.debtdetail;

import android.support.annotation.NonNull;

import com.chikeandroid.debtmanager.base.BasePresenter;
import com.chikeandroid.debtmanager.base.BaseView;
import com.chikeandroid.debtmanager.data.Debt;
import com.chikeandroid.debtmanager.data.Payment;
import com.chikeandroid.debtmanager.data.PersonDebt;

/**
 * Created by Chike on 4/20/2017.
 */

public interface DebtDetailContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void showPersonDebt(@NonNull PersonDebt personDebt);

        void showMissingDebt();

        void showPersonDebtDeleted();

        void showPaymentDeleted();
    }

    interface Presenter extends BasePresenter {

        void addPartialPayment(@NonNull Payment payment);

        void editPayment(@NonNull Payment payment, @NonNull Debt debt);

        void deletePersonDebt(@NonNull PersonDebt personDebt);

        void deletePayment(@NonNull Payment payment);

        void refreshPayments();

    }
}

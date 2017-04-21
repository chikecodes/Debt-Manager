package com.chikeandroid.debtmanager20.debtdetail;

import android.support.annotation.NonNull;

import com.chikeandroid.debtmanager20.base.BasePresenter;
import com.chikeandroid.debtmanager20.base.BaseView;

/**
 * Created by Chike on 4/20/2017.
 */

public interface DebtDetailContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void openEditDebtUi();

        void deleteDebt();

        void callDebtor();

        void addPartialPayment();
    }

    interface Presenter extends BasePresenter {

        void deleteDebt(@NonNull String debtId);

        void addAdditionalDebt();
    }
}

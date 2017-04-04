package com.chikeandroid.debtmanager20.oweme;

import android.support.annotation.NonNull;

import com.chikeandroid.debtmanager20.base.BasePresenter;
import com.chikeandroid.debtmanager20.base.BaseView;
import com.chikeandroid.debtmanager20.data.Debt;

import java.util.List;

/**
 * Created by Chike on 3/13/2017.
 */

public interface OweMeContract {

    interface View extends BaseView<Presenter> {

        void showDebts(List<Debt> debts);

        void showDebtDetailsUi(String debtId);

        void showNoDebts();

        void showLoadingDebtsError();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void loadDebts(boolean forceUpdate);

        void openDebtDetails(@NonNull Debt debt);
    }
}

package com.chikeandroid.debtmanager20.OweMe;

import android.support.annotation.NonNull;

import com.chikeandroid.debtmanager20.base.MvpPresenter;
import com.chikeandroid.debtmanager20.base.MvpView;
import com.chikeandroid.debtmanager20.data.Debt;

import java.util.List;

/**
 * Created by Chike on 3/13/2017.
 */

public interface OweMeContract {

    interface View extends MvpView {

        void showDebts(List<Debt> debts);

        void showDebtDetailsUi(String debtId);

        void showNoDebts();

        void showLoadingDebtsError();

        boolean isActive();
    }

    interface Presenter<V extends View> extends MvpPresenter<V> {

        void loadDebts(boolean forceUpdate);

        void openDebtDetails(@NonNull Debt debt);
    }
}

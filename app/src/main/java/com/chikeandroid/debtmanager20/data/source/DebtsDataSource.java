package com.chikeandroid.debtmanager20.data.source;

import android.support.annotation.NonNull;

import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.data.Person;

import java.util.List;

/**
 * Created by Chike on 3/22/2017.
 */

public interface DebtsDataSource {

    interface LoadDebtsCallback {

        void onDebtsLoaded(List<Debt> debts);

        void onDataNotAvailable();
    }

    interface GetDebtCallback {

        void onDebtLoaded(Debt debt);

        void onDataNotAvailable();
    }

    void getDebts(@NonNull LoadDebtsCallback callback);

    void getDebt(@NonNull String debtId, @NonNull GetDebtCallback callback);

    void getIOwedDebts(@NonNull LoadDebtsCallback callback);

    void getOwedDebts(@NonNull LoadDebtsCallback callback);

    void saveDebt(@NonNull Debt debt, @NonNull Person person);

    void refreshDebts();

    void deleteAllDebts();

    void deleteDebt(@NonNull String debtId);


}

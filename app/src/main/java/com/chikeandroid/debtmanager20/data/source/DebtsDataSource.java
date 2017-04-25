package com.chikeandroid.debtmanager20.data.source;

import android.support.annotation.NonNull;

import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.data.Person;
import com.chikeandroid.debtmanager20.data.PersonDebt;

import java.util.List;

/**
 * Created by Chike on 3/22/2017.
 */

public interface DebtsDataSource {

    PersonDebt getDebt(@NonNull String debtId);

    List<PersonDebt> getAllDebts();

    List<PersonDebt> getAllDebtsByType(@NonNull int debtType);

    void saveDebt(@NonNull Debt debt, @NonNull Person person);

    void refreshDebts();

    void deleteAllDebts();

    void deleteDebt(@NonNull String debtId);

    void deleteAllDebtsByType(@NonNull int debtType);

    void updateDebt(@NonNull PersonDebt personDebt);

    String saveNewPerson(@NonNull Person person) ;
}

package com.chikeandroid.debtmanager20.data.source;

import android.support.annotation.NonNull;

import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.data.Person;
import com.chikeandroid.debtmanager20.data.PersonDebt;

import java.util.List;

/**
 * Created by Chike on 3/22/2017.
 * Main entry point for accessing debts data.
 */

public interface PersonDebtsDataSource {

    PersonDebt getPersonDebt(@NonNull String debtId, @NonNull int debtType);

    List<PersonDebt> getAllPersonDebts();

    List<PersonDebt> getAllPersonDebtsByType(@NonNull int debtType);

    void savePersonDebt(@NonNull Debt debt, @NonNull Person person);

    void refreshDebts();

    void deleteAllPersonDebts();

    void deletePersonDebt(@NonNull PersonDebt personDebt);

    void deleteAllPersonDebtsByType(@NonNull int debtType);

    void updatePersonDebt(@NonNull PersonDebt personDebt);

    String saveNewPerson(@NonNull Person person);

    Person getPerson(@NonNull String personId);

    void deletePerson(@NonNull String personId);
}

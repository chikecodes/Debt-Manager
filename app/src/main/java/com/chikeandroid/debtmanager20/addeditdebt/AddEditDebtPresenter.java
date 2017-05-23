package com.chikeandroid.debtmanager20.addeditdebt;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.data.Person;
import com.chikeandroid.debtmanager20.data.PersonDebt;
import com.chikeandroid.debtmanager20.data.source.PersonDebtsDataSource;
import com.chikeandroid.debtmanager20.data.source.PersonDebtsRepository;
import com.chikeandroid.debtmanager20.util.EspressoIdlingResource;

import javax.inject.Inject;

/**
 * Created by Chike on 3/16/2017.
 */

public class AddEditDebtPresenter implements AddEditDebtContract.Presenter {

    @NonNull
    private final PersonDebtsDataSource mDebtsRepository;

    @NonNull
    private final AddEditDebtContract.View mAddDebtsView;

    @Nullable
    private final boolean mEditDebt;

    @Inject
    AddEditDebtPresenter(PersonDebtsRepository debtsRepository, AddEditDebtContract.View view, boolean editDebt) {
        mDebtsRepository = debtsRepository;
        mAddDebtsView = view;
        mEditDebt = editDebt;
    }

    @Inject
    void setUpListeners() {
        mAddDebtsView.setPresenter(this);
    }

    @Override
    public void saveDebt(Person person, Debt debt) {

        EspressoIdlingResource.increment();

        if (isUpdateDebt()) {
            updatePersonDebt(person, debt);
        } else {
            createPersonDebt(person, debt);
        }
    }

    private void updatePersonDebt(Person person, Debt debt) {
        if (!isUpdateDebt()) {
            throw new RuntimeException("updatePersonDebt() was called but debt is new.");
        }
        PersonDebt personDebt = new PersonDebt(person, debt);
        mDebtsRepository.updatePersonDebt(personDebt);
        mAddDebtsView.showDebts();
    }

    private void createPersonDebt(Person person, Debt debt) {

        if (person.isEmpty() && debt.isEmpty()) {
            mAddDebtsView.showEmptyDebtError();
        } else {
            mDebtsRepository.savePersonDebt(debt, person);
            mAddDebtsView.showDebts();
        }
    }

    private boolean isUpdateDebt() {
        return mEditDebt;
    }

    @Override
    public void start() {
        // do nothing for now
    }

    @Override
    public void stop() {
        // do nothing for now
    }
}

package com.chikeandroid.debtmanager20.adddebt;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.data.Person;
import com.chikeandroid.debtmanager20.data.PersonDebt;
import com.chikeandroid.debtmanager20.data.source.DebtsDataSource;
import com.chikeandroid.debtmanager20.data.source.DebtsRepository;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Chike on 3/16/2017.
 */

public class AddEditDebtPresenter implements AddEditDebtContract.Presenter {

    @NonNull
    private final DebtsDataSource mDebtsRepository;

    @NonNull
    private final AddEditDebtContract.View mAddDebtsView;

    @Nullable
    private String mDebtId;

    @Nullable
    private String mPersonId;

    @Inject
    AddEditDebtPresenter(DebtsRepository debtsRepository, AddEditDebtContract.View view,
                         @Nullable @Named("debt_id") String debtId, @Nullable @Named("person_id") String personId) {
        mDebtsRepository = debtsRepository;
        mAddDebtsView = view;
        mDebtId = debtId;
        mPersonId = personId;
    }

    @Inject
    void setUpListeners() {
        mAddDebtsView.setPresenter(this);
    }

    @Override
    public void saveDebt(String name, String phoneNumber, double amount, String note,
                         long createdDate, long dueDate, int debtType, int status) {

        if(isNewDebt()) {
            createDebt(name, phoneNumber, amount, note, createdDate, dueDate, debtType, status);
        } else {
            updateDebt(name, phoneNumber, amount, note, createdDate, dueDate, debtType, status);
        }
    }

    private void updateDebt(String name, String phoneNumber, double amount, String note, long createdDate, long dueDate, int debtType, int status) {
        if(isNewDebt()) {
            throw new RuntimeException("updateDebt() was called but debt is new.");
        }

        Person person = new Person(mPersonId, name, phoneNumber);
        Debt debt = new Debt.Builder(mDebtId, person.getId(), amount, createdDate, debtType, status)
                .dueDate(dueDate)
                .note(note)
                .build();
        PersonDebt personDebt = new PersonDebt(person, debt);
        mDebtsRepository.updateDebt(personDebt);
        mAddDebtsView.showDebts();

    }

    private void createDebt(String name, String phoneNumber, double amount, String note,
                            long createdAt, long dateDue, int debtType, int status) {

        Person person = new Person(UUID.randomUUID().toString(), name, phoneNumber);
        Debt debt = new Debt.Builder(UUID.randomUUID().toString(), person.getId(), amount, createdAt,
                debtType, status)
                .dueDate(dateDue)
                .note(note)
                .build();

        if(person.isEmpty() && debt.isEmpty()) {
            mAddDebtsView.showEmptyDebtError();
        } else {
            mDebtsRepository.saveDebt(debt, person);
            mAddDebtsView.showDebts();
        }
    }

    private boolean isNewDebt() {
        return mDebtId == null && mPersonId == null;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}

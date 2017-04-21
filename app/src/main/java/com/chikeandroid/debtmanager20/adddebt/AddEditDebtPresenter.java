package com.chikeandroid.debtmanager20.adddebt;

import android.support.annotation.NonNull;

import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.data.Person;
import com.chikeandroid.debtmanager20.data.source.DebtsDataSource;
import com.chikeandroid.debtmanager20.data.source.DebtsRepository;

import java.util.UUID;

import javax.inject.Inject;

/**
 * Created by Chike on 3/16/2017.
 */

public class AddEditDebtPresenter implements AddEditDebtContract.Presenter {

    @NonNull
    private final DebtsDataSource mDebtsRepository;

    @NonNull
    private final AddEditDebtContract.View mAddDebtsView;

    @Inject
    AddEditDebtPresenter(DebtsRepository debtsRepository, AddEditDebtContract.View view) {
        mDebtsRepository = debtsRepository;
        mAddDebtsView = view;
    }

    @Inject
    void setUpListeners() {
        mAddDebtsView.setPresenter(this);
    }

    @Override
    public void saveDebt(String name, String phoneNumber, double amount, String note,
                         long createdDate, long dueDate, int debtType, int status) {

        createDebt(name, phoneNumber, amount, note, createdDate, dueDate, debtType, status);
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

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}

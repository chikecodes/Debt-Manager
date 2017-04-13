package com.chikeandroid.debtmanager20.adddebt;

import android.support.annotation.NonNull;

import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.data.Person;
import com.chikeandroid.debtmanager20.data.source.DebtsDataSource;
import com.chikeandroid.debtmanager20.data.source.DebtsRepository;

import javax.inject.Inject;

/**
 * Created by Chike on 3/16/2017.
 */

public class AddDebtPresenter implements AddDebtContract.Presenter {

    @NonNull
    private final DebtsDataSource mDebtsRepository;

    @NonNull
    private final AddDebtContract.View mAddDebtsView;

    @Inject
    AddDebtPresenter(DebtsRepository debtsRepository, AddDebtContract.View view) {
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

        Person person = new Person(name, phoneNumber);
        Debt debt = new Debt.Builder(person.getId(), amount, createdAt, debtType, status)
                .dueDate(dateDue)
                .note(note)
                .build();

        if(debt.isEmpty()) {
            // mAddDebtsView.showEmptyDebtError();
        } else {
            mDebtsRepository.saveDebt(debt, person);
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}

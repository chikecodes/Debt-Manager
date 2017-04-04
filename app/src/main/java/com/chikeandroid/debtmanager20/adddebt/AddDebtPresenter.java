package com.chikeandroid.debtmanager20.adddebt;

import android.support.annotation.NonNull;
import android.util.Log;

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
        Log.d("kolo", "presenter created");
    }

    @Inject
    void setUpListeners() {
        mAddDebtsView.setPresenter(this);
        Log.d("kolo", "presenter setupListener");

    }

    @Override
    public void saveDebt(String name, String phoneNumber, double amount, String note,
                         long createdDate, long dueDate, int debtType, int status) {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}

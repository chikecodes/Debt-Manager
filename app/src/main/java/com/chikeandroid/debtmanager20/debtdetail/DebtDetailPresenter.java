package com.chikeandroid.debtmanager20.debtdetail;

import android.support.annotation.NonNull;

import com.chikeandroid.debtmanager20.data.source.DebtsDataSource;
import com.chikeandroid.debtmanager20.data.source.DebtsRepository;

import javax.inject.Inject;

/**
 * Created by Chike on 4/20/2017.
 */

public class DebtDetailPresenter implements DebtDetailContract.Presenter {

    @NonNull
    private final DebtsDataSource mDebtsRepository;

    @NonNull
    private final DebtDetailContract.View mDebtDetailView;

    @Inject
    public DebtDetailPresenter(DebtsRepository debtsRepository, DebtDetailContract.View view) {
        mDebtsRepository = debtsRepository;
        mDebtDetailView = view;
    }

    @Inject
    void setUpListener() {
        mDebtDetailView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void deleteDebt(@NonNull String debtId) {

    }

    @Override
    public void addAdditionalDebt() {

    }
}

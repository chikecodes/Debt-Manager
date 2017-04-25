package com.chikeandroid.debtmanager20.oweme;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.data.PersonDebt;
import com.chikeandroid.debtmanager20.data.source.DebtsDataSource;
import com.chikeandroid.debtmanager20.data.source.DebtsRepository;
import com.chikeandroid.debtmanager20.data.loaders.DebtsLoader;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Chike on 3/13/2017.
 */

public class OweMeDebtsPresenter implements OweMeDebtsContract.Presenter, LoaderManager.LoaderCallbacks<List<PersonDebt>> {

    private static final String TAG = "OweMeDebtsPresenter";

    private final static int OWE_ME_DEBTS_QUERY = 1;

    @NonNull
    private final DebtsDataSource mDebtsRepository;

    @NonNull
    private final OweMeDebtsContract.View mOweMeDebtsView;

    @NonNull
    private final LoaderManager mLoaderManager;

    private final DebtsLoader mLoader;

    private List<PersonDebt> mCurrentDebts;

    @Inject
    OweMeDebtsPresenter(DebtsRepository debtsRepository, OweMeDebtsContract.View view,
                        LoaderManager loaderManager, DebtsLoader loader) {
        mLoader = loader;
        mDebtsRepository = debtsRepository;
        mOweMeDebtsView = view;
        mLoaderManager = loaderManager;
    }

    @Inject
    void setUpListeners() {
        mOweMeDebtsView.setPresenter(this);
    }

    @Override
    public void start() {
        mLoaderManager.initLoader(OWE_ME_DEBTS_QUERY, null, this);
    }

    @Override
    public void stop() {

    }

    @Override
    public void openDebtDetails(@NonNull Debt debt) {

    }

    @Override
    public Loader<List<PersonDebt>> onCreateLoader(int id, Bundle args) {
        // set loading indicator true
        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<PersonDebt>> loader, List<PersonDebt> data) {

        // set view loading indicator to false
        mCurrentDebts = data;
        if(mCurrentDebts == null) {
            mOweMeDebtsView.showLoadingDebtsError();
        } else {
            showOweMeDebts();
        }
    }

    private void showOweMeDebts() {
        List<PersonDebt> debtsToShow = new ArrayList<>();
        if(mCurrentDebts != null) {
            for(PersonDebt personDebt : mCurrentDebts) {
                if(personDebt.getDebt().getDebtType() == Debt.DEBT_TYPE_OWED) {
                    debtsToShow.add(personDebt);
                }
            }
        }

        processDebts(debtsToShow);
    }
    private void processDebts(List<PersonDebt> debts) {
        if(debts.isEmpty()) {
            // show empty debt view
            mOweMeDebtsView.showEmptyView();
        } else {
            mOweMeDebtsView.showDebts(debts);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<PersonDebt>> loader) {

    }
}

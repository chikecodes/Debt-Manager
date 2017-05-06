package com.chikeandroid.debtmanager20.iowe;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.data.PersonDebt;
import com.chikeandroid.debtmanager20.data.source.PersonDebtsDataSource;
import com.chikeandroid.debtmanager20.data.source.PersonDebtsRepository;
import com.chikeandroid.debtmanager20.iowe.loader.IOweLoader;
import com.chikeandroid.debtmanager20.util.EspressoIdlingResource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Chike on 3/14/2017.
 * Listens to user actions from the UI ({@link IOweFragment}), retrieves the data and updates the
 */

public class IOwePresenter implements IOweContract.Presenter, LoaderManager.LoaderCallbacks<List<PersonDebt>> {

    private final static int IOWe_QUERY = 1;

    @NonNull
    private final IOweContract.View mIOweView;

    @NonNull
    private final LoaderManager mLoaderManager;

    @NonNull
    private final PersonDebtsDataSource mPersonDebtsRepository;

    private final IOweLoader mLoader;

    private List<PersonDebt> mCurrentDebts;

    @Inject
    IOwePresenter(IOweContract.View view, PersonDebtsRepository debtsRepository, LoaderManager loaderManager, IOweLoader loader) {
        mLoader = loader;
        mIOweView = view;
        mPersonDebtsRepository = debtsRepository;
        mLoaderManager = loaderManager;
    }

    @Inject
    void setUpListeners() {
        mIOweView.setPresenter(this);
    }

    @Override
    public void start() {
        mLoaderManager.initLoader(IOWe_QUERY, null, this);
    }

    @Override
    public Loader<List<PersonDebt>> onCreateLoader(int id, Bundle args) {
        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<PersonDebt>> loader, List<PersonDebt> data) {

        // This callback may be called twice, once for the cache and once for loading
        // the data from the server API, so we check before decrementing, otherwise
        // it throws "Counter has been corrupted!" exception.
        if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
            EspressoIdlingResource.decrement(); // Set app as idle.
        }

        // set view loading indicator to false
        mCurrentDebts = data;
        if(mCurrentDebts == null) {
            mIOweView.showLoadingDebtsError();
        } else {
            showIOweDebts();
        }
    }

    private void showIOweDebts() {
        List<PersonDebt> debtsToShow = new ArrayList<>();
        if(mCurrentDebts != null) {
            for(PersonDebt personDebt : mCurrentDebts) {
                if(personDebt.getDebt().getDebtType() == Debt.DEBT_TYPE_IOWE) {
                    debtsToShow.add(personDebt);
                }
            }
        }

        processDebts(debtsToShow);
    }
    private void processDebts(List<PersonDebt> debts) {
        if(debts.isEmpty()) {
            mIOweView.showEmptyView();
        } else {
            mIOweView.showDebts(debts);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<PersonDebt>> loader) {
        //  remove any references it has to the Loader's data.
    }

    @Override
    public void stop() {
        // presenter callback stop
    }

    @Override
    public void batchDeletePersonDebts(@NonNull List<PersonDebt> personDebts, @NonNull int debtType) {

        if(!personDebts.isEmpty()) {
            mPersonDebtsRepository.batchDelete(personDebts, debtType);
        }
    }
}

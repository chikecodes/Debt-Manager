package com.chikeandroid.debtmanager20.oweme;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.data.PersonDebt;
import com.chikeandroid.debtmanager20.data.source.PersonDebtsDataSource;
import com.chikeandroid.debtmanager20.data.source.PersonDebtsRepository;
import com.chikeandroid.debtmanager20.oweme.loader.OweMeLoader;
import com.chikeandroid.debtmanager20.util.EspressoIdlingResource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Chike on 3/13/2017.
 * Listens to user actions from the UI ({@link OweMeFragment}), retrieves the data and updates the
 */

public class OweMePresenter implements OweMeContract.Presenter, LoaderManager.LoaderCallbacks<List<PersonDebt>> {

    private final static int OWE_ME_DEBTS_QUERY = 1;

    @NonNull
    private final OweMeContract.View mOweMeDebtsView;

    @NonNull
    private final PersonDebtsDataSource mPersonDebtsRepository;

    @NonNull
    private final LoaderManager mLoaderManager;

    private final OweMeLoader mLoader;

    private List<PersonDebt> mCurrentDebts;

    @Inject
    OweMePresenter(OweMeContract.View view, PersonDebtsRepository debtsRepository, LoaderManager loaderManager, OweMeLoader loader) {
        mLoader = loader;
        mOweMeDebtsView = view;
        mPersonDebtsRepository = debtsRepository;
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
        // presenter stop callback
    }

    @Override
    public Loader<List<PersonDebt>> onCreateLoader(int id, Bundle args) {
        // set loading indicator true
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
            mOweMeDebtsView.showEmptyView();
        } else {
            mOweMeDebtsView.showDebts(debts);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<PersonDebt>> loader) {
        //  remove any references it has to the Loader's data.
    }

    @Override
    public void batchDeletePersonDebts(@NonNull List<PersonDebt> personDebts, @NonNull int debtType) {

        if(!personDebts.isEmpty()) {
            mPersonDebtsRepository.batchDelete(personDebts, debtType);
        }
    }
}

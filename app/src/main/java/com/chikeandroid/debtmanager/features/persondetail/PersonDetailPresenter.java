package com.chikeandroid.debtmanager.features.persondetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.chikeandroid.debtmanager.data.Debt;
import com.chikeandroid.debtmanager.data.Person;
import com.chikeandroid.debtmanager.features.persondetail.loader.PersonDebtsLoader;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Chike on 4/20/2017.
 * Listens to user actions from the UI ({@link PersonDetailFragment}), retrieves the data and updates
 * the UI as required.
 */
public class PersonDetailPresenter implements PersonDetailContract.Presenter, LoaderManager.LoaderCallbacks<List<Debt>> {

    private static final int DEBT_QUERY = 106;

    @NonNull
    private final PersonDetailContract.View mPersonDebtsDetailView;

    @NonNull
    private final LoaderManager mLoaderManager;

    private final PersonDebtsLoader mLoader;
    private final Person mPerson;

    @Inject
    public PersonDetailPresenter(PersonDetailContract.View view, LoaderManager loaderManager,
                                 PersonDebtsLoader loader, Person person) {
        mPersonDebtsDetailView = view;
        mLoaderManager = loaderManager;
        mLoader = loader;
        mPerson = person;
    }

    @Inject
    void setUpListener() {
        mPersonDebtsDetailView.setPresenter(this);
    }

    @Override
    public void start() {
        mLoaderManager.initLoader(DEBT_QUERY, null, this);
    }

    @Override
    public void stop() {
        // stop presenter
    }

    @Override
    public Loader<List<Debt>> onCreateLoader(int id, Bundle args) {
        if (mPerson == null) {
            return null;
        }
        // can set loading indicator
        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<Debt>> loader, List<Debt> data) {

        if (data.isEmpty()) {
            mPersonDebtsDetailView.showMissingPersonDebts();
        }else {
            mPersonDebtsDetailView.showPersonDebts(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Debt>> loader) {
        // remove any references it has to the Loader's data.
    }
}

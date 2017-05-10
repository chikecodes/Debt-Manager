package com.chikeandroid.debtmanager20.people;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.chikeandroid.debtmanager20.data.Person;
import com.chikeandroid.debtmanager20.data.source.PersonDebtsDataSource;
import com.chikeandroid.debtmanager20.data.source.PersonDebtsRepository;
import com.chikeandroid.debtmanager20.people.loader.PeopleLoader;
import com.chikeandroid.debtmanager20.util.EspressoIdlingResource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Chike on 3/14/2017.
 * Listens to user actions from the UI ({@link PeopleFragment}), retrieves the data and updates the
 */

public class PeoplePresenter implements PeopleContract.Presenter, LoaderManager.LoaderCallbacks<List<Person>> {

    private final static int PEOPLE_QUERY = 5;

    @NonNull
    private final PeopleContract.View mPeopleView;

    @NonNull
    private final PersonDebtsDataSource mPersonDebtsRepository;

    @NonNull
    private final LoaderManager mLoaderManager;

    private final PeopleLoader mLoader;

    private List<Person> mCurrentPeople;

    @Inject
    PeoplePresenter(PeopleContract.View view, PersonDebtsRepository debtsRepository,
                    LoaderManager loaderManager, PeopleLoader loader) {
        mLoader = loader;
        mPeopleView = view;
        mPersonDebtsRepository = debtsRepository;
        mLoaderManager = loaderManager;
    }

    @Inject
    void setUpListeners() {
        mPeopleView.setPresenter(this);
    }

    @Override
    public void start() {
        mLoaderManager.initLoader(PEOPLE_QUERY, null, this);
    }

    @Override
    public Loader<List<Person>> onCreateLoader(int id, Bundle args) {
        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<Person>> loader, List<Person> data) {

        // This callback may be called twice, once for the cache and once for loading
        // the data from the server API, so we check before decrementing, otherwise
        // it throws "Counter has been corrupted!" exception.
        if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
            EspressoIdlingResource.decrement(); // Set app as idle.
        }

        mCurrentPeople = data;
        if(mCurrentPeople == null) {
            mPeopleView.showLoadingPeopleError();
        } else {
            showPeople();
        }
    }

    private void showPeople() {
        List<Person> peopleToShow = new ArrayList<>();
        if(mCurrentPeople != null) {
            for(Person person : mCurrentPeople) {
                peopleToShow.add(person);
            }
        }

        processPeople(peopleToShow);
    }

    private void processPeople(List<Person> people) {
        if(people.isEmpty()) {
            mPeopleView.showEmptyView();
        } else {
            mPeopleView.showPeople(people);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Person>> loader) {
        //  remove any references it has to the Loader's data.
    }

    @Override
    public void stop() {

    }
}

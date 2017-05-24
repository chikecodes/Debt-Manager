package com.chikeandroid.debtmanager.features.people.loader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import com.chikeandroid.debtmanager.data.Debt;
import com.chikeandroid.debtmanager.data.Person;
import com.chikeandroid.debtmanager.data.source.PersonDebtsRepository;
import com.chikeandroid.debtmanager.util.EspressoIdlingResource;

import java.util.List;

/**
 * Created by Chike on 5/10/2017.
 * Custom {@link android.content.Loader} for a list of {@link Person}, using the
 * {@link PersonDebtsRepository} as its source. This Loader is a {@link AsyncTaskLoader} so it queries
 * the data asynchronously.
 */
public class PeopleLoader extends AsyncTaskLoader<List<Person>> implements PersonDebtsRepository.DebtsRepositoryObserver {

    private final PersonDebtsRepository mPersonDebtsRepository;

    public PeopleLoader(Context context, @NonNull PersonDebtsRepository personDebtsRepository) {
        super(context);
        mPersonDebtsRepository = personDebtsRepository;
    }

    @Override
    public List<Person> loadInBackground() {

        // App is busy until further notice
        EspressoIdlingResource.increment();

        return mPersonDebtsRepository.getAllPersonWithDebts();
    }

    @Override
    public void deliverResult(List<Person> data) {
        if (isReset()) {
            return;
        }

        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        // Deliver any previously loaded data immediately if available.
        if (mPersonDebtsRepository.cachedPeopleAvailable()) {
            deliverResult(mPersonDebtsRepository.getAllPersonWithDebts());
        }

        // Begin monitoring the underlying data source
        mPersonDebtsRepository.addContentObserver(this);

        if (takeContentChanged() || !mPersonDebtsRepository.cachedPeopleAvailable()) {
            // When a change has  been delivered or the repository cache isn't available, we force
            // a load.
            forceLoad();
        }
    }

    @Override
    public void onDebtsChanged(int debtType) {
        if (debtType == Debt.DEBT_TYPE_OWED || debtType == Debt.DEBT_TYPE_IOWE && isStarted()) {
            forceLoad();

        }
        // forceLoad();
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        mPersonDebtsRepository.removeContentObserver(this);
    }
}

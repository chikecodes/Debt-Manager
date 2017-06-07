package com.chikeandroid.debtmanager.features.persondetail.loader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import com.chikeandroid.debtmanager.data.Debt;
import com.chikeandroid.debtmanager.data.source.PersonDebtsRepository;
import com.chikeandroid.debtmanager.util.EspressoIdlingResource;

import java.util.List;

/**
 * Created by Chike on 5/19/2017.
 * Custom Loader that loads a list of a Person Debts, using the
 * {@link PersonDebtsRepository} as its source. This Loader is a {@link AsyncTaskLoader} so it queries
 * the data asynchronously.
 */
public class PersonDebtsLoader extends AsyncTaskLoader<List<Debt>> implements PersonDebtsRepository.DebtsRepositoryObserver {

    private final PersonDebtsRepository mPersonDebtsRepository;
    private final String mPersonPhoneNumber;

    public PersonDebtsLoader(Context context,
                             @NonNull PersonDebtsRepository personDebtsRepository,
                             @NonNull String personPhoneNumber) {
        super(context);
        mPersonDebtsRepository = personDebtsRepository;
        mPersonPhoneNumber = personPhoneNumber;
    }

    @Override
    public List<Debt> loadInBackground() {

        // App is busy until further notice
        EspressoIdlingResource.increment();

        return mPersonDebtsRepository.getPersonDebts(mPersonPhoneNumber);
    }

    @Override
    public void deliverResult(List<Debt> data) {
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
            deliverResult(mPersonDebtsRepository.getPersonDebts(mPersonPhoneNumber));
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
       /* if(debtType == Debt.DEBT_TYPE_OWED || debtType == Debt.DEBT_TYPE_IOWE) {
            if (isStarted()) {
                forceLoad();
            }
        }*/
        forceLoad();
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

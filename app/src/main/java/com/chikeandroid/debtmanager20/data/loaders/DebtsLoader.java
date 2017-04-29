package com.chikeandroid.debtmanager20.data.loaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import com.chikeandroid.debtmanager20.data.PersonDebt;
import com.chikeandroid.debtmanager20.data.source.PersonDebtsRepository;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Chike on 4/15/2017.
 * Custom {@link android.content.Loader} for a list of {@link PersonDebt}, using the
 * {@link PersonDebtsRepository} as its source. This Loader is a {@link AsyncTaskLoader} so it queries
 * the data asynchronously.
 */

public class DebtsLoader extends AsyncTaskLoader<List<PersonDebt>> implements PersonDebtsRepository.DebtsRepositoryObserver {

    private final PersonDebtsRepository mDebtsRepository;

    public DebtsLoader(Context context, @NonNull PersonDebtsRepository repository) {
        super(context);
        checkNotNull(repository);
        mDebtsRepository = repository;
    }

    @Override
    public List<PersonDebt> loadInBackground() {
        return mDebtsRepository.getAllPersonDebts();
    }

    @Override
    public void deliverResult(List<PersonDebt> data) {
        if(isReset()) {
            return;
        }

        if(isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        // Deliver any previously loaded data immediately if available.
        if(mDebtsRepository.cachedDebtsAvailable()) {
            deliverResult(mDebtsRepository.getAllPersonDebts());
        }

        // Begin monitoring the underlying data source
        mDebtsRepository.addContentObserver(this);

        if(takeContentChanged() || !mDebtsRepository.cachedDebtsAvailable()) {
            // When a change has  been delivered or the repository cache isn't available, we force
            // a load.
            forceLoad();
        }
    }

    @Override
    public void onDebtsChanged() {
        if(isStarted()) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        mDebtsRepository.removeContentObserver(this);
    }
}

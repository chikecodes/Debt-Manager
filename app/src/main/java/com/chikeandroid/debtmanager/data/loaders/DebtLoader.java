package com.chikeandroid.debtmanager.data.loaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import com.chikeandroid.debtmanager.data.Debt;
import com.chikeandroid.debtmanager.data.PersonDebt;
import com.chikeandroid.debtmanager.data.source.PersonDebtsRepository;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Chike on 4/24/2017.
 * (@link DebtsRespository} as its source. This Loader is a {@link AsyncTaskLoader} so it queries
 * the data asynchronously.
 */
public class DebtLoader extends AsyncTaskLoader<PersonDebt> implements PersonDebtsRepository.DebtsRepositoryObserver {

    private final String mDebtId;
    private final int mDebtType;

    private final PersonDebtsRepository mDebtsRepository;

    public DebtLoader(Context context, @NonNull PersonDebtsRepository repository,
                      @NonNull String debtId, @NonNull int debtType) {
        super(context);
        checkNotNull(repository);
        mDebtsRepository = repository;
        mDebtId = debtId;
        mDebtType = debtType;
    }

    @Override
    public PersonDebt loadInBackground() {

        return mDebtsRepository.getPersonDebt(mDebtId, mDebtType);
    }

    @Override
    public void deliverResult(PersonDebt data) {
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
        boolean cacheAvailable = false;
        if (mDebtType == Debt.DEBT_TYPE_IOWE) {
            if (mDebtsRepository.cachedIOweDebtsAvailable()) {
                deliverResult(mDebtsRepository.getCachedIOweDebt(mDebtId));
            }
            cacheAvailable = mDebtsRepository.cachedIOweDebtsAvailable();
        }else if (mDebtType == Debt.DEBT_TYPE_OWED) {
            if (mDebtsRepository.cachedOwedDebtsAvailable()) {
                deliverResult(mDebtsRepository.getCachedOwedDebt(mDebtId));
            }
            cacheAvailable = mDebtsRepository.cachedOwedDebtsAvailable();
        }

        // Begin monitoring the underlying data source
        mDebtsRepository.addContentObserver(this);

        if (takeContentChanged() || !cacheAvailable) {
            // When a change has  been delivered or the repository cache isn't available, we force
            // a load.
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

    @Override
    public void onDebtsChanged(int debtType) {
        if (isStarted()) {
            forceLoad();
        }
    }
}

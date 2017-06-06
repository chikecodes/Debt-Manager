package com.chikeandroid.debtmanager.features.debtdetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.chikeandroid.debtmanager.data.Debt;
import com.chikeandroid.debtmanager.data.Payment;
import com.chikeandroid.debtmanager.data.PersonDebt;
import com.chikeandroid.debtmanager.features.debtdetail.loaders.DebtLoader;
import com.chikeandroid.debtmanager.data.source.PersonDebtsDataSource;
import com.chikeandroid.debtmanager.data.source.PersonDebtsRepository;
import com.chikeandroid.debtmanager.util.EspressoIdlingResource;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Chike on 4/20/2017.
 * Listens to user actions from the UI ({@link DebtDetailFragment}), retrieves the data and updates
 * the UI as required.
 */
public class DebtDetailPresenter implements DebtDetailContract.Presenter, LoaderManager.LoaderCallbacks<PersonDebt> {

    private static final int DEBT_QUERY = 106;

    @NonNull
    private final PersonDebtsDataSource mDebtsRepository;

    @NonNull
    private final DebtDetailContract.View mDebtDetailView;

    @NonNull
    private final LoaderManager mLoaderManager;

    private final DebtLoader mLoader;

    private final String mDebtId;

    private int mDebtType;

    @Inject
    public DebtDetailPresenter(@NonNull String debtId,
                               @NonNull PersonDebtsRepository debtsRepository,
                               @NonNull DebtDetailContract.View view,
                               @NonNull LoaderManager loaderManager,
                               @NonNull DebtLoader loader) {
        mDebtId = debtId;
        mDebtsRepository = debtsRepository;
        mDebtDetailView = view;
        mLoaderManager = loaderManager;
        mLoader = loader;
    }

    @Inject
    void setUpListener() {
        mDebtDetailView.setPresenter(this);
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
    public void addPartialPayment(@NonNull Payment payment) {
        checkNotNull(payment);
        mDebtsRepository.savePayment(payment);
        refreshPayments();
    }

    @Override
    public void editPayment(@NonNull Payment payment, @NonNull Debt debt) {
        checkNotNull(payment);
        checkNotNull(debt);
        EspressoIdlingResource.increment();
        mDebtsRepository.editPayment(payment, debt);
        refreshPayments();
    }

    @Override
    public void deletePersonDebt(@NonNull PersonDebt personDebt) {
        checkNotNull(personDebt);
        EspressoIdlingResource.increment();
        mDebtsRepository.deletePersonDebt(personDebt);
        mDebtDetailView.showPersonDebtDeleted();
    }

    @Override
    public void deletePayment(@NonNull Payment payment) {
        checkNotNull(payment);
        EspressoIdlingResource.increment();
        mDebtsRepository.deletePayment(payment);
        mDebtDetailView.showPaymentDeleted();
        refreshPayments();
    }

    @Override
    public void refreshPayments() {
        PersonDebt personDebt = mDebtsRepository.getPersonDebt(mDebtId, mDebtType);

        if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
            EspressoIdlingResource.decrement(); // Set app as idle.
        }

        showPersonDebtInView(personDebt);
    }

    @Override
    public Loader<PersonDebt> onCreateLoader(int id, Bundle args) {
        if (mDebtId == null) {
            return null;
        }
        // can set loading indicator
        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<PersonDebt> loader, PersonDebt personDebt) {
        if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
            EspressoIdlingResource.decrement(); // Set app as idle.
        }
        showPersonDebtInView(personDebt);
    }

    private void showPersonDebtInView(PersonDebt personDebt) {
        if (personDebt == null) {
            mDebtDetailView.showMissingDebt();
        }else {
            mDebtDetailView.showPersonDebt(personDebt);
            mDebtType = personDebt.getDebt().getDebtType();
        }
    }

    @Override
    public void onLoaderReset(Loader<PersonDebt> loader) {
        // remove any references it has to the Loader's data.
    }
}

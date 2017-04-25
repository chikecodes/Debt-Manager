package com.chikeandroid.debtmanager20.debtdetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.chikeandroid.debtmanager20.data.PersonDebt;
import com.chikeandroid.debtmanager20.data.source.DebtsDataSource;
import com.chikeandroid.debtmanager20.data.source.DebtsRepository;
import com.chikeandroid.debtmanager20.data.loaders.DebtLoader;

import javax.inject.Inject;

/**
 * Created by Chike on 4/20/2017.
 * Listens to user actions from the UI ({@link DebtDetailFragment}), retrieves the data and updates
 * the UI as required.
 */
public class DebtDetailPresenter implements DebtDetailContract.Presenter, LoaderManager.LoaderCallbacks<PersonDebt> {

    private static final int DEBT_QUERY = 106;

    @NonNull
    private final DebtsDataSource mDebtsRepository;

    @NonNull
    private final DebtDetailContract.View mDebtDetailView;

    @NonNull
    private final LoaderManager mLoaderManager;

    private final DebtLoader mLoader;

    private String mDebtId;

    @Inject
    public DebtDetailPresenter(@Nullable String debtId, DebtsRepository debtsRepository,
                               DebtDetailContract.View view, LoaderManager loaderManager, DebtLoader loader) {
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

    }

    @Override
    public void deleteDebt(@NonNull String debtId) {

    }

    @Override
    public void addAdditionalDebt() {

    }

    @Override
    public Loader<PersonDebt> onCreateLoader(int id, Bundle args) {
        if(mDebtId == null) {
            return null;
        }
        // can set loading indicator
        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<PersonDebt> loader, PersonDebt data) {
        if(data != null) {
            mDebtDetailView.showDebt(data);
        }else {
            mDebtDetailView.showMissingDebt();
        }
    }

    @Override
    public void onLoaderReset(Loader<PersonDebt> loader) {

    }
}

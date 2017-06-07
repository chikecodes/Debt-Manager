package com.chikeandroid.debtmanager.features.debtdetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;

import com.chikeandroid.debtmanager.data.source.PersonDebtsRepository;
import com.chikeandroid.debtmanager.features.debtdetail.loaders.DebtLoader;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Chike on 4/20/2017.
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link DebtDetailPresenter}.
 */

@Module
public class DebtDetailPresenterModule {

    private final DebtDetailContract.View mView;
    private final String mDebtId;
    private final int mDebtType;
    private final LoaderManager mLoaderManager;

    public DebtDetailPresenterModule(DebtDetailContract.View view,
                                     @NonNull String debtId,
                                     int debtType,
                                     @NonNull LoaderManager loaderManager) {
        mView = view;
        mDebtId = debtId;
        mDebtType = debtType;
        mLoaderManager = loaderManager;
    }

    @Provides
    DebtDetailContract.View providesDebtDetailContractView() {
        return mView;
    }

    @Provides
    LoaderManager providesLoaderManager() {
        return mLoaderManager;
    }

    @Provides
    DebtLoader providesOweMeDebtLoader(Context context, PersonDebtsRepository repository) {
        return new DebtLoader(context, repository, mDebtId, mDebtType);
    }

    @Provides
    String providesDebtId() {
        return mDebtId;
    }
}

package com.chikeandroid.debtmanager20.debtdetail;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;

import com.chikeandroid.debtmanager20.data.source.PersonDebtsRepository;
import com.chikeandroid.debtmanager20.data.loaders.DebtLoader;

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
    private final Fragment mContext;
    private final String mDebtId;
    private final int mDebtType;

    public DebtDetailPresenterModule(DebtDetailContract.View view, @Nullable String debtId, int debtType) {
        mView = view;
        mContext = (Fragment) view;
        mDebtId = debtId;
        mDebtType = debtType;
    }

    @Provides
    DebtDetailContract.View providesDebtDetailContractView() {
        return mView;
    }

    @Provides
    LoaderManager providesLoaderManager() {
        return mContext.getLoaderManager();
    }

    @Provides
    DebtLoader providesOweMeDebtLoader(Context context, PersonDebtsRepository repository) {
        return new DebtLoader(context, repository, mDebtId, mDebtType);
    }

    @Provides
    @Nullable
    String providesDebtId() {
        return mDebtId;
    }
}

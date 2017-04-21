package com.chikeandroid.debtmanager20.debtdetail;

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

    public DebtDetailPresenterModule(DebtDetailContract.View view) {
        mView = view;
    }

    @Provides
    DebtDetailContract.View providesDebtDetailContractView() {
        return mView;
    }
}

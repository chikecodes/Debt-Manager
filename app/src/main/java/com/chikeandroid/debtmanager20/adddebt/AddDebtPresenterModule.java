package com.chikeandroid.debtmanager20.adddebt;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Chike on 3/25/2017.
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link AddDebtPresenter}.
 */
@Module
public class AddDebtPresenterModule {

    private final AddDebtContract.View mView;

    public AddDebtPresenterModule(AddDebtContract.View view) {
        mView = view;
    }

    @Provides
    AddDebtContract.View providesAddDebtContractView() {
        return mView;
    }

}

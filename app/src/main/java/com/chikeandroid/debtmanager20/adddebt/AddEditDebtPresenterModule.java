package com.chikeandroid.debtmanager20.adddebt;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Chike on 3/25/2017.
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link AddEditDebtPresenter}.
 */
@Module
public class AddEditDebtPresenterModule {

    private final AddEditDebtContract.View mView;

    public AddEditDebtPresenterModule(AddEditDebtContract.View view) {
        mView = view;
    }

    @Provides
    AddEditDebtContract.View providesAddDebtContractView() {
        return mView;
    }

}

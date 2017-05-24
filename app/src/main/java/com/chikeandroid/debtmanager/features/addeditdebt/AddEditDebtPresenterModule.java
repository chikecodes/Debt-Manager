package com.chikeandroid.debtmanager.features.addeditdebt;

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
    private final boolean mEditDebt;

    public AddEditDebtPresenterModule(AddEditDebtContract.View view, boolean editDebt) {
        mView = view;
        mEditDebt = editDebt;
    }

    @Provides
    AddEditDebtContract.View providesAddDebtContractView() {
        return mView;
    }

    @Provides
    boolean providesEditDebt() {
        return mEditDebt;
    }
}

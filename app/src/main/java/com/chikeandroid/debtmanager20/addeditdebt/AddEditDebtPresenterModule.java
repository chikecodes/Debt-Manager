package com.chikeandroid.debtmanager20.addeditdebt;

import android.support.annotation.Nullable;

import javax.inject.Named;

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
    private String mDebtId;
    private String mPersonId;

    public AddEditDebtPresenterModule(AddEditDebtContract.View view, @Nullable String debtId, @Nullable String personId) {
        mView = view;
        mDebtId = debtId;
       mPersonId = personId;
    }

    @Provides
    AddEditDebtContract.View providesAddDebtContractView() {
        return mView;
    }

    @Provides
    @Nullable
    @Named("debt_id")
    String providesDebtId() {
        return mDebtId;
    }

    @Provides
    @Nullable
    @Named("person_id")
    String providesPersonId() {
        return mPersonId;
    }
}

package com.chikeandroid.debtmanager20.people;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;

import com.chikeandroid.debtmanager20.data.source.PersonDebtsRepository;
import com.chikeandroid.debtmanager20.people.loader.PeopleLoader;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Chike on 5/10/2017.
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link PeoplePresenter}.
 */
@Module
public class PeoplePresenterModule {

    private final PeopleContract.View mView;
    private final Fragment mContext;

    public PeoplePresenterModule(PeopleContract.View view) {
        mView = view;
        mContext = (Fragment) view;
    }

    @Provides
    PeopleContract.View providePeopleContractView() {
        return mView;
    }

    @Provides
    LoaderManager providesLoaderManager() {
        return mContext.getLoaderManager();
    }

    @Provides
    PeopleLoader providesPeopleLoader(Context context, PersonDebtsRepository repository) {
        return new PeopleLoader(context, repository);
    }
}

package com.chikeandroid.debtmanager20.persondetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;

import com.chikeandroid.debtmanager20.data.Person;
import com.chikeandroid.debtmanager20.data.source.PersonDebtsRepository;
import com.chikeandroid.debtmanager20.persondetail.loader.PersonLoader;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Chike on 4/20/2017.
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link PersonDetailPresenter}.
 */
@Module
public class PersonDetailPresenterModule {

    private final PersonDetailContract.View mView;
    private final Fragment mContext;
    private final Person mPerson;

    public PersonDetailPresenterModule(PersonDetailContract.View view, @NonNull Person person) {
        mView = view;
        mContext = (Fragment) view;
        mPerson = person;
    }

    @Provides
    PersonDetailContract.View providesDebtDetailContractView() {
        return mView;
    }

    @Provides
    LoaderManager providesLoaderManager() {
        return mContext.getLoaderManager();
    }

    @Provides
    PersonLoader providesPersonLoader(Context context, PersonDebtsRepository repository) {
        return new PersonLoader(context, repository, mPerson);
    }

    @Provides
    Person providesPerson() {
        return mPerson;
    }
}

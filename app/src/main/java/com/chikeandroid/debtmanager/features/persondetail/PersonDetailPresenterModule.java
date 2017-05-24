package com.chikeandroid.debtmanager.features.persondetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;

import com.chikeandroid.debtmanager.data.Person;
import com.chikeandroid.debtmanager.data.source.PersonDebtsRepository;
import com.chikeandroid.debtmanager.features.persondetail.loader.PersonDebtsLoader;

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
    PersonDebtsLoader providesPersonLoader(Context context, PersonDebtsRepository repository) {
        return new PersonDebtsLoader(context, repository, mPerson);
    }

    @Provides
    Person providesPerson() {
        return mPerson;
    }
}

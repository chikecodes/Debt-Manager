package com.chikeandroid.debtmanager20.people;

import android.support.annotation.NonNull;

import com.chikeandroid.debtmanager20.base.MvpPresenter;
import com.chikeandroid.debtmanager20.base.MvpView;
import com.chikeandroid.debtmanager20.data.Person;

import java.util.List;

/**
 * Created by Chike on 3/14/2017.
 */

public interface PeopleContract {

    interface View extends MvpView {

        void showPeople(List<Person> persons);

        void showPersonDebtDetailsUi(String personId);

        void showNoPerson();

        void showLoadingPersonError();

        boolean isActive();
    }

    interface Presenter<V extends View> extends MvpPresenter<V> {

        void loadPersons(boolean forceUpdate);

        void openPersonDebtDetails(@NonNull Person person);
    }
}

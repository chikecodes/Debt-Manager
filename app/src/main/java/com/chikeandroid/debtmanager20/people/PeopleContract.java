package com.chikeandroid.debtmanager20.people;

import android.support.annotation.NonNull;

import com.chikeandroid.debtmanager20.base.BasePresenter;
import com.chikeandroid.debtmanager20.base.BaseView;
import com.chikeandroid.debtmanager20.data.Person;

import java.util.List;

/**
 * Created by Chike on 3/14/2017.
 */

public interface PeopleContract {

    interface View extends BaseView<Presenter> {

        void showPeople(List<Person> persons);

        void showNoPerson();

        void showLoadingPersonError();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void loadPersons(boolean forceUpdate);

        void openPersonDebtDetails(@NonNull Person person);
    }
}

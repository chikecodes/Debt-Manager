package com.chikeandroid.debtmanager20.people;

import com.chikeandroid.debtmanager20.base.BasePresenter;
import com.chikeandroid.debtmanager20.base.BaseView;
import com.chikeandroid.debtmanager20.data.Person;

import java.util.List;

/**
 * Created by Chike on 3/14/2017.
 * This specifies the contract between the view {@link PeopleFragment} and the presenter {@link PeoplePresenter}.
 */

public interface PeopleContract {

    interface View extends BaseView<Presenter> {

        void showPeople(List<Person> persons);

        void showEmptyView();

        void showLoadingPeopleError();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {


    }
}

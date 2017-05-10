package com.chikeandroid.debtmanager20.people;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chikeandroid.debtmanager20.DebtManagerApplication;
import com.chikeandroid.debtmanager20.R;
import com.chikeandroid.debtmanager20.data.Person;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Chike on 3/14/2017.
 * Display a List of {@link Person}s .
 */
public class PeopleFragment extends Fragment implements PeopleContract.View {

    @Inject
    PeoplePresenter mPeoplePresenter;

    private PeopleContract.Presenter mPresenter;

    public PeopleFragment() {
        // Required empty public constructor
    }

    public static PeopleFragment newInstance() {
        return new PeopleFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DaggerPeopleComponent.builder()
                .peoplePresenterModule(new PeoplePresenterModule(this))
                .applicationComponent(((DebtManagerApplication) getActivity().getApplication()).getComponent())
                .build()
                .inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_page_3, container, false);
    }

    @Override
    public void setPresenter(PeopleContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showPeople(List<Person> persons) {

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void showLoadingPeopleError() {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}

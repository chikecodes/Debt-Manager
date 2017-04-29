package com.chikeandroid.debtmanager20.people;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chikeandroid.debtmanager20.R;
import com.chikeandroid.debtmanager20.data.Person;

import java.util.List;

/**
 * Created by Chike on 3/14/2017.
 */

public class PeopleFragment extends Fragment implements PeopleContract.View {

    public PeopleFragment() {
        // Required empty public constructor
    }

    public static PeopleFragment newInstance() {
        return new PeopleFragment();
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
    public void showPeople(List<Person> persons) {
        // show list of people to ui
    }

    @Override
    public void showNoPerson() {
        // show no person available message
    }

    @Override
    public void showLoadingPersonError() {
        // show error getting person
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(PeopleContract.Presenter presenter) {
        // set presenter for this view
    }
}

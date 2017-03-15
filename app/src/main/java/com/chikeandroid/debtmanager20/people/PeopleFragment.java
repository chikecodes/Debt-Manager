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

    }

    public static PeopleFragment newInstance() {
        return new PeopleFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_page_3, container, false);

        return view;
    }

    @Override
    public void showPeople(List<Person> persons) {

    }

    @Override
    public void showPersonDebtDetailsUi(String personId) {

    }

    @Override
    public void showNoPerson() {

    }

    @Override
    public void showLoadingPersonError() {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}

package com.chikeandroid.debtmanager.features.people;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chikeandroid.debtmanager.DebtManagerApplication;
import com.chikeandroid.debtmanager.R;
import com.chikeandroid.debtmanager.data.Person;
import com.chikeandroid.debtmanager.databinding.PeopleFragmentBinding;
import com.chikeandroid.debtmanager.features.people.adapter.PeopleAdapter;
import com.chikeandroid.debtmanager.features.persondetail.PersonDetailActivity;
import com.chikeandroid.debtmanager.util.ViewUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Chike on 3/14/2017.
 * Display a List of {@link Person}s .
 */
public class PeopleFragment extends Fragment implements PeopleContract.View {

    private PeopleAdapter mPeopleAdapter;
    private TextView mTextViewEmptyDebts;

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
        mPeopleAdapter = new PeopleAdapter(this, getActivity(), new ArrayList<>(0));
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        PeopleFragmentBinding peopleFragmentBinding = DataBindingUtil.inflate(inflater,
                R.layout.people_fragment, container, false);

        mTextViewEmptyDebts = peopleFragmentBinding.tvNoPerson;
        RecyclerView recyclerView = peopleFragmentBinding.rvPeople;
        LinearLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), ViewUtil.getGridSpanCount(getActivity()));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mPeopleAdapter);

        mPeopleAdapter.setOnItemClickListener((view, person, position) -> PersonDetailActivity.start(getActivity(), person));

        return peopleFragmentBinding.getRoot();
    }

    @Override
    public void setPresenter(PeopleContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showPeople(List<Person> persons) {

        if (mTextViewEmptyDebts.getVisibility() == View.VISIBLE) {
            mTextViewEmptyDebts.setVisibility(View.GONE);
        }

        Collections.sort(persons, (person1, person2) -> person1.getFullname().compareTo(person2.getFullname()));

        mPeopleAdapter.updatePersonListItems(persons);
    }

    @Override
    public void showEmptyView() {
        mPeopleAdapter.updatePersonListItems(new ArrayList<>());
        mTextViewEmptyDebts.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadingPeopleError() {
        ViewUtil.showToast(getActivity(), getString(R.string.msg_loading_people_error));
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}

package com.chikeandroid.debtmanager20.iowe;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chikeandroid.debtmanager20.DebtManagerApplication;
import com.chikeandroid.debtmanager20.R;
import com.chikeandroid.debtmanager20.data.PersonDebt;
import com.chikeandroid.debtmanager20.databinding.IoweFragmentBinding;
import com.chikeandroid.debtmanager20.iowe.adapter.IOweAdapter;
import com.chikeandroid.debtmanager20.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Chike on 3/14/2017.
 * Display a List of {@link PersonDebt}s that user owes.
 */

public class IOweFragment extends Fragment implements IOweContract.View {

    private static final String TAG = "IOweFragment";
    private IOweAdapter mIOweAdapter;
    private TextView mTextViewEmptyDebts;

    @Inject
    IOwePresenter mIOwePresenter;

    private IOweContract.Presenter mPresenter;

    public IOweFragment() {
        // Required empty public constructor
    }

    public static IOweFragment newInstance() {
        return new IOweFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated()");
        DaggerIOweComponent.builder()
                .iOwePresenterModule(new IOwePresenterModule(this))
                .applicationComponent(((DebtManagerApplication) getActivity().getApplication()).getComponent())
                .build()
                .inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mIOweAdapter = new IOweAdapter(getActivity(), new ArrayList<PersonDebt>(0));
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

        IoweFragmentBinding ioweFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.iowe_fragment, container, false);
        final View view = ioweFragmentBinding.getRoot();

        RecyclerView recyclerViewIowe = ioweFragmentBinding.rvIowe;
        recyclerViewIowe.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewIowe.setAdapter(mIOweAdapter);
        mTextViewEmptyDebts = ioweFragmentBinding.tvNoDebts;

        return view;
    }

    @Override
    public void showDebts(List<PersonDebt> debts) {
        if(mTextViewEmptyDebts.getVisibility() == View.VISIBLE) {
            mTextViewEmptyDebts.setVisibility(View.GONE);
        }

        mIOweAdapter.updatePersonDebtListItems(debts);
        Log.d(TAG, "debts size is " + debts.size());
    }

    @Override
    public void showDebtDetailsUi(String debtId) {
        // open debt detail ui
    }

    @Override
    public void showLoadingDebtsError() {
        ViewUtil.showToast(getActivity(), getString(R.string.msg_loading_debts_error));
    }

    @Override
    public void showEmptyView() {
        mIOweAdapter.updatePersonDebtListItems(new ArrayList<PersonDebt>());
        mTextViewEmptyDebts.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(IOweContract.Presenter presenter) {
        // set presenter
        mPresenter = presenter;
    }
}

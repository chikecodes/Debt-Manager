package com.chikeandroid.debtmanager20.oweme;

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
import com.chikeandroid.debtmanager20.databinding.OweMeFragmentBinding;
import com.chikeandroid.debtmanager20.oweme.adapter.OweMeAdapter;
import com.chikeandroid.debtmanager20.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Chike on 3/13/2017.
 * Display a List of {@link PersonDebt}s that people owe the user.
 */
public class OweMeFragment extends Fragment implements OweMeContract.View {

    private static final String TAG = "OweMeDebtsFragment";
    private OweMeAdapter mOweMeAdapter;
    private TextView mTextViewEmptyDebts;

    @Inject
    OweMePresenter mOweMePresenter;

    private OweMeContract.Presenter mPresenter;

    public OweMeFragment() {
        // Required empty public constructor
    }

    public static OweMeFragment newInstance() {
        return new OweMeFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mOweMeAdapter = new OweMeAdapter(getActivity(), new ArrayList<PersonDebt>(0));
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
        mPresenter.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated()");
        DaggerOweMeComponent.builder()
                .oweMePresenterModule(new OweMePresenterModule(this))
                .applicationComponent(((DebtManagerApplication) getActivity().getApplication()).getComponent())
                .build()
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        OweMeFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.owe_me_fragment, container, false);
        final View view = binding.getRoot();

        RecyclerView recyclerViewOweMeDebts = binding.rvOweme;
        recyclerViewOweMeDebts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewOweMeDebts.setAdapter(mOweMeAdapter);
        mTextViewEmptyDebts = binding.tvNoDebts;

        Log.d(TAG, "onCreateView()");
        return view;
    }

    @Override
    public void showDebts(List<PersonDebt> debts) {

        if(mTextViewEmptyDebts.getVisibility() == View.VISIBLE) {
            mTextViewEmptyDebts.setVisibility(View.GONE);
        }

        mOweMeAdapter.updatePersonDebtListItems(debts);
        Log.d(TAG, "debts size is " + debts.size());
    }

    @Override
    public void showEmptyView() {
        mOweMeAdapter.updatePersonDebtListItems(new ArrayList<PersonDebt>());
        mTextViewEmptyDebts.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadingDebtsError() {
        ViewUtil.showToast(getActivity(), getString(R.string.msg_loading_debts_error));
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(OweMeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.stop();
        Log.d(TAG, "onStop: ");
    }


}

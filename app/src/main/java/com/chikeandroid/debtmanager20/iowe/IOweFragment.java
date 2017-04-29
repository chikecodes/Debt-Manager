package com.chikeandroid.debtmanager20.iowe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chikeandroid.debtmanager20.R;
import com.chikeandroid.debtmanager20.data.Debt;

import java.util.List;

/**
 * Created by Chike on 3/14/2017.
 */

public class IOweFragment extends Fragment implements IOweContract.View {

    public IOweFragment() {
        // Required empty public constructor
    }

    public static IOweFragment newInstance() {
        return new IOweFragment();
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

        return inflater.inflate(R.layout.fragment_page_2, container, false);
    }


    @Override
    public void showDebts(List<Debt> debts) {
        // show debtds
    }

    @Override
    public void showDebtDetailsUi(String debtId) {
        // open debt detail ui
    }

    @Override
    public void showNoDebts() {
        // show no debts
    }

    @Override
    public void showLoadingDebtsError() {
        // showing debts error on loading
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(IOweContract.Presenter presenter) {
        // set presenter
    }
}

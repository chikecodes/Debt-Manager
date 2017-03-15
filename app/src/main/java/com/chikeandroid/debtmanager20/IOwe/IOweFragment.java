package com.chikeandroid.debtmanager20.IOwe;

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

    }

    public static IOweFragment newInstance() {
        return new IOweFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_page_2, container, false);

        return view;
    }


    @Override
    public void showDebts(List<Debt> debts) {

    }

    @Override
    public void showDebtDetailsUi(String debtId) {

    }

    @Override
    public void showNoDebts() {

    }

    @Override
    public void showLoadingDebtsError() {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}

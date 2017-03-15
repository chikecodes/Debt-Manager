package com.chikeandroid.debtmanager20.oweme;

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
 * Created by Chike on 3/13/2017.
 */

public class OweMeFragment extends Fragment implements OweMeContract.View {


    public OweMeFragment() {

    }

    public static OweMeFragment newInstance() {
        return new OweMeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_page, container, false);

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

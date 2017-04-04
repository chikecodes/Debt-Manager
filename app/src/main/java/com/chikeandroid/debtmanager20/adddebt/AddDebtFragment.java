package com.chikeandroid.debtmanager20.adddebt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.chikeandroid.debtmanager20.R;

import es.dmoral.toasty.Toasty;

/**
 * Created by Chike on 3/16/2017.
 */

public class AddDebtFragment extends Fragment implements AddDebtContract.View {

    private RadioGroup mRadioGroupDebtType;
    private EditText mEditTextAmount;
    private EditText mEditTextName;
    private EditText mEditTextPhoneNumber;
    private EditText mEditTextComment;
    private Button mButtonDateDue;
    private Button mButtonDateCreated;

    private AddDebtContract.Presenter mPresenter;

    public static AddDebtFragment newInstance() {
        return new AddDebtFragment();
    }

    public AddDebtFragment() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Create the presenter
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add_debt, container, false);

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Add Debt");

        setHasOptionsMenu(true);
        setRetainInstance(true);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_add_debt, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if(itemId == R.id.menu_save_debt) {
            Toasty.success(getActivity(), "Debt Saved", Toast.LENGTH_LONG, true).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showErroSavingDebt() {

    }

    @Override
    public void setPresenter(AddDebtContract.Presenter presenter) {
        mPresenter = presenter;
    }
}

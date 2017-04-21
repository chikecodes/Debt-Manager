package com.chikeandroid.debtmanager20.debtdetail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.chikeandroid.debtmanager20.R;
import com.chikeandroid.debtmanager20.adddebt.AddEditDebtActivity;
import com.chikeandroid.debtmanager20.adddebt.AddEditDebtFragment;
import com.chikeandroid.debtmanager20.data.PersonDebt;
import com.chikeandroid.debtmanager20.databinding.FragmentDebtDetailBinding;
import com.chikeandroid.debtmanager20.util.StringUtil;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Chike on 4/20/2017.
 */

public class DebtDetailFragment extends Fragment implements DebtDetailContract.View {

    public static final String EXTRA_PERSON_DEBT = "com.chikeandroid.debtmanager20.debtdetail.DebtDetailFragment.extra_person_debt";

    private PersonDebt mPersonDebt;

    public static DebtDetailFragment newInstance(Bundle bundle) {
        DebtDetailFragment debtDetailFragment = new DebtDetailFragment();
        debtDetailFragment.setArguments(bundle);
        return debtDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentDebtDetailBinding fragmentDebtDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_debt_detail, container, false);

        Bundle bundle = getArguments();
        PersonDebt personDebt = bundle.getParcelable(EXTRA_PERSON_DEBT);
        checkNotNull(personDebt);
        mPersonDebt = personDebt;
        fragmentDebtDetailBinding.setPersonDebt(personDebt);

        Toolbar toolbar = fragmentDebtDetailBinding.toolbar;
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(StringUtil.commaNumber(personDebt.getDebt().getAmount()));
        }


        FloatingActionButton fab = fragmentDebtDetailBinding.fabScrolling;

        return fragmentDebtDetailBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_debt_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;
            case R.id.action_edit:
                Intent intent = new Intent(getActivity(), AddEditDebtActivity.class);
                intent.putExtra(AddEditDebtFragment.ARGUMENT_EDIT_DEBT, mPersonDebt);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(DebtDetailContract.Presenter presenter) {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void openEditDebtUi() {

    }

    @Override
    public void deleteDebt() {

    }

    @Override
    public void callDebtor() {

    }

    @Override
    public void addPartialPayment() {

    }
}

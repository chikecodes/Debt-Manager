package com.chikeandroid.debtmanager20.debtdetail;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chikeandroid.debtmanager20.DebtManagerApplication;
import com.chikeandroid.debtmanager20.R;
import com.chikeandroid.debtmanager20.addeditdebt.AddEditDebtActivity;
import com.chikeandroid.debtmanager20.addeditdebt.AddEditDebtFragment;
import com.chikeandroid.debtmanager20.data.PersonDebt;
import com.chikeandroid.debtmanager20.databinding.FragmentDebtDetailBinding;
import com.chikeandroid.debtmanager20.util.StringUtil;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Chike on 4/20/2017.
 */

public class DebtDetailFragment extends Fragment implements DebtDetailContract.View {

    public static final String EXTRA_DEBT_ID = "com.chikeandroid.debtmanager20.debtdetail.DebtDetailFragment.extra_debt_id";
    public static final String ARG_DEBT_ID = "com.chikeandroid.debtmanager20.debtdetail.DebtDetailFragment.argument_debt_id";

    private PersonDebt mPersonDebt;
    private FragmentDebtDetailBinding mFragmentDebtDetailBinding;
    private ActionBar mActionBar;
    private String mDebtId;
    private View mView;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Inject
    DebtDetailPresenter mDebtDetailPresenter;

    private DebtDetailContract.Presenter mPresenter;

    public static DebtDetailFragment newInstance(String debtId) {
        Bundle args = new Bundle();
        args.putString(ARG_DEBT_ID, debtId);
        DebtDetailFragment debtDetailFragment = new DebtDetailFragment();
        debtDetailFragment.setArguments(args);
        return debtDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mDebtId = getArguments().getString(ARG_DEBT_ID);
        }
        DaggerDebtDetailComponent.builder()
                .debtDetailPresenterModule(new DebtDetailPresenterModule(this, mDebtId))
                .applicationComponent(((DebtManagerApplication) getActivity().getApplication()).getComponent()).build()
                .inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mFragmentDebtDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_debt_detail, container, false);

        FloatingActionButton fab = mFragmentDebtDetailBinding.fabScrolling;
        mCollapsingToolbarLayout = mFragmentDebtDetailBinding.collapsingToolbar;

        mCollapsingToolbarLayout.setTitle("");
        mView = mFragmentDebtDetailBinding.getRoot();

        mToolbar = mFragmentDebtDetailBinding.toolbar;
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowTitleEnabled(false);
        }

        return mView;
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
                startActivityForResult(intent, AddEditDebtActivity.REQUEST_EDIT_DEBT);
                break;
            case R.id.action_delete:
                String message = String.format(getString(R.string.delete_dialog_title),
                        mPersonDebt.getPerson().getFullname(), StringUtil.commaNumber(mPersonDebt.getDebt().getAmount()));
                new AlertDialog.Builder(getContext())
                        .setMessage(message)
                        .setPositiveButton(getString(R.string.dialog_delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mPresenter.deletePersonDebt(mPersonDebt);
                            }
                        })
                        .setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (AddEditDebtActivity.REQUEST_EDIT_DEBT == requestCode && Activity.RESULT_OK == resultCode) {
            Toast.makeText(getActivity(), "Debt edited successfully", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setPresenter(DebtDetailContract.Presenter presenter) {
        checkNotNull(presenter);
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void openEditDebtUi() {

    }

    @Override
    public void callDebtor() {

    }

    @Override
    public void addPartialPayment() {

    }

    @Override
    public void showPersonDebt(@NonNull PersonDebt personDebt) {
        checkNotNull(personDebt);
        mPersonDebt = personDebt;

        mFragmentDebtDetailBinding.setPersonDebt(mPersonDebt);
        mCollapsingToolbarLayout.setTitle(StringUtil.commaNumber(mPersonDebt.getDebt().getAmount()));
    }

    @Override
    public void showMissingDebt() {

    }

    @Override
    public void showPersonDebtDeleted() {

        Toast.makeText(getActivity(), getString(R.string.msg_success_message_deleted), Toast.LENGTH_LONG).show();
        getActivity().finish();
    }
}

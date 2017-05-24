package com.chikeandroid.debtmanager.features.debtdetail;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.chikeandroid.debtmanager.DebtManagerApplication;
import com.chikeandroid.debtmanager.R;
import com.chikeandroid.debtmanager.features.addeditdebt.AddEditDebtActivity;
import com.chikeandroid.debtmanager.data.PersonDebt;
import com.chikeandroid.debtmanager.databinding.FragmentDebtDetailBinding;
import com.chikeandroid.debtmanager.util.StringUtil;
import com.chikeandroid.debtmanager.util.ViewUtil;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Chike on 4/20/2017.
 * Displays a Debt Detail screen
 */

public class DebtDetailFragment extends Fragment implements DebtDetailContract.View {

    public static final String EXTRA_DEBT_ID = "com.chikeandroid.debtmanager20.features.debtdetail.DebtDetailFragment.extra_debt_id";
    public static final String EXTRA_DEBT_TYPE = "com.chikeandroid.debtmanager20.features.debtdetail.DebtDetailFragment.extra_debt_type";

    public static final String ARG_DEBT_ID = "com.chikeandroid.debtmanager20.features.debtdetail.DebtDetailFragment.argument_debt_id";
    public static final String ARG_DEBT_TYPE = "com.chikeandroid.debtmanager20.features.debtdetail.DebtDetailFragment.argument_debt_type";

    private PersonDebt mPersonDebt;
    private FragmentDebtDetailBinding mFragmentDebtDetailBinding;
    private String mDebtId;
    private int mDebtType;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private String mPhoneNumber;

    @Inject
    DebtDetailPresenter mDebtDetailPresenter;

    private DebtDetailContract.Presenter mPresenter;

    public static DebtDetailFragment newInstance(String debtId, int debtType) {
        Bundle args = new Bundle();
        args.putString(ARG_DEBT_ID, debtId);
        args.putInt(ARG_DEBT_TYPE, debtType);
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
            mDebtType = getArguments().getInt(ARG_DEBT_TYPE);
        }
        DaggerDebtDetailComponent.builder()
                .debtDetailPresenterModule(new DebtDetailPresenterModule(this, mDebtId, mDebtType))
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

        // FloatingActionButton fab = mFragmentDebtDetailBinding.fabScrolling;
        mCollapsingToolbarLayout = mFragmentDebtDetailBinding.collapsingToolbar;

        mCollapsingToolbarLayout.setTitle("");
        View view = mFragmentDebtDetailBinding.getRoot();

        Button callButton = mFragmentDebtDetailBinding.debtDetailContent.btnCall;
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!StringUtil.isEmpty(mPhoneNumber)) {
                    callDebtor();
                }
            }
        });

        Button smsButton = mFragmentDebtDetailBinding.debtDetailContent.btnSms;
        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!StringUtil.isEmpty(mPhoneNumber)) {
                    smsDebtor();
                }
            }
        });
        setUpToolbar();

        return view;
    }

    private void setUpToolbar() {
        Toolbar toolbar = mFragmentDebtDetailBinding.toolbar;
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
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
                openEditDebtUi();
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
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (AddEditDebtActivity.REQUEST_EDIT_DEBT == requestCode && Activity.RESULT_OK == resultCode) {
            ViewUtil.showToast(getActivity(), getString(R.string.msg_edit_debt_success));
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

    private void openEditDebtUi() {
        AddEditDebtActivity.startFromDebtDetailScreen(getActivity(), mPersonDebt, this);
    }

    private void callDebtor() {
        String dial = "tel:" + mPhoneNumber;
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
    }

    private void smsDebtor() {
        String sms = "smsto:" + mPhoneNumber;
        startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse(sms)));
    }

    @Override
    public void addPartialPayment() {
        // add partial payment
    }

    @Override
    public void showPersonDebt(@NonNull PersonDebt personDebt) {
        checkNotNull(personDebt);
        mPersonDebt = personDebt;
        mPhoneNumber = personDebt.getPerson().getPhoneNumber();
        mFragmentDebtDetailBinding.setPersonDebt(mPersonDebt);
        mCollapsingToolbarLayout.setTitle(StringUtil.commaNumber(mPersonDebt.getDebt().getAmount()));

        Glide.with(getActivity())
                .load(personDebt.getPerson().getImageUri())
                .dontAnimate()
                .into(mFragmentDebtDetailBinding.image);
    }

    @Override
    public void showMissingDebt() {
        ViewUtil.showToast(getActivity(), getString(R.string.msg_missing_debt));
    }

    @Override
    public void showPersonDebtDeleted() {

        ViewUtil.showToast(getActivity(), getString(R.string.msg_success_message_deleted));
        getActivity().finish();
    }
}

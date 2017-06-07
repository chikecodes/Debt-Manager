package com.chikeandroid.debtmanager.features.debtdetail;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chikeandroid.debtmanager.DebtManagerApplication;
import com.chikeandroid.debtmanager.R;
import com.chikeandroid.debtmanager.data.Payment;
import com.chikeandroid.debtmanager.data.PersonDebt;
import com.chikeandroid.debtmanager.databinding.DialogAddPaymentBinding;
import com.chikeandroid.debtmanager.databinding.FragmentDebtDetailBinding;
import com.chikeandroid.debtmanager.features.addeditdebt.AddEditDebtActivity;
import com.chikeandroid.debtmanager.features.debtdetail.adapter.PaymentAdapter;
import com.chikeandroid.debtmanager.util.StringUtil;
import com.chikeandroid.debtmanager.util.TimeUtil;
import com.chikeandroid.debtmanager.util.ValidationUtil;
import com.chikeandroid.debtmanager.util.ViewUtil;
import com.chikeandroid.debtmanager.util.validator.EditTextIntegerValidator;
import com.chikeandroid.debtmanager.util.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    private int mPaymentAction;
    private long mPaymentDateEntered;
    private PaymentAdapter mPaymentAdapter;
    private TextView mTextViewTotalAmount;
    private DebtDetailContract.Presenter mPresenter;
    private EditText mEditTextPaymentAmount;
    private EditText mEditTextPaymentComment;
    private DialogAddPaymentBinding mDialogAddEditPaymentBinding;
    private ImageButton mImageButtonSavePayment;
    private ImageButton mImageButtonDeletePayment;
    private Dialog mAddEditPaymentDialog;

    @Inject
    DebtDetailPresenter mDebtDetailPresenter;

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
        mPaymentAdapter = new PaymentAdapter(getActivity(), new ArrayList<Payment>(0));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            mDebtId = getArguments().getString(ARG_DEBT_ID);
            mDebtType = getArguments().getInt(ARG_DEBT_TYPE);
        }
        DaggerDebtDetailComponent.builder()
                .debtDetailPresenterModule(new DebtDetailPresenterModule(this, mDebtId, mDebtType,
                        getActivity().getSupportLoaderManager()))
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

        FloatingActionButton fab = mFragmentDebtDetailBinding.fabAddPayment;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddPaymentDialog();
            }
        });
        mCollapsingToolbarLayout = mFragmentDebtDetailBinding.collapsingToolbar;
        mTextViewTotalAmount = mFragmentDebtDetailBinding.debtDetailContent.totalAmountLayout.tvTotalAmount;

        mCollapsingToolbarLayout.setTitle("");
        View view = mFragmentDebtDetailBinding.getRoot();

        setUpToolbar();

        setUpPaymentsRecyclerViewAndAdapter();

        setUpAddEditPaymentDialog();

        return view;
    }

    private void setUpAddEditPaymentDialog() {
        mAddEditPaymentDialog = new Dialog(getContext(), R.style.DialogFullscreen);
        mDialogAddEditPaymentBinding = DataBindingUtil.inflate(mAddEditPaymentDialog.getLayoutInflater(),
                R.layout.dialog_add_payment, null, false);
        mAddEditPaymentDialog.setContentView(mDialogAddEditPaymentBinding.getRoot());
        ImageView imageViewClosePaymentDialog = mDialogAddEditPaymentBinding.ivCloseAddPaymentDialog;
        mEditTextPaymentAmount = mDialogAddEditPaymentBinding.etPaymentAmount;
        mEditTextPaymentComment = mDialogAddEditPaymentBinding.etPaymentComment;
        imageViewClosePaymentDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddEditPaymentDialog.dismiss();
            }
        });

        RadioGroup radioGroupAction = mDialogAddEditPaymentBinding.rgAction;
        mPaymentAction = Payment.PAYMENT_ACTION_DEBT_DECREASE;
        radioGroupAction.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (checkedId == R.id.rb_leave) {
                    mPaymentAction = Payment.PAYMENT_ACTION_DEBT_DONT_CHANGE;
                }else if (checkedId == R.id.rb_increase) {
                    mPaymentAction = Payment.PAYMENT_ACTION_DEBT_INCREASE;
                }else if (checkedId == R.id.rb_decrease) {
                    mPaymentAction = Payment.PAYMENT_ACTION_DEBT_DECREASE;
                }
            }
        });

        mImageButtonSavePayment = mDialogAddEditPaymentBinding.ibSavePayment;
        mImageButtonDeletePayment = mDialogAddEditPaymentBinding.ibDeletePayment;
    }

    private void setUpPaymentsRecyclerViewAndAdapter() {

        RecyclerView recyclerView = mFragmentDebtDetailBinding.debtDetailContent.rvPayments;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mPaymentAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        mPaymentAdapter.setOnItemClickListener(new PaymentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Payment payment, int position) {
                openEditPaymentDialog(payment);
            }
        });
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
            case R.id.action_call:
                callDebtor();
                break;
            case R.id.action_sms:
                smsDebtor();
                break;
            case R.id.action_add_payment:
                openAddPaymentDialog();
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

    private void openAddPaymentDialog() {

        mPaymentDateEntered = System.currentTimeMillis();
        mEditTextPaymentAmount.setText(" ");
        mEditTextPaymentComment.setText("");
        mDialogAddEditPaymentBinding.tvAddEditPaymentDialogTitle.setText(getString(R.string.title_add_payment));
        if (mDialogAddEditPaymentBinding.ibDeletePayment.getVisibility() == View.VISIBLE) {
            mDialogAddEditPaymentBinding.ibDeletePayment.setVisibility(View.INVISIBLE);
        }

        final Button buttonDateEntered = mDialogAddEditPaymentBinding.btnPaymentDateCreated;
        buttonDateEntered.setText(String.format(getString(R.string.created_date),
                TimeUtil.millis2String(System.currentTimeMillis())));

        buttonDateEntered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(System.currentTimeMillis(), buttonDateEntered);
            }
        });

        mImageButtonSavePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ValidationUtil.isInValid(new EditTextIntegerValidator(mEditTextPaymentAmount, getActivity()))) {
                    Toast.makeText(getActivity(), "Invalid", Toast.LENGTH_LONG).show();
                } else {
                    Payment payment = new Payment.Builder()
                            .action(mPaymentAction)
                            .dateEntered(mPaymentDateEntered)
                            .note(mEditTextPaymentComment.getText().toString())
                            .debtId(mDebtId)
                            .amount(Double.valueOf(mEditTextPaymentAmount.getText().toString()))
                            .id(UUID.randomUUID().toString())
                            .personPhoneNumber(mPhoneNumber)
                            .build();

                    mPresenter.addPartialPayment(payment);
                    // ViewUtil.showToast(getActivity(), "Payment saved successfully");
                    Toast.makeText(getActivity(), "Payment saved successfully", Toast.LENGTH_LONG).show();
                    mAddEditPaymentDialog.dismiss();
                }
            }
        });

        mAddEditPaymentDialog.show();
    }

    private void openEditPaymentDialog(final Payment payment) {

        mEditTextPaymentAmount.setText(String.valueOf(payment.getAmount()));
        mEditTextPaymentComment.setText(payment.getNote());
        mDialogAddEditPaymentBinding.ibDeletePayment.setVisibility(View.VISIBLE);
        mDialogAddEditPaymentBinding.tvAddEditPaymentDialogTitle.setText(getString(R.string.title_edit_payment));
        mPaymentDateEntered = payment.getDateEntered();
        if (payment.getAction() == Payment.PAYMENT_ACTION_DEBT_DECREASE) {
            mDialogAddEditPaymentBinding.rbDecrease.setChecked(true);
            mDialogAddEditPaymentBinding.rbIncrease.setChecked(false);
            mDialogAddEditPaymentBinding.rbLeave.setChecked(false);
        }else if (payment.getAction() == Payment.PAYMENT_ACTION_DEBT_INCREASE) {
            mDialogAddEditPaymentBinding.rbIncrease.setChecked(true);
            mDialogAddEditPaymentBinding.rbLeave.setChecked(false);
            mDialogAddEditPaymentBinding.rbDecrease.setChecked(false);
        }else if (payment.getAction() == Payment.PAYMENT_ACTION_DEBT_DONT_CHANGE) {
            mDialogAddEditPaymentBinding.rbIncrease.setChecked(false);
            mDialogAddEditPaymentBinding.rbLeave.setChecked(true);
            mDialogAddEditPaymentBinding.rbDecrease.setChecked(false);
        }

        final Button buttonDateEntered = mDialogAddEditPaymentBinding.btnPaymentDateCreated;
        buttonDateEntered.setText(String.format(getString(R.string.created_date),
                TimeUtil.millis2String(mPaymentDateEntered)));

        buttonDateEntered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(mPaymentDateEntered, buttonDateEntered);
            }
        });

        mImageButtonSavePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ValidationUtil.isInValid(new EditTextIntegerValidator(mEditTextPaymentAmount, getActivity()))) {
                    Toast.makeText(getActivity(), "Invalid", Toast.LENGTH_LONG).show();
                } else {
                    Payment updatedPayment = new Payment.Builder()
                            .action(mPaymentAction)
                            .dateEntered(mPaymentDateEntered)
                            .note(mEditTextPaymentComment.getText().toString())
                            .debtId(mDebtId)
                            .amount(Double.valueOf(mEditTextPaymentAmount.getText().toString()))
                            .id(payment.getId())
                            .personPhoneNumber(mPhoneNumber)
                            .build();

                    mPresenter.editPayment(updatedPayment, mPersonDebt.getDebt());
                    ViewUtil.showToast(getActivity(), "Payment edited successfully");

                    mAddEditPaymentDialog.dismiss();
                }
            }
        });

        mImageButtonDeletePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(getContext())
                        .setMessage("Are you sure?")
                        .setPositiveButton(getString(R.string.dialog_delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mPresenter.deletePayment(payment);
                                mAddEditPaymentDialog.dismiss();
                            }
                        })
                        .setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });

        mAddEditPaymentDialog.show();
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

        if (System.currentTimeMillis() > mPersonDebt.getDebt().getDueDate()) {
            mFragmentDebtDetailBinding.debtDetailContent.tvDateDue.setTextColor(Color.RED);
        }

        if (!personDebt.getPayments().isEmpty()) {
            mFragmentDebtDetailBinding.debtDetailContent.cvPayments.setVisibility(View.VISIBLE);
            showPayments(personDebt.getPayments());
        }else {
            mFragmentDebtDetailBinding.debtDetailContent.cvPayments.setVisibility(View.INVISIBLE);
        }
    }

    private void showDatePickerDialog(long dateTimeStamp, final Button button) {

        final Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(dateTimeStamp);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String dateString = TimeUtil.millis2String(calendar.getTimeInMillis());
                button.setText(String.format(getString(R.string.created_date), dateString));

                mPaymentDateEntered = calendar.getTimeInMillis();

            }
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    @Override
    public void showMissingDebt() {
        ViewUtil.showToast(getActivity(), getString(R.string.msg_missing_debt));
    }

    @Override
    public void showPersonDebtDeleted() {
        ViewUtil.showToast(getActivity(), getString(R.string.msg_success_message_debt_deleted));
        getActivity().finish();
    }

    @Override
    public void showPaymentDeleted() {
        ViewUtil.showToast(getActivity(), getString(R.string.msg_success_message_payment_deleted));
    }

    private void showPayments(List<Payment> payments) {
        Collections.sort(payments, new Comparator<Payment>() {
            @Override
            public int compare(Payment payment, Payment payment2) {

                Date payment1CreatedDate = TimeUtil.millis2Date(payment.getDateEntered());
                Date payment2CreatedDate = TimeUtil.millis2Date(payment.getDateEntered());

                return payment2CreatedDate.compareTo(payment1CreatedDate);
            }
        });

        mPaymentAdapter.updatePaymentListItems(payments);

        double total = 0;
        for (Payment payment: payments) {
            total += payment.getAmount();
        }

        mTextViewTotalAmount.setText(String.format(getString(R.string.total_debt_amount),
                StringUtil.commaNumber(total)));
    }
}

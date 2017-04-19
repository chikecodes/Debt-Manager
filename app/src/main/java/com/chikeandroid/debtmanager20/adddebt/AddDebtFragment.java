package com.chikeandroid.debtmanager20.adddebt;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.IdRes;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.chikeandroid.debtmanager20.R;
import com.chikeandroid.debtmanager20.data.Debt;
import com.chikeandroid.debtmanager20.databinding.FragmentAddDebtBinding;
import com.chikeandroid.debtmanager20.util.TimeUtil;
import com.chikeandroid.debtmanager20.util.ValidationUtil;
import com.chikeandroid.debtmanager20.util.validator.EditTextFullNameValidator;
import com.chikeandroid.debtmanager20.util.validator.EditTextIntegerValidator;
import com.chikeandroid.debtmanager20.util.validator.EditTextPhoneNumberValidator;

import java.util.Calendar;

/**
 * Created by Chike on 3/16/2017.
 */

public class AddDebtFragment extends Fragment implements AddDebtContract.View {

    private EditText mEditTextAmount;
    private EditText mEditTextName;
    private EditText mEditTextPhoneNumber;
    private EditText mEditTextComment;
    private Button mButtonDateDue;
    private Button mButtonDateCreated;
    private int mDebtType;
    private static final int REQUEST_CONTACT = 1;
    private Calendar mCalendar;
    private long mDebtCreatedAt;
    private long mDebtDue;

    private AddDebtContract.Presenter mPresenter;

    public static AddDebtFragment newInstance() {
        return new AddDebtFragment();
    }

    public AddDebtFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentAddDebtBinding fragmentAddDebtBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_debt, container, false);

        Toolbar toolbar = fragmentAddDebtBinding.toolbar;
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Add Debt");

        mCalendar = Calendar.getInstance();

        String currentDateString = TimeUtil.formatDateToString(mCalendar.get(Calendar.DAY_OF_WEEK),
                mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)) + " " + mCalendar.get(Calendar.YEAR);

        mEditTextComment = fragmentAddDebtBinding.etComment;
        mEditTextAmount = fragmentAddDebtBinding.etAmount;
        mEditTextName = fragmentAddDebtBinding.etFullName;
        mEditTextPhoneNumber = fragmentAddDebtBinding.etPhoneNumber;
        mButtonDateDue = fragmentAddDebtBinding.btnDateDue;
        mButtonDateDue.setText(String.format(getString(R.string.due_date), currentDateString));
        mButtonDateDue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(mButtonDateDue.getId());
            }
        });

        mButtonDateCreated = fragmentAddDebtBinding.btnDateCreated;
        mButtonDateCreated.setText(String.format(getString(R.string.created_date),currentDateString));
        mButtonDateCreated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(mButtonDateCreated.getId());
            }
        });

        RadioGroup radioGroupDebtType = fragmentAddDebtBinding.rgDebtType;
        mDebtType = Debt.DEBT_TYPE_OWED;
        radioGroupDebtType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {

                if(checkedId == R.id.rb_owed_by_me) {
                    mDebtType = Debt.DEBT_TYPE_i_OWE;
                }else if(checkedId == R.id.rb_owed_to_me) {
                    mDebtType = Debt.DEBT_TYPE_OWED;
                }
            }
        });

        ImageButton imageButtonContacts = fragmentAddDebtBinding.ibContacts;
        imageButtonContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(pickContactIntent, REQUEST_CONTACT);
            }
        });

        mDebtCreatedAt = System.currentTimeMillis();
        mDebtDue = System.currentTimeMillis();

        setHasOptionsMenu(true);
        setRetainInstance(true);

        return fragmentAddDebtBinding.getRoot();
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

            if(ValidationUtil.isInValid(new EditTextFullNameValidator(mEditTextName, getActivity()),
                    new EditTextPhoneNumberValidator(mEditTextPhoneNumber, getActivity()),
                    new EditTextIntegerValidator(mEditTextAmount, getActivity())
                    )) {

                Toast.makeText(getActivity(), "Invalid", Toast.LENGTH_LONG).show();

            }else {
                mPresenter.saveDebt(mEditTextName.getText().toString(),
                        mEditTextPhoneNumber.getText().toString(), Double.valueOf(mEditTextAmount.getText().toString()), mEditTextComment.getText().toString(),
                        mDebtCreatedAt, mDebtDue, mDebtType, Debt.DEBT_STATUS_ACTIVE);
            }
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
    public void showDebts() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void showEmptyDebtError() {
        Toast.makeText(getActivity(), getString(R.string.msg_empty_debt), Toast.LENGTH_LONG).show();
    }

    @Override
    public void setPresenter(AddDebtContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK ) return ;
        if(requestCode == REQUEST_CONTACT) {
            Uri contactUri = data.getData();
            String[] queryFields = new String[] {
                    ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER
            };
            Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);
            if(c.getCount() == 0 ) {
                c.close();
                return;
            }
            c.moveToFirst();
            mEditTextName.setText(c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
            mEditTextPhoneNumber.setText(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            c.close();
        }
    }

    private void showDatePickerDialog(final int buttonId) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);

                String dateString = TimeUtil.formatDateToString(dayOfWeek, monthOfYear, dayOfMonth) + " " + year;

                if(buttonId == mButtonDateDue.getId()) {
                    mButtonDateDue.setText(String.format(getString(R.string.due_date), dateString));
                    mDebtDue = mCalendar.getTimeInMillis();

                }else if(buttonId == mButtonDateCreated.getId()) {
                    mButtonDateCreated.setText(String.format(getString(R.string.created_date), dateString));
                    mDebtCreatedAt = mCalendar.getTimeInMillis();
                }

            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
}

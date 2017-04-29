package com.chikeandroid.debtmanager20.util.validator;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.EditText;

import com.chikeandroid.debtmanager20.R;
import com.chikeandroid.debtmanager20.util.ValidationUtil;

/**
 * Created by Chike on 4/11/2017.
 */
public class EditTextPhoneNumberValidator extends EditTextValidator {

    private final Context mContext;

    public EditTextPhoneNumberValidator(EditText editText, Context context) {
        super(editText);
        mContext = context;
        // String phoneNumber = getPhoneNumber();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ValidationUtil.isInValid(EditTextPhoneNumberValidator.this);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // do nothing
            }
        });
    }

    @Override
    public boolean isValid() {
        if (ValidationUtil.isEmpty(this)) {
            setErrorMessage(String.format(mContext.getString(R.string.required_field), "Phone number"));
            return false;
        }else if(!Patterns.PHONE.matcher(getPhoneNumber()).matches()) {
            setErrorMessage("Invalid phone number");
            return false;
        }
        return true;
    }

    private String getPhoneNumber() {
      return mEditText.getText().toString();
    }
}

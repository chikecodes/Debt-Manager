package com.chikeandroid.debtmanager20.util.validator;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.chikeandroid.debtmanager20.R;
import com.chikeandroid.debtmanager20.util.ValidationUtil;

import java.util.regex.Pattern;

/**
 * Created by Chike on 4/11/2017.
 */

public class EditTextFullNameValidator extends EditTextValidator {

    private static final String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";
    private Pattern mPattern;
    private Context mContext;

    public EditTextFullNameValidator(EditText editText, Context context) {
        super(editText);
        mContext = context;
        // pattern = Pattern.compile(USERNAME_PATTERN);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ValidationUtil.isInValid(EditTextFullNameValidator.this);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public boolean isValid() {
        if (ValidationUtil.isEmpty(this)) {
            setErrorMessage(String.format(mContext.getString(R.string.required_field), "Full name"));
            return false;
        }
      /*  Matcher matcher = pattern.matcher(mEditText.getText().toString());
        return matcher.matches();*/
        return true;
    }
}

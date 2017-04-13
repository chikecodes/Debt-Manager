package com.chikeandroid.debtmanager20.util.validator;

import android.widget.EditText;

/**
 * https://github.com/bendaniel10/appcommons/blob/master/app/src/main/java/com/bentech/android/appcommons/validator/EditTextValidator.java
 */

public abstract class EditTextValidator {

    protected EditText mEditText;
    protected String mErrorMessage;

    public EditTextValidator(EditText editText) {
        mEditText = editText;
    }

    public EditText getEditText() {
        return mEditText;
    }

    public void setEditText(EditText editText) {
        mEditText = editText;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public abstract boolean isValid();

    public EditTextValidator setErrorMessage(String errorMessage) {
        mErrorMessage = errorMessage;
        return this;
    }


}

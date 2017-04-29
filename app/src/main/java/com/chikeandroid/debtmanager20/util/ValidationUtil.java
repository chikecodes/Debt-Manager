package com.chikeandroid.debtmanager20.util;

import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.chikeandroid.debtmanager20.util.validator.EditTextValidator;

/**
 * Created by Chike on 4/10/2017.
 */

public final class ValidationUtil {

    private ValidationUtil() {}

    public static boolean isEmpty(EditTextValidator editTextValidator) {

        EditText editText = editTextValidator.getEditText();
        String value = editText.getText().toString().trim();

        boolean isEmpty = StringUtil.isEmpty(value);

        if(isEmpty) {
            if (editText.getVisibility() != View.VISIBLE) {
                return false;
            }
            setError(editText, editTextValidator.getErrorMessage());
        }else {
            clearError(editText);
        }
        return isEmpty;
    }

    public static boolean isInValid(EditTextValidator editTextValidator) {
        boolean isValid = editTextValidator.isValid();
        if (isValid) {
            clearError(editTextValidator.getEditText());
        } else {
            setError(editTextValidator.getEditText(), editTextValidator.getErrorMessage());
        }
        return !isValid;
    }

    public static boolean isInValid(EditTextValidator... editTextValidators) {
        boolean isInValid = false;
        for (EditTextValidator editTextValidator : editTextValidators) {
            if (isInValid(editTextValidator)) {
                isInValid = true;
            }
        }
        return isInValid;
    }

    private static void setError(EditText editText, String errorString) {
        if (editText.getParent() != null && editText.getParent() instanceof TextInputLayout) {
            ((TextInputLayout) editText.getParent()).setError(editText.getHint() == null ? errorString : editText.getHint());
            ((TextInputLayout) editText.getParent()).setErrorEnabled(true);
        } else if (editText.getParent() != null
                && editText.getParent().getParent() != null && editText.getParent().getParent() instanceof TextInputLayout) {
            ((TextInputLayout) editText.getParent().getParent()).setError(editText.getHint() == null ? errorString : editText.getHint());
            ((TextInputLayout) editText.getParent().getParent()).setErrorEnabled(true);
        } else {
            editText.setError(editText.getHint() == null ? errorString : editText.getHint());
        }
    }

    public static void setError(EditText editText, int errorStringId) {
        if (editText != null) {
            setError(editText, editText.getResources().getString(errorStringId));
        }
    }

    private static void clearError(EditText editText) {
        if (editText.getParent() != null && editText.getParent() instanceof TextInputLayout) {
            ((TextInputLayout) editText.getParent()).setErrorEnabled(false);
            ((TextInputLayout) editText.getParent()).setError(null);
        }else if (editText.getParent() != null
                && editText.getParent().getParent() != null && editText.getParent().getParent() instanceof TextInputLayout) {
            ((TextInputLayout) editText.getParent().getParent()).setErrorEnabled(false);
            ((TextInputLayout) editText.getParent().getParent()).setError(null);
        }  else {
            editText.setError(null);
        }
    }





}

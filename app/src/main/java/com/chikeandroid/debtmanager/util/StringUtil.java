package com.chikeandroid.debtmanager.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Patterns;

import java.text.DecimalFormat;

/**
 * Created by Chike on 4/10/2017.
 */

public final class StringUtil {

    private StringUtil() {}

    public static boolean isEmail(final @NonNull CharSequence str) {
        return Patterns.EMAIL_ADDRESS.matcher(str).matches();
    }

    public static boolean isEmpty(final @Nullable String str) {
        return str == null || TextUtils.isEmpty(str);
    }

    public static boolean isPresent(final @Nullable String str) {
        return !isEmpty(str);
    }

    public static String commaNumber(double amount) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        return formatter.format(amount);
    }
}

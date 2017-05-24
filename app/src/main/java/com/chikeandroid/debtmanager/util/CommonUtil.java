package com.chikeandroid.debtmanager.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Chike on 3/11/2017.
 */

public final class CommonUtil {

    private CommonUtil() {

    }

    @SuppressLint("all")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        String emailPattern =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(emailPattern);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

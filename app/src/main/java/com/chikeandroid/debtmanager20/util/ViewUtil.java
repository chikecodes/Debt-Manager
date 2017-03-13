package com.chikeandroid.debtmanager20.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.chikeandroid.debtmanager20.R;

/**
 * Created by Chike on 3/11/2017.
 */

public class ViewUtil {

    private ViewUtil() {

    }

    public static float getScreenDensity(final @NonNull Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int getScreenHeightDp(final @NonNull Context context) {
        return context.getResources().getConfiguration().screenHeightDp;
    }

    public static int getScreenWidthDp(final @NonNull Context context) {
        return context.getResources().getConfiguration().screenWidthDp;
    }

    public static boolean isFontScaleLarge(final @NonNull Context context) {
        return context.getResources().getConfiguration().fontScale > 1.5f;
    }

    public static boolean isLandscape(final @NonNull Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isPortrait(final @NonNull Context context) {
        return !isLandscape(context);
    }


    public static float pxToDp(float px) {
        float densityDpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        return px / (densityDpi / 160f);
    }

    public static int dpToPx(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }

    /**
     * Show a dialog box to the user.
     */
    public static void showDialog(final @NonNull Context context, final @Nullable String title, final @NonNull String message) {
        //new ConfirmDialog(context, title, message).show();
    }

    public static void showDialog(final @NonNull Context context, final @Nullable String title,
                                  final @NonNull String message, final @NonNull String buttonMessage) {
       // new ConfirmDialog(context, title, message, buttonMessage).show();
    }

    /**
     * Show a toast with default bottom gravity to the user.
     */
    @SuppressLint("InflateParams")
    public static void showToast(final @NonNull Context context, final @NonNull String message) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.toast, null);
        final TextView text = (TextView) view.findViewById(R.id.toast_text_view);
        text.setText(message);

        final Toast toast = new Toast(context);
        toast.setView(view);
        toast.show();
    }

    /**
     * Sets the visiblity of a view to {@link View#VISIBLE} or {@link View#GONE}. Setting
     * the view to GONE removes it from the layout so that it no longer takes up any space.
     */
    public static void setGone(final @NonNull View view, final boolean gone) {
        if (gone) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("InflateParams")
    public static void showToastFromTop(final @NonNull Context context, final @NonNull String message, final int xOffset,
                                        final int yOffset) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.toast, null);
        final TextView text = (TextView) view.findViewById(R.id.toast_text_view);
        text.setText(message);

        final Toast toast = new Toast(context);
        toast.setView(view);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, xOffset, yOffset);
        toast.show();
    }

    /**
     * Sets the visiblity of a view to {@link View#VISIBLE} or {@link View#INVISIBLE}. Setting
     * the view to INVISIBLE makes it hidden, but it still takes up space.
     */
    public static void setInvisible(final @NonNull View view, final boolean hidden) {
        if (hidden) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }
}

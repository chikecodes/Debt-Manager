package com.chikeandroid.debtmanager20.util;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by Chike on 4/10/2017.
 * A Time utility class
 */

public final class TimeUtil {

    private TimeUtil() {

    }

    public static String getDayOfMonthSuffix(final int n) {
        checkArgument(n >= 1 && n <= 31, "illegal day of month: " + n);
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:  return "st";
            case 2:  return "nd";
            case 3:  return "rd";
            default: return "th";
        }
    }

    public static String getCurrentDay(int dayIndex){

        String[] daysArray = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        return daysArray[dayIndex];
    }

    public static String getMonthForInt(int num){

        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }

    public static String formatDateToString(int dayOfWeek, int month, int dayOfMonth) {
        return getCurrentDay(dayOfWeek - 1) + ", " +
                getMonthForInt(month) + " " +
                dayOfMonth + getDayOfMonthSuffix(dayOfMonth);
    }

    public static String millis2String(long millis) {
        String pattern = "MMM d, yyyy";
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(new Date(millis));
    }

    public static String dateToString(int year, int month, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(year, month, dayOfMonth);

        return TimeUtil.millis2String(calendar.getTimeInMillis());
    }

}

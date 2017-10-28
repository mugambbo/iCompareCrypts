package com.hellotractor.icomparecrypts.util;

import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Abdulmajid on 02/12/2016.
 */
public class DateTimeUtil {

    public static String todaysDateTime () {
        return DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date()).toString();
    }


    public static long convertDateTimeToMillis(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parsedDate = sdf.parse(date);
        long millis = parsedDate.getTime();
        return millis;
    }

    /**
     * Converts a date from the format yyyy-MM-ddTHH:mm:ss to its equivalent
     * in relative time span e.g. 3 minutes ago.
     * @param dateTime A date in the format yyyy-MM-ddTHH:mm:ss e.g. 2017-09-22T12:23:49
     * @param minResolution Minimum time to report e.g. 3 secs ago (0 minutes ago) MINUTE_IN_MILLIS
     * @param transitionResolution Time it takes to transit from e.g. 7 days ago to Dec 12 (WEEK_IN_MILLIS)
     * @param flags FLAGS such as abbreviations, etc.
     * @return Formatted date in relative time span e.g. 3 minutes ago
     */
    public static String getTimeAgo(String dateTime, long minResolution, long transitionResolution, int flags, Context context){
        long dateTimeInMillis;
        try {
            dateTimeInMillis = convertDateTimeToMillis(dateTime);
            return (String) DateUtils.getRelativeDateTimeString(context, dateTimeInMillis, minResolution, transitionResolution, flags);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }
}
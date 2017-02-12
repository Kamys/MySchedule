package com.kamys.github.myschedule.presenter;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Used for calculate time start and end lesson.
 */

class LessonTimeManager {
    private static final String TAG = LessonTimeManager.class.getName();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

    private static Date parseDate(String time) throws ParseException {
        try {
            Date parse = sdf.parse(time);
            Calendar calendarNow = Calendar.getInstance();
            calendarNow.setTime(parse);

            Calendar instance = Calendar.getInstance();

            instance.set(Calendar.HOUR_OF_DAY, calendarNow.get(Calendar.HOUR_OF_DAY));
            instance.set(Calendar.MINUTE, calendarNow.get(Calendar.MINUTE));
            instance.set(Calendar.SECOND, calendarNow.get(Calendar.SECOND));

            Date time1 = instance.getTime();
            Log.d(TAG, "parseDateIsTime: return " + parse);
            return time1;
        } catch (ParseException e) {
            Log.w(TAG, "parseDateIsTime: Failed parse time - " + time, e);
            throw e;
        }
    }

    /**
     * The same thing calculateHowToStart(Date) only with time parsing.
     *
     * @param time left to before is start.
     * @return 0 if time already.
     * @throws ParseException if parsing failed.
     * @see LessonTimeManager#calculateHowToStart(Date)
     */
    long calculateHowToStart(String time) throws ParseException {
        Log.d(TAG, "calculateHowToStart: time - " + time);
        Date date = parseDate(time);
        long millisToStart = calculateHowToStart(date);
        Log.d(TAG, "calculateHowToStart: dataToStart - " + millisToStart);
        return millisToStart;
    }

    /**
     * To calculate the time until the beginning.
     *
     * @param date left to before is start.
     * @return 0 if time already.
     */
    long calculateHowToStart(Date date) {
        Log.d(TAG, "calculateHowToStart: data - " + date);

        final long millisInDate = date.getTime();
        final long currentMils = System.currentTimeMillis();
        final long remainingMillis = millisInDate - currentMils;

        if (remainingMillis < 0) {
            Log.d(TAG, "calculateHowToStart: return 0 time already start.");
            return 0;
        }
        Log.d(TAG, "calculateHowToStart: return " + remainingMillis);
        return remainingMillis;
    }
}

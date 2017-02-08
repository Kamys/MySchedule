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

public class LessonTimeManager {
    private static final String TAG = LessonTimeManager.class.getName();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public static Date parseDate(String time) throws ParseException {
        try {
            Date parse = sdf.parse(time);
            Log.d(TAG, "parseDateIsTime: return " + parse);
            return parse;
        } catch (ParseException e) {
            Log.w(TAG, "parseDateIsTime: Failed parse time - " + time, e);
            throw e;
        }
    }


    /**
     * Cleaning all failed except SECOND and HOUR_OF_DAY.
     * For cleaning unnecessary field.
     *
     * @param date Data which need cleaning.
     * @return Cleaning data only with SECOND and HOUR_OF_DAY.
     */
    public Date cleaningData(Date date) {
        Log.d(TAG, "cleaningData: data " + date);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        final int second = calendar.get(Calendar.SECOND);
        final int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        calendar.setTime(new Date(0));

        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);

        Date time = calendar.getTime();
        Log.d(TAG, "cleaningData: return " + time);
        return time;
    }

    public Date calculateToStart(String time) throws ParseException {
        Log.d(TAG, "calculateToStart: time - " + time);
        Date date = parseDate(time);
        //Date cleaningData = cleaningData(date);
        Date dataToStart = calculateToStart(date);
        Log.d(TAG, "calculateToStart: dataToStart - " + dataToStart);
        return dataToStart;
    }

    /**
     * To calculate the time until the beginning.
     *
     * @param date left to before is start.
     */
    public Date calculateToStart(Date date) {
        Log.d(TAG, "calculateToStart: data - " + date);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        final Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(new Date());

        subtractParameters(calendar, nowCalendar, Calendar.HOUR_OF_DAY);
        subtractParameters(calendar, nowCalendar, Calendar.SECOND);
        subtractParameters(calendar, nowCalendar, Calendar.MINUTE);

        Date time = calendar.getTime();
        Log.d(TAG, "calculateToStart: time - " + time);
        return time;
    }

    private void subtractParameters(Calendar calendar, Calendar nowCalendar, int field) {
        Log.d(TAG, "subtractParameters: Before " + calendar.getTime());
        int value = nowCalendar.get(field);
        Log.d(TAG, "subtractParameters: subtract field - " + field + " = " + value);
        calendar.add(field, -1 * value);
        Log.d(TAG, "subtractParameters: After " + calendar.getTime());
    }


}

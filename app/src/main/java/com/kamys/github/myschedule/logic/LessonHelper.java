package com.kamys.github.myschedule.logic;

import android.content.res.Resources;
import android.util.Log;

import com.parsingHTML.logic.ParsingHTML;
import com.parsingHTML.logic.element.AttributeName;
import com.parsingHTML.logic.element.DayName;
import com.parsingHTML.logic.element.ElementName;
import com.parsingHTML.logic.element.NumeratorName;
import com.parsingHTML.logic.extractor.xml.ExtractorSchedule;
import com.parsingHTML.logic.extractor.xml.Lesson;
import com.parsingHTML.logic.extractor.xml.XPathBuilder;
import com.parsingHTML.logic.parser.exception.ExceptionParser;

import org.jsoup.nodes.Element;
import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Менеджер для работ с Lesson.
 */
public class LessonHelper {

    private static final String TAG = LessonHelper.class.getName();


    public static ArrayList<Lesson> getLesson(DayName dayName, NumeratorName numerator, Document document) {
        Log.d(TAG, "getLesson() dayName " + dayName + " numerator = " + numerator + " document = " + document);
        ArrayList<Lesson> lessons = null;
        XPathBuilder.XPathElement xPathLesson = new XPathBuilder.XPathElement(ElementName.LESSON);
        if (numerator != NumeratorName.EMPTY) {
            xPathLesson.addAttr(AttributeName.NUMERATOR, numerator.getName());
            xPathLesson.addAttr(AttributeName.NUMERATOR, NumeratorName.EMPTY.getName());
        }
        try {
            lessons = ExtractorSchedule.extractLessonWhitTime(dayName, xPathLesson, document);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getLesson() return " + lessons);
        return lessons;
    }

    public static Document parsingHTML(InputStream timeContent, InputStream scheduleContent) throws IOException {
        Log.i(TAG, "parsingHTML()1 InputStream");
        try {
            return ParsingHTML.transformation(
                    ParsingHTML.parsingSchedule(timeContent, scheduleContent, "UTF-8"));
        } catch (ExceptionParser exceptionParser) {
            exceptionParser.printStackTrace();
            return null;
        }
    }

    static Document parsingHTML(Element timeContent, Element scheduleContent) {
        Log.i(TAG, "parsingHTML()1 Element");
        try {
            return ParsingHTML.transformation(
                    ParsingHTML.parsingSchedule(timeContent, scheduleContent, "UTF-8"));
        } catch (ExceptionParser exceptionParser) {
            exceptionParser.printStackTrace();
            return null;
        }
    }


    static Document parsingHTML(Resources resources) throws IOException {
        Log.i(TAG, "parsingHTML()2");
        return null;
        /*return parsingHTML(
                resources.openRawResource(R.raw.raspbukepru),
                resources.openRawResource(R.raw.raspbukepru2));*/
    }


    /**
     * Возвр ащает сегодняшний день.
     *
     * @return сегодняшний день.
     */
    public static DayName calcDayNameToDay() {
        Calendar calendar = Calendar.getInstance();
        Log.i(TAG, "calendar = " + calendar);
        calendar.setTime(new Date());
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        Log.d(TAG, "DAY_OF_WEEK = " + day);
        switch (day) {
            case 1:
                return DayName.SUNDAY;
            case 2:
                return DayName.MONDAY;
            case 3:
                return DayName.TUESDAY;
            case 4:
                return DayName.WEDNESDAY;
            case 5:
                return DayName.THURSDAY;
            case 6:
                return DayName.FRIDAY;
            case 7:
                return DayName.SATURDAY;
            default:
                Log.w(TAG, "calcDayNameToDay() return null! day = " + day);
                return DayName.MONDAY;
        }
    }

    /**
     * Возвращает сегодняшний нумератор.
     *
     * @return сегодняшний нумератор.
     */
    public static NumeratorName calcNumeratorToDay() {
        Calendar calendar = Calendar.getInstance();
        Log.i(TAG, "calendar = " + calendar);
        int weekOfMonth = calendar.get(Calendar.WEEK_OF_YEAR);
        if (weekOfMonth % 2 == 0) {
            Log.i(TAG, "calcNumeratorToDay return NUMERATOR");
            return NumeratorName.NUMERATOR;
        } else {
            Log.i(TAG, "calcNumeratorToDay return DENOMINATOR");
            return NumeratorName.DENOMINATOR;
        }
    }

}

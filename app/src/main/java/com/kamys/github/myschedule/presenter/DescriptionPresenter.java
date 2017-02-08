package com.kamys.github.myschedule.presenter;

import android.util.Log;

import com.kamys.github.myschedule.view.ViewData;
import com.parsingHTML.logic.extractor.xml.Lesson;

import java.text.ParseException;
import java.util.Date;

/**
 * Need for presenter view {@link com.kamys.github.myschedule.view.activity.DescriptionActivity}.
 */

public class DescriptionPresenter implements Presenter {
    private static final String TAG = DescriptionPresenter.class.getName();
    private final ViewData<Lesson> view;
    private final Lesson lesson;

    public DescriptionPresenter(ViewData<Lesson> view, Lesson lesson) {
        this.view = view;
        this.lesson = lesson;
    }

    @Override
    public void update() {
        view.showData(lesson);
    }

    @Override
    public void stop() {

    }

    public Date getStartTime() {
        String time1 = lesson.getTime1();
        return calculateToStart(time1);
    }

    public Date getEndTime() {
        String time2 = lesson.getTime2();
        return calculateToStart(time2);
    }

    private Date calculateToStart(String time) {
        LessonTimeManager lessonTimeManager = new LessonTimeManager();
        try {
            Date dataToStart = lessonTimeManager.calculateToStart(time);
            Log.d(TAG, "getStartTime: dataToStart - " + dataToStart);
            return dataToStart;
        } catch (ParseException e) {
            Log.w(TAG, "getStartTime: Failed time - " + time, e);
            view.showError(e.getMessage());
            return new Date(0);
        }
    }
}
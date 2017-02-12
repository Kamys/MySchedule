package com.kamys.github.myschedule.presenter;

import android.util.Log;

import com.kamys.github.myschedule.view.ViewData;
import com.parsingHTML.logic.extractor.xml.Lesson;

import java.text.ParseException;

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

    public long getMillisToStart() {
        String time = lesson.getTime1();
        return calculateToStart(time);
    }

    public long getMillisToEnd() {
        String time = lesson.getTime2();
        return calculateToStart(extractDateIsSecond(time));
    }

    // TODO: 12.02.2017 delete this and add getTimeEnd pair in getMillisToEnd().
    private String extractDateIsSecond(String time) {
        return time.split("-")[1];
    }

    private long calculateToStart(String time) {
        LessonTimeManager lessonTimeManager = new LessonTimeManager();
        try {
            long millisToStart = lessonTimeManager.calculateHowToStart(time);
            Log.d(TAG, "getMillisToStart: dataToStart - " + millisToStart);
            return millisToStart;
        } catch (ParseException e) {
            Log.w(TAG, "getMillisToStart: Failed time - " + time, e);
            view.showError(e.getMessage());
            return 0;
        }
    }
}

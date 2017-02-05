package com.kamys.github.myschedule.presenter;

import android.util.Log;

import com.kamys.github.myschedule.logic.factory.LessonFactory;
import com.kamys.github.myschedule.view.ViewData;
import com.parsingHTML.logic.element.NumeratorName;
import com.parsingHTML.logic.extractor.xml.Lesson;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Need for management {@link Lesson}.
 */

public class LessonsPresenter {

    private static final String TAG = LessonsPresenter.class.getName();
    private NumeratorName numeratorToday = calcNumeratorToDay();

    private ViewData<ArrayList<ArrayList<Lesson>>> view;
    private LessonFactory lessonFactory;

    public LessonsPresenter(ViewData<ArrayList<ArrayList<Lesson>>> view) {
        this.view = view;
        lessonFactory = new LessonFactory(view.getContext());
    }

    /**
     * Return numerator today.
     *
     * @return numerator today.
     */
    private static NumeratorName calcNumeratorToDay() {
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

    public void update() {
        view.showData(lessonFactory.createArrayLesson(numeratorToday));
    }

    public void itemSelected(NumeratorName numerator) {
        numeratorToday = numerator;
        update();

    }
}

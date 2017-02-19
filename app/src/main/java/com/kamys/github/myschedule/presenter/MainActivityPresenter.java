package com.kamys.github.myschedule.presenter;

import android.util.Log;

import com.kamys.github.myschedule.logic.factory.LessonFactory;
import com.kamys.github.myschedule.view.ViewData;
import com.parsingHTML.logic.element.NumeratorName;
import com.parsingHTML.logic.extractor.xml.Lesson;

import java.util.Calendar;
import java.util.List;

/**
 * Need for presenter view {@link com.kamys.github.myschedule.view.activity.MainActivity}.
 */

public class MainActivityPresenter implements Presenter {

    private static final String TAG = MainActivityPresenter.class.getName();
    private final ViewData<List<List<Lesson>>> view;
    private NumeratorName numeratorToday = calcNumeratorToDay();
    private LessonFactory lessonFactory;

    public MainActivityPresenter(ViewData<List<List<Lesson>>> view) {
        this.view = view;
        lessonFactory = new LessonFactory(view.getContext());
    }

    /**
     * Return numerator today.
     *
     * @return numerator today.
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

    @Override
    public void update() {
        view.showData(lessonFactory.createArrayLesson(numeratorToday));
    }

    @Override
    public void stop() {

    }

    public void itemSelected(NumeratorName numerator) {
        numeratorToday = numerator;
        update();

    }

    public void onClickFloatingActionButton() {

    }
}

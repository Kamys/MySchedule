package com.kamys.github.myschedule.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.kamys.github.myschedule.R;
import com.kamys.github.myschedule.logic.factory.CardViewFactory;
import com.kamys.github.myschedule.presenter.DescriptionPresenter;
import com.kamys.github.myschedule.view.ViewData;
import com.parsingHTML.logic.extractor.xml.Lesson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Отображает подробное описание урока.
 */
public class DescriptionActivity extends AppCompatActivity implements ViewData<Lesson> {
    public static final String KEY_LESSON = "KeyLesson";
    private static final String TAG = DescriptionActivity.class.getName();
    @BindView(R.id.desc_name)
    TextView lessonName;
    @BindView(R.id.desc_time1)
    TextView time1;
    @BindView(R.id.desc_time2)
    TextView time2;
    @BindView(R.id.desc_description)
    TextView lessonDescription;
    @BindView(R.id.desc_image_number)
    ImageView lessonNumber;
    @BindView(R.id.desc_name_teachers)
    TextView textViewNameTeachers;
    @BindView(R.id.desc_before_lesson_time)
    TextView beforeLessonTime;

    private Lesson lesson = null;
    private DescriptionPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        ButterKnife.bind(this);

        lesson = (Lesson) getIntent().getSerializableExtra(KEY_LESSON);
        presenter = new DescriptionPresenter(this, lesson);
    }

    /**
     * Getting data of lesson and set in view.
     */
    private void initDescription(Lesson lesson) {
        lessonName.setText(lesson.getName());
        time1.setText(lesson.getTime1());
        time2.setText(lesson.getTime2());
        lessonDescription.setText(lesson.getDescription());
        textViewNameTeachers.setText(lesson
                .getTeacherNames()
                .replace(",", ",\n"));

        lessonNumber.setImageResource(CardViewFactory.getImageResourceForLessonNumber(lesson.getNumber()));
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "onStart()");
        presenter.update();
        Log.i(TAG, "onStart: update");
        startTimer(beforeLessonTime, "Test:", presenter.getStartTime());
        Log.i(TAG, "onStart: startTimer");

        super.onStart();
    }

    // TODO: 07.02.2017 Out logic in presenter.
    private void startTimer(final TextView beforeLessonTime, String txt, Date date) {
        Log.i(TAG, "startTimer: data - " + date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        Log.v(TAG, "startTimer: HOUR_OF_DAY - " + hourOfDay);
        int minute = calendar.get(Calendar.MINUTE);
        Log.v(TAG, "startTimer: MINUTE - " + minute);
        int second = calendar.get(Calendar.SECOND);
        Log.v(TAG, "startTimer: SECOND - " + second);
        int allSec = hourOfDay * 3600000 + minute * 60000 + second;
        Log.v(TAG, "startTimer: sec - " + allSec);

        int year = calendar.get(Calendar.YEAR);
        if (year != 1970) {
            Log.i(TAG, "startTimer: Pairs end.");
            allSec = 0;
        }

        MyCountDownTimer myCountDownTimer = new MyCountDownTimer(allSec, 1000, calendar, beforeLessonTime);
        myCountDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void showData(Lesson lesson) {
        initDescription(lesson);
    }

    @Override
    public void showError(String s) {

    }

    @Override
    public Context getContext() {
        return null;
    }

    private class MyCountDownTimer extends CountDownTimer {

        private final Calendar calendar;
        private final TextView timerText;
        private final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());


        MyCountDownTimer(long millisInFuture, long countDownInterval, Calendar calendar, TextView timerText) {
            super(millisInFuture, countDownInterval);
            this.calendar = calendar;
            this.timerText = timerText;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            calendar.add(Calendar.SECOND, -1);
            timerText.setText(format.format(calendar.getTime()));
        }

        @Override
        public void onFinish() {
            timerText.setText("Пара началась!!");
        }
    }
}

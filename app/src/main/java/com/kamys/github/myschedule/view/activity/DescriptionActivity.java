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

import java.util.Locale;
import java.util.concurrent.TimeUnit;

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
    @BindView(R.id.desc_until_the_start)
    TextView untilTheStart;
    @BindView(R.id.desc_until_the_end)
    TextView untilTheEnd;

    private DescriptionPresenter presenter;
    private MyCountDownTimer myCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        ButterKnife.bind(this);

        Lesson lesson = (Lesson) getIntent().getSerializableExtra(KEY_LESSON);
        presenter = new DescriptionPresenter(this, lesson);
    }

    /**
     * Getting data of lesson and set in view.
     */
    private void initDescription(Lesson lesson) {
        Log.i(TAG, "initDescription: lesson - " + lesson);
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
        // TODO: 12.02.2017 out string in res.
        startTimer(untilTheStart, "Pairs start!!", presenter.getMillisToStart());
        startTimer(untilTheEnd, "Pairs end!!", presenter.getMillisToEnd());
        Log.i(TAG, "onStart: startTimer");

        super.onStart();
    }

    private void startTimer(final TextView textView, String messageForEnd, long millisToStart) {

        myCountDownTimer = new MyCountDownTimer(millisToStart, textView, messageForEnd);
        myCountDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        myCountDownTimer.cancel();
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

        private static final long COUNT_DOWN_INTERVAL = 1000;
        private static final String FORMAT = "%02d:%02d:%02d";
        private final TextView timerText;
        private final String messageForEnd;


        MyCountDownTimer(long millisInFuture, TextView timerText, String messageForEnd) {
            super(millisInFuture, COUNT_DOWN_INTERVAL);
            Log.i(TAG, "MyCountDownTimer: millisInFuture - " + millisInFuture);
            this.messageForEnd = messageForEnd;
            this.timerText = timerText;
        }

        public void onTick(long millisUntilFinished) {

            String timeToStart = String.format(Locale.getDefault(), FORMAT,
                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
            timerText.setText(timeToStart);
        }

        @Override
        public void onFinish() {
            timerText.setText(messageForEnd);
        }
    }
}

package com.kamys.github.myschedule.view.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.kamys.github.myschedule.R;
import com.kamys.github.myschedule.logic.factory.CardViewFactory;
import com.parsingHTML.logic.extractor.xml.Lesson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Отображает подробное описание урока.
 */
public class DescriptionActivity extends AppCompatActivity {
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
    private Lesson lesson = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        ButterKnife.bind(this);

        lesson = (Lesson) getIntent().getSerializableExtra(KEY_LESSON);

        Log.d(TAG, "Lesson = " + lesson);
        initDescription();

        lessonNumber.setImageResource(CardViewFactory.getImageResourceForLessonNumber(lesson.getNumber()));
    }

    /**
     * Getting data of lesson and set in view.
     */
    private void initDescription() {
        lessonName.setText(lesson.getName());
        time1.setText(lesson.getTime1());
        time2.setText(lesson.getTime2());
        lessonDescription.setText(lesson.getDescription());
        textViewNameTeachers.setText(lesson
                .getTeacherNames()
                .replace(",", ",\n"));
    }

    @Override
    protected void onStart() {
        final TextView beforeLessonTime = (TextView) findViewById(R.id.desc_before_lesson_time);
        String time1 = lesson.getTime1();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            Date date = sdf.parse(time1);
            startTimer(beforeLessonTime, "Test:", date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        super.onStart();
    }

    private void startTimer(final TextView beforeLessonTime, String txt, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Calendar calendarThis = Calendar.getInstance();
        calendarThis.setTime(new Date());

        calendar.add(Calendar.HOUR_OF_DAY, calendarThis.get(Calendar.HOUR_OF_DAY) * -1);
        calendar.add(Calendar.MINUTE, calendarThis.get(Calendar.MINUTE) * -1);
        calendar.add(Calendar.SECOND, calendarThis.get(Calendar.SECOND) * -1);

        int sec = calendar.get(Calendar.HOUR_OF_DAY) * 3600000 + calendar.get(Calendar.MINUTE) * 60000;

        MyCountDownTimer myCountDownTimer = new MyCountDownTimer(sec, 1000, calendar, beforeLessonTime);
        myCountDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
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
            timerText.setText("-----");
        }
    }
}

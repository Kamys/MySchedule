package com.kamys.github.myschedule.logic;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kamys.github.myschedule.R;
import com.kamys.github.myschedule.view.activity.DescriptionActivity;
import com.parsingHTML.logic.element.NumeratorName;
import com.parsingHTML.logic.extractor.xml.Lesson;

/**
 * Фабрика по сазданию CardView.
 */
public class CardViewFactory {
    private static final String TAG = CardViewFactory.class.getName();
    /**
     * Размер для нормальной строки.
     * Используется для контроля занимаемого места строкой..
     */
    private static final int lengthStringNormal = 28;
    private static int[] imagesForLessonNumber = {
            R.drawable.lesson_number_1,
            R.drawable.lesson_number_2,
            R.drawable.lesson_number_3,
            R.drawable.lesson_number_4,
            R.drawable.lesson_number_5,
            R.drawable.lesson_number_6
    };
    private LayoutInflater layoutInflater;
    private ViewGroup viewGroup;
    private Activity activity;

    public CardViewFactory(LayoutInflater layoutInflater, ViewGroup viewGroup, Activity activity) {
        this.layoutInflater = layoutInflater;
        this.viewGroup = viewGroup;
        this.activity = activity;
    }

    /**
     * Контролирует размер строки.
     * Если строка большая облезает и добовляет в конец троиточие.
     *
     * @param s строка.
     */
    private static String formatString(final String s, final int length) {
        if (s.length() > length) {
            return s.substring(0, length - 3).concat("...");
        }
        return s;
    }

    /**
     * Get Resource is image for number is lesson.
     *
     * @param lessonNumber The number is lesson.
     * @return Resource is image for number is lesson.
     */
    public static int getImageResourceForLessonNumber(int lessonNumber) {
        lessonNumber--;  //For combination with array.
        if (lessonNumber < 0 || lessonNumber >= imagesForLessonNumber.length) {
            Log.e(TAG, "Failed getImageResourceForLessonNumber lessonNumber = " + lessonNumber);
            return 0;
        }
        return imagesForLessonNumber[lessonNumber];
    }

    public CardView addNewCard(Lesson lesson) {
        CardView card = (CardView) layoutInflater.inflate(R.layout.custom_card_viwe, viewGroup, false);

        TextView textViewName = (TextView) card.findViewById(R.id.card_name);
        textViewName.setText(formatString(lesson.getName(), lengthStringNormal));

        TextView textViewDescription = (TextView) card.findViewById(R.id.card_description);
        textViewDescription.setText(lesson.getDescription());

        TextView textViewTime1 = (TextView) card.findViewById(R.id.card_time_1);
        textViewTime1.setText(lesson.getTime1());

        TextView textViewTime2 = (TextView) card.findViewById(R.id.card_time_2);
        textViewTime2.setText(lesson.getTime2());

        TextView textViewNumerator = (TextView) card.findViewById(R.id.card_numerator);
        textViewNumerator.setText(formatNumerator(lesson.getNumeratorName()));

        ImageView imageViewNumber = (ImageView) card.findViewById(R.id.card_image_number);
        imageViewNumber.setImageResource(getImageResourceForLessonNumber(lesson.getNumber()));

        card.setOnClickListener(new CardViewListener(lesson));

        return card;
    }

    private String formatNumerator(NumeratorName numeratorName) {
        if (numeratorName == NumeratorName.EMPTY) return " ";
        return numeratorName.getName();
    }

    /**
     * Обработчик нажатия на CardView.
     * Вызывает DescriptionActivity.
     */
    private class CardViewListener implements View.OnClickListener {

        /**
         * Lesson для DescriptionActivity.
         */
        private final Lesson lesson;

        CardViewListener(Lesson lesson) {
            this.lesson = lesson;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, DescriptionActivity.class);
            intent.putExtra(DescriptionActivity.KEY_LESSON, lesson);
            activity.startActivity(intent);
        }
    }
}

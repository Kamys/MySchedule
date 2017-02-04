package com.kamys.github.myschedule.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kamys.github.myschedule.R;
import com.kamys.github.myschedule.logic.CardViewFactory;
import com.parsingHTML.logic.element.DayName;
import com.parsingHTML.logic.extractor.xml.Lesson;

import java.util.ArrayList;
import java.util.List;


/**
 * Фрагмент отображаем список пар.
 */
public class DayFragment extends Fragment {
    public static final String KEY_DAY_NAME = "KeyDayName";
    public static final String KEY_LESSON_LIST = "KeyLessonList";
    private static final String TAG = DayFragment.class.getName();
    /**
     * День который отображает DayFragment.
     */
    private DayName dayName;
    private ArrayList<Lesson> lessons;
    private LinearLayout linearLayout;
    private LayoutInflater inflater;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");
        // Inflate the layout for this fragment

        this.inflater = inflater;
        View myFragment = this.inflater.inflate(R.layout.fragment_list_cards, container, false);
        linearLayout = (LinearLayout) myFragment.findViewById(R.id.liner_layout);

        Bundle arguments = getArguments();

        if (dayName == null) {
            int intExtraDayName = arguments.getInt(KEY_DAY_NAME, 0);
            dayName = DayName.values()[intExtraDayName];
            Log.d(TAG, "Get dayName of arguments = " + dayName.getName());
        }

        if (lessons == null) {
            lessons = (ArrayList<Lesson>) arguments.getSerializable(KEY_LESSON_LIST);
            Log.d(TAG, "Get lessons of arguments = " + lessons);
        }

        Log.i(TAG, "DayFragment = " + toString());
        addCardView(inflater, linearLayout, lessons);
        return myFragment;
    }

    private void addCardView(LayoutInflater inflater, LinearLayout linearLayout, List<Lesson> lessons) {
        Log.d(TAG, "addCardView() " + dayName.getNameShort());
        CardViewFactory cardViewFactory = new CardViewFactory(inflater, linearLayout, getActivity());
        for (Lesson lesson : lessons) {
            Log.d(TAG, "add lesson" + lesson);
            linearLayout.addView(cardViewFactory.addNewCard(lesson));
        }
    }

    public DayName getDayName() {
        return dayName;
    }

    public void update(ArrayList<Lesson> lessons) {
        this.lessons = lessons;
        if (linearLayout != null && inflater != null) {
            linearLayout.removeAllViews();
            addCardView(inflater, linearLayout, lessons);
            Log.d(TAG, "update() " + toString());
        } else {
            Log.d(TAG, "update() failed " + toString());
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy() DayName " + dayName);
        super.onDestroy();
    }


    @Override
    public void onPause() {
        Log.i(TAG, "onStop()" + toString());
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop()" + toString());
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState()");
        if (dayName != null && lessons != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(DayFragment.KEY_LESSON_LIST, lessons);
            bundle.putInt(DayFragment.KEY_DAY_NAME, dayName.ordinal());
            outState.putAll(bundle);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public String toString() {
        return "DayFragment{" +
                "dayName=" + dayName +
                '}';
    }
}

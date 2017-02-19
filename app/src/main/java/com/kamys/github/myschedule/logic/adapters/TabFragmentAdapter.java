package com.kamys.github.myschedule.logic.adapters;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.kamys.github.myschedule.view.fragment.DayFragment;
import com.parsingHTML.logic.element.DayName;
import com.parsingHTML.logic.extractor.xml.Lesson;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for tabs with day.
 */
public class TabFragmentAdapter extends FragmentPagerAdapter {
    private static final String TAG = TabFragmentAdapter.class.getName();
    /**
     * This list contains dayFragments for tab.
     */
    private final List<DayFragment> dayFragments = new ArrayList<>();

    public TabFragmentAdapter(FragmentManager manager) {
        super(manager);
        for (int i = 0; i < DayName.values().length; i++) {
            DayFragment day = createDay(i);
            dayFragments.add(day);
        }
        Log.i(TAG, "Create TabFragmentAdapter " + toString());
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem position = " + position);
        return dayFragments.get(position);
    }

    // TODO: 19.02.2017 out create DayFragment in factory.
    @NonNull
    private DayFragment createDay(int position) {
        Log.i(TAG, "createDay: position = " + position);
        DayFragment newDay = new DayFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DayFragment.KEY_DAY_NAME, position);
        Log.d(TAG, "getItem bundle = " + bundle);
        newDay.setArguments(bundle);
        return newDay;
    }

    @Override
    public int getCount() {
        return DayName.values().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Log.v(TAG, "getPageTitle: position = " + position);
        return DayName.values()[position].getNameShort();
    }

    public void updateLesson(List<List<Lesson>> lessonForDay) {
        Log.d(TAG, "updateLesson()");
        updateAllLesson(lessonForDay);
        notifyDataSetChanged();
    }

    private void updateAllLesson(List<List<Lesson>> arrayLists) {
        Log.i(TAG, "updateAllLesson()");
        for (int i = 0; i < dayFragments.size(); i++) {
            DayFragment dayFragment = dayFragments.get(i);
            dayFragment.update(arrayLists.get(i));
        }
    }

    @Override
    public String toString() {
        return "TabFragmentAdapter{" +
                "dayFragments=" + dayFragments +
                '}';
    }
}

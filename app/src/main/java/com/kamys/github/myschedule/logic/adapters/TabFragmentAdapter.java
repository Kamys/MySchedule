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

/**
 * Адаптео для Tabs.
 */
public class TabFragmentAdapter extends FragmentPagerAdapter {
    private static final String TAG = TabFragmentAdapter.class.getName();
    private final ArrayList<DayFragment> dayFragments = new ArrayList<>();
    private ArrayList<ArrayList<Lesson>> arrayLists;

    public TabFragmentAdapter(FragmentManager manager, ArrayList<ArrayList<Lesson>> arrayLists) {
        super(manager);
        this.arrayLists = arrayLists;
        for (int i = 0; i < DayName.values().length; i++) {
            DayFragment day = createDay(i);
            dayFragments.add(day);
        }
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem position = " + position);
        return dayFragments.get(position);
    }

    @NonNull
    private DayFragment createDay(int position) {
        DayFragment newDay = new DayFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DayFragment.KEY_LESSON_LIST, arrayLists.get(position));
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
        Log.v(TAG, "getPageTitle position = " + position);
        return DayName.values()[position].getNameShort();
    }

    public void updateLesson(ArrayList<ArrayList<Lesson>> arrayLists) {
        Log.d(TAG, "updateLesson()");
        this.arrayLists = arrayLists;
        updateAllLesson(arrayLists);
        notifyDataSetChanged();
    }

    private void updateAllLesson(ArrayList<ArrayList<Lesson>> arrayLists) {
        for (int i = 0; i < dayFragments.size(); i++) {
            dayFragments.get(i).update(arrayLists.get(i));
        }
    }
}

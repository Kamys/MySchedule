package com.kamys.github.myschedule.logic.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.kamys.github.myschedule.logic.factory.DayFragmentFactory;
import com.kamys.github.myschedule.view.fragment.DayFragment;
import com.parsingHTML.logic.element.DayName;
import com.parsingHTML.logic.extractor.xml.Lesson;

import java.util.List;

/**
 * Adapter for tabs with day.
 */
public class TabFragmentAdapter extends FragmentPagerAdapter {
    private static final String TAG = TabFragmentAdapter.class.getName();
    /**
     * This list contains dayFragments for tab.
     */
    private final List<DayFragment> dayFragments;
    private final DayFragmentFactory factory = new DayFragmentFactory();

    public TabFragmentAdapter(FragmentManager manager) {
        super(manager);
        dayFragments = factory.createAll();
        Log.i(TAG, "Create TabFragmentAdapter " + toString());
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem position = " + position);
        return dayFragments.get(position);
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

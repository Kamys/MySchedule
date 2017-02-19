package com.kamys.github.myschedule.logic.factory;

import android.os.Bundle;
import android.util.Log;

import com.kamys.github.myschedule.view.fragment.DayFragment;
import com.parsingHTML.logic.element.DayName;

import java.util.ArrayList;
import java.util.List;

/**
 * This factory for created {@link DayFragment};
 */

public class DayFragmentFactory {
    private static final String TAG = DayFragmentFactory.class.getName();

    /**
     * Create all {@link DayFragment} for week.
     */
    public List<DayFragment> createAll() {
        Log.d(TAG, "createAll()");
        List<DayFragment> dayFragments = new ArrayList<>();
        for (int i = 0; i < DayName.values().length; i++) {
            DayFragment day = createDay(i);
            dayFragments.add(day);
        }
        return dayFragments;
    }

    private DayFragment createDay(int numberOfDay) {
        Log.d(TAG, "createDay: numberOfDay = " + numberOfDay);
        DayFragment newDay = new DayFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DayFragment.KEY_DAY_NAME, numberOfDay);
        Log.d(TAG, "getItem bundle = " + bundle);
        newDay.setArguments(bundle);
        return newDay;
    }
}

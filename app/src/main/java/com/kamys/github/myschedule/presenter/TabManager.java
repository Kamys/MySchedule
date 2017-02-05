package com.kamys.github.myschedule.presenter;

import android.support.design.widget.TabLayout;
import android.util.Log;

import com.kamys.github.myschedule.R;
import com.kamys.github.myschedule.logic.LessonHelper;

/**
 * Need for management {@link TabLayout}.
 */

public class TabManager {
    private static final String TAG = TabManager.class.getName();
    private final TabLayout tabLayout;
    /**
     * Позиция TabLayout.Tab.
     * Выделяется в зависимости от дня недели.
     */
    private int positionTabSelect = -1;

    public TabManager(TabLayout tabLayout) {
        this.tabLayout = tabLayout;
        update();
    }

    public void update() {
        Log.d(TAG, "update()");
        selectTabToday();
    }

    public void resetpositionTabSelect() {
        positionTabSelect = -1;
    }

    /**
     * Выделить Tab с сегодняшним днём.
     */
    private void selectTabToday() {
        int ordinal = LessonHelper.calcDayNameToDay().ordinal();
        Log.i(TAG, "selectTabToday: ordinal = " + ordinal + " positionTabSelect = " + positionTabSelect);

        if (positionTabSelect == -1) {
            Log.d(TAG, "positionTabSelect == -1.");
            selectTab(ordinal);
            return;
        }

        if (positionTabSelect == ordinal) {
            Log.d(TAG, "Tab saved position.");
        } else {
            Log.d(TAG, "Tab new position.");
            resetSelectTab(positionTabSelect);
            selectTab(ordinal);
        }
    }

    /**
     * Выбрать Tab.
     *
     * @param position позиция Tab.
     */
    private void selectTab(int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        if (tab != null) {
            tab.setIcon(R.drawable.star);
            Log.d(TAG, "Select tab = " + tab.getText());
            positionTabSelect = position;
        } else {
            Log.w(TAG, "Select tab == null!");
        }
    }

    /**
     * Убрать выбор с Tab.
     *
     * @param position позиция Tab.
     */
    private void resetSelectTab(int position) {
        TabLayout.Tab tabAt = tabLayout.getTabAt(position);
        if (tabAt != null) {
            tabAt.setIcon(null);
            Log.d(TAG, "resetSelectTab() to " + tabAt.getText());
        } else {
            Log.w(TAG, "Failed resetSelectTab() tabAt == null. positionTabSelect = " + this.positionTabSelect);
        }
    }
}

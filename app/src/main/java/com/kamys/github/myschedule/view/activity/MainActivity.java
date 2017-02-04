package com.kamys.github.myschedule.view.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.kamys.github.myschedule.R;
import com.kamys.github.myschedule.logic.LessonHelper;
import com.kamys.github.myschedule.logic.SchedulesHelper;
import com.kamys.github.myschedule.logic.TabFragmentAdapter;
import com.kamys.github.myschedule.view.fragment.DayFragment;
import com.parsingHTML.logic.element.DayName;
import com.parsingHTML.logic.element.NumeratorName;
import com.parsingHTML.logic.extractor.xml.Lesson;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Главное MainActivity.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    /**
     * DrawerLayout который находится на  MainActivity.
     */
    private DrawerLayout drawerLayout;
    /**
     * Кнопка обновления.
     */
    private FloatingActionButton fab;
    /**
     * Toolbar который находится на  MainActivity.
     */
    private Toolbar toolbar;
    /**
     * TabLayout который находится на  MainActivity.
     */
    private TabLayout tabLayout;
    /**
     * ViewPager который находится на  MainActivity.
     * для отображения DayFragment.
     */
    private ViewPager viewPager;
    /**
     * Позиция TabLayout.Tab.
     * Выделяется в зависимости от дня недели.
     */
    private int positionTabSelect = -1;
    private SchedulesHelper helper = new SchedulesHelper(this);

    // TODO: 22.10.2016 Add description.
    private NumeratorName numeratorToday = LessonHelper.calcNumeratorToDay();
    private TabFragmentAdapter tabFragmentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager(), createArrayLesson());
        viewPager.setAdapter(tabFragmentAdapter);
        updateFragmentAdapter();

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.addDrawerListener(new MyDrawerListener());

        fab = (FloatingActionButton) findViewById(R.id.fab);
        settingFloatingActionButton(fab);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new MenuItemListener(navigationView, drawerLayout));

        setTitle(R.string.schedule);
    }

    /**
     * Настройка для FloatingActionButton.
     */
    private void settingFloatingActionButton(FloatingActionButton fab) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick FloatingActionButton.");
            }
        });
    }

    private void updateLesson() {
        updateFragmentAdapter();
        positionTabSelect = -1;
        selectTabToday();
    }

    private void updateFragmentAdapter() {
        tabFragmentAdapter.updateLesson(createArrayLesson());
    }

    @NonNull
    private ArrayList<ArrayList<Lesson>> createArrayLesson() {
        Document doc = helper.initializationDocument();
        int length = DayName.values().length;
        ArrayList<ArrayList<Lesson>> arrayLists = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            ArrayList<Lesson> lessons = LessonHelper.getLesson(DayName.values()[i], numeratorToday, doc);
            arrayLists.add(lessons);
        }
        helper.saveDOC(doc);
        return arrayLists;
    }

    private List<DayFragment> createDayFragments(Document doc) {
        List<DayFragment> dayFragments = new ArrayList<>();
        for (DayName dayName : DayName.values()) {
            dayFragments.add(createDayFragment(dayName, doc));
        }
        Log.d(TAG, "createDayFragments return " + dayFragments);
        return dayFragments;
    }

    private DayFragment createDayFragment(DayName dayName, Document document) {
        DayFragment fragment = new DayFragment();
        Bundle bundle = new Bundle();

        ArrayList<Lesson> lesson = LessonHelper.getLesson(dayName, numeratorToday, document);

        bundle.putSerializable(DayFragment.KEY_LESSON_LIST, lesson);
        bundle.putInt(DayFragment.KEY_DAY_NAME, dayName.ordinal());
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void onStart() {
        super.onStart();
        selectTabToday();
        selectNumeratorToday();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause()");
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_num:
                Log.i(TAG, "Menu main select \"num\".");
                numeratorToday = NumeratorName.NUMERATOR;
                break;
            case R.id.menu_main_den:
                Log.i(TAG, "Menu main select \"den\".");
                numeratorToday = NumeratorName.DENOMINATOR;
                break;
            case R.id.menu_main_all:
                numeratorToday = NumeratorName.EMPTY;
                Log.i(TAG, "Menu main select \"all\".");
                break;
            default:
                Log.w(TAG, "Failed onOptionsItemSelected item = " + item);
                return false;
        }
        item.setChecked(true);
        updateLesson();
        return true;
    }

    private void selectNumeratorToday() {
        numeratorToday = LessonHelper.calcNumeratorToDay();
        Log.i(TAG, "selectNumeratorToday = " + numeratorToday);


        // TODO: 23.10.2016 Implement select numerator when start app.
        /*if(numeratorToday == NumeratorName.NUMERATOR){
            onOptionsItemSelected(menu.findItem(R.id.menu_main_num));
            return;
        }
        if(numeratorToday == NumeratorName.DENOMINATOR){
            onOptionsItemSelected(menu.findItem(R.id.menu_main_den));
            return;
        }
        if(numeratorToday == NumeratorName.EMPTY){
            onOptionsItemSelected(menu.findItem(R.id.menu_main_all));
            return;
        }*/

    }

    /**
     * Выделить Tab с сегодняшним днём.
     */
    private void selectTabToday() {
        int ordinal = LessonHelper.calcDayNameToDay().ordinal();
        Log.i(TAG, "selectTabToday() ordinal = " + ordinal + " positionTabSelect = " + positionTabSelect);

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
            viewPager.setCurrentItem(position);
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


    /**
     * Слушатель для открытия и закрытия меню.
     */
    class MyDrawerListener implements DrawerLayout.DrawerListener {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            Log.i(TAG, "onDrawerOpened");
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            Log.i(TAG, "onDrawerClosed");
        }

        @Override
        public void onDrawerStateChanged(int newState) {
        }
    }

    /**
     * Слушателт для Navigation Drawer меню.
     */
    private class MenuItemListener implements NavigationView.OnNavigationItemSelectedListener {

        final NavigationView navigationView;
        final DrawerLayout drawerLayout;

        MenuItemListener(NavigationView navigationView, DrawerLayout drawerLayout) {
            this.navigationView = navigationView;
            this.drawerLayout = drawerLayout;
        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Log.d(TAG, "onNavigationItemSelected() Item = " + item.getTitle());
            selectItem(item);
            return true;
        }

        private void selectItem(MenuItem item) {

            Intent intent = null;
            switch (item.getItemId()) {
                case R.id.menu_nav_schedule_setting:
                    intent = new Intent(MainActivity.this, BrowserActivity.class);
                    break;
                case R.id.menu_nav_setting:
                    //intent = new Intent(MainActivity.this, SettingActivity.class);
                    break;
            }

            if (intent != null) {
                startActivity(intent);
            }

            item.setCheckable(true);
            setTitle(item.getTitle());
            drawerLayout.closeDrawers();
        }
    }
}

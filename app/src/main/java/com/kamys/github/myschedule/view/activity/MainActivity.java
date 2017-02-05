package com.kamys.github.myschedule.view.activity;


import android.content.Context;
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
import com.kamys.github.myschedule.logic.adapters.TabFragmentAdapter;
import com.kamys.github.myschedule.logic.factory.AlertDialogFactory;
import com.kamys.github.myschedule.presenter.LessonsPresenter;
import com.kamys.github.myschedule.presenter.TabManager;
import com.kamys.github.myschedule.view.ViewData;
import com.parsingHTML.logic.element.NumeratorName;
import com.parsingHTML.logic.extractor.xml.Lesson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Главное MainActivity.
 */
public class MainActivity extends AppCompatActivity implements ViewData<ArrayList<ArrayList<Lesson>>> {

    private static final String TAG = MainActivity.class.getName();

    /**
     * DrawerLayout который находится на  MainActivity.
     */
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    /**
     * Кнопка обновления.
     */
    @BindView(R.id.fab)
    FloatingActionButton fab;
    /**
     * Toolbar который находится на  MainActivity.
     */
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    /**
     * TabLayout который находится на  MainActivity.
     */
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    /**
     * ViewPager который находится на MainActivity.
     * для отображения DayFragment.
     */
    @BindView(R.id.viewpager)
    ViewPager viewPager;


    private TabFragmentAdapter tabFragmentAdapter;
    private TabManager tabManager;
    private LessonsPresenter presenter;
    private ArrayList<ArrayList<Lesson>> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        settingFloatingActionButton(fab);
        setSupportActionBar(toolbar);
        setTitle(R.string.schedule);


        presenter = new LessonsPresenter(this);
        presenter.start();

        drawerLayout.addDrawerListener(new MyDrawerListener());


        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new MenuItemListener(navigationView, drawerLayout));

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
        tabManager.update();

    }

    private void updateFragmentAdapter() {
        if (data != null) {
            tabFragmentAdapter.updateLesson(data);
            Log.i(TAG, "updateFragmentAdapter: update");
        } else {
            Log.w(TAG, "updateFragmentAdapter: Failed! data = null");
        }
    }


/*    private List<DayFragment> createDayFragments(Document doc) {
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

        ArrayList<Lesson> lesson = LessonHelper.getLesson(dayName, presenter.getNumeratorToday(), document);

        bundle.putSerializable(DayFragment.KEY_LESSON_LIST, lesson);
        bundle.putInt(DayFragment.KEY_DAY_NAME, dayName.ordinal());
        fragment.setArguments(bundle);
        return fragment;
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_num:
                Log.i(TAG, "Menu main select \"num\".");
                presenter.setNumeratorToday(NumeratorName.NUMERATOR);
                break;
            case R.id.menu_main_den:
                Log.i(TAG, "Menu main select \"den\".");
                presenter.setNumeratorToday(NumeratorName.DENOMINATOR);
                break;
            case R.id.menu_main_all:
                presenter.setNumeratorToday(NumeratorName.EMPTY);
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

    @Override
    public void showData(ArrayList<ArrayList<Lesson>> data) {
        this.data = data;
        Log.i(TAG, "showData: data - " + this.data);

        tabFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager(), data);
        viewPager.setAdapter(tabFragmentAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabManager = new TabManager(tabLayout);
    }

    @Override
    public void showError(String message) {
        Log.i(TAG, "showError: message - " + message);
        AlertDialogFactory.showAlertDialogError(getApplicationContext(), message);
    }

    @Override
    public Context getContext() {
        Log.i(TAG, "getContext()");
        return getApplicationContext();
    }

    @Override
    protected void onStart() {
        super.onStart();
        tabManager.update();
        //selectNumeratorToday();
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

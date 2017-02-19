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
import com.kamys.github.myschedule.presenter.MainActivityPresenter;
import com.kamys.github.myschedule.presenter.TabManager;
import com.kamys.github.myschedule.view.ViewData;
import com.parsingHTML.logic.element.NumeratorName;
import com.parsingHTML.logic.extractor.xml.Lesson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Главное MainActivity.
 */
public class MainActivity extends AppCompatActivity implements ViewData<List<List<Lesson>>> {

    private static final String TAG = MainActivity.class.getName();
    private static final int[] ID_MENU_ITEMS = {
            R.id.menu_main_num,
            R.id.menu_main_den,
            R.id.menu_main_all,
    };
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
    private MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        settingFloatingActionButton(fab);
        fab.hide();
        setSupportActionBar(toolbar);
        setTitle(R.string.schedule);

        tabFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabFragmentAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabManager = new TabManager(tabLayout);


        presenter = new MainActivityPresenter(this);
        presenter.update();

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
                presenter.onClickFloatingActionButton();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NumeratorName numerator;

        switch (item.getItemId()) {
            case R.id.menu_main_num:
                numerator = NumeratorName.NUMERATOR;
                break;
            case R.id.menu_main_den:
                numerator = NumeratorName.DENOMINATOR;
                break;
            case R.id.menu_main_all:
                numerator = NumeratorName.EMPTY;
                break;
            default:
                Log.w(TAG, "onOptionsItemSelected: Failed select item = " + item);
                return false;
        }
        Log.i(TAG, "onOptionsItemSelected: select " + numerator);
        presenter.itemSelected(numerator);
        item.setChecked(true);
        return true;
    }

    @Override
    public void showData(List<List<Lesson>> data) {
            tabFragmentAdapter.updateLesson(data);
        Log.i(TAG, "showData: update data - " + data);
            tabManager.resetpositionTabSelect();
            tabManager.update();
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

        selectTodayNumeratorInMenuItem(menu);

        return super.onPrepareOptionsMenu(menu);
    }

    private void selectTodayNumeratorInMenuItem(Menu menu) {
        NumeratorName numeratorToday = MainActivityPresenter.calcNumeratorToDay();

        int ordinal = numeratorToday.ordinal();
        int idMenuItem = ID_MENU_ITEMS[ordinal];
        Log.i(TAG, "onPrepareOptionsMenu: numeratorToday = " + numeratorToday + " id menu item = " + idMenuItem);

        MenuItem item = menu.findItem(idMenuItem);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
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

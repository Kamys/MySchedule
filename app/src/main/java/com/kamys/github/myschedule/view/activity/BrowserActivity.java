package com.kamys.github.myschedule.view.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

import com.kamys.github.myschedule.R;
import com.kamys.github.myschedule.logic.BrowserSelectSchedules;
import com.kamys.github.myschedule.logic.SchedulesHelper;

/**
 * Activity для отображения BrowserSelectSchedules.
 */
public class BrowserActivity extends AppCompatActivity {
    private static final String TAG = BrowserActivity.class.getName();
    private FloatingActionButton fab;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        fab = (FloatingActionButton) findViewById(R.id.fab2);
        webView = (WebView) findViewById(R.id.webView);

        new BrowserSelectSchedules(new SchedulesHelper(this), this);

    }

    public FloatingActionButton getFab() {
        return fab;
    }

    public WebView getWebView() {
        return webView;
    }

    public void fabShow() {
        fab.show();
    }

    public void fabHide() {
        fab.hide();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
    }

}

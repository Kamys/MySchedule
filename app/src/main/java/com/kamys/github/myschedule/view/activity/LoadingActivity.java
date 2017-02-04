package com.kamys.github.myschedule.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kamys.github.myschedule.R;

public class LoadingActivity extends AppCompatActivity {
    private static final String TAG = LoadingActivity.class.getName();

    /**
     * Время ожидания
     **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Log.i(TAG, "LoadingActivity onCreate()");
    }

    @Override
    protected void onResume() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(LoadingActivity.this, MainActivity.class);
                LoadingActivity.this.startActivity(mainIntent);
                LoadingActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
        super.onResume();
    }
}

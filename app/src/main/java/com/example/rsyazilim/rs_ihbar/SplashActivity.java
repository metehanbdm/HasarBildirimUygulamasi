package com.example.rsyazilim.rs_ihbar;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.rsyazilim.rs_ihbar.network.LoginService;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000; // 2 saniye

    private Handler mHandler;

    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            startMainActivity();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        mHandler = new Handler();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mHandler.postDelayed(mRunnable, SPLASH_TIME_OUT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        removeCallbacks();
    }

    @Override
    protected void onStop() {
        super.onStop();

        removeCallbacks();
    }

    private void removeCallbacks() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
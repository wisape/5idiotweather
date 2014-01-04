package com.wisape.fiveidiotweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by wisape on 14-1-4.
 */
public class fiveidiot_splash extends Activity {
    private static final long SPLASH_DELAY_MILLIS = 500;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startMainActivity();
            }
        }, SPLASH_DELAY_MILLIS);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, fiveidiot.class);
        this.startActivity(intent);
        this.finish();
    }
}
package com.wisape.fiveidiotweather;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Activity;

/**
 * Created by wisape on 13-11-5.
 */

public class fiveidiot extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MainFragment mainFragment = new MainFragment();
        fragmentTransaction.add(R.id.main_activity, mainFragment);
        fragmentTransaction.commit();
        Intent intent = new Intent(this, fiveidiotservice.class);
        intent.putExtra("weather_path", "http://m.weather.com.cn/data/101110101.html");
        startService(intent);

    }

}

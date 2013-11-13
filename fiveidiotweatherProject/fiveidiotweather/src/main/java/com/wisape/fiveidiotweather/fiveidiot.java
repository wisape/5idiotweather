package com.wisape.fiveidiotweather;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.util.Log;

/**
 * Created by wisape on 13-11-5.
 */

public class fiveidiot extends FragmentActivity {
    MainPagerAdapter mainPagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
 * Start the Service which get the weather information
 */
        Intent intent = new Intent(this, fiveidiotservice.class);
        intent.putExtra("weather_path", "http://m.weather.com.cn/data/101110101.html");
        startService(intent);
        Log.d("5sha", "weather" + (2 + 1));

        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.main_activity);
        viewPager.setAdapter(mainPagerAdapter);
    }

    private class MainPagerAdapter extends FragmentPagerAdapter {

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new MainFragment();

            return fragment;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "天气" + position;
        }
    }
}

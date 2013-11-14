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
import android.widget.Toast;

/**
 * Created by wisape on 13-11-5.
 */

public class fiveidiot extends FragmentActivity implements MainFragment.OnMainChangeFragment{
    MainPagerAdapter mainPagerAdapter;
    SubPagerAdapter subPagerAdapter;
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
        subPagerAdapter = new SubPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainPagerAdapter);
    }

    @Override
    public void onMainFragmentClicked(int position) {
        Log.d("5sha", "set sub Adapter");
        int item = viewPager.getCurrentItem();
        viewPager.setAdapter(subPagerAdapter);
        viewPager.setCurrentItem(item);
    }

    @Override
    public void onBackPressed() {
        Log.d("5sha", "back Adapter");
        if (viewPager.getAdapter() == subPagerAdapter) {
            int item = viewPager.getCurrentItem();
            viewPager.setAdapter(mainPagerAdapter);
            viewPager.setCurrentItem(item);
        } else {
            Toast.makeText(this, "退出应用", Toast.LENGTH_LONG).show();
            super.onBackPressed();
        }
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

    private class SubPagerAdapter extends FragmentPagerAdapter {

        public SubPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new SubFragment();

            return fragment;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "天气详情" + position;
        }
    }
}

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
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.List;

/**
 * Created by wisape on 13-11-5.
 */

public class fiveidiot extends FragmentActivity implements MainFragment.OnMainChangeFragment{
    FragmentManager fragmentManager;
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

        fragmentManager = getSupportFragmentManager();
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.main_activity);
        viewPager.setAdapter(mainPagerAdapter);
    }

    @Override
    public void onMainFragmentClicked(Fragment fragment, int position) {
        Log.d("5sha", "set sub Adapter");
        SubFragment subFragment = new SubFragment();
        FragmentManager fm = fragment.getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.main_fragment, subFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        int count = fragmentManager.getBackStackEntryCount();
        Log.d("5sha", "pre count is " + count);
    }

    @Override
    public void onBackPressed() {
        Log.d("5sha", "back Adapter");
        if (!popMainFragment(fragmentManager)) {
            super.onBackPressed();
        }
    }

    private boolean popMainFragment(FragmentManager fm) {
        Log.d("5sha", "PopMainFragment");
        int count = 0;
        for (int i = 0; i < mainPagerAdapter.getCount(); i++) {
        Fragment fragment = mainPagerAdapter.getItem(i);
        if ((count = fragment.getChildFragmentManager().getBackStackEntryCount()) > 0) {
            Log.d("5sha", "count = " + count);
            if (fragment.getChildFragmentManager().popBackStackImmediate()) {
                return true;
            }
        }
        Log.d("5sha", "count = " + count);
        }
        return false;
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

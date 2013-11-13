package com.wisape.fiveidiotweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

/**
 * Created by wangjianmei on 13-11-13.
 */
public class fiveidiotdetail extends FragmentActivity {
    SubPagerAdapter subPagerAdapter;
    ViewPager viewPager;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        subPagerAdapter = new SubPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.sub_activity);
        viewPager.setAdapter(subPagerAdapter);
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
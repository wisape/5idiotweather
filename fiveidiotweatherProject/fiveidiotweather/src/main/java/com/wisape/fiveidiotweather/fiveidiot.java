package com.wisape.fiveidiotweather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by wisape on 13-11-5.
 */

public class fiveidiot extends FragmentActivity {
    private DrawerLayout slideLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView menuList;
    private String[] menuItems;
    private FragmentManager fragmentManager;
    private MainPagerAdapter mainPagerAdapter;
    private ViewPager viewPager;
    private fiveidiot_citys mCitys;
    private String[] citys = {"101010100", "101180201", "101020100", "101181401", "101110101"};
    private ArrayList<String> citys_a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
         * Start the Service which get the weather information
         */

        mCitys = new fiveidiot_citys(getApplicationContext());
        Intent intent = new Intent(this, fiveidiotservice.class);
        intent.putExtra("citys", citys);
        startService(intent);

        slideLayout = (DrawerLayout) findViewById(R.id.slide_layout);
        slideLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        menuList = (ListView) findViewById(R.id.menu_list);
        menuItems = getResources().getStringArray(R.array.menu_array);

        menuList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.menu_list_item, menuItems));
        menuList.setOnItemClickListener(new MenuItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this, slideLayout, R.drawable.ic_launcher,
                R.string.slide_open, R.string.slide_close) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(R.string.app_name);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle("设置");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        slideLayout.setDrawerListener(mDrawerToggle);

        fragmentManager = getSupportFragmentManager();
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.main_activity);
        viewPager.setAdapter(mainPagerAdapter);
    }

    private class MenuItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Toast.makeText(getApplicationContext(), "position is " + i, Toast.LENGTH_LONG).show();
        }
    }

    private class MainPagerAdapter extends FragmentPagerAdapter {

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new fiveidiot_main_fragment();
            return fragment;
        }

        @Override
        public int getCount() {
            return citys.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "天气" + position;
        }
    }
}

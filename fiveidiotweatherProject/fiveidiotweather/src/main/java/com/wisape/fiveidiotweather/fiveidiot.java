package com.wisape.fiveidiotweather;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.IBinder;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private ServiceConnection sconn;
    private Intent service_intent;
    private fiveidiotservice mservice;
    private DrawerLayout slideLayout;
    private ActionBar actionBar;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView menuList;
    private String[] menuItems;
    private FragmentManager fragmentManager;
    private MainPagerAdapter mainPagerAdapter;
    private ViewPager viewPager;
    private fiveidiot_citys mCitys;
    private String[] mcitys = {"101010100", "101180201", "101020100", "101181401", "101110101"};
    private ArrayList<String> citys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
         * Start the Service which get the weather information
         */

        mCitys = new fiveidiot_citys(getApplicationContext());
        citys = mCitys.get_citys();
        citys.add("北京");
        citys.add("安阳");
        citys.add("上海");
        sconn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mservice = ((fiveidiotservice.fiBinder)iBinder).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        service_intent = new Intent(this, fiveidiotservice.class);
        bindService(service_intent, sconn, Context.BIND_AUTO_CREATE);

        slideLayout = (DrawerLayout) findViewById(R.id.slide_layout);
        slideLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        menuList = (ListView) findViewById(R.id.menu_list);
        menuItems = getResources().getStringArray(R.array.menu_array);

        menuList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.menu_list_item, menuItems));
        menuList.setOnItemClickListener(new MenuItemClickListener());

        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this, slideLayout, R.drawable.ic_drawer,
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fiveidiot, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.update:
                update_data();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class MenuItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Toast.makeText(getApplicationContext(), "position is " + i, Toast.LENGTH_LONG).show();
        }
    }

    private class MainPagerAdapter extends FragmentPagerAdapter
            implements ViewPager.OnPageChangeListener{
        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new fiveidiot_main_fragment(citys.get(i));
            return fragment;
        }

        @Override
        public int getCount() {
            return citys.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return citys.get(position);
        }

        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        @Override
        public void onPageSelected(int i) {
            Log.d("5sha", "view pager position is " + i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    private void update_data() {
        bindService(service_intent, sconn, Context.BIND_AUTO_CREATE);
        mservice.update_service();
        update_ui();
        unbindService(sconn);
    }

    private void update_ui() {

    }
}

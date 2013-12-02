package com.wisape.fiveidiotweather;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.support.v4.view.PagerAdapter;
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
    public final static String BROADCAST_UPDATE_UI = "com.wisape.fiveidiotweather.update_ui";
    private fiveidiot_receiver receiver;
    private ServiceConnection sconn;
    private Intent service_intent;
    private fiveidiotservice mservice;
    private DrawerLayout slideLayout;
    private ActionBar actionBar;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView menuList;
    private ListView cityList;
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

        cityList = (ListView) findViewById(R.id.city_list);
        cityList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.menu_list_item, menuItems));
        cityList.setOnItemClickListener(new MenuItemClickListener());

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

            public void onDrawerOpened(View View) {
                switch (View.getId()) {
                    case R.id.city_list:
                        getActionBar().setTitle("城市列表");
                        break;
                    case R.id.menu_list:
                        getActionBar().setTitle("设置");
                        break;
                }
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        slideLayout.setDrawerListener(mDrawerToggle);

        fragmentManager = getSupportFragmentManager();
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.main_activity);
        viewPager.setAdapter(mainPagerAdapter);

        receiver = new fiveidiot_receiver();
        IntentFilter update_filter = new IntentFilter(BROADCAST_UPDATE_UI);
        registerReceiver(receiver, update_filter);

    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
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
            case R.id.newcity:
//                add new city
                fiveidiot_cityids_db cityids_db = new fiveidiot_cityids_db(getApplicationContext());
                return true;
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

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }

    private void update_data() {
        bindService(service_intent, sconn, Context.BIND_AUTO_CREATE);
        mservice.update_service();
        unbindService(sconn);
    }

    private void update_ui() {
        Log.d("5sha", "need update UI");
        Toast.makeText(this, "need update UI", Toast.LENGTH_LONG).show();
        mainPagerAdapter.notifyDataSetChanged();
        viewPager.invalidate();
    }

    private class fiveidiot_receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            update_ui();
        }
    }
}

package com.wisape.fiveidiotweather;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wisape.fiveidiotweather.core.fiveidiot_service;
import com.wisape.fiveidiotweather.core.data.fiveidiot_citys;
import com.wisape.fiveidiotweather.core.fiveidiot_set_ui;
import com.wisape.fiveidiotweather.ui.fiveidiot_main_fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by wisape on 13-11-5.
 */

public class fiveidiot extends FragmentActivity {
    public final static String BROADCAST_UPDATE_UI = "com.wisape.fiveidiotweather.update_ui";
//    private fiveidiot_receiver receiver;
    private DrawerLayout slideLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView menuList;
    private ListView cityList;
    private String[] menuItems;
    private fiveidiot_citys mCitys;
    private CityArrayAdapter cityArrayAdapter;
    private ViewPager viewPager;
    private ArrayList<String> citys;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
         * Start the Service which get the weather information
         */
        Intent preintent = new Intent("com.wisape.fiveidiotweather.start.fiveidiotservice");
        sendBroadcast(preintent);

        preintent.setAction(fiveidiot_set_ui.WIDGET_UPDATE);
        sendBroadcast(preintent);
        Button addButton = (Button)findViewById(R.id.main_add_city);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), fiveidiot_add_city.class);
                startActivity(it);
            }
        });

        mCitys = new fiveidiot_citys(getApplicationContext());
        citys = mCitys.get_citys();

        slideLayout = (DrawerLayout) findViewById(R.id.slide_layout);
        slideLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        menuList = (ListView) findViewById(R.id.menu_list);
        menuItems = getResources().getStringArray(R.array.menu_array);
        TextView menuTitle = new TextView(getApplicationContext());
        menuTitle.setTextColor(getResources().getColor(R.color.textcolor));
        menuTitle.setGravity(Gravity.CENTER);
        menuTitle.setTextSize(20);
        menuTitle.setBackgroundColor(getResources().getColor(R.color.titlecolor));
        menuTitle.setText(R.string.setting);
        menuList.addHeaderView(menuTitle, null, false);
        menuList.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                R.layout.menu_list_item, menuItems));
        menuList.setOnItemClickListener(new MenuItemClickListener());

        cityList = (ListView) findViewById(R.id.city_list);
        cityArrayAdapter = new CityArrayAdapter(getApplicationContext(), R.layout.city_list_item, R.id.city_list_item, citys);
        TextView cityTitle = new TextView(getApplicationContext());
        cityTitle.setTextColor(getResources().getColor(R.color.textcolor));
        cityTitle.setGravity(Gravity.CENTER);
        cityTitle.setTextSize(20);
        cityTitle.setBackgroundColor(getResources().getColor(R.color.titlecolor));
        cityTitle.setText(R.string.citylist);
        cityList.addHeaderView(cityTitle, null, false);
        cityList.setAdapter(cityArrayAdapter);
        cityList.setOnItemClickListener(new CitysItemClickListener());

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this, slideLayout, R.drawable.ic_drawer,
                R.string.setting, R.string.app_name) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(R.string.app_name);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View View) {
                switch (View.getId()) {
                    case R.id.city_list:
                        getActionBar().setTitle(R.string.citylist);
                        break;
                    case R.id.menu_list:
                        getActionBar().setTitle(R.string.setting);
                        break;
                }
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        slideLayout.setDrawerListener(mDrawerToggle);

        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.main_activity);
        viewPager.setAdapter(mainPagerAdapter);

        fiveidiot_receiver receiver = new fiveidiot_receiver();
        IntentFilter update_filter = new IntentFilter(BROADCAST_UPDATE_UI);
        registerReceiver(receiver, update_filter);
    }
//
//    @Override
//    public void onDestroy() {
//        unregisterReceiver(receiver);
//        super.onDestroy();
//    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_MENU) {
            if (menuList.isShown()) {
                slideLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (cityList.isShown());
                slideLayout.closeDrawer(GravityCompat.END);
            slideLayout.openDrawer(GravityCompat.START);
            return true;
        }

        if (keycode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
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
//              add new city
                Intent it = new Intent(getApplicationContext(), fiveidiot_add_city.class);
                startActivity(it);
                slideLayout.closeDrawers();
                return true;
            case R.id.havecitys:
                if (cityList.isShown()) {
                    slideLayout.closeDrawer(GravityCompat.END);
                    return true;
                } else if (menuList.isShown());
                    slideLayout.closeDrawer(GravityCompat.START);
                slideLayout.openDrawer(GravityCompat.END);
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

    private void startWebview(String title, String url) {
        Intent intent = new Intent(getApplicationContext(), fiveidiot_web.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("url", url);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private class MenuItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            switch (i) {
                case 1:
                    Intent setting_intent = new Intent(getApplicationContext(), fiveidiot_setting.class);
                    startActivity(setting_intent);
                    break;
                case 2:
                    startWebview("介绍", "http://5idiot.duapp.com/weather");
                    break;
                case 3:
                    Intent data=new Intent(Intent.ACTION_SENDTO);
                    data.setData(Uri.parse("mailto:wisape@gmail.com"));
                    data.putExtra(Intent.EXTRA_SUBJECT, "5傻天气建议");
                    startActivity(data);
                    break;
                case 4:
                    /*for score*/
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
//                            Uri.parse("https://play.google.com/store/apps/details?id=com.imangi.templerun"));
////                    browserIntent.setClassName("com.android.vending", "com.android.vending.AssetBrowserActivity");
//                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(browserIntent);
                    break;
                case 5:
                    startWebview("公益", "http://5idiot.duapp.com/pb");
                    break;
                default:
                    break;
            }
            slideLayout.closeDrawers();
        }
    }

    private class CitysItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            slideLayout.closeDrawers();
            viewPager.setCurrentItem(i - 1, true);
        }
    }

    private class CityArrayAdapter extends ArrayAdapter<String> {
        private ArrayList<String> texts;
        public CityArrayAdapter(Context context, int resource, int textViewResourceId, ArrayList<String> objects) {
            super(context, resource, textViewResourceId, objects);
            texts = objects;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            View row = null;
            if (convertView != null) {
                row = convertView;
            } else {
                LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflator.inflate(R.layout.city_list_item, parent, false);
            }
            TextView tv = (TextView) row.findViewById(R.id.city_list_item);
            tv.setText(texts.get(position));
            ImageButton btn = (ImageButton) row.findViewById(R.id.discard_city);
            btn.setTag(position);
            btn.setOnClickListener(new DiscardClickListener());
            return row;
        }
    }

    private class DiscardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            slideLayout.closeDrawers();
            final int position = Integer.parseInt(view.getTag().toString());
            AlertDialog.Builder builder = new AlertDialog.Builder(fiveidiot.this);
            builder.setMessage("确定删除城市：" + citys.get(position) + "?");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getApplicationContext(), "删除城市:" + citys.get(position), Toast.LENGTH_SHORT).show();
                    deleteCity(position);
                    slideLayout.closeDrawers();
                    return;
                }
            });

            builder.setNegativeButton("不删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    return;
                }
            });

            builder.create().show();
        }
    }

    private void deleteCity(int position) {
        mCitys.delete_city(citys.get(position));
        citys.remove(position);
        update_ui();
    }

    private void addCity(String city) {
        citys.add(city);
        update_data();
        update_ui();
        viewPager.setCurrentItem(citys.indexOf(city));
    }


    private void update_data() {
        if (!net_available()) {
            Toast.makeText(getApplicationContext(), "网络不给力！", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent service_intent = new Intent(getApplicationContext(), fiveidiot_service.class);
        startService(service_intent);
    }

    public boolean net_available() {
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            return true;
        }
        return false;
    }

    private void update_ui() {
        cityArrayAdapter.notifyDataSetChanged();
        cityList.invalidate();
        viewPager.getAdapter().notifyDataSetChanged();
//        mainPagerAdapter.notifyDataSetChanged();
        viewPager.invalidate();
    }


    private class fiveidiot_receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String addcity = intent.getStringExtra("addcity");
            if (addcity != null) {
                addCity(addcity);
            } else {
                int city_index = intent.getIntExtra("tocity", -1);
                if (city_index >= 0) {
                    viewPager.setCurrentItem(city_index);
                    return;
                }
                update_ui();
            }
        }
    }

    private class MainPagerAdapter extends FragmentPagerAdapter
            implements ViewPager.OnPageChangeListener{

        private final FragmentManager mFragmentManager;
        private FragmentTransaction mCurTransaction = null;
        private Fragment mCurrentPrimaryItem = null;


        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager = fm;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (mCurTransaction == null) {
                mCurTransaction = mFragmentManager.beginTransaction();
            }

            // Do we already have this fragment?
            String name = citys.get(position);
            Fragment fragment = mFragmentManager.findFragmentByTag(name);
            if (fragment != null) {
                mCurTransaction.attach(fragment);
            } else {
                fragment = getItem(position);
                mCurTransaction.add(container.getId(), fragment,
                        citys.get(position));
            }
            if (fragment != mCurrentPrimaryItem) {
                fragment.setMenuVisibility(false);
                fragment.setUserVisibleHint(false);
            }

            return fragment;

        }


        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new WeakReference<fiveidiot_main_fragment>(new fiveidiot_main_fragment(citys.get(i))).get();
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

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (mCurTransaction == null) {
                mCurTransaction = mFragmentManager.beginTransaction();
            }
            mCurTransaction.detach((Fragment)object);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            Fragment fragment = (Fragment)object;
            if (fragment != mCurrentPrimaryItem) {
                if (mCurrentPrimaryItem != null) {
                    mCurrentPrimaryItem.setMenuVisibility(false);
                    mCurrentPrimaryItem.setUserVisibleHint(false);
                }
                if (fragment != null) {
                    fragment.setMenuVisibility(true);
                    fragment.setUserVisibleHint(true);
                }
                mCurrentPrimaryItem = fragment;
            }
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            if (mCurTransaction != null) {
                mCurTransaction.commitAllowingStateLoss();
                mCurTransaction = null;
                mFragmentManager.executePendingTransactions();
            }
        }
    }

}

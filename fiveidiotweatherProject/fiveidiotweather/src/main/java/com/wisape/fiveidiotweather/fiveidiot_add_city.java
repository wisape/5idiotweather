package com.wisape.fiveidiotweather;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.wisape.fiveidiotweather.core.data.fiveidiot_cityids_db;
import com.wisape.fiveidiotweather.core.data.fiveidiot_citys;

import java.lang.ref.WeakReference;

/**
 * Created by wisape on 13-12-2.
 */
public class fiveidiot_add_city extends Activity implements SearchView.OnQueryTextListener {
    private fiveidiot_cityids_db cityids_db;
    private ListView listView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_city_activity);
        cityids_db = new WeakReference<fiveidiot_cityids_db>(new fiveidiot_cityids_db(getApplicationContext())).get();
        listView = (ListView) findViewById(R.id.add_city);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cityids_db.getProvinces()));
        listView.setOnItemClickListener(new onProvinceItemClick());
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.addcity);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_city_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cityids_db.findCitys(s)));
        listView.setOnItemClickListener(new onCityItemClick());
        return false;
    }


    private class onProvinceItemClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            listView.setAdapter(new ArrayAdapter<String>(fiveidiot_add_city.this, android.R.layout.simple_list_item_1, cityids_db.getCitys(i)));
            listView.setOnItemClickListener(new onCityItemClick());
        }
    }

    private class onCityItemClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String city = (String)adapterView.getItemAtPosition(i);
            String cityid = cityids_db.getCityid(city);
            int city_index = -1;
            //To do add the city

            fiveidiot_citys city_box = new WeakReference<fiveidiot_citys>(new fiveidiot_citys(getApplicationContext())).get();
            String city_b = city.replace(".", "");
            Intent it = new Intent(fiveidiot.BROADCAST_UPDATE_UI);
            city_index = city_box.has_city(city_b);
            if (city_index < 0) {
                city_box.set_city(city_b, cityid);
                it.putExtra("addcity", city_b);
                Toast.makeText(getApplicationContext(), "添加城市：" +city_b , Toast.LENGTH_SHORT).show();
            } else {
                it.putExtra("tocity", city_index);
                Toast.makeText(getApplicationContext(), "已添加城市：" +city_b , Toast.LENGTH_SHORT).show();
            }

            sendBroadcast(it);
            finish();
        }
    }
}

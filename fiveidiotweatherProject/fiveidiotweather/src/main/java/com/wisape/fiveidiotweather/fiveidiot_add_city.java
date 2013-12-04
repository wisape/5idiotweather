package com.wisape.fiveidiotweather;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by wisape on 13-12-2.
 */
public class fiveidiot_add_city extends Activity{
    private fiveidiot_cityids_db cityids_db;
    private ListView listView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_city_activity);
        cityids_db = new fiveidiot_cityids_db(getApplicationContext());
        listView = (ListView) findViewById(R.id.add_city);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cityids_db.getProvinces()));
        listView.setOnItemClickListener(new onProvinceItemClick());
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
            //To do add the city
            Toast.makeText(getApplicationContext(), "City is " +city + "id is" + cityid, Toast.LENGTH_SHORT).show();

        }
    }
}

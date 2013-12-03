package com.wisape.fiveidiotweather;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * Created by gz09 on 13-12-2.
 */
public class fiveidiot_add_city extends Activity implements View.OnClickListener {
    private fiveidiot_cityids_db cityids_db;
    private ListView listView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_city_activity);
        cityids_db = new fiveidiot_cityids_db(getApplicationContext());
        listView = (ListView) findViewById(R.id.add_city);
        listView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}
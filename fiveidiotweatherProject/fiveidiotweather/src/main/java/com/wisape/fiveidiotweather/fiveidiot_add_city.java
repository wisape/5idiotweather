package com.wisape.fiveidiotweather;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by gz09 on 13-12-2.
 */
public class fiveidiot_add_city extends Activity {
    fiveidiot_cityids_db cityids_db;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_city_activity);
        cityids_db = new fiveidiot_cityids_db(getApplicationContext());
    }
}
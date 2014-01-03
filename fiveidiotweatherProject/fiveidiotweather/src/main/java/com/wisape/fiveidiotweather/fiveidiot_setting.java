package com.wisape.fiveidiotweather;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

/**
 * Created by wisape on 13-12-18.
 */
public class fiveidiot_setting extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new setting_fragment()).commit();
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("设置");
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

    public class setting_fragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.preference);
        }
    }
}
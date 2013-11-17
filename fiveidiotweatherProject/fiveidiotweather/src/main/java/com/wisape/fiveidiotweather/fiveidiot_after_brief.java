package com.wisape.fiveidiotweather;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

/**
 * Created by wangjianmei on 13-11-17.
 */
public class fiveidiot_after_brief extends ListFragment {
    SimpleAdapter adapter = null;
    fiveidiotreaddb readdb = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readdb = new fiveidiotreaddb(getActivity());
        adapter = new SimpleAdapter(getActivity(), readdb.getAfterBriefAdapterData(),
                R.layout.after_brief, new String[]{"image", "weather",
                "temp", "wind", "week", "date"},
                new int[]{R.id.after_image, R.id.weather_d, R.id.temp_d,
                R.id.wind_d, R.id.week_d, R.id.data_d});
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        return rootView;
    }

}

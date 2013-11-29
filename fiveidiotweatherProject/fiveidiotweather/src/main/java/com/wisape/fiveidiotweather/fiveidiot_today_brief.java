package com.wisape.fiveidiotweather;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;

/**
 * Created by wisape on 13-11-17.
 */
public class fiveidiot_today_brief extends Fragment {
    private fiveidiotreaddb readdb;
    private String mcity;

    public fiveidiot_today_brief(String city) {
        mcity = city;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readdb = new fiveidiotreaddb(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.today_brief, container, false);
        Map<String, Object> brief_map = readdb.getTodayBriefMapData(mcity);
        setViewData(rootView, brief_map);
        return rootView;
    }

    private void setViewData(View view, Map<String, Object> data_map) {
        if (data_map.get("city") == null)
            return;
        ((TextView) view.findViewById(R.id.city)).setText(data_map.get("city").toString());
        ((TextView) view.findViewById(R.id.date)).setText(data_map.get("date").toString());
        ((TextView) view.findViewById(R.id.week)).setText(data_map.get("week0").toString());
        ((TextView) view.findViewById(R.id.temp)).setText(data_map.get("temp0").toString());
        ((TextView) view.findViewById(R.id.weather)).setText(data_map.get("weather0").toString());
        ((TextView) view.findViewById(R.id.wind)).setText(data_map.get("wind").toString());
        ((TextView) view.findViewById(R.id.nowtemp)).setText(data_map.get("nowtemp").toString() + "℃");
        ((ImageView) view.findViewById(R.id.today_image)).setImageResource(R.drawable.ic_launcher);
    }
}

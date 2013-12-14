package com.wisape.fiveidiotweather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by wisape on 13-11-17.
 */
public class fiveidiot_after_brief extends Fragment {
    private String mcity;
    private fiveidiotreaddb readdb;

    public fiveidiot_after_brief(String city) {
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
        View rootView = inflater.inflate(R.layout.after_brief, container, false);
        List<Map<String, Object>> after_map = readdb.getAfterBriefAdapterData(mcity);
        setViewData(rootView, after_map);
        return rootView;
    }

    private String setDefault(Object str1, String str2) {
        if (str1 == null)
            return str2;
        return str1.toString();
    }

    private void setViewData(View view, List<Map<String, Object>> data_map) {
        ((TextView) view.findViewById(R.id.week_1)).setText(setDefault(data_map.get(0).get("week"), "1"));
        ((TextView) view.findViewById(R.id.temp_1)).setText(setDefault(data_map.get(0).get("temp"),"1"));
        ((TextView) view.findViewById(R.id.weather_1)).setText(setDefault(data_map.get(0).get("weather"),"1"));
        ((ImageView) view.findViewById(R.id.image_1)).setImageResource(readdb.getImageId(data_map.get(0).get("image").toString()));

        ((TextView) view.findViewById(R.id.week_2)).setText(setDefault(data_map.get(1).get("week"),"1"));
        ((TextView) view.findViewById(R.id.temp_2)).setText(setDefault(data_map.get(1).get("temp"),"1"));
        ((TextView) view.findViewById(R.id.weather_2)).setText(setDefault(data_map.get(1).get("weather"),"1"));
        ((ImageView) view.findViewById(R.id.image_2)).setImageResource(readdb.getImageId(data_map.get(1).get("image").toString()));

        ((TextView) view.findViewById(R.id.week_3)).setText(setDefault(data_map.get(2).get("week"),"1"));
        ((TextView) view.findViewById(R.id.temp_3)).setText(setDefault(data_map.get(2).get("temp"),"1"));
        ((TextView) view.findViewById(R.id.weather_3)).setText(setDefault(data_map.get(2).get("weather"),"1"));
        ((ImageView) view.findViewById(R.id.image_3)).setImageResource(readdb.getImageId(data_map.get(2).get("image").toString()));

        ((TextView) view.findViewById(R.id.week_4)).setText(setDefault(data_map.get(3).get("week"),"1"));
        ((TextView) view.findViewById(R.id.temp_4)).setText(setDefault(data_map.get(3).get("temp"),"1"));
        ((TextView) view.findViewById(R.id.weather_4)).setText(setDefault(data_map.get(3).get("weather"),"1"));
        ((ImageView) view.findViewById(R.id.image_4)).setImageResource(readdb.getImageId(data_map.get(3).get("image").toString()));
    }
}

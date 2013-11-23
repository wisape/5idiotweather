package com.wisape.fiveidiotweather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;

/**
 * Created by wisape on 13-11-17.
 */

public class fiveidiot_today_detail extends Fragment {
    fiveidiotreaddb readdb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readdb = new fiveidiotreaddb(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.today_other_info, container, false);
        Map<String, Object> detail_map = readdb.getTodayDetailMapData();
        setViewData(rootView, detail_map);
        return rootView;
    }

    private void setViewData(View view, Map<String, Object> data_map) {
        ((TextView) view.findViewById(R.id.uv)).setText(data_map.get("uv").toString());
        ((TextView) view.findViewById(R.id.allergy)).setText(data_map.get("allergy").toString());
        ((TextView) view.findViewById(R.id.travel)).setText(data_map.get("travel").toString());
        ((TextView) view.findViewById(R.id.sucare)).setText(data_map.get("sucare").toString());
        ((TextView) view.findViewById(R.id.chenlian)).setText(data_map.get("chenlian").toString());
        ((TextView) view.findViewById(R.id.washcar)).setText(data_map.get("washcar").toString() + "℃");
        ((TextView) view.findViewById(R.id.index_d)).setText(data_map.get("index_d").toString() + "℃");

    }
}

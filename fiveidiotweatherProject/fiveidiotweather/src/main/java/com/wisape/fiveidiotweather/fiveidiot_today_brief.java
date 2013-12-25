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
    private fiveidiot_set_ui set_ui;
    private String mcity;

    public fiveidiot_today_brief(String city) {
        mcity = city;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        set_ui = new fiveidiot_set_ui(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.today_brief, container, false);
        set_ui.setTodayUi(rootView, mcity);
        return rootView;
    }
}

package com.wisape.fiveidiotweather.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wisape.fiveidiotweather.R;
import com.wisape.fiveidiotweather.core.fiveidiot_set_ui;

/**
 * Created by wisape on 13-11-17.
 */
public class fiveidiot_today_brief extends Fragment {
    private String mcity;

    public fiveidiot_today_brief(String city) {
        mcity = city;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.today_brief, container, false);
        fiveidiot_set_ui set_ui = new fiveidiot_set_ui(getActivity());
        set_ui.setTodayUi(rootView, mcity);
        return rootView;
    }
}

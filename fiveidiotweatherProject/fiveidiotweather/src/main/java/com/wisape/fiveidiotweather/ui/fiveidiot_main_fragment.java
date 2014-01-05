package com.wisape.fiveidiotweather.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wisape.fiveidiotweather.R;
import com.wisape.fiveidiotweather.core.fiveidiot_set_ui;

/**
 * Created by wisape on 13-11-17.
 */
public class fiveidiot_main_fragment extends Fragment {
    private String mcity;
//    private int backgroud_color;

    public fiveidiot_main_fragment(String city) {
        mcity = city;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fiveidiot_set_ui set_ui = new fiveidiot_set_ui(getActivity());
//        backgroud_color = set_ui.getMainColor();
        fiveidiot_today_brief tb = new fiveidiot_today_brief(mcity, set_ui);
        fiveidiot_after_brief ab = new fiveidiot_after_brief(mcity, set_ui);
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.today_brief, tb);
        ft.add(R.id.weather_other, ab);
        ft.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
//        rootView.setBackgroundColor(backgroud_color);
        return rootView;
    }

}

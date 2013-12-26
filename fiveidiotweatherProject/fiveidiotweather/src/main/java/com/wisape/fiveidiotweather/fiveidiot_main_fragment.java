package com.wisape.fiveidiotweather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wisape on 13-11-17.
 */
public class fiveidiot_main_fragment extends Fragment {
    private String mcity;

    public fiveidiot_main_fragment(String city) {
        mcity = city;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fiveidiot_today_brief tb = new fiveidiot_today_brief(mcity);
        fiveidiot_after_brief ab = new fiveidiot_after_brief(mcity);
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
        return rootView;
    }
}

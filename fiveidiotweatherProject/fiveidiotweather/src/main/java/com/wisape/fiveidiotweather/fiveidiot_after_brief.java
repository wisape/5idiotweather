package com.wisape.fiveidiotweather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wisape on 13-11-17.
 */
public class fiveidiot_after_brief extends Fragment {
    private String mcity;

    public fiveidiot_after_brief(String city) {
        mcity = city;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.after_brief, container, false);
        return rootView;
    }
}

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

    public fiveidiot_after_brief(String city) {
        mcity = city;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.after_brief, container, false);
        fiveidiot_set_ui set_ui = new fiveidiot_set_ui(getActivity());
        set_ui.setAfterUi(rootView, mcity);
        return rootView;
    }
}

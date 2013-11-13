package com.wisape.fiveidiotweather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by wisape on 13-11-5.
 */
public class SubFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sub_fragment, container, false);

        ((TextView) rootView.findViewById(R.id.sub_title)).setText("西安");
        return rootView;
    }
}

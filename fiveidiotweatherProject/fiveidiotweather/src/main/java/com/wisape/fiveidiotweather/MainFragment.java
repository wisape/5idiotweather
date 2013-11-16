package com.wisape.fiveidiotweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Created by wisape on 13-11-5.
 */

public class MainFragment extends ListFragment {
    SimpleAdapter adapter = null;
    OnMainChangeFragment mCallback;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new SimpleAdapter(getActivity(), getData(),
                R.layout.meta_fragment, new String[]{"image", "title", "detail"},
                new int[]{R.id.imageView, R.id.title, R.id.detail});
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);

        ((TextView) rootView.findViewById(R.id.main_title)).setText("西安");
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (OnMainChangeFragment) activity;
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        HashMap<String, Object> map = null;
        for (int i = 0; i < 5; i++) {
            map = new HashMap<String, Object>();
            map.put("image", R.drawable.ic_launcher);
            map.put("title", "今天");
            map.put("detail", "多云");
            list.add(map);
        }
        return list;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mCallback.onMainFragmentClicked(this, position);
    }


    public interface OnMainChangeFragment {
        public void onMainFragmentClicked(Fragment fragment, int position);
    }
}

package com.wisape.fiveidiotweather;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;

/**
 * Created by wisape on 13-11-24.
 */
public class fiveidiot_citys {
    private final static String PREFER_NAME = "com.wisape.5sha";
    private final static int MAX_CITYS = 5;
    private final static String NO_CITY = "5sha";
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private Context mContext;

    public fiveidiot_citys(Context context) {
        mContext = context;
        sharedPref = context.getSharedPreferences(
                PREFER_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public boolean set_city(String city) {
        if ((get_citys().size() >= MAX_CITYS) || (find_city(city) < 0)) {
            return false;
        }

        for (int i = 0; i < MAX_CITYS; i++) {
            String old_city = sharedPref.getString(mContext.getString(R.string.city_name) + i, NO_CITY);
            if (old_city.equals(NO_CITY))
                editor.putString(mContext.getString(R.string.city_name) + i, city);
        }

        editor.commit();
        return true;
    }

    public boolean delete_city(String city) {
        int index;
        if ((index = find_city(city)) >= 0) {
            editor.putString(mContext.getString(R.string.city_name) + index, NO_CITY);
            editor.commit();
            return true;
        }
        return false;
    }

    public ArrayList<String> get_citys() {
        ArrayList<String> citys = new ArrayList<String>();
        for (int i = 0; i < MAX_CITYS; i++) {
            String city = sharedPref.getString(mContext.getString(R.string.city_name) + i, "null");
            if (!city.equals(NO_CITY))
            citys.add(city);
        }
        return citys;
    }

    private int find_city(String city) {
        ArrayList<String> citys = get_citys();
        if (!citys.contains(city))
            return -1;
        return citys.indexOf(city);
    }
}

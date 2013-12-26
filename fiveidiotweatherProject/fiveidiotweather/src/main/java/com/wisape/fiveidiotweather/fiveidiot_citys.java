package com.wisape.fiveidiotweather;

import android.content.Context;
import java.util.ArrayList;

/**
 * Created by wisape on 13-11-24.
 */
public class fiveidiot_citys {
    private final static String CITYS_TABLE = "citys";
    private fiveidiotdb citydb;

    public fiveidiot_citys(Context context) {
        citydb = new fiveidiotdb(context);
        citydb.create_table(CITYS_TABLE);
    }

    public boolean set_city(String city, String cityid) {
        citydb.insert(CITYS_TABLE, city, cityid);
        return true;
    }

    public boolean delete_city(String city) {
        if (citydb.delete(CITYS_TABLE, city) > 0) {
            citydb.delete_table(city);
            return true;
        }
        return false;
    }

    public ArrayList<String> get_citys() {
        return citydb.getnames(CITYS_TABLE);
    }

    public String find_cityid(String city) {
            return citydb.getvalue(CITYS_TABLE, city);
    }
}

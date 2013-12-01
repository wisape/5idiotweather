package com.wisape.fiveidiotweather;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by wangjianmei on 13-12-1.
 */
public class fiveidiot_cityids_db {
    private final static String CITYS_DB = "./cityids.db";
    private final static String PROVINCES_TABLE = "provinces";
    private final static String PROVINCES_ID = "id";
    private final static String CITYS_PROVINCES_ID = "province id";
    private final static String CITYS_TABLE = "citys";
    private final static String CITYS_ID = "city num";
    private final static String NAME = "name";
    private SQLiteDatabase citydb;
    public fiveidiot_cityids_db(Context context) {
        citydb = SQLiteDatabase.openDatabase(CITYS_DB, null, SQLiteDatabase.OPEN_READONLY);
    }

    public ArrayList<String> getProvinces() {
        ArrayList<String> provinces = null;
        Cursor cursor = citydb.rawQuery("select * from " + PROVINCES_TABLE, null);
        while (cursor.moveToNext()) {
            provinces.add(cursor.getString(cursor.getColumnIndex(NAME)));
        }

        return provinces;
    }

    public ArrayList<String> getCitys(int province_id) {
        ArrayList<String> citys = null;

        return citys;
    }

    public String getCityid(String city) {
        String cityid = null;
        Cursor cursor = citydb.query(true, CITYS_TABLE, new String[]{CITYS_PROVINCES_ID, NAME, CITYS_ID},
                NAME + "==?", new String[]{city}, null, null, null, null);

        if (cursor.moveToNext()) {
            cityid = cursor.getString(cursor.getColumnIndex(CITYS_ID));
        }
        cursor.close();
        return cityid;
    }
}

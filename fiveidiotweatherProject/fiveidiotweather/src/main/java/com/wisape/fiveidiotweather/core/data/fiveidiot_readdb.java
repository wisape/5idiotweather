package com.wisape.fiveidiotweather.core.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wisape.fiveidiotweather.R;
import com.wisape.fiveidiotweather.core.fiveidiot_service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wisape on 13-11-10.
 */
public class fiveidiot_readdb {
    private SQLiteOpenHelper dbhelper = null;
    public String[] TODAY_PROPS = {"city", "todayupdatetime", "date", "nowtemp",  "wind", "humidity"};
//    public static final String[] DEF_PROPS = {"uv", "allergy", "suncure", "washcar", "chenlian", "travel", "dress", "dress_d"};
    public String[] SMP_PROPS = {"image", "image_n", "temp", "weather", "wind", "week"};
    private String DB_NAME = "fiveidiot";
    private String NAME = "name";
    private String VALUE = "value";
    List<Map<String, Object>> info_list = null;

    public fiveidiot_readdb(Context context) {
        dbhelper = new SQLiteOpenHelper(context, DB_NAME, null, 1) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {

            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

            }
        };
    }

    private String getvalue(SQLiteDatabase db, String table, String key) {
        String value = null;

        if (!has_table(db, table)) {
            return value;
        }
        Cursor cursor = db.query(table, null, NAME + " = ?", new String[]{key}, null, null, null);

        if (cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                value = cursor.getString(cursor.getColumnIndexOrThrow(VALUE));
            }
        }
        cursor.close();
        return value;
    }

    private boolean has_table(SQLiteDatabase db, String table) {
        Cursor cursor = db.rawQuery("select name from sqlite_master where type='table' order by name", null);
        while(cursor.moveToNext()) {
            if (table.equals(cursor.getString(0))) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }
//
//    public Map<String, Object> getTodayDetailMapData(String table) {
//        SQLiteDatabase db = dbhelper.getReadableDatabase();
//        HashMap<String, Object> map = new HashMap<String, Object>();
//
//        for (int i = 0; i < DEF_PROPS.length; i++) {
//            map.put(DEF_PROPS[i], getvalue(db, table, DEF_PROPS[i]));
//        }
//        db.close();
//        return map;
//    }

    private List<Map<String, Object>> getBriefAdapterData(String table) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        HashMap<String, Object> map = null;

        for (int i = 0; i < 6; i++) {
            map = new HashMap<String, Object>();
            for (int j = 0; j < SMP_PROPS.length; j++) {
                map.put(SMP_PROPS[j], getvalue(db, table, SMP_PROPS[j] + i));
            }
            list.add(map);
        }

        String date = fiveidiot_service.system_date();
        if (!date.equals(getvalue(db, table, "date"))) {
            list.remove(0);
        }
        db.close();
        return list;
    }

    public Map<String, Object> getTodayBriefMapData(String table) {
        if (info_list == null) {
            info_list = getBriefAdapterData(table);
        }
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String date = fiveidiot_service.system_date();

        Map<String, Object> map = info_list.get(0);
        for (int i = 0; i < TODAY_PROPS.length; i++) {
            if (TODAY_PROPS[i].equals("date")) {
                map.put("date", date);
                continue;
            }
            map.put(TODAY_PROPS[i], getvalue(db, table, TODAY_PROPS[i]));
        }

        db.close();
        return map;
    }

    public List<Map<String, Object>> getAfterBriefAdapterData(String table) {
        if (info_list == null) {
            info_list = getBriefAdapterData(table);
        }
        return info_list.subList(1, info_list.size());
    }

    public int getImageId(String image_title) {
        if (image_title.equals("1"))
            return 0;
        if(image_title.equals("晴"))
            return R.drawable.sun;
        else if(image_title.equals("阴"))
            return R.drawable.cloud;
        else if(image_title.equals("多云"))
            return R.drawable.cloud_sun;
        else if(image_title.equals("阵雨"))
            return R.drawable.rain_sun;
        else if(image_title.equals("雾") || image_title.equals("霾"))
            return R.drawable.fog;

        if (image_title.contains("雨"))
            return R.drawable.rain;
        if (image_title.contains("雪"))
            return R.drawable.snow;

        return R.drawable.dust;
    }
}

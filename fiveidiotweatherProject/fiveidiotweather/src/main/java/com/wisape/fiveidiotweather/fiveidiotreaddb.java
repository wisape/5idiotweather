package com.wisape.fiveidiotweather;

import android.content.Context;
import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wisape on 13-11-10.
 */
public class fiveidiotreaddb {
    private SQLiteOpenHelper dbhelper = null;
    public static final String[] TODAY_PROPS = {"city", "date","week0", "weather0", "nowtemp", "temp0", "wind", "humidity"};
    public static final String[] DEF_PROPS = {"uv", "allergy", "suncure", "washcar", "chenlian", "travel", "dress", "dress_d"};
    public static final String[] SMP_PROPS = {"image", "image_n", "temp", "weather", "wind", "week"};
    private static final String DB_NAME = "fiveidiot";
    private static final String NAME = "name";
    private static final String VALUE = "value";

    public fiveidiotreaddb(Context context) {
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

    private void delete_table(String table) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + table);
        db.close();
    }

    public Map<String, Object> getTodayDetailMapData(String table) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        HashMap<String, Object> map = new HashMap<String, Object>();

        for (int i = 0; i < DEF_PROPS.length; i++) {
            map.put(DEF_PROPS[i], getvalue(db, table, DEF_PROPS[i]));
        }
        db.close();
        return map;
    }

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
        db.close();
        return  list;
    }

    public Map<String, Object> getTodayBriefMapData(String table) {
        Map<String, Object> map = getBriefAdapterData(table).get(0);
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        for (int i = 0; i < TODAY_PROPS.length; i++) {
            map.put(TODAY_PROPS[i], getvalue(db, table, TODAY_PROPS[i]));
        }

        db.close();
        return map;
    }

    public List<Map<String, Object>> getAfterBriefAdapterData(String table) {
        List<Map<String, Object>> list = getBriefAdapterData(table);
        return list.subList(1, list.size());
    }
}

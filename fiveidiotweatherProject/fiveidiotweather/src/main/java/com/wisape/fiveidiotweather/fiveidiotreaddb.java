package com.wisape.fiveidiotweather;

import android.content.Context;
import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wisape on 13-11-10.
 */
public class fiveidiotreaddb {
    private SQLiteOpenHelper dbhelper = null;
    public static final String[] DEF_PROPS = {"city", "date", "week", "uv", "allergy", "suncure", "washcar", "chenlian", "travel", "dress", "dress_d"};
    public static final String[] SMP_PROPS = {"image", "image_n", "temp", "weather", "wind"};
    private static final String DB_NAME = "fiveidiot";
    private static final String TABLE_NAME = "weather";
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

        SQLiteDatabase db = dbhelper.getReadableDatabase();

        db.close();
    }

    private String getvalue(String key) {
        String value = null;
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, NAME + " = ?", new String[]{key}, null, null, null);

        if (cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                value = cursor.getString(cursor.getColumnIndexOrThrow(VALUE));
            }
        }
        cursor.close();
        db.close();

        return value;
    }

    Map<String, Object> getDetailAdapterData() {
        HashMap<String, Object> map = new HashMap<String, Object>();

        for (int i = 0; i < DEF_PROPS.length; i++) {
            map.put(DEF_PROPS[i], getvalue(DEF_PROPS[i]));
        }

        return map;
    }

    List<Map<String, Object>> getSimpleAdapterData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        HashMap<String, Object> map = null;
        for (int i = 0; i < 6; i++) {
            map = new HashMap<String, Object>();
            for (int j = 0; j < SMP_PROPS.length; j++) {
                map.put(SMP_PROPS[j], getvalue(SMP_PROPS[j] + i));
            }
            list.add(map);
        }
        return  list;
    }
}

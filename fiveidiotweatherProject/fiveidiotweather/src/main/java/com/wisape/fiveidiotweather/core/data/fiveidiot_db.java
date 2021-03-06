package com.wisape.fiveidiotweather.core.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wisape.fiveidiotweather.R;
import com.wisape.fiveidiotweather.core.fiveidiot_service;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by wisape on 13-10-27.
 */

public class fiveidiot_db {
    private SQLiteOpenHelper dbhelper = null;
    private String DB_NAME = "fiveidiot";
    private String NAME = "name";
    private String VALUE = "value";
//    private SQLiteDatabase db;
//    private Cursor cursor = null;
//    private ContentValues cv = null;

//    public String[] TODAY_PROPS = {"city", "todayupdatetime", "date", "nowtemp",  "wind", "humidity"};
//    public static final String[] DEF_PROPS = {"uv", "allergy", "suncure", "washcar", "chenlian", "travel", "dress", "dress_d"};
//    public String[] SMP_PROPS = {"image", "image_n", "temp", "weather", "wind", "week"};
//    List<Map<String, Object>> info_list = null;

    public fiveidiot_db(Context context) {
        dbhelper = new SQLiteOpenHelper(context.getApplicationContext(), DB_NAME, null, 1) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {
            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

            }
        };
//        ContentValues cv = new ContentValues();
    }

    public synchronized void create_table(String table) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.execSQL("create table if not exists "+ table +" ( "+ NAME +" TEXT, "+ VALUE +"  TEXT);");
        db.close();
    }

    public synchronized void insert(String table, String key, String vlaue) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = new WeakReference<Cursor>(db.query(table, null, NAME + " = ?", new String[]{key}, null, null, null)).get();
            if (cursor.getCount() > 0) {
                update(db, table, key, vlaue);
            } else {
                ContentValues cv = new ContentValues();
                cv.put(NAME, key);
                cv.put(VALUE, vlaue);
                db.insert(table, null, cv);
                cv.clear();
            }
        } finally {
            cursor.close();
        }

        db.close();
    }

    private synchronized int update(SQLiteDatabase db, String table, String key, String value) {
        ContentValues cv = new ContentValues();
        cv.put(VALUE, value);
        return db.update(table, cv,  NAME +" = ?", new String[]{key});
    }

    public synchronized ArrayList<String> getnames(String table) {
        ArrayList<String> names = new ArrayList<String>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = new WeakReference<Cursor>(db.rawQuery("select * from " + table, null)).get();
        try {
            while (cursor.moveToNext()) {
                names.add(cursor.getString(cursor.getColumnIndexOrThrow(NAME)));
            }
        } finally {
            cursor.close();
        }

        db.close();
        return names;
    }

    public synchronized int delete(String table, String key) {
        int rel;
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        rel = db.delete(table, NAME + " = ?", new String[]{key});
        db.close();
        return rel;
    }

    public void delete_table(String table) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + table);
        db.close();
    }

    public synchronized String getvalue(String table, String key) {
        String value = null;
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = new WeakReference<Cursor>(db.query(table, null, NAME + " = ?", new String[]{key}, null, null, null)).get();
            if (cursor.getCount() > 0) {
                for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                    value = cursor.getString(cursor.getColumnIndexOrThrow(VALUE));
                }
            }
        } finally {
              cursor.close();
        }
        db.close();

        return value;
    }

    private String getvalue(SQLiteDatabase db, String table, String key) {
        String value = null;

//        if (!has_table(db, table)) {
//            return value;
//        }
        Cursor cursor = null;
        try {
//            String querycmd = new StringBuilder().append("select * from ").append(table).append(" where ").append(NAME).append("=?").toString();
            cursor = new WeakReference<Cursor>(db.query(table, null, NAME + " = ?", new String[]{key}, null, null, null)).get();
//            cursor = db.rawQuery(querycmd, new String[]{key});

            if (cursor.getCount() > 0) {
                for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                    value = cursor.getString(cursor.getColumnIndexOrThrow(VALUE));
                }
            }
        } finally {
            cursor.close();
        }

        return value;
    }

    public boolean has_table(String table) {
        Boolean is_has_table = false;
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = new WeakReference<Cursor>(db.rawQuery("select name from sqlite_master where type='table' order by name", null)).get();
            while(cursor.moveToNext()) {
                if (table.equals(cursor.getString(0))) {
                    is_has_table = true;
                }
            }
        } finally {
            cursor.close();
        }

        db.close();
        return is_has_table;
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
        String[] SMP_PROPS = {"image", "image_n", "temp", "weather", "wind", "week"};
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        WeakReference<ArrayList> weaklist = new WeakReference<ArrayList>(new ArrayList<Map<String, Object>>());
        List<Map<String, Object>> list = weaklist.get();
        WeakHashMap<String, Object> map = null;

        for (int i = 0; i < 6; i++) {
            map = new WeakHashMap<String, Object>();
            for (int j = 0; j < SMP_PROPS.length; j++) {
                map.put(SMP_PROPS[j], getvalue(db, table, new StringBuffer(SMP_PROPS[j]).append(i).toString()));
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
        String[] TODAY_PROPS = {"city", "todayupdatetime", "date", "nowtemp",  "wind", "uv", "humidity", "pm2_5", "quality"};
        List<Map<String, Object>> info_list = getBriefAdapterData(table);
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
        List<Map<String, Object>> info_list = getBriefAdapterData(table);
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

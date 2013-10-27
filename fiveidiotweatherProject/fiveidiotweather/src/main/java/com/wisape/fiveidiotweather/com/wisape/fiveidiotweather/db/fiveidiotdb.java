package com.wisape.fiveidiotweather.com.wisape.fiveidiotweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by wisape on 13-10-27.
 */

public class fiveidiotdb {
    private SQLiteDatabase db = null;
    private static final String DB_NAME = "fiveidiot";
    public fiveidiotdb(Context context) {
        fiveidiotdbhelper dbhelper = new fiveidiotdbhelper(context, DB_NAME , null, 1);
        db = dbhelper.getWritableDatabase();
    }

    public void insert(String key, String vlaue) {
        ContentValues cv = new ContentValues();
        Cursor cursor = query(key);
        if (cursor.getCount() > 0)
            update(key, vlaue);
        else {
            cv.put(key, vlaue);
            db.insert(DB_NAME, null, cv);
        }
    }

    public int update(String key, String value) {
        ContentValues cv = new ContentValues();
        cv.put(key, value);
        return db.update(DB_NAME, cv, "name = ?", new String[]{key});
    }

    public int delete(String key) {
        return db.delete(DB_NAME, "name = ?", new String[]{key});
    }

    public Cursor query(String key) {
        return db.query(DB_NAME, null, "name = ?", new String[]{key}, null, null, null);
    }

    public String getvalue(String key) {
        Cursor cursor = query(key);

        if (cursor.getCount() > 0)
            return cursor.getString(0);
        else
            return null;
    }
}

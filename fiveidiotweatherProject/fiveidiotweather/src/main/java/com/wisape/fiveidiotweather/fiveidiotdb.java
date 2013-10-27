package com.wisape.fiveidiotweather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
        Log.d("5sha", "insert1");
        ContentValues cv = new ContentValues();
        Log.d("5sha", "insert1");
//        Cursor cursor = query(key);
//        Log.d("5sha", "insert3");
//        if (cursor.getCount() > 0) {
//            Log.d("5sha", "insert4");
//            update(key, vlaue);
//        } else {
            Log.d("5sha", "insert5");
            cv.put(key, vlaue);
            db.insert(DB_NAME, null, cv);
        Log.d("5sha", "insert6");
       // }
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

    /**
     * Created by wisape on 13-10-27.
     */

    public static class fiveidiotdbhelper extends SQLiteOpenHelper {
        public fiveidiotdbhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

        }
    }
}

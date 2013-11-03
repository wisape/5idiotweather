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
    private SQLiteDatabase db_r = null;
    private static final String DB_NAME = "fiveidiot";
    private static final String TABLE_NAME = "weather";
    private static final String NAME = "name";
    private static final String VALUE = "value";
    public fiveidiotdb(Context context ) {
        fiveidiotdbhelper dbhelper = new fiveidiotdbhelper(context, DB_NAME , null, 1);
        db = dbhelper.getWritableDatabase();
        db_r = dbhelper.getReadableDatabase();
        db.execSQL("create table if not exists "+ TABLE_NAME +" ( "+ NAME +" TEXT, "+ VALUE +"  TEXT);");
    }

    public void insert(String key, String vlaue) {
        ContentValues cv = new ContentValues();
        Cursor cursor = query(key);
        if (cursor.getCount() > 0) {
            update(key, vlaue);
        } else {
            cv.put(NAME, key);
            cv.put(VALUE, vlaue);
            db.insert(TABLE_NAME, null, cv);
        }
    }

    public int update(String key, String value) {
        ContentValues cv = new ContentValues();
        cv.put(VALUE, value);
        return db.update(TABLE_NAME, cv,  NAME +" = ?", new String[]{key});
    }

    public int delete(String key) {
        return db.delete(TABLE_NAME, NAME +" = ?", new String[]{key});
    }

    public Cursor query(String key) {
        return db.query(TABLE_NAME, null,  NAME +" = ?", new String[]{key}, null, null, null);
    }

    public String getvalue(String key) {
        String tvalue = null;
        Cursor cursor = query(key);

        if (cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                tvalue = cursor.getString(cursor.getColumnIndexOrThrow(VALUE));
            }
        }
        cursor.close();
        return tvalue;
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

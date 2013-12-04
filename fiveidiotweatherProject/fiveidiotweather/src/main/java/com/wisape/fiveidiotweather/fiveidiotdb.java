package com.wisape.fiveidiotweather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by wisape on 13-10-27.
 */

public class fiveidiotdb {
    private SQLiteOpenHelper dbhelper = null;
    private static final String DB_NAME = "fiveidiot";
    private static final String NAME = "name";
    private static final String VALUE = "value";

    public fiveidiotdb(Context context) {
        dbhelper = new SQLiteOpenHelper(context, DB_NAME, null, 1) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {
            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

            }
        };
    }

    public synchronized void create_table(String table) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.execSQL("create table if not exists "+ table +" ( "+ NAME +" TEXT, "+ VALUE +"  TEXT);");
        db.close();
    }

    public synchronized void insert(String table, String key, String vlaue) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = db.query(table, null, NAME + " = ?", new String[]{key}, null, null, null);

        if (cursor.getCount() > 0) {
            update(db, table, key, vlaue);
        } else {
            cv.put(NAME, key);
            cv.put(VALUE, vlaue);
            db.insert(table, null, cv);
        }
        db.close();
    }

    public synchronized int update(SQLiteDatabase db, String table, String key, String value) {
        ContentValues cv = new ContentValues();
        cv.put(VALUE, value);
        return db.update(table, cv,  NAME +" = ?", new String[]{key});
    }

    public synchronized ArrayList<String> getnames(String table) {
        ArrayList<String> names = new ArrayList<String>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + table, null);
        while (cursor.moveToNext()) {
            names.add(cursor.getString(cursor.getColumnIndexOrThrow(NAME)));
        }
        cursor.close();
        db.close();
        return names;
    }

    public synchronized int delete(String table, String key) {
        int rel;
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        rel = db.delete(table, NAME + " = ?", new String[]{key});
        return rel;
    }

    public synchronized String getvalue(String table, String key) {
        String value = null;
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        Cursor cursor = db.query(table, null, NAME + " = ?", new String[]{key}, null, null, null);

        if (cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                value = cursor.getString(cursor.getColumnIndexOrThrow(VALUE));
            }
        }
        cursor.close();
        db.close();

        return value;
    }
}

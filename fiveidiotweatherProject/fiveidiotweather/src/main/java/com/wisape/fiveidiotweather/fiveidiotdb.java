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
    private SQLiteOpenHelper dbhelper = null;
    private static final String DB_NAME = "fiveidiot";
    private static final String TABLE_NAME = "weather";
    private static final String NAME = "name";
    private static final String VALUE = "value";
    public fiveidiotdb(Context context ) {
        dbhelper = new SQLiteOpenHelper(context, DB_NAME, null, 1) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {

            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

            }
        };

        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.execSQL("create table if not exists "+ TABLE_NAME +" ( "+ NAME +" TEXT, "+ VALUE +"  TEXT);");
        db.close();
    }

    public void insert(String key, String vlaue) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, NAME + " = ?", new String[]{key}, null, null, null);

        if (cursor.getCount() > 0) {
            update(db, key, vlaue);
        } else {
            cv.put(NAME, key);
            cv.put(VALUE, vlaue);
            db.insert(TABLE_NAME, null, cv);
        }
        db.close();
    }

    public int update(SQLiteDatabase db, String key, String value) {
        ContentValues cv = new ContentValues();
        cv.put(VALUE, value);
        return db.update(TABLE_NAME, cv,  NAME +" = ?", new String[]{key});
    }

    public int delete(String key) {
        int rel;
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        rel = db.delete(TABLE_NAME, NAME + " = ?", new String[]{key});
        return rel;
    }

    public String getvalue(String key) {
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
}

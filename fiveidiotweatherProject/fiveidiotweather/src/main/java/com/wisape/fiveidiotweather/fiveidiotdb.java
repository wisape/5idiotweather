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
    private static final String NAME = "name";
    private static final String VALUE = "value";
    public fiveidiotdb(Context context) {
        fiveidiotdbhelper dbhelper = new fiveidiotdbhelper(context, DB_NAME , null, 1);
        db = dbhelper.getWritableDatabase();
    }

    public void insert(String key, String vlaue) {
        ContentValues cv = new ContentValues();
//        Cursor cursor = query(key);
//        if (cursor.getCount() > 0) {
//            update(key, vlaue);
//        } else {
            cv.put(NAME, key);
            cv.put(VALUE, vlaue);
            db.insert(DB_NAME, null, cv);
//        }
//        cursor.close();
    }

    public int update(String key, String value) {
        ContentValues cv = new ContentValues();
        cv.put(VALUE, value);
        return db.update(DB_NAME, cv,  NAME +" = ?", new String[]{key});
    }

    public int delete(String key) {
        return db.delete(DB_NAME, NAME +" = ?", new String[]{key});
    }

    public Cursor query(String key) {
        return db.query(DB_NAME, null,  NAME +" = ?", new String[]{key}, null, null, null);
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
            String sql = "create table "+ DB_NAME +" ( "+ NAME +" , "+ VALUE +"  );";
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

        }
    }
}

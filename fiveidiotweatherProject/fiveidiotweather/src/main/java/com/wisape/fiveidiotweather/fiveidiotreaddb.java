package com.wisape.fiveidiotweather;

import android.content.Context;
import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wisape on 13-11-10.
 */
public class fiveidiotreaddb {
    private SQLiteOpenHelper dbhelper = null;
    public static final String[] DEF_PROPS = {"city", "date", "week","temp", "weather", "image", "wind"};
    public static final String[] SMP_PROPS = {"image", "temp", "weather", "wind"};
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

    List<Map<String, Object>> getAdapterData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        return  list;
    }
}

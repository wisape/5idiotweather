package com.wisape.fiveidiotweather;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by wangjianmei on 13-12-1.
 */
public class fiveidiot_cityids_db {
    private final static String DB_PATH = "/data/data/com.wisape.fiveidiotweather/databases/";
    private final static String DB_NAME = "cityids";
    private final static String PROVINCES_TABLE = "provinces";
    private final static String PROVINCES_ID = "id";
    private final static String CITYS_PROVINCES_ID = "province_id";
    private final static String CITYS_TABLE = "citys";
    private final static String CITYS_ID = "city_num";
    private final static String NAME = "name";
    private SQLiteOpenHelper dbhelper;
    private SQLiteDatabase citydb;
    public fiveidiot_cityids_db(Context context) {
        try {
            copyCityDb(context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        dbhelper = new SQLiteOpenHelper(context, DB_NAME, null, 1) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {

            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

            }
        };

        citydb = dbhelper.getReadableDatabase();
        citydb.close();
    }

    private void copyCityDb(Context context) throws IOException {
        String databaseFilename = DB_PATH + DB_NAME;
        File dir = new File(DB_PATH);
        if (!dir.exists())
            dir.mkdir();
        if (!(new File(databaseFilename)).exists()) {
            InputStream is = context.getResources().openRawResource(R.raw.cityids);
            FileOutputStream fos = new FileOutputStream(databaseFilename);
            byte[] buffer = new byte[8192];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            fos.close();
            is.close();
        }
    }

    public ArrayList<String> getProvinces() {
        ArrayList<String> provinces = new ArrayList<String>();
        citydb = dbhelper.getReadableDatabase();
        Cursor cursor = citydb.rawQuery("select * from " + PROVINCES_TABLE, null);
        while (cursor.moveToNext()) {
            provinces.add(cursor.getString(cursor.getColumnIndexOrThrow(NAME)));
        }
        cursor.close();
        citydb.close();
        return provinces;
    }

    public ArrayList<String> getCitys(int province_id) {
        ArrayList<String> citys = new ArrayList<String>();
        citydb = dbhelper.getReadableDatabase();
        Cursor cursor = citydb.rawQuery("select * from " + CITYS_TABLE + " where "
                + CITYS_PROVINCES_ID + " = ?", new String[]{String.valueOf(province_id)});
        while (cursor.moveToNext()) {
            citys.add(cursor.getString(cursor.getColumnIndexOrThrow(NAME)));
        }
        cursor.close();
        citydb.close();
        return citys;
    }

    public String getCityid(String city) {
        String cityid = null;
        citydb = dbhelper.getReadableDatabase();
        Cursor cursor = citydb.rawQuery("select * from " + CITYS_TABLE + " where "
                + NAME + " = ?", new String[]{city});
        if (cursor.moveToNext()) {
            cityid = cursor.getString(cursor.getColumnIndex(CITYS_ID));
        }
        cursor.close();
        citydb.close();
        return cityid;
    }
}

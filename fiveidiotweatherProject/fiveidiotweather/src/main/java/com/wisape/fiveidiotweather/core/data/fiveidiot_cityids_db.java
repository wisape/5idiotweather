package com.wisape.fiveidiotweather.core.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wisape.fiveidiotweather.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by wisape on 13-12-1.
 */
public class fiveidiot_cityids_db {
    private String DB_PATH = "/data/data/com.wisape.fiveidiotweather/databases/";
    private String DB_NAME = "cityids";
    private String PROVINCES_TABLE = "provinces";
    private String CITYS_PROVINCES_ID = "province_id";
    private String CITYS_TABLE = "citys";
    private String CITYS_ID = "city_num";
    private String NAME = "name";
    private SQLiteOpenHelper dbhelper;

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
        SQLiteDatabase citydb = dbhelper.getReadableDatabase();
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
        SQLiteDatabase citydb = dbhelper.getReadableDatabase();
        Cursor cursor = citydb.rawQuery("select * from " + CITYS_TABLE + " where "
                + CITYS_PROVINCES_ID + " = ?", new String[]{String.valueOf(province_id)});
        while (cursor.moveToNext()) {
            citys.add(cursor.getString(cursor.getColumnIndexOrThrow(NAME)));
        }
        cursor.close();
        citydb.close();
        return citys;
    }


    public ArrayList<String> findCitys(String part) {
        ArrayList<String> citys = new ArrayList<String>();
        SQLiteDatabase citydb = dbhelper.getReadableDatabase();
        Cursor cursor = citydb.rawQuery("select * from " + CITYS_TABLE, null);
        while (cursor.moveToNext()) {
            String city = cursor.getString(cursor.getColumnIndexOrThrow(NAME));
            if (city.contains(part)) {
                citys.add(city);
            }
        }
        cursor.close();
        citydb.close();
        return citys;
    }

    public String getCityid(String city) {
        String cityid = null;
        SQLiteDatabase citydb = dbhelper.getReadableDatabase();
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

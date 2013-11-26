package com.wisape.fiveidiotweather;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by wisape on 13-9-21.
 */
public class fiveidiotservice extends Service {
    private final static String per_address = "http://m.weather.com.cn/data/";
    private final static String suf_address = ".html";
    private fiveidiot_citys mCitys;
    private fiveidiotdb db = null;
    private ArrayList<String> city_ids;

    public IBinder onBind(Intent intent) {
        return new fiBinder();
    }

    public class fiBinder extends Binder {
        fiveidiotservice getService() {
            return fiveidiotservice.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db = new fiveidiotdb(this);
        mCitys = new fiveidiot_citys(getApplicationContext());
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                set_city_ids();
                while (true) {
                    for (i = 0; i < city_ids.size(); i++) {
                        try {
                            Log.d("5sha", "in service");
                            unwrap_save_data(city_ids.get(i));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(6000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    private ArrayList<String> get_city_ids(ArrayList<String> citys) {
        ArrayList<String> m_city_ids = new ArrayList<String>();
        m_city_ids.add("101010100");
        m_city_ids.add("101180201");
        return m_city_ids;
    }

    public void set_city_ids() {
        city_ids = get_city_ids(mCitys.get_citys());
    }

    public void update_service() {
        set_city_ids();

        for (int i = 0; i < city_ids.size(); i++) {
            try {
                unwrap_save_data(city_ids.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void unwrap_save_data(String city_code) throws IOException {
        String weather_path = per_address + city_code + suf_address;
        fiveidiotnet net = new fiveidiotnet(weather_path);
        fiveidiotanalyze analyzer = new fiveidiotanalyze(net.getContext());
        String city = analyzer.get_city();

        db.create_table(city);

        db.insert(city, "city", city);
        db.insert(city, "date", analyzer.get_date());
        db.insert(city, "dress", analyzer.get_dress()[0]);
        db.insert(city, "dress_d", analyzer.get_dress()[1]);
        db.insert(city, "chenlian", analyzer.get_cl());
        db.insert(city, "washcar", analyzer.get_xc());
        db.insert(city, "suncure", analyzer.get_ls());
        db.insert(city, "uv", analyzer.get_uv());
        db.insert(city, "travel", analyzer.get_tr());
        db.insert(city, "allergy", analyzer.get_gm());
        db.insert(city, "nowtemp", analyzer.get_nowtemp());
        String[] weather = analyzer.get_weathers();
        String[] temp = analyzer.get_temps();
        String[] wind = analyzer.get_winds();
        String[] image = analyzer.get_images();
        String[] weeks = analyzer.get_week();
        for (int i = 0; i < weather.length; i++) {
            db.insert(city, "week" + i, weeks[i]);
            db.insert(city, "weather" + i, weather[i]);
            db.insert(city, "temp" + i, temp[i]);
            db.insert(city, "wind" + i, wind[i]);
            db.insert(city, "image" + i, image[i * 2]);
            db.insert(city, "image_n" + i, image[i * 2 + 1]);
        }
    }

}

package com.wisape.fiveidiotweather.core;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;

import com.wisape.fiveidiotweather.core.data.fiveidiot_analyze;
import com.wisape.fiveidiotweather.core.data.fiveidiot_db;
import com.wisape.fiveidiotweather.core.data.fiveidiot_today_analyze;
import com.wisape.fiveidiotweather.fiveidiot;
import com.wisape.fiveidiotweather.core.data.fiveidiot_citys;
import com.wisape.fiveidiotweather.net.fiveidiot_net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by wisape on 13-9-21.
 */
public class fiveidiot_service extends Service {
    private String per_address = "http://m.weather.com.cn/data/";
    private String today_per_address = "http://www.weather.com.cn/data/sk/";
    private String suf_address = ".html";
    private fiveidiot_net_receiver net_receiver;

    public IBinder onBind(Intent intent) {
        return new fiBinder();
    }

    public class fiBinder extends Binder {
        public fiveidiot_service getService() {
            return fiveidiot_service.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        net_receiver = new fiveidiot_net_receiver();
        IntentFilter net_filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        get_weather_info_thread thread = new get_weather_info_thread();
        this.registerReceiver(net_receiver, net_filter);
        thread.start();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(net_receiver);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private class get_weather_info_thread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(900000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fiveidiot_citys mCitys = new fiveidiot_citys(getApplicationContext());
                ArrayList<String> citys = mCitys.get_citys();
                for (int i = 0; i < citys.size(); i++) {
                    try {
                        String city = citys.get(i);
                        manage_data(city, mCitys.find_cityid(city));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void update_service() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                fiveidiot_citys mCitys = new fiveidiot_citys(getApplicationContext());
                ArrayList<String> citys = mCitys.get_citys();
                for (int i = 0; i < citys.size(); i++) {
                    try {
                        String city = citys.get(i);
                        manage_data(city, mCitys.find_cityid(city));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private synchronized void manage_data(String city, String city_id) throws IOException {
        boolean today, other;
        if (!net_available())
            return;
        today = unwrap_save_now_data(city, city_id);
        other = unwrap_save_data(city, city_id);
        if (today || other) {
            Intent it = new Intent(fiveidiot.BROADCAST_UPDATE_UI);
            Intent widget_intent = new Intent(fiveidiot_set_ui.WIDGET_UPDATE);
            sendBroadcast(it);
            sendBroadcast(widget_intent);
        }
    }

    private synchronized boolean unwrap_save_data(String city, String city_code) throws IOException {
        if (city_code == null)
            return false;

        String weather_path = per_address + city_code + suf_address;
        fiveidiot_net net = new fiveidiot_net(weather_path);
        fiveidiot_analyze analyzer = new fiveidiot_analyze(net.getContext());
        fiveidiot_db db = new fiveidiot_db(this);
        String date = analyzer.get_date();
        String system_date = system_date();

        db.create_table(city);

        String saved_date = db.getvalue(city, "date");
        String saved_update_time = db.getvalue(city, "updatetime");
        if (saved_date != null && saved_update_time != null) {
            if (saved_date.equals(date) && saved_update_time.equals(analyzer.get_update_time())) {
                return false;
            }
        }

        db.insert(city, "date", system_date);
        db.insert(city, "dress", analyzer.get_dress()[0]);
        db.insert(city, "dress_d", analyzer.get_dress()[1]);
//        db.insert(city, "chenlian", analyzer.get_cl());
//        db.insert(city, "washcar", analyzer.get_xc());
//        db.insert(city, "suncure", analyzer.get_ls());
        db.insert(city, "uv", analyzer.get_uv());
//        db.insert(city, "travel", analyzer.get_tr());
//        db.insert(city, "allergy", analyzer.get_gm());
        db.insert(city, "updatetime", analyzer.get_update_time());
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
        return true;
    }

    private synchronized boolean unwrap_save_now_data(String city, String city_code) throws IOException {
        if (city_code == null)
            return false;
        String today_weather_path = today_per_address + city_code + suf_address;
        fiveidiot_net net = new fiveidiot_net(today_weather_path);
        fiveidiot_today_analyze analyzer = new fiveidiot_today_analyze(net.getContext());
        fiveidiot_db db = new fiveidiot_db(this);

        db.create_table(city);

        String saved_update_time = db.getvalue(city, "todayupdatetime");
        if (saved_update_time != null) {
            if (saved_update_time.equals(analyzer.get_update_time())) {
                return false;
            }
        }

        db.insert(city, "city", analyzer.get_city());
        db.insert(city, "nowtemp", analyzer.get_now_temp());
        db.insert(city, "todayupdatetime", analyzer.get_update_time());
        db.insert(city, "wind", analyzer.get_wind());
        db.insert(city, "humidity", analyzer.get_hum());

        return true;
    }

    public static String system_date() {
        Calendar calendar = Calendar.getInstance();
        int myear, mmonth, mday;
        myear = calendar.get(Calendar.YEAR);
        mmonth = calendar.get(Calendar.MONTH) + 1;
        mday = calendar.get(Calendar.DATE);
        return myear + "年" + mmonth + "月" + mday + "日";
    }

    public boolean net_available() {
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            return true;
        }
        return false;
    }

    private class fiveidiot_net_receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (net_available()) {
                update_service();
            }
        }
    }

}

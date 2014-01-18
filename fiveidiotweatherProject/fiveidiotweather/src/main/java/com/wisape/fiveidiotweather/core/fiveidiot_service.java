package com.wisape.fiveidiotweather.core;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

import com.wisape.fiveidiotweather.core.data.fiveidiot_analyze;
import com.wisape.fiveidiotweather.core.data.fiveidiot_db;
import com.wisape.fiveidiotweather.fiveidiot;
import com.wisape.fiveidiotweather.core.data.fiveidiot_citys;
import com.wisape.fiveidiotweather.net.fiveidiot_net;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by wisape on 13-9-21.
 */
public class fiveidiot_service extends Service {
    private String per_address = "http://m.weather.com.cn/data/";
    private String today_per_address = "http://www.weather.com.cn/data/sk/";
    private String pollution_address = "http://5idiot.duapp.com/weather/";
    private String suf_address = ".html";
    private fiveidiot_net_receiver net_receiver;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        net_receiver = new fiveidiot_net_receiver();
        IntentFilter net_filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(net_receiver, net_filter);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(net_receiver);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        update_service();
        return super.onStartCommand(intent, flags, startId);
    }

    public void update_service() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!net_available()) {
                    System.gc();
                    return;
                }
                fiveidiot_citys mCitys = new WeakReference<fiveidiot_citys>(new fiveidiot_citys(getApplicationContext())).get();
                ArrayList<String> citys = mCitys.get_citys();
                fiveidiot_analyze analyzer = new WeakReference<fiveidiot_analyze>(new fiveidiot_analyze()).get();
                fiveidiot_db db = new WeakReference<fiveidiot_db>(new fiveidiot_db(getApplicationContext())).get();
                String city;
                for (int i = 0; i < citys.size(); i++) {
                    try {
                        city = citys.get(i);
                        manage_data(db, analyzer, city, mCitys.find_cityid(city));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private synchronized void manage_data(fiveidiot_db db, fiveidiot_analyze analyzer, String city, String city_id) throws IOException {
        boolean today, other;
        today = unwrap_save_now_data(db, analyzer, city, city_id);
        other = unwrap_save_data(db, analyzer, city, city_id);
        if (today || other) {
            Intent it = new Intent(fiveidiot.BROADCAST_UPDATE_UI);
            Intent widget_intent = new Intent(fiveidiot_set_ui.WIDGET_UPDATE);
            sendBroadcast(it);
            sendBroadcast(widget_intent);
        }
        analyzer.clear();
        System.gc();
    }

    private synchronized boolean unwrap_save_data(fiveidiot_db db, fiveidiot_analyze analyzer, String city, String city_code) throws IOException {
        if (city_code == null)
            return false;

        String weather_path = new StringBuffer(per_address).append(city_code).append(suf_address).toString();
        fiveidiot_net net = new WeakReference<fiveidiot_net>(new fiveidiot_net(weather_path)).get();
        analyzer.init_brief_info(net.getContext());
        String date = analyzer.get_date();
        String system_date = system_date();

        db.create_table(city);

        String saved_date = db.getvalue(city, "date");
        String saved_update_time = db.getvalue(city, "updatetime");
        if (saved_date != null && saved_update_time != null) {
            if (system_date.equals(date) && saved_update_time.equals(analyzer.get_brief_update_time())) {
                return false;
            }
        }

        db.insert(city, "date", date);
//        db.insert(city, "dress", analyzer.get_dress()[0]);
//        db.insert(city, "dress_d", analyzer.get_dress()[1]);
//        db.insert(city, "chenlian", analyzer.get_cl());
//        db.insert(city, "washcar", analyzer.get_xc());
//        db.insert(city, "suncure", analyzer.get_ls());
        db.insert(city, "uv", analyzer.get_uv());
//        db.insert(city, "travel", analyzer.get_tr());
//        db.insert(city, "allergy", analyzer.get_gm());
        db.insert(city, "updatetime", analyzer.get_brief_update_time());
        List<String> weather = analyzer.get_weathers();
        List<String> temp = analyzer.get_temps();
        List<String> wind = analyzer.get_winds();
        List<String> image = analyzer.get_images();
        List<String> weeks = analyzer.get_week();

        for (int i = 0; i < weather.size(); i++) {
            db.insert(city, new StringBuffer("week").append(i).toString(), weeks.get(i));
            db.insert(city, new StringBuffer("weather").append(i).toString(), weather.get(i));
            db.insert(city, new StringBuffer("temp").append(i).toString(), temp.get(i));
            db.insert(city, new StringBuffer("wind").append(i).toString(), wind.get(i));
            db.insert(city, new StringBuffer("image").append(i).toString(), image.get(i));
        }
        return true;
    }

    private synchronized boolean unwrap_save_now_data(fiveidiot_db db, fiveidiot_analyze analyzer, String city, String city_code) throws IOException {
        if (city_code == null)
            return false;
        String today_weather_path = new StringBuffer(today_per_address ).append(city_code).append(suf_address).toString();
        fiveidiot_net net = new WeakReference<fiveidiot_net>(new fiveidiot_net(today_weather_path)).get();
        analyzer.init_today_info(net.getContext());

        db.create_table(city);

        String saved_update_time = db.getvalue(city, "todayupdatetime");
        if (saved_update_time != null) {
            if (saved_update_time.equals(analyzer.get_today_update_time())) {
                return false;
            }
        }
        String subcity = analyzer.get_city();
        db.insert(city, "city", subcity);
        db.insert(city, "nowtemp", analyzer.get_now_temp());
        db.insert(city, "todayupdatetime", analyzer.get_today_update_time());
        db.insert(city, "wind", analyzer.get_today_wind());
        db.insert(city, "humidity", analyzer.get_today_hum());

        unwrap_save_pollution_data(db, analyzer, city, city.replace(subcity, ""));

        return true;
    }

    private synchronized boolean unwrap_save_pollution_data(fiveidiot_db db, fiveidiot_analyze analyzer, String city, String supercity) throws IOException {
        if (city == null)
            return false;
        String pollution_path = new StringBuffer(pollution_address).append(URLEncoder.encode(city, "UTF-8")).toString();
        fiveidiot_net net = new WeakReference<fiveidiot_net>(new fiveidiot_net(pollution_path)).get();
        String content = net.getContext();
        if (content.equals(city)) {
            if (supercity.equals("")) {
                return false;
            }
            pollution_path = new StringBuffer(pollution_address).append(URLEncoder.encode(supercity, "UTF-8")).toString();
            net = new fiveidiot_net(pollution_path);
            content = net.getContext();
            if (content.equals(supercity))
                return false;
        }
        analyzer.init_pollute_info(content);

        db.create_table(city);

        db.insert(city, "pm2_5", new StringBuffer(analyzer.get_pm2_5()).append("  PM2.5").toString());
        db.insert(city, "quality", analyzer.get_quality());

        return true;
    }

    public static String system_date() {
        Calendar calendar = Calendar.getInstance();
        int myear, mmonth, mday;
        myear = calendar.get(Calendar.YEAR);
        mmonth = calendar.get(Calendar.MONTH) + 1;
        mday = calendar.get(Calendar.DATE);
        return new StringBuffer().append(myear).append("年").append(mmonth).append("月").append(mday ).append("日").toString();
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

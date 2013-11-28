package com.wisape.fiveidiotweather;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by wisape on 13-9-21.
 */
public class fiveidiotservice extends Service {
    private final static String per_address = "http://m.weather.com.cn/data/";
    private final static String today_per_address = "http://www.weather.com.cn/data/sk/";
    private final static String suf_address = ".html";
    private fiveidiot_net_receiver net_receiver;
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
        net_receiver = new fiveidiot_net_receiver();
        IntentFilter net_filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        mCitys = new fiveidiot_citys(getApplicationContext());
        get_weather_info_thread thread = new get_weather_info_thread();
        this.registerReceiver(net_receiver, net_filter);
        thread.start();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private class get_weather_info_thread extends Thread {
        @Override
        public void run() {
            int i = 0;
            String id = null;
            set_city_ids();
            while (true) {
                for (i = 0; i < city_ids.size(); i++) {
                    try {
                        id = city_ids.get(i);
                        manage_data(id);
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
    }

    private synchronized ArrayList<String> get_city_ids(ArrayList<String> citys) {
        ArrayList<String> m_city_ids = new ArrayList<String>();
        m_city_ids.add("101010100");
        m_city_ids.add("101180201");
        return m_city_ids;
    }

    public synchronized void set_city_ids() {
        city_ids = get_city_ids(mCitys.get_citys());
    }

    public void update_service() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                String id = null;
                set_city_ids();
                for (i = 0; i < city_ids.size(); i++) {
                    try {
                        id = city_ids.get(i);
                        manage_data(id);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private synchronized void manage_data(String id) throws IOException {
        boolean today, other;
        today = unwrap_save_now_data(id);
        other = unwrap_save_data(id);
        if (today || other) {
            Intent it = new Intent(fiveidiot.BROADCAST_UPDATE_UI);
            sendBroadcast(it);
        }
    }

    private synchronized boolean unwrap_save_data(String city_code) throws IOException {
        if (city_code == null)
            return false;

        int gap_date = 0;
        String weather_path = per_address + city_code + suf_address;
        fiveidiotnet net = new fiveidiotnet(weather_path);
        fiveidiotanalyze analyzer = new fiveidiotanalyze(net.getContext());
        String city = analyzer.get_city();
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
        if (!date.equals(system_date())) {
            gap_date = 1;
        }
        db.insert(city, "date", system_date);
        db.insert(city, "dress", analyzer.get_dress()[0]);
        db.insert(city, "dress_d", analyzer.get_dress()[1]);
        db.insert(city, "chenlian", analyzer.get_cl());
        db.insert(city, "washcar", analyzer.get_xc());
        db.insert(city, "suncure", analyzer.get_ls());
        db.insert(city, "uv", analyzer.get_uv());
        db.insert(city, "travel", analyzer.get_tr());
        db.insert(city, "allergy", analyzer.get_gm());
        db.insert(city, "updatetime", analyzer.get_update_time());
        String[] weather = analyzer.get_weathers();
        String[] temp = analyzer.get_temps();
        String[] wind = analyzer.get_winds();
        String[] image = analyzer.get_images();
        String[] weeks = analyzer.get_week();

        for (int i = 0; i < weather.length - gap_date; i++) {
            int j = i + gap_date;
            db.insert(city, "week" + i, weeks[j]);
            db.insert(city, "weather" + i, weather[j]);
            db.insert(city, "temp" + i, temp[j]);
            db.insert(city, "wind" + i, wind[j]);
            db.insert(city, "image" + i, image[j * 2]);
            db.insert(city, "image_n" + i, image[j * 2 + 1]);
        }
        return true;
    }

    private synchronized boolean unwrap_save_now_data(String city_code) throws IOException {
        if (city_code == null)
            return false;
        String today_weather_path = today_per_address + city_code + suf_address;
        fiveidiotnet net = new fiveidiotnet(today_weather_path);
        fiveidiot_today_analyze analyzer = new fiveidiot_today_analyze(net.getContext());
        String city = analyzer.get_city();

        db.create_table(city);

        String saved_update_time = db.getvalue(city, "todayupdatetime");
        if (saved_update_time != null) {
            if (saved_update_time.equals(analyzer.get_update_time())) {
                return false;
            }
        }

        db.insert(city, "city", city);
        db.insert(city, "nowtemp", analyzer.get_now_temp());
        db.insert(city, "todayupdatetime", analyzer.get_update_time());
        db.insert(city, "wind", analyzer.get_wind());
        db.insert(city, "humidity", analyzer.get_hum());

        return true;
    }


    private String system_date() {
        Calendar calendar = Calendar.getInstance();
        int myear, mmonth, mday;
        myear = calendar.get(Calendar.YEAR);
        mmonth = calendar.get(Calendar.MONTH);
        mday = calendar.get(Calendar.DATE);
        return myear + "年" + mmonth + "月" + mday + "日";
    }

    private class fiveidiot_net_receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null && info.isAvailable()) {
                update_service();
            }
        }
    }

}

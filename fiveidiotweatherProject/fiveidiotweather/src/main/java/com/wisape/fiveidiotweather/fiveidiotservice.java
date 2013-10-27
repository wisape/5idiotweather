package com.wisape.fiveidiotweather;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by wisape on 13-9-21.
 */
public class fiveidiotservice extends Service {

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final fiveidiotdb db = new fiveidiotdb(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String wt_content = null;
                while (true) {
                    fiveidiotnet net = new fiveidiotnet("http://m.weather.com.cn/data/101110101.html");
                    try {
                        wt_content = net.getContext();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (wt_content != null) {
                        fiveidiotanalyze wt_an = new fiveidiotanalyze(wt_content);
                        db.insert("city", wt_an.get_city());
                        db.insert("data", wt_an.get_date());
                        String[] weather = wt_an.get_weathers();
                        db.insert("weather0", weather[0]);
                        Log.d("5sha", "save in the database");
                    }
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }
}

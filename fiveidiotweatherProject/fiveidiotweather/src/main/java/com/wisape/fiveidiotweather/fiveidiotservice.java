package com.wisape.fiveidiotweather;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by wisape on 13-9-21.
 */
public class fiveidiotservice extends Service {
    private fiveidiotdb db = null;
    private String weather_path = null;
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
    }

    public void set_weatherpath(String path) {
        weather_path = path;
    }


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String wt_content = null;
                String default_path = intent.getStringExtra("weather_path");
                while (true) {
                    fiveidiotnet net = null;
                    if (weather_path != null) {
                        net = new fiveidiotnet(weather_path);
                    } else {
                        net = new fiveidiotnet(default_path);
                        Log.d("5sha", default_path);
                    }

                    try {
                        wt_content = net.getContext();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (wt_content != null) {
                        fiveidiotanalyze wt_an = new fiveidiotanalyze(wt_content);
                        db.insert("city", wt_an.get_city());
                        db.insert("date", wt_an.get_date());
                        String[] weather = wt_an.get_weathers();
                        String[] temp = wt_an.get_temps();
                        String[] wind = wt_an.get_winds();
                        String[] image = wt_an.get_images();
                        for (int i = 0; i < weather.length; i++) {
                            db.insert("weather" + i, weather[i]);
                            db.insert("temp" + i, temp[i]);
                            db.insert("wind" + i, wind[i]);
                            db.insert("image" + i, image[i]);
                        }
                        Log.d("5sha", wt_an.get_city());
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

}

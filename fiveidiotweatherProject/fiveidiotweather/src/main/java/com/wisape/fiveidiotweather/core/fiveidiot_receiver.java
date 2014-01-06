package com.wisape.fiveidiotweather.core;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by wisape on 13-12-28.
 */
public class fiveidiot_receiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent it = new Intent(context, fiveidiot_service.class);
        PendingIntent pendIntent = PendingIntent.getService(context,
                100, it, PendingIntent.FLAG_UPDATE_CURRENT);
        long triggerAtTime = SystemClock.elapsedRealtime() + 500;
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, 1800000, pendIntent);
    }
}

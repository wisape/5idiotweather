package com.wisape.fiveidiotweather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by wisape on 13-12-28.
 */
public class fiveidiot_receiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Intent intent_service = new Intent(context, fiveidiotservice.class);
        context.startService(intent_service);
    }
}

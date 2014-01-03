package com.wisape.fiveidiotweather.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wisape.fiveidiotweather.core.fiveidiot_service;

/**
 * Created by wisape on 13-12-28.
 */
public class fiveidiot_receiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Intent intent_service = new Intent(context, fiveidiot_service.class);
        context.startService(intent_service);
    }
}

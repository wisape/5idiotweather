package com.wisape.fiveidiotweather;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by wisape on 13-12-22.
 */
public class fiveidiot_clock_widget4x1_provider extends AppWidgetProvider {
    private IntentFilter intentFilter = null;
    private RemoteViews views;


    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_TIME_TICK) || action.equals(Intent.ACTION_TIME_CHANGED) || action.equals(Intent.ACTION_TIMEZONE_CHANGED)) {
            views.setTextViewText(R.id.clock_time, fiveidiot_set_ui.getTime());
            ComponentName wd = new ComponentName(context, fiveidiot_clock_widget4x1_provider.class);
            AppWidgetManager.getInstance(context).updateAppWidget(wd, views);
        }

        if (action.equals("clock_next_city4x1")) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                int mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);
                int city_index = extras.getInt("CityIndex", 0);
                city_index += 1;
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                updateWidgetView(context, appWidgetManager, mAppWidgetId, city_index);
            }
        }
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

//        add clock update reciver
        if (intentFilter == null) {
            intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_TIME_TICK);
            intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
            intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            context.getApplicationContext().registerReceiver(this, intentFilter);
        }

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            views = new RemoteViews(context.getPackageName(), R.layout.clock_widget4x1);
            updateWidgetView(context, appWidgetManager, appWidgetId, 0);
        }
    }


    private void updateWidgetView(Context context, AppWidgetManager appWidgetManager, int appWidgetId, int city_index) {
        fiveidiot_set_ui set_ui = new fiveidiot_set_ui(context);
        Intent intent = new Intent(context, fiveidiot.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Intent clock = new Intent(AlarmClock.ACTION_SET_ALARM);
        PendingIntent clockpendIntent = PendingIntent.getActivity(context, 0, clock, 0);
        if (views == null)
            views = new RemoteViews(context.getPackageName(), R.layout.clock_widget4x1);
        set_ui.setWidgetTodayUi(views, city_index, true);
        Intent nextIntent = new Intent("clock_next_city4x1");
        Bundle bundle = new Bundle();
        bundle.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        bundle.putInt("CityIndex", city_index);
        nextIntent.putExtras(bundle);
        PendingIntent nextpendIntent = PendingIntent.getBroadcast(context, appWidgetId, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.today_con, pendingIntent);
        views.setOnClickPendingIntent(R.id.clock_time, clockpendIntent);
        views.setOnClickPendingIntent(R.id.next_city, nextpendIntent);
        // Tell the AppWidgetManager to perform an update on the current app widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
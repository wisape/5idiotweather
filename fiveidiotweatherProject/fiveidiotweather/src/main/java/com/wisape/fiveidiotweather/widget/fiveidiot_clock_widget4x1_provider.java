package com.wisape.fiveidiotweather.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.wisape.fiveidiotweather.R;
import com.wisape.fiveidiotweather.core.fiveidiot_set_ui;

import java.lang.ref.WeakReference;

/**
 * Created by wisape on 13-12-22.
 */
public class fiveidiot_clock_widget4x1_provider extends AppWidgetProvider {
//    private IntentFilter intentFilter = null;

    public void onReceive(Context context, Intent intent) {
        super.onReceive(context.getApplicationContext(), intent);
        Context con = context.getApplicationContext();
        String action = intent.getAction();
        if (action.equals("clock_next_city4x1")) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                int mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);
                int city_index = extras.getInt("CityIndex", 0);
                city_index += 1;
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(con);
                updateWidgetView(con, appWidgetManager, mAppWidgetId, city_index);
            }
        }
        if (intent.getAction().equals(fiveidiot_set_ui.WIDGET_UPDATE) ||
                action.equals(Intent.ACTION_TIME_TICK) || action.equals(Intent.ACTION_TIME_CHANGED) ||
                action.equals(Intent.ACTION_TIMEZONE_CHANGED)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(con);
            ComponentName wd = new ComponentName(con, fiveidiot_clock_widget4x1_provider.class);
            onUpdate(con, appWidgetManager, appWidgetManager.getAppWidgetIds(wd));
        }
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        Context con = context.getApplicationContext();

//        add clock update reciver
//        if (intentFilter == null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_TIME_TICK);
            intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
            intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            con.registerReceiver(this, intentFilter);
//        }

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            updateWidgetView(con, appWidgetManager, appWidgetId, 0);
        }
    }

    private void updateWidgetView(Context context, AppWidgetManager appWidgetManager, int appWidgetId, int city_index) {
        WeakReference<fiveidiot_set_ui> weak_set_ui = new WeakReference<fiveidiot_set_ui>(new fiveidiot_set_ui(context));
//        fiveidiot_set_ui set_ui = new fiveidiot_set_ui(context);
        fiveidiot_set_ui set_ui = weak_set_ui.get();
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.clock_widget4x1);
        set_ui.setWidgetTodayUi(views, city_index,  "clock_next_city4x1", appWidgetId, city_index, false);
        // Tell the AppWidgetManager to perform an update on the current app widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
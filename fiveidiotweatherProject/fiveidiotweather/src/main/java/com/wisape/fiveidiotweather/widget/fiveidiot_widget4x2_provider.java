package com.wisape.fiveidiotweather.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.wisape.fiveidiotweather.R;
import com.wisape.fiveidiotweather.core.fiveidiot_set_ui;

import java.lang.ref.WeakReference;

/**
 * Created by wisape on 13-12-3.
 */
public class fiveidiot_widget4x2_provider extends AppWidgetProvider {
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        Context con = context.getApplicationContext();
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            updateWidgetView(con, appWidgetManager,  appWidgetId, 0);
        }
    }

    public void onReceive(Context context, Intent intent) {
        super.onReceive(context.getApplicationContext(), intent);
        Context con = context.getApplicationContext();
        if (intent.getAction().equals("next_city4x2")) {
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
        if (intent.getAction().equals(fiveidiot_set_ui.WIDGET_UPDATE)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(con);
            ComponentName wd = new ComponentName(con, fiveidiot_widget4x2_provider.class);
            onUpdate(con, appWidgetManager, appWidgetManager.getAppWidgetIds(wd));
        }
    }

    private void updateWidgetView(Context context, AppWidgetManager appWidgetManager, int appWidgetId, int city_index) {
        WeakReference<fiveidiot_set_ui> weak_set_ui = new WeakReference<fiveidiot_set_ui>(new fiveidiot_set_ui(context));
//        fiveidiot_set_ui set_ui = new fiveidiot_set_ui(context);
        fiveidiot_set_ui set_ui = weak_set_ui.get();
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget4x2);
        set_ui.setWidgetTodayUi(views, city_index, "next_city4x2", appWidgetId, city_index, true);
        // Tell the AppWidgetManager to perform an update on the current app widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}

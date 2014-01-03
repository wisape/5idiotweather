package com.wisape.fiveidiotweather.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.wisape.fiveidiotweather.R;
import com.wisape.fiveidiotweather.fiveidiot;
import com.wisape.fiveidiotweather.core.fiveidiot_set_ui;

/**
 * Created by wisape on 13-12-3.
 */
public class fiveidiot_widget4x2_provider extends AppWidgetProvider {
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            updateWidgetView(context, appWidgetManager,  appWidgetId, 0);
        }
    }

    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals("next_city4x2")) {
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
        if (intent.getAction().equals(fiveidiot_set_ui.WIDGET_UPDATE)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName wd = new ComponentName(context, fiveidiot_widget4x2_provider.class);
            onUpdate(context, appWidgetManager, appWidgetManager.getAppWidgetIds(wd));
        }
    }

    private void updateWidgetView(Context context, AppWidgetManager appWidgetManager, int appWidgetId, int city_index) {
        fiveidiot_set_ui set_ui = new fiveidiot_set_ui(context);
        Intent intent = new Intent(context, fiveidiot.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget4x2);
        set_ui.setWidgetTodayUi(views, city_index, true);
        Intent nextIntent = new Intent("next_city4x2");
        Bundle bundle = new Bundle();
        bundle.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        bundle.putInt("CityIndex", city_index);
        nextIntent.putExtras(bundle);
        PendingIntent nextpendIntent = PendingIntent.getBroadcast(context, appWidgetId, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.today_con, pendingIntent);
        views.setOnClickPendingIntent(R.id.next_city, nextpendIntent);
        // Tell the AppWidgetManager to perform an update on the current app widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}

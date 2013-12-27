package com.wisape.fiveidiotweather;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Created by wisape on 13-12-3.
 */
public class fiveidiot_widget4x1_provider extends AppWidgetProvider {
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
        Log.d("5sha", "In receiver " + intent.getAction());
        if (intent.getAction().equals("next_city")) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                int mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);
                int city_index = extras.getInt("CityIndex", 0);
                city_index += 1;
                Log.d("5sha", "city_index = " + city_index + " id =" + mAppWidgetId);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                updateWidgetView(context, appWidgetManager, mAppWidgetId, city_index);
            }
        }
    }

    private void updateWidgetView(Context context, AppWidgetManager appWidgetManager, int appWidgetId, int city_index) {
        fiveidiot_set_ui set_ui = new fiveidiot_set_ui(context);
        Intent intent = new Intent(context, fiveidiot.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget4x1);
        Log.d("5sha", "city_index is " + city_index + " 2id = " + appWidgetId);
        set_ui.setWidgetTodayUi(views, city_index, false);
        Intent nextIntent = new Intent("next_city");
        Bundle bundle = new Bundle();
        bundle.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        bundle.putInt("CityIndex", city_index);
        nextIntent.putExtras(bundle);
        PendingIntent nextpendIntent = pendingIntent.getBroadcast(context, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.today_con, pendingIntent);
        views.setOnClickPendingIntent(R.id.next_city, nextpendIntent);
        // Tell the AppWidgetManager to perform an update on the current app widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}

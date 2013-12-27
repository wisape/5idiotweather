package com.wisape.fiveidiotweather;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by wisape on 13-12-3.
 */
public class fiveidiot_widget4x1_provider extends AppWidgetProvider {
    private HashMap<Integer, Integer> citys;
    public fiveidiot_widget4x1_provider() {
        super();
        citys = new HashMap<Integer, Integer>();
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            if (!citys.containsKey(appWidgetId)) {
                citys.put(appWidgetId, 0);
            }
            Log.d("5sha", "id is = " + appWidgetId);
            updateWidgetView(context, appWidgetManager,  appWidgetId);
        }
    }

    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals("next_city")) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                int mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);
                int city_index = extras.getInt("CityIndex", 0);
                city_index += 1;
                citys.put(mAppWidgetId, city_index);
                Log.d("5sha", "city_index = " + city_index + " id =" + mAppWidgetId);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                updateWidgetView(context, appWidgetManager, mAppWidgetId);
            }
        }
    }

    private void updateWidgetView(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        fiveidiot_set_ui set_ui = new fiveidiot_set_ui(context);
        Intent intent = new Intent(context, fiveidiot.class);
        int city_index = citys.get(appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget4x1);
        Log.d("5sha", "city_index is " + city_index + " 2id = " + appWidgetId);
        set_ui.setWidgetTodayUi(views, city_index, false);
        Intent nextIntent = new Intent("next_city");
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

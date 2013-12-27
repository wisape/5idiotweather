package com.wisape.fiveidiotweather;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by wisape on 13-12-3.
 */
public class fiveidiot_widget4x1_provider extends AppWidgetProvider {
    int city_index = 0;
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            RemoteViews views;
            int appWidgetId = appWidgetIds[i];
            Intent intent = new Intent(context, fiveidiot.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views = new RemoteViews(context.getPackageName(), R.layout.widget4x1);
            updateViews(context, views, 0);
            Intent nextIntent = new Intent("next_city");
            Bundle nextBundle = new Bundle();
            nextBundle.putParcelable("view", views);
            if (views == null) {
                Log.d("5sha", "views  before is null");
                return;
            }
            nextIntent.putExtras(nextBundle);
            PendingIntent nextpendIntent = pendingIntent.getBroadcast(context, 0, nextIntent, 0);
            views.setOnClickPendingIntent(R.id.today_con, pendingIntent);
            views.setOnClickPendingIntent(R.id.next_city, nextpendIntent);
            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        if (action.equals("next_city")) {
            Log.d("5sha", "next_city");
            city_index += 1;
            RemoteViews views = (RemoteViews)intent.getExtras().getParcelable("view");
            if (views == null) {
                Log.d("5sha", "views is null");
                return;
            }
            updateViews(context, views, city_index);
        }
    }

    private void updateViews(Context context, RemoteViews views, int index) {
        fiveidiot_set_ui set_ui = new fiveidiot_set_ui(context);
        set_ui.setWidgetTodayUi(views, index, false);
    }
}

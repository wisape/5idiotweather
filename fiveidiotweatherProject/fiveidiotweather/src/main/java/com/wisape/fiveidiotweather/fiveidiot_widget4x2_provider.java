package com.wisape.fiveidiotweather;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by wisape on 13-12-3.
 */
public class fiveidiot_widget4x2_provider extends AppWidgetProvider {
    private fiveidiot_set_ui set_ui = null;
    private RemoteViews views;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        if (set_ui == null) {
            set_ui = new fiveidiot_set_ui(context);
        }
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            Intent intent = new Intent(context, fiveidiot.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views = new RemoteViews(context.getPackageName(), R.layout.widget4x2);
            updateViews(views, 0);
            views.setOnClickPendingIntent(R.id.today_con, pendingIntent);
            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private void updateViews(RemoteViews views, int index) {
        set_ui.setWidgetTodayUi(views, index, true);
    }
}

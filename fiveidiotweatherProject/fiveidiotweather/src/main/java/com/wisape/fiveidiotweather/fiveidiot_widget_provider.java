package com.wisape.fiveidiotweather;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by wisape on 13-12-3.
 */
public class fiveidiot_widget_provider extends AppWidgetProvider {
    private fiveidiotreaddb readdb;
    private fiveidiot_citys citydb;
    private RemoteViews views;
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            Intent intent = new Intent(context, fiveidiot.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views = new RemoteViews(context.getPackageName(), R.layout.widget4x1);
            updateViews(context, views, 0);
            views.setOnClickPendingIntent(R.id.today_con, pendingIntent);
            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private String setDefault(Object str1, String str2) {
        if (str1 == null)
            return str2;
        return str1.toString();
    }

    private void updateViews(Context context, RemoteViews views, int index) {
        readdb = new fiveidiotreaddb(context);
        citydb = new fiveidiot_citys(context);
        ArrayList<String> mCitys = citydb.get_citys();
        index = index % mCitys.size();
        Map<String, Object> data_map = readdb.getTodayBriefMapData(mCitys.get(index));
        if (data_map.get("city") == null)
            return;
        views.setTextViewText(R.id.city, data_map.get("city").toString());
        views.setTextViewText(R.id.temp,setDefault(data_map.get("temp0"),"1"));
        views.setTextViewText(R.id.weather,setDefault(data_map.get("weather0"),"1"));
        views.setTextViewText(R.id.wind,setDefault(data_map.get("wind"),"1"));
        views.setTextViewText(R.id.nowtemp,setDefault(data_map.get("nowtemp"),"1") + "℃");
        views.setImageViewResource(R.id.today_image, R.drawable.cloud);
    }
}

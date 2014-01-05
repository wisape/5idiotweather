package com.wisape.fiveidiotweather.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.wisape.fiveidiotweather.R;
import com.wisape.fiveidiotweather.core.data.fiveidiot_citys;
import com.wisape.fiveidiotweather.core.data.fiveidiot_db;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by wisape on 13-12-25.
 */
public class fiveidiot_set_ui {
    public final static String WIDGET_UPDATE = "com.wisape.fiveidiotweather.widget_update";
    private String DEFAULT = "更新..";
    private String DEFAULT_WEEK = "星期？";
    private Context con;

    public fiveidiot_set_ui(Context context) {
        con = context;
    }

    private String setDefault(Object str1, String str2) {
        if (str1 == null)
            return str2;
        return str1.toString();
    }

    private int setDefaultImage(int id1, int id2) {
        if (id1 == 0)
            return id2;
        return id1;
    }

    public String getTime() {
        final Calendar date = Calendar.getInstance();
        int hour = date.get(Calendar.HOUR_OF_DAY);
        int minute = date.get(Calendar.MINUTE);
        return new StringBuffer().append(hour < 10 ? "0" + hour : hour).append(
                ":").append(minute < 10 ? "0" + minute : minute).toString();
    }

    private int getWidgetColor() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String scolor = preferences.getString("setting_widget_color", "222222");
        int salpha = (100 - Integer.valueOf(preferences.getString("setting_widget_trans", "0"))) * 255 / 100;
        String color;
        if (salpha > 15)
            color = "#" + Integer.toHexString(salpha) + scolor;
        else
            color = "#0" + Integer.toHexString(salpha) + scolor;
        return Color.parseColor(color);
    }

//    public int getMainColor() {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(con);
//        String scolor = preferences.getString("setting_main_background", "333333");
//        return Color.parseColor("#" + scolor);
//    }


    public void setWidgetClock(RemoteViews views) {
        views.setInt(R.id.today_con, "setBackgroundColor", getWidgetColor());
        views.setTextViewText(R.id.clock_time, getTime());
    }

    public void setWidgetTodayUi(RemoteViews views, int city_index, boolean has_after) {
        fiveidiot_citys citydb = new fiveidiot_citys(con);
        fiveidiot_db readdb = new fiveidiot_db(con);
        ArrayList<String> mCitys = citydb.get_citys();
        int index = city_index % mCitys.size();
        String city = mCitys.get(index);

        setWidgetClock(views);

        Map<String, Object> data_map = readdb.getTodayBriefMapData(city);
        if (data_map.get("city") == null)
            return;
        views.setTextViewText(R.id.city, data_map.get("city").toString());
        views.setTextViewText(R.id.temp,setDefault(data_map.get("temp"),DEFAULT));
        views.setTextViewText(R.id.weather,setDefault(data_map.get("weather"),DEFAULT));
//        views.setTextViewText(R.id.wind,setDefault(data_map.get("wind"),DEFAULT));
        views.setTextViewText(R.id.nowtemp,setDefault(data_map.get("nowtemp") + "℃",DEFAULT));
        views.setImageViewResource(R.id.today_image,
                setDefaultImage(readdb.getImageId(data_map.get("image").toString()),
                        R.drawable.download));

        if (has_after) {
            setWidgetAfterUi(readdb, views, city);
        }
    }

    private void setWidgetAfterUi(fiveidiot_db readdb, RemoteViews views, String mcity) {
        List<Map<String, Object>> data_map = readdb.getAfterBriefAdapterData(mcity);
        if (data_map == null)
            return;

//        views.setTextViewText(R.id.week_1, setDefault(data_map.get(0).get("week"), DEFAULT));
        views.setTextViewText(R.id.temp_1, setDefault(data_map.get(0).get("temp"), DEFAULT));
//        views.setTextViewText(R.id.weather_1, setDefault(data_map.get(0).get("weather"), DEFAULT));
        views.setImageViewResource(R.id.image_1, setDefaultImage(readdb.getImageId(setDefault(data_map.get(0).get("image"), "1")),
                R.drawable.download));

//        views.setTextViewText(R.id.week_2, setDefault(data_map.get(1).get("week"), DEFAULT));
        views.setTextViewText(R.id.temp_2, setDefault(data_map.get(1).get("temp"), DEFAULT));
//        views.setTextViewText(R.id.weather_2, setDefault(data_map.get(1).get("weather"), DEFAULT));
        views.setImageViewResource(R.id.image_2, setDefaultImage(readdb.getImageId(setDefault(data_map.get(1).get("image"), "1")),
                R.drawable.download));

//        views.setTextViewText(R.id.week_3, setDefault(data_map.get(2).get("week"), DEFAULT));
        views.setTextViewText(R.id.temp_3, setDefault(data_map.get(2).get("temp"), DEFAULT));
//        views.setTextViewText(R.id.weather_3, setDefault(data_map.get(2).get("weather"), DEFAULT));
        views.setImageViewResource(R.id.image_3, setDefaultImage(readdb.getImageId(setDefault(data_map.get(2).get("image"), "1")),
                R.drawable.download));

//        views.setTextViewText(R.id.week_4, setDefault(data_map.get(3).get("week"), DEFAULT));
        views.setTextViewText(R.id.temp_4, setDefault(data_map.get(3).get("temp"), DEFAULT));
//        views.setTextViewText(R.id.weather_4, setDefault(data_map.get(3).get("weather"), DEFAULT));
        views.setImageViewResource(R.id.image_4, setDefaultImage(readdb.getImageId(setDefault(data_map.get(3).get("image"), "1")),
                R.drawable.download));
    }

    public void setTodayUi(View view, String mcity) {
        ((TextView) view.findViewById(R.id.city)).setText(mcity);
        fiveidiot_db readdb = new fiveidiot_db(con);
        Map<String, Object> data_map = readdb.getTodayBriefMapData(mcity);
        if (data_map.get("city") == null)
            return;
        ((TextView) view.findViewById(R.id.city)).setText(setDefault(data_map.get("city").toString(), mcity));
        ((TextView) view.findViewById(R.id.updatetime)).setText("(更新时间：" + setDefault(data_map.get("todayupdatetime"), DEFAULT) + ")");
        ((TextView) view.findViewById(R.id.date)).setText(setDefault(data_map.get("date"), DEFAULT));
        ((TextView) view.findViewById(R.id.week)).setText(setDefault(data_map.get("week"),DEFAULT_WEEK));
        ((TextView) view.findViewById(R.id.temp)).setText(setDefault(data_map.get("temp"),DEFAULT));
        ((TextView) view.findViewById(R.id.weather)).setText(setDefault(data_map.get("weather"),DEFAULT));
        ((TextView) view.findViewById(R.id.wind)).setText(setDefault(data_map.get("wind"),DEFAULT));
        ((TextView) view.findViewById(R.id.nowtemp)).setText(setDefault(data_map.get("nowtemp") + "℃",DEFAULT));
        ((ImageView) view.findViewById(R.id.today_image)).setImageResource(setDefaultImage(readdb.getImageId(setDefault(data_map.get("image"), "1")),
                R.drawable.download));
    }

    public void setAfterUi(View view, String mcity) {
        fiveidiot_db readdb = new fiveidiot_db(con);
        List<Map<String, Object>> data_map = readdb.getAfterBriefAdapterData(mcity);
        if (data_map == null)
            return;
        ((TextView) view.findViewById(R.id.week_1)).setText(setDefault(data_map.get(0).get("week"), DEFAULT_WEEK));
        ((TextView) view.findViewById(R.id.temp_1)).setText(setDefault(data_map.get(0).get("temp"),DEFAULT));
        ((TextView) view.findViewById(R.id.weather_1)).setText(setDefault(data_map.get(0).get("weather"),DEFAULT));
        ((ImageView) view.findViewById(R.id.image_1)).setImageResource(setDefaultImage(readdb.getImageId(setDefault(data_map.get(0).get("image"), "1")),
                R.drawable.download));

        ((TextView) view.findViewById(R.id.week_2)).setText(setDefault(data_map.get(1).get("week"), DEFAULT_WEEK));
        ((TextView) view.findViewById(R.id.temp_2)).setText(setDefault(data_map.get(1).get("temp"),DEFAULT));
        ((TextView) view.findViewById(R.id.weather_2)).setText(setDefault(data_map.get(1).get("weather"),DEFAULT));
        ((ImageView) view.findViewById(R.id.image_2)).setImageResource(setDefaultImage(readdb.getImageId(setDefault(data_map.get(1).get("image"), "1")),
                R.drawable.download));

        ((TextView) view.findViewById(R.id.week_3)).setText(setDefault(data_map.get(2).get("week"),DEFAULT_WEEK));
        ((TextView) view.findViewById(R.id.temp_3)).setText(setDefault(data_map.get(2).get("temp"),DEFAULT));
        ((TextView) view.findViewById(R.id.weather_3)).setText(setDefault(data_map.get(2).get("weather"),DEFAULT));
        ((ImageView) view.findViewById(R.id.image_3)).setImageResource(setDefaultImage(readdb.getImageId(setDefault(data_map.get(2).get("image"), "1")),
                R.drawable.download));

        ((TextView) view.findViewById(R.id.week_4)).setText(setDefault(data_map.get(3).get("week"),DEFAULT_WEEK));
        ((TextView) view.findViewById(R.id.temp_4)).setText(setDefault(data_map.get(3).get("temp"),DEFAULT));
        ((TextView) view.findViewById(R.id.weather_4)).setText(setDefault(data_map.get(3).get("weather"),DEFAULT));
        ((ImageView) view.findViewById(R.id.image_4)).setImageResource(setDefaultImage(readdb.getImageId(setDefault(data_map.get(3).get("image"), "1")),
                R.drawable.download));
    }
}
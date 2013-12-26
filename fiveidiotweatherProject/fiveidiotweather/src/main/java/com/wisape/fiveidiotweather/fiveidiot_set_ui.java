package com.wisape.fiveidiotweather;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by wisape on 13-12-25.
 */
public class fiveidiot_set_ui {
    private fiveidiotreaddb readdb;
    private fiveidiot_citys citydb;

    public fiveidiot_set_ui(Context context) {
        readdb = new fiveidiotreaddb(context);
        citydb = new fiveidiot_citys(context);
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

    public static String getTime() {
        final Calendar date = Calendar.getInstance();
        int hour = date.get(Calendar.HOUR_OF_DAY);
        int minute = date.get(Calendar.MINUTE);
        return new StringBuffer().append(hour < 10 ? "0" + hour : hour).append(
                ":").append(minute < 10 ? "0" + minute : minute).toString();
    }

    public void setWidgetTodayUi(RemoteViews views, int city_index, boolean has_after) {
        ArrayList<String> mCitys = citydb.get_citys();
        int index = city_index % mCitys.size();
        String city = mCitys.get(index);

        views.setTextViewText(R.id.clock_time, getTime());

        Map<String, Object> data_map = readdb.getTodayBriefMapData(city);
        if (data_map.get("city") == null)
            return;
        views.setTextViewText(R.id.city, data_map.get("city").toString());
        views.setTextViewText(R.id.temp,setDefault(data_map.get("temp0"),"1"));
        views.setTextViewText(R.id.weather,setDefault(data_map.get("weather0"),"1"));
        views.setTextViewText(R.id.wind,setDefault(data_map.get("wind"),"1"));
        views.setTextViewText(R.id.nowtemp,setDefault(data_map.get("nowtemp"),"1") + "℃");
        views.setImageViewResource(R.id.today_image,
                setDefaultImage(readdb.getImageId(data_map.get("image").toString()),
                        R.drawable.download));

        if (has_after) {
            setWidgetAfterUi(views, city);
        }
    }

    private void setWidgetAfterUi(RemoteViews views, String mcity) {
        List<Map<String, Object>> data_map = readdb.getAfterBriefAdapterData(mcity);
        if (data_map == null)
            return;

        views.setTextViewText(R.id.week_1, setDefault(data_map.get(0).get("week"), "1"));
        views.setTextViewText(R.id.temp_1, setDefault(data_map.get(0).get("temp"), "1"));
        views.setTextViewText(R.id.weather_1, setDefault(data_map.get(0).get("weather"), "1"));
        views.setImageViewResource(R.id.image_1, setDefaultImage(readdb.getImageId(setDefault(data_map.get(0).get("image"), "1")),
                R.drawable.download));

        views.setTextViewText(R.id.week_2, setDefault(data_map.get(1).get("week"), "1"));
        views.setTextViewText(R.id.temp_2, setDefault(data_map.get(1).get("temp"), "1"));
        views.setTextViewText(R.id.weather_2, setDefault(data_map.get(1).get("weather"), "1"));
        views.setImageViewResource(R.id.image_2, setDefaultImage(readdb.getImageId(setDefault(data_map.get(1).get("image"), "1")),
                R.drawable.download));

        views.setTextViewText(R.id.week_3, setDefault(data_map.get(2).get("week"), "1"));
        views.setTextViewText(R.id.temp_3, setDefault(data_map.get(2).get("temp"), "1"));
        views.setTextViewText(R.id.weather_3, setDefault(data_map.get(2).get("weather"), "1"));
        views.setImageViewResource(R.id.image_3, setDefaultImage(readdb.getImageId(setDefault(data_map.get(2).get("image"), "1")),
                R.drawable.download));

        views.setTextViewText(R.id.week_4, setDefault(data_map.get(3).get("week"), "1"));
        views.setTextViewText(R.id.temp_4, setDefault(data_map.get(3).get("temp"), "1"));
        views.setTextViewText(R.id.weather_4, setDefault(data_map.get(3).get("weather"), "1"));
        views.setImageViewResource(R.id.image_4, setDefaultImage(readdb.getImageId(setDefault(data_map.get(3).get("image"), "1")),
                R.drawable.download));
    }

    public void setTodayUi(View view, String mcity) {
        ((TextView) view.findViewById(R.id.city)).setText(mcity);
        Map<String, Object> data_map = readdb.getTodayBriefMapData(mcity);
        if (data_map.get("city") == null)
            return;
        ((TextView) view.findViewById(R.id.city)).setText(setDefault(data_map.get("city").toString(), mcity));
        ((TextView) view.findViewById(R.id.updatetime)).setText("(更新时间：" + setDefault(data_map.get("todayupdatetime"), "1") + ")");
        ((TextView) view.findViewById(R.id.date)).setText(setDefault(data_map.get("date"), "1"));
        ((TextView) view.findViewById(R.id.week)).setText(setDefault(data_map.get("week0"),"1"));
        ((TextView) view.findViewById(R.id.temp)).setText(setDefault(data_map.get("temp0"),"1"));
        ((TextView) view.findViewById(R.id.weather)).setText(setDefault(data_map.get("weather0"),"1"));
        ((TextView) view.findViewById(R.id.wind)).setText(setDefault(data_map.get("wind"),"1"));
        ((TextView) view.findViewById(R.id.nowtemp)).setText(setDefault(data_map.get("nowtemp"),"1") + "℃");
        ((ImageView) view.findViewById(R.id.today_image)).setImageResource(setDefaultImage(readdb.getImageId(setDefault(data_map.get("image"), "1")),
                R.drawable.download));
    }

    public void setAfterUi(View view, String mcity) {
        List<Map<String, Object>> data_map = readdb.getAfterBriefAdapterData(mcity);
        if (data_map == null)
            return;
        ((TextView) view.findViewById(R.id.week_1)).setText(setDefault(data_map.get(0).get("week"), "1"));
        ((TextView) view.findViewById(R.id.temp_1)).setText(setDefault(data_map.get(0).get("temp"),"1"));
        ((TextView) view.findViewById(R.id.weather_1)).setText(setDefault(data_map.get(0).get("weather"),"1"));
        ((ImageView) view.findViewById(R.id.image_1)).setImageResource(setDefaultImage(readdb.getImageId(setDefault(data_map.get(0).get("image"), "1")),
                R.drawable.download));

        ((TextView) view.findViewById(R.id.week_2)).setText(setDefault(data_map.get(1).get("week"), "1"));
        ((TextView) view.findViewById(R.id.temp_2)).setText(setDefault(data_map.get(1).get("temp"),"1"));
        ((TextView) view.findViewById(R.id.weather_2)).setText(setDefault(data_map.get(1).get("weather"),"1"));
        ((ImageView) view.findViewById(R.id.image_2)).setImageResource(setDefaultImage(readdb.getImageId(setDefault(data_map.get(1).get("image"), "1")),
                R.drawable.download));

        ((TextView) view.findViewById(R.id.week_3)).setText(setDefault(data_map.get(2).get("week"),"1"));
        ((TextView) view.findViewById(R.id.temp_3)).setText(setDefault(data_map.get(2).get("temp"),"1"));
        ((TextView) view.findViewById(R.id.weather_3)).setText(setDefault(data_map.get(2).get("weather"),"1"));
        ((ImageView) view.findViewById(R.id.image_3)).setImageResource(setDefaultImage(readdb.getImageId(setDefault(data_map.get(2).get("image"), "1")),
                R.drawable.download));

        ((TextView) view.findViewById(R.id.week_4)).setText(setDefault(data_map.get(3).get("week"),"1"));
        ((TextView) view.findViewById(R.id.temp_4)).setText(setDefault(data_map.get(3).get("temp"),"1"));
        ((TextView) view.findViewById(R.id.weather_4)).setText(setDefault(data_map.get(3).get("weather"),"1"));
        ((ImageView) view.findViewById(R.id.image_4)).setImageResource(setDefaultImage(readdb.getImageId(setDefault(data_map.get(3).get("image"), "1")),
                R.drawable.download));
    }
}

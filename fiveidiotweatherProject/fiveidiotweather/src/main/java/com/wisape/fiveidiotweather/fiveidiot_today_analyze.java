package com.wisape.fiveidiotweather;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wisape on 13-11-27.
 */
public class fiveidiot_today_analyze {
    private String weather_info = null;
    private JSONObject json_object = null;

    /**
     *{"weatherinfo":{"city":"北京","cityid":"101010100","temp":"-1",
     * "WD":"西北风","WS":"4级","SD":"13%","WSE":"4","time":"20:20",
     * "isRadar":"1","Radar":"JC_RADAR_AZ9010_JB"}}
     */

    public fiveidiot_today_analyze(String info){
        weather_info = info;
        init_info(weather_info);
    }

    void init_info(String info) {
        try {
            json_object = new JSONObject(info).getJSONObject("weatherinfo");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String get_city() {
        String city = null;
        if (null != json_object) {
            try {
                city = json_object.getString("city");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return city;
    }

    public String get_now_temp() {
        String now_temp = null;
        if (null != json_object) {
            try {
                now_temp = json_object.getString("temp");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return now_temp;
    }

    public String get_wind() {
        String wind = null;
        if (null != json_object) {
            try {
                wind = json_object.getString("WD") + json_object.getString("WS");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return wind;
    }

    public String get_hum() {
        String humidity = null;
        if (null != json_object) {
            try {
                humidity = json_object.getString("SD");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return humidity;
    }

    public String get_update_time() {
        String update_time = null;
        if (null != json_object) {
            try {
                update_time = json_object.getString("time");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return update_time;
    }

}

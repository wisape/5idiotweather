package com.wisape.fiveidiotweather.core.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wisape on 13-11-27.
 */
public class fiveidiot_today_analyze {
    public final String[] TODAY_KEYS = {"city", "temp", "SD","time", "WD", "WS"};
    private JSONObject json_object = null;
    private HashMap<String, String> today_info;

    /**
     *{"weatherinfo":{"city":"北京","cityid":"101010100","temp":"-1",
     * "WD":"西北风","WS":"4级","SD":"13%","WSE":"4","time":"20:20",
     * "isRadar":"1","Radar":"JC_RADAR_AZ9010_JB"}}
     */

    public fiveidiot_today_analyze(String info){
        String weather_info = info;
        init_info(weather_info);
    }

    void init_info(String info) {
        today_info = new HashMap<String, String>();
        try {
            json_object = new JSONObject(info).getJSONObject("weatherinfo");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String key;
        for (int i = 0; i < TODAY_KEYS.length; i++) {
            key = TODAY_KEYS[i];
            try {
                today_info.put(key, json_object.getString(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String get_city() {
        return today_info.get("city");
    }

    public String get_now_temp() {
        return today_info.get("temp");
    }

    public String get_wind() {
        return today_info.get("WD") + today_info.get("WS");
    }

    public String get_hum() {
        return today_info.get("SD");
    }

    public String get_update_time() {
        return today_info.get("time");
    }

}

package com.wisape.fiveidiotweather;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wispae on 13-9-21.
 */
public class fiveidiotanalyze {
    private String weather_info = null;
    private JSONObject json_object = null;
    /**
     *{"weatherinfo":{"city":"西安","city_en":"xian","date_y":"2013年9月26日",
     * "date":"","week":"星期四","fchh":"18","cityid":"101110101","temp1":"14℃~26℃",
     * "temp2":"14℃~30℃","temp3":"15℃~29℃","temp4":"16℃~25℃","temp5":"16℃~24℃",
     * "temp6":"14℃~22℃","tempF1":"57.2℉~78.8℉","tempF2":"57.2℉~86℉",
     * "tempF3":"59℉~84.2℉","tempF4":"60.8℉~77℉","tempF5":"60.8℉~75.2℉",
     * "tempF6":"57.2℉~71.6℉","weather1":"多云转晴","weather2":"晴","weather3":"晴转多云",
     * "weather4":"多云转阴","weather5":"阴","weather6":"阴转多云","img1":"1","img2":"0",
     * "img3":"0","img4":"99","img5":"0","img6":"1","img7":"1","img8":"2","img9":"2",
     * "img10":"99","img11":"2","img12":"1","img_single":"0","img_title1":"多云",
     * "img_title2":"晴","img_title3":"晴","img_title4":"晴","img_title5":"晴",
     * "img_title6":"多云","img_title7":"多云","img_title8":"阴","img_title9":"阴",
     * "img_title10":"阴","img_title11":"阴","img_title12":"多云","img_title_single":"晴",
     * "wind1":"东风小于3级","wind2":"东风小于3级","wind3":"东风小于3级","wind4":"东风小于3级",
     * "wind5":"东风小于3级","wind6":"东风小于3级","fx1":"东风","fx2":"东风","fl1":"小于3级",
     * "fl2":"小于3级","fl3":"小于3级","fl4":"小于3级","fl5":"小于3级","fl6":"小于3级",
     * "index":"舒适","index_d":"建议着长袖衫裤等服装。体弱者宜着长袖衫裤加马甲。昼夜温差较大，
     * 需适时增减衣服，避免感冒着凉。","index48":"舒适","index48_d":"建议着长袖衫裤等服装。
     * 体弱者宜着长袖衫裤加马甲。昼夜温差较大，需适时增减衣服，避免感冒着凉。","index_uv":"强",
     * "index48_uv":"很强","index_xc":"适宜","index_tr":"适宜","index_co":"较舒适","st1":"26",
     * "st2":"14","st3":"29","st4":"15","st5":"30","st6":"16","index_cl":"适宜",
     * "index_ls":"适宜","index_ag":"较易发"}}
     */

    public fiveidiotanalyze(String info){
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

    public String get_date() {
        String date = null;
        if (null != json_object) {
            try {
                date = json_object.getString("date_y");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return date;
    }
    public String get_week() {
        String week = null;
        if (null != json_object) {
            try {
                week = json_object.getString("week");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return week;
    }

    public String [] get_temps() {
        String temp[] = new String[6];
        if (null != json_object) {
            try {
                temp[0] = json_object.getString("temp1");
                temp[1] = json_object.getString("temp2");
                temp[2] = json_object.getString("temp3");
                temp[3] = json_object.getString("temp4");
                temp[4] = json_object.getString("temp5");
                temp[5] = json_object.getString("temp6");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return temp;
    }

    public String [] get_weathers() {
        String weather[] = new String[6];
        if (null != json_object) {
            try {
                weather[0] = json_object.getString("weather1");
                weather[1] = json_object.getString("weather2");
                weather[2] = json_object.getString("weather3");
                weather[3] = json_object.getString("weather4");
                weather[4] = json_object.getString("weather5");
                weather[5] = json_object.getString("weather6");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return weather;
    }

    public String [] get_winds() {
        String wind[] = new String[6];
        if (null != json_object) {
            try {
                wind[0] = json_object.getString("wind1");
                wind[1] = json_object.getString("wind2");
                wind[2] = json_object.getString("wind3");
                wind[3] = json_object.getString("wind4");
                wind[4] = json_object.getString("wind5");
                wind[5] = json_object.getString("wind6");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return wind;
    }

    public String [] get_images() {
        String wind[] = new String[6];
        if (null != json_object) {
            try {
                wind[0] = json_object.getString("img_title1");
                wind[1] = json_object.getString("img_title2");
                wind[2] = json_object.getString("img_title3");
                wind[3] = json_object.getString("img_title4");
                wind[4] = json_object.getString("img_title5");
                wind[5] = json_object.getString("img_title6");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return wind;
    }

    public String[] get_cy() {
        String cy[] = new String[4];
        if (null != json_object) {
            try {
                cy[0] = json_object.getString("index");
                cy[1] = json_object.getString("incex_d");
                cy[2] = json_object.getString("index48");
                cy[3] = json_object.getString("incex48_d");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return cy;
    }

    public String[] get_uv() {
        String uv[] = new String[2];
        if (null != json_object) {
            try {
                uv[0] = json_object.getString("index_uv");
                uv[1] = json_object.getString("index_uv");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return uv;
    }

    public String get_tr() {
        String tr = null;
        if (null != json_object) {
            try {
                tr = json_object.getString("index_tr");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tr;
    }

    public String get_gm() {
        String gm = null;
        if (null != json_object) {
            try {
                gm = json_object.getString("index_ag");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return gm;
    }
}

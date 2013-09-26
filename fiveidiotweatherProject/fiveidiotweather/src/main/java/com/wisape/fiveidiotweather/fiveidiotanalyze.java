package com.wisape.fiveidiotweather;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wispae on 13-9-21.
 */
public class fiveidiotanalyze {
    private static String weather_info = null;
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
    }

    static void init_info(String info) {
        JSONObject json_object = null;
        String city = null;
        String data = null;
        try {
            json_object = new JSONObject(info).getJSONObject("weatherinfo");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (null != json_object) {
            try {
                city = json_object.getString("city");
                data = json_object.getString("date_y");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

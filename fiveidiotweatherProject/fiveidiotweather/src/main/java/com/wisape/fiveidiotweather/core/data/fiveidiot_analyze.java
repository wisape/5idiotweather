package com.wisape.fiveidiotweather.core.data;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wispae on 13-9-21.
 */
public class fiveidiot_analyze {
    private JSONObject json_object = null;
    private HashMap<String, String> brief_info;
    private HashMap<String, List<String>> list_breif_info;
    private HashMap<String, String> today_info;
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

    public void init_brief_info(String info) {
        try {
            json_object = new JSONObject(info).getJSONObject("weatherinfo");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        brief_info = get_brief_info(json_object);
        list_breif_info = get_list_brief_info(json_object);
    }

    private HashMap<String, String> get_brief_info(JSONObject json_object) {
        String[] BRIEF_KEYS = {"date_y", "fchh", "index_uv"};
        HashMap<String, String> info = new HashMap<String, String>();
        String key;
        for (int i = 0; i < BRIEF_KEYS.length; i++) {
            key = BRIEF_KEYS[i];
            try {
                info.put(key, json_object.getString(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return info;
    }

    private HashMap<String, List<String>> get_list_brief_info(JSONObject json_object) {
        String[] LIST_BRIEF_KEYS = {"week", "temp","wind", "weather", "img_title"};
        String[] WEEKS = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日",};
        HashMap<String, List<String>> info = new HashMap<String, List<String>>();
        if (null != json_object) {
            try {
                for (int j = 0; j < LIST_BRIEF_KEYS.length; j++) {
                ArrayList<String> value = new ArrayList<String>();
                if (LIST_BRIEF_KEYS[j].equals("week")) {
                    int i = 0;
                    String week = json_object.getString("week");
                    for (i = 0; i < WEEKS.length; i++) {
                        if (week.equals(WEEKS[i])) {
                            break;
                        }
                    }
                    for (int k = 0; k < 6; k++) {
                        value.add(WEEKS[(i + k) % 7]);
                    }
                    info.put("week", value);
                    continue;
                }
                for (int i = 0; i < 6; i++) {
                    if (LIST_BRIEF_KEYS[j].equals("temp"))
                        value.add(json_object.getString(new StringBuffer(LIST_BRIEF_KEYS[j]).append(i + 1).toString().replace("℃~", "/")));
                    else if (LIST_BRIEF_KEYS[j].equals("img_title")) {
                        value.add(json_object.getString(new StringBuffer(LIST_BRIEF_KEYS[j]).append(i * 2 + 1).toString()));
                    } else
                        value.add(json_object.getString(new StringBuffer(LIST_BRIEF_KEYS[j]).append(i + 1).toString()));
                }
                info.put(LIST_BRIEF_KEYS[j], value);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return info;
    }

    public void clear() {
        brief_info = null;
        list_breif_info = null;
        today_info = null;
    }


//    public String get_city() {
//        String city = null;
//        if (null != json_object) {
//            try {
//                city = json_object.getString("city");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return city;
//    }

    public String get_date() {
        return brief_info.get("date_y");
    }

    public String get_brief_update_time() {
        return brief_info.get("fchh");
    }

    public List<String> get_week() {
        return list_breif_info.get("week");
    }

    public List<String> get_temps() {
        return list_breif_info.get("temp");
    }

    public List<String> get_weathers() {
        return list_breif_info.get("weather");
    }

    public List<String> get_winds() {
        return list_breif_info.get("wind");
    }

    public List<String> get_images() {
        return list_breif_info.get("img_title");
    }

//    public String[] get_dress() {
//        String cy[] = new String[2];
//        if (null != json_object) {
//            try {
//                cy[0] = json_object.getString("index");
//                cy[1] = json_object.getString("index_d");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return cy;
//    }

    public String get_uv() {
        return brief_info.get("index_uv");
    }
//
//    public String get_tr() {
//        String tr = null;
//        if (null != json_object) {
//            try {
//                tr = json_object.getString("index_tr");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return tr;
//    }
//
//    public String get_cl() {
//        String tr = null;
//        if (null != json_object) {
//            try {
//                tr = json_object.getString("index_cl");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return tr;
//    }
//
//    public String get_ls() {
//        String tr = null;
//        if (null != json_object) {
//            try {
//                tr = json_object.getString("index_ls");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return tr;
//    }
//
//    public String get_xc() {
//        String tr = null;
//        if (null != json_object) {
//            try {
//                tr = json_object.getString("index_xc");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return tr;
//    }
//
//    public String get_gm() {
//        String gm = null;
//        if (null != json_object) {
//            try {
//                gm = json_object.getString("index_ag");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return gm;
//    }

    /**
     *{"weatherinfo":{"city":"北京","cityid":"101010100","temp":"-1",
     * "WD":"西北风","WS":"4级","SD":"13%","WSE":"4","time":"20:20",
     * "isRadar":"1","Radar":"JC_RADAR_AZ9010_JB"}}
     */

    public void init_today_info(String info) {
        String[] TODAY_KEYS = {"city", "temp", "SD","time", "WD", "WS"};
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

    public String get_today_wind() {
        return new StringBuffer(today_info.get("WD")).append(today_info.get("WS")).toString();
    }

    public String get_today_hum() {
        return today_info.get("SD");
    }

    public String get_today_update_time() {
        return today_info.get("time");
    }
}

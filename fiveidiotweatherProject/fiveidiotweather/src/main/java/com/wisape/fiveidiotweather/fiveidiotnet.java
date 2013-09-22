package com.wisape.fiveidiotweather;


import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wisape on 13-9-21.
 */

public class fiveidiotnet {
    private static String weatherAddress = "http://m.weather.com.cn/data/101110101.html";

    public fiveidiotnet(String address) {
        weatherAddress = address;

    }


    public String getContext() throws IOException {
        URL weatherUrl = new URL(weatherAddress);
        String context = "诶有数据";
        HttpURLConnection conn = (HttpURLConnection) weatherUrl.openConnection();

        try {
            InputStream in = new BufferedInputStream(conn.getInputStream());
            InputStreamReader reader = new InputStreamReader(in);
            context = reader.toString();

        } finally {
            conn.disconnect();
        }

        return context;
    }

}

package com.wisape.fiveidiotweather.net;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wisape on 13-9-21.
 */

public class fiveidiot_net {
    private String weatherAddress;
    private StringBuffer buffer;
    private BufferedReader br;
    private InputStream is;
    public fiveidiot_net() {
        buffer = new StringBuffer();
    }

    public void setUrl(String address) {
        weatherAddress = address;
    }

    public String getContext() throws IOException {
        URL weatherUrl = new URL(weatherAddress);
        String context = "No data!!";
        HttpURLConnection conn = (HttpURLConnection) weatherUrl.openConnection();

        try {
            is = new BufferedInputStream(conn.getInputStream());
            br = new BufferedReader(new InputStreamReader(is));
            buffer.delete(0, buffer.length());
            String line;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
            context = buffer.toString();
        } finally {
            conn.disconnect();
        }

        return context;
    }

}

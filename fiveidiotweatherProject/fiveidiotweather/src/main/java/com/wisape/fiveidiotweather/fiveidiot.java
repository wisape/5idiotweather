package com.wisape.fiveidiotweather;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import java.io.IOException;

public class fiveidiot extends Activity {
    String texts = null;
    TextView text = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView)findViewById(R.id.text);
        mythread th = new mythread();
        th.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fiveidiot, menu);
        return true;
    }

    class mythread extends Thread {
        @Override
        public synchronized void start() {
            super.start();
        }

        @Override
        public void run() {
            super.run();
            fiveidiotnet net = new fiveidiotnet("http://m.weather.com.cn/data/101110101.html");
            try {
                texts = net.getContext();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(text != null) {
                Log.d("5sha", texts);
            }
            try {
                sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
}

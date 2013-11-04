package com.wisape.fiveidiotweather;

import android.content.Context;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.app.Activity;
import android.os.IBinder;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.widget.TextView;
import android.util.Log;

public class fiveidiot extends Activity {
    private TextView text = null;
    private ServiceConnection sconn = null;
    private fiveidiotservice fs = null;
    private InterThread interthread = null;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("5sha", "get the message");
            switch (msg.what) {
                case 1:
                    text.setText(msg.getData().getString("date"));
                    Log.d("5sha", msg.getData().getString("date"));
                    break;
                default:
                    Log.d("5sha", "did not goto case");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView)findViewById(R.id.text);

        sconn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                fs = ((fiveidiotservice.fiBinder)iBinder).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };

        start_weather_service("http://m.weather.com.cn/data/101110101.html");
        interthread = new InterThread(mHandler);
        interthread.run();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fiveidiot, menu);
        return true;
    }

    @Override
    protected void onStop() {
        unbindService(sconn);
        super.onStop();
    }

    class InterThread extends Thread {
        Handler inhandler = null;
        public InterThread(Handler mHandler) {
            inhandler = mHandler;
        }

        @Override
        public void run() {
            fiveidiotdb db = new fiveidiotdb(fiveidiot.this);
            while (true) {
                Message message = new Message();
                Bundle bundle = new Bundle();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                bundle.putString("city", db.getvalue("city"));
                bundle.putString("date", db.getvalue("date"));
                bundle.putString("weather0", db.getvalue("weather0"));
                message.setData(bundle);
                message.what = 1;
                if (inhandler != null) {
                    inhandler.sendMessage(message);
//                    Log.d("5sha", "Send Message");
//                    Log.d("5sha", db.getvalue("city"));
//                    Log.d("5sha", db.getvalue("date"));
//                    Log.d("5sha", db.getvalue("weather0"));
                }
            }
        }
    }

    private void start_weather_service(String wpath)
    {
        Intent it = new Intent(this, fiveidiotservice.class);
        bindService(it, sconn, Context.BIND_AUTO_CREATE);
        it.putExtra("weather_path", wpath);
        startService(it);
    }

}

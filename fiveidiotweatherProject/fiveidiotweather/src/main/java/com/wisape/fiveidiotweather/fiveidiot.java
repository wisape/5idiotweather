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



public class fiveidiot extends Activity {
    private TextView text = null;
    private ServiceConnection sconn;
    private fiveidiotservice fs;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    text.setText(msg.getData().getString("date"));
                    break;
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
                fs.setHandler(mHandler);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };

        Intent it = new Intent(this, fiveidiotservice.class);
        //startService(it);
        bindService(it, sconn, Context.BIND_AUTO_CREATE);
        startService(it);

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

}

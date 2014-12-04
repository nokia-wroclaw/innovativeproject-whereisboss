package com.example.baksu.whereismyboss;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Baksu on 2014-12-02.
 */
public class ActivityMain extends Activity {

    private ServerTransmission serverTransmission;
    private Intent service;
    private ThreadBackgroundScan threadScan;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Intent service = new Intent(this, ServerTransmission.class);
        bindService(service, bService, this.BIND_AUTO_CREATE);
    }

    @Override
    public void onBackPressed() {
        unbindService(bService);
        this.finish();
    }

    public void bntClick(View v)
    {
        switch(v.getId())
        {
            case R.id.bntScan: bntScan(); break;
            case R.id.bntReport: bntReport(); break;
            case R.id.bntSearch: bntSearch(); break;
            case R.id.bntLogout: bntLogout(); break;//TODO: Dorobić przycisk logout
        }
    }

    public void bntScan()
    {
        threadScan = new ThreadBackgroundScan((WifiManager)getSystemService(Context.WIFI_SERVICE),"room elo");
        threadScan.start();
        Toast.makeText(this, "Skanowanie rozpoczęte", Toast.LENGTH_LONG).show();
    }

    public void bntReport()
    {
        threadScan.stop();
        Toast.makeText(this, "Skanowanie zostało przerwane", Toast.LENGTH_LONG).show();
    }

    public void bntSearch()
    {

    }

    public void bntLogout()
    {

    }

    ServiceConnection bService = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder binder) {
            ServerTransmission.MyBinder b = (ServerTransmission.MyBinder) binder;
            serverTransmission = b.getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            serverTransmission = null;
        }
    };
}

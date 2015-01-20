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
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by Baksu on 2014-12-02.
 */
public class ActivityMain extends Activity {

    private ServerTransmission serverTransmission;
    private Intent service;
    private ThreadBuildingDownloads threadScan;
    private Context context;
    private ProgressBar loading;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        loading = (ProgressBar)findViewById(R.id.loading_spinner);
        context = getApplicationContext();
        Intent service = new Intent(this, ServerTransmission.class);
        bindService(service, bService, this.BIND_AUTO_CREATE);
    }

    public void onResume()
    {
        super.onResume();
        loading.setVisibility(View.INVISIBLE);
    }

    public void onDestroy()
    {
        super.onDestroy();
        serverTransmission.destroy();
        this.unbindService(bService);
    }

    @Override
    public void onBackPressed() {
        //serverTransmission.endConnection();
        //this.unbindService(bService);
        //this.finish();
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
        loading.setVisibility(View.VISIBLE);
        threadScan = new ThreadBuildingDownloads(serverTransmission,context);
        threadScan.start();
    }

    public void bntReport()
    {
        Intent report = new Intent(context, ActivityReport.class);
        report.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(report);
    }

    public void bntSearch()
    {
        Intent search = new Intent(context, ActivitySearch.class);
        search.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(search);
    }

    public void bntLogout()
    {
        serverTransmission.logout();
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

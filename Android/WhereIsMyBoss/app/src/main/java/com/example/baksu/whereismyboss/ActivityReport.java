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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Created by Baksu on 2014-12-08.
 */
public class ActivityReport extends Activity {

    private ServerTransmission serverTransmission;
    private ThreadReportPosition threadReport;
    private Context context;
    private WifiManager wifiManager;
    private Switch reportSwitch;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_activity);

        context = getApplicationContext();
        wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);

        reportSwitch = (Switch) findViewById(R.id.switchReport);
        reportSwitch.setChecked(false);
        reportSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    bntStartReport();
                }
                else
                {
                    bntStopReport();
                }

            }
        });

        Intent service = new Intent(this, ServerTransmission.class);
        bindService(service, bService, this.BIND_AUTO_CREATE);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    public void onDestroy()
    {
        super.onDestroy();
        if(threadReport != null)
            threadReport.stop();
        Toast.makeText(context, "Reportowanie zostało przerwane", Toast.LENGTH_LONG).show();
        if(bService != null)
            this.unbindService(bService);
    }

    public void bntClick(View v)
    {
        switch(v.getId())
        {
            case R.id.bntLogout: bntLogout(); break;
        }
    }

    /**
     * Metoda odpowiedzialna za rozpoczecie reportowania pozycji uzytkownika
     */
    private void bntStartReport()
    {
        threadReport = new ThreadReportPosition(wifiManager,serverTransmission);
        threadReport.start();
        Toast.makeText(context, "Reportowanie rozpoczęte", Toast.LENGTH_LONG).show();
    }

    /**
     * Metoda odpowiedzialna za zakończenie reportowania pozycji uzytkownika
     */
    private void bntStopReport()
    {
        threadReport.stop();
        Toast.makeText(context, "Reportowanie zostało przerwane", Toast.LENGTH_LONG).show();
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

package com.example.baksu.whereismyboss;

import com.example.baksu.whereismyboss.Sniffer.WifiScanReceier;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

    WifiManager wifiManager;
    WifiScanReceier wifiReceier;
    Sniffer sniff;
    ListView list;
    TextView serverLog;
    ServerTransmission serverTransmission;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        sniff = new Sniffer(wifiManager);
        this.wifiReceier = sniff.getReceier();
        serverTransmission = new ServerTransmission();
    }

    public void onPause()
    {
        unregisterReceiver(sniff.getReceier());
        super.onPause();
    }

    public void onResume()
    {
        registerReceiver(wifiReceier, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onRestart();
    }

    public void bntClick(View v)
    {
        switch(v.getId()){
            case R.id.bntSniff: startSniff(); break;
            case R.id.bntSend: bntSend(); break;
            case R.id.bntText: bntText(); break;
        }
    }

    public void startSniff ()
    {
        sniff.startScan();
        list = (ListView)findViewById(R.id.list);
        list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,sniff.getList()));
    }

    public void bntSend() {
        serverTransmission.createConnect(sniff.getListToSend());
        serverTransmission.startConnection();
    }

    public void bntText(){
        serverLog = (TextView)findViewById(R.id.serverLog);
        serverLog.setText(serverTransmission.getText());
    }

}


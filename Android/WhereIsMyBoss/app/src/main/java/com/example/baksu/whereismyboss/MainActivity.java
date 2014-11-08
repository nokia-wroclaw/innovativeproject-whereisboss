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

public class MainActivity extends Activity {

    Sniffer sniff;
    WifiManager wifiManager;
    ListView list;
    WifiScanReceier wifiReceier;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        sniff = new Sniffer(wifiManager);
        this.wifiReceier = sniff.getReceier();
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
        }
    }

    public void startSniff ()
    {
        list = (ListView)findViewById(R.id.list);
        list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,sniff.getList()));
    }


}


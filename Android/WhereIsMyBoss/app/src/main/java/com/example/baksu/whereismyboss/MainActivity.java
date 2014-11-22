package com.example.baksu.whereismyboss;

import com.example.baksu.whereismyboss.Sniffer.WifiScanReceier;

import android.app.Activity;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

    WifiManager wifiManager;
    WifiScanReceier wifiReceier;
    WifiInfo info;
    Sniffer sniff;
    ListView list;
    Spinner rooms;
    ServerTransmission serverTransmission;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rooms = (Spinner)findViewById(R.id.roomList);           //Znalezienie komponentów na GUI
        list = (ListView)findViewById(R.id.list);

        wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        sniff = new Sniffer(wifiManager);
        wifiReceier = sniff.getReceier();
        serverTransmission = new ServerTransmission();
        serverTransmission.createConnect();
        serverTransmission.startConnection();
        info = wifiManager.getConnectionInfo();
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
        switch(v.getId())
        {
            case R.id.bntSniff: startSniff(); break;
            case R.id.bntSend: bntSend(); break;
            case R.id.bntGetRoom: bntGetRoom(); break;
        }
    }

    public void startSniff ()
    {
        sniff.startScan();
        list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,sniff.getList()));
    }

    public void bntSend()
    {
        serverTransmission.sendList(sniff.getListToSend(), info.getMacAddress(), rooms.getSelectedItem().toString());
    }

    public void bntGetRoom()
    {
        serverTransmission.downloadRoom();
        while(serverTransmission.getRooms() == null) {               //Petla oczekujaca na odebranie informacji o pokojach
        }
        rooms.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,serverTransmission.getRooms()));
    }

}


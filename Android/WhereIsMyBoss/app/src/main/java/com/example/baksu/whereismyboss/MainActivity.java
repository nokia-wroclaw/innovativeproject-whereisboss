package com.example.baksu.whereismyboss;

import android.app.Activity;
import android.net.wifi.WifiInfo;
import android.os.Bundle;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class MainActivity extends Activity {

    private BackgroundScanThread scanThread;
    private static ServerTransmission serverTransmission;
    private static Spinner rooms;
    private static WifiInfo info;
    WifiManager wifiManager;
    ListView list;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rooms = (Spinner)findViewById(R.id.roomList);           //Znalezienie komponentów na GUI

        wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        serverTransmission = new ServerTransmission();
        serverTransmission.createConnect();
        serverTransmission.startConnection();
        info = wifiManager.getConnectionInfo();
    }

    public void onPause()
    {
        super.onPause();
    }

    public void onResume()
    {
        super.onRestart();
    }

    public void bntClick(View v)
    {
        switch(v.getId())
        {
            case R.id.bntSend: bntSend(); break;
            case R.id.bntGetRoom: bntGetRoom(); break;
            case R.id.bntScan: bntStartScan(); break;
            case R.id.bntStopScan: bntStopScan(); break;
        }
    }

    public static ServerTransmission getServerTransmission()
    {
        return serverTransmission;
    }

    public static String getRoom()
    {
        return rooms.getSelectedItem().toString();
    }

    public static WifiInfo getWifiInfo()
    {
        return info;
    }

    public void bntStartScan()
    {
        scanThread = new BackgroundScanThread(wifiManager);
        scanThread.start();
    }

    public void bntStopScan()
    {
        scanThread.stop();
    }

    public void bntSend()
    {
       // serverTransmission.sendList(sniff.getListToSend(), info.getMacAddress(), rooms.getSelectedItem().toString());
    }

    public void bntGetRoom()
    {
        serverTransmission.downloadRoom();
        while(serverTransmission.getRooms() == null) {               //Petla oczekujaca na odebranie informacji o pokojach
        }
        rooms.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,serverTransmission.getRooms()));
    }

}


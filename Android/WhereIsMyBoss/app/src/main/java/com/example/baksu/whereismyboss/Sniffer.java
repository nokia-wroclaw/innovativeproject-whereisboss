package com.example.baksu.whereismyboss;

import java.util.List;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

public class Sniffer{
    WifiManager wifiManager;
    List<ScanResult> wifiScanList;
    WifiScanReceier wifiReceier;
    String wifis[];


    public Sniffer(WifiManager wM) {
        this.wifiManager = wM;
        wifiReceier = new WifiScanReceier();
        wifiManager.startScan();
    }

    public String[] getList()
    {
        wifiManager.startScan();
        return wifis;
    }

    public WifiScanReceier getReceier(){
        return wifiReceier;
    }

    class WifiScanReceier extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            List<ScanResult> wifiScanList = wifiManager.getScanResults();
            wifis = new String[wifiScanList.size()];
            for(int i = 0; i< wifiScanList.size(); i++)
            {
                wifis[i] = ((wifiScanList.get(i)).toString());
            }

        }
    }
}

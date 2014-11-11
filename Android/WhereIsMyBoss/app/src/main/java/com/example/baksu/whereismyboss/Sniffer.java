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
    String wifis[][];
    String lista[];


    public Sniffer(WifiManager wM) {
        this.wifiManager = wM;
        wifiReceier = new WifiScanReceier();
        wifiManager.startScan();
    }

    public void startScan(){
        wifiManager.startScan();
    }

    public String[] getList()
    {
        return lista;
    }

    public String[][] getListToSend(){
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
            wifis = new String[4][wifiScanList.size()];
            lista = new String[wifiScanList.size()];
            for(int i = 0; i< wifiScanList.size(); i++)
            {
                lista[i] = wifiScanList.get(i).toString();
                wifis[0][i] = wifiScanList.get(i).SSID;
                wifis[1][i] = wifiScanList.get(i).BSSID;
                wifis[2][i] = Integer.toString(wifiScanList.get(i).level);
                wifis[3][i] = Integer.toString(wifiScanList.get(i).frequency);
            }
        }
    }
}

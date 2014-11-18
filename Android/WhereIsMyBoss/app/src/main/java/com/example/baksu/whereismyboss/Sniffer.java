package com.example.baksu.whereismyboss;

import java.util.List;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Sniffer{
    WifiManager wifiManager;
    List<ScanResult> wifiScanList;
    WifiScanReceier wifiReceier;
    String lista[];
    JSONArray arr;


    public Sniffer(WifiManager wM) {
        this.wifiManager = wM;
        wifiReceier = new WifiScanReceier();
        wifiManager.startScan();
    }

    public void startScan()
    {
        wifiManager.startScan();
    }

    public String[] getList()
    {
        return lista;
    }

    public JSONArray getListToSend()
    {
        return arr;
    }

    public WifiScanReceier getReceier()
    {
        return wifiReceier;
    }

    class WifiScanReceier extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            List<ScanResult> wifiScanList = wifiManager.getScanResults();
            lista = new String[wifiScanList.size()];
            arr = new JSONArray();
            for(int i = 0; i< wifiScanList.size(); i++)
            {
                lista[i] = wifiScanList.get(i).toString();
                JSONObject obj = new JSONObject();
                try {
                    obj.put("SSID", wifiScanList.get(i).SSID);
                    obj.put("BSSID", wifiScanList.get(i).BSSID);
                    obj.put("level", Integer.toString(wifiScanList.get(i).level));
                    obj.put("frequency", Integer.toString(wifiScanList.get(i).frequency));
                    arr.put(i,obj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
               // wifis[0][i] = wifiScanList.get(i).SSID;
               // wifis[1][i] = wifiScanList.get(i).BSSID;
              //  wifis[2][i] = Integer.toString(wifiScanList.get(i).level);
               // wifis[3][i] = Integer.toString(wifiScanList.get(i).frequency);
                //wifis[4][i] = Long.toString(wifiScanList.get(i).timestamp);
            }
        }
    }
}

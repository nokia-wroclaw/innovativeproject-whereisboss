package com.example.baksu.whereismyboss;

import java.util.List;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.net.wifi.*;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Sniffer{
    WifiManager wifiManager;
    List<ScanResult> wifiScanList;
    WifiScanReceier wifiReceier;
    String lista[] = null;
    JSONArray arr;


    public Sniffer(WifiManager wM) {        //Konstruktor tworzacy wifimanagera
        this.wifiManager = wM;
        wifiReceier = new WifiScanReceier();
        wifiManager.startScan();
    }

    public void startScan()               //Funkcja obsługująca zbieranie danych przez wifimanager
    {
        wifiManager.startScan();
        List<ScanResult> wifiScanList = wifiManager.getScanResults();
        lista = new String[wifiScanList.size()];
        arr = new JSONArray();
        for(int i = 0; i< wifiScanList.size(); i++)
        {
            lista[i] = wifiScanList.get(i).toString();
            JSONObject obj = new JSONObject();
            try {
                obj.put("ssid", wifiScanList.get(i).SSID);
                obj.put("bssid", wifiScanList.get(i).BSSID);
                obj.put("level", wifiScanList.get(i).level);
                obj.put("frequency", wifiScanList.get(i).frequency);
                obj.put("timestamp",System.currentTimeMillis());                    //Pobiera obecny czas jaki jest wyświetlony na device
                arr.put(i,obj);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }      //Rozpoczęcia scanu

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
        }
    }
}

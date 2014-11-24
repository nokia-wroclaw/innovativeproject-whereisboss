package com.example.baksu.whereismyboss;

import java.util.List;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
*   Klasa odpowiedzialna za skanowanie wszystkich dostępnych access pointów
 */
public class Sniffer{
    WifiManager wifiManager;
    List<ScanResult> wifiScanList;
    String lista[] = null;
    JSONArray arr;
    private List<ScanResult> all;
    private List<ScanResult> numbers;

/*
* Konstruktor wifiManagere'a
 */
    public Sniffer(WifiManager wM) {
        this.wifiManager = wM;
    }
/*
* Metoda odpowiedzialna za przeskanowanie wszystkich dostępnych access pointów w zasięgu sygnału
* i stworzenie na ich podstawie JSONArray w której są JSONObject.
*
 */
    public void startScan()
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
    }

/*
* Metoda odpowiedzialna za zbieranie przez jakiś czas informacji o access poitach i tworzenie tablicy
* JSONArray zawierająca uśrednione moce sygnałów.
 */
  /*  public void sredniaSkanu()
    {
        wifiManager.startScan();
        List<ScanResult> list = wifiManager.getScanResults();

        for(int i = 0; i< wifiScanList.size(); i++)
        {
            for(int j = 0; j<all.size(); j++)
            {

            }
        }
    }*/

    public String[] getList()
    {
        return lista;
    }

    public JSONArray getListToSend()
    {
        return arr;
    }

}

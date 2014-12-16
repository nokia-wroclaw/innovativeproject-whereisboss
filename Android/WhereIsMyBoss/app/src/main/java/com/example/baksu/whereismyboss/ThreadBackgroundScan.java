package com.example.baksu.whereismyboss;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Baksu on 2014-11-24.
 * Klasa odpowiedzialna za działanie skanowania w tle
 */
public class ThreadBackgroundScan implements Runnable {

    private Thread backgroundThread;
    private boolean running = true;
    private WifiManager wifiManager;
    private Sniffer sniffer;
    private ServerTransmission serverTransmission;
    private String building;
    private String floor;
    private String room;
    private final int ilosc = 10;

    private JSONArray arr;
    String lista[] = null;

    public ThreadBackgroundScan(WifiManager wM,ServerTransmission serverTransmission, String building, String floor, String room)
    {
        this.wifiManager = wM;
        this.serverTransmission = serverTransmission;
        this.building = building;
        this.floor = floor;
        this.room = room;
        sniffer = new Sniffer(wifiManager);
    }

    /**
     * Metoda odpowiedzialna za wystartowanie wątku
     */
    public void start() {
        if( backgroundThread == null ) {
            backgroundThread = new Thread( this );
            backgroundThread.start();
        }
    }

    /**
     * Metoda odpowiedzialna za zatrzymanie i zakończenie wątku
     */
    public void stop() {
        running = false;
        backgroundThread.interrupted();
    }

    /**
     * Metoda odpowiedzialna za działania jakie ma wykonać wątek po uruchomieniu
     */
    public void run() {
        while(running){
           /* arr = new JSONArray();
            try {
                for(int i = 0 ; i<ilosc; i++)
                {
                    oneScan();
                    backgroundThread.sleep(300);
                }
                averageLevel();
                backgroundThread.sleep(500);
                serverTransmission.sendList(arr, building,floor,room);
                backgroundThread.sleep(30*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            try
            {
               /* sniffer.clearArr();
                for(int i = 0 ; i<ilosc; i++) {
                    sniffer.oneScan();
                    backgroundThread.sleep(300);
                }
                sniffer.averageLevel();
                backgroundThread.sleep(500);
                serverTransmission.sendList(arr, "co","jest","grane");
                backgroundThread.sleep(30*1000);*/



                sniffer.startScan();
                backgroundThread.sleep(500);
                serverTransmission.sendList(sniffer.getListToSend(), building,floor,room);
                backgroundThread.sleep(30*1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void oneScan()
    {
        wifiManager.startScan();
        List<ScanResult> wifiScanList = wifiManager.getScanResults();
        lista = new String[wifiScanList.size()];

        for(int i = 0; i < wifiScanList.size(); i++) {
            for (int j = 0; j < arr.length(); j++) {
                try {
                    if (wifiScanList.get(i).BSSID.equals(arr.getJSONObject(j).getString("bssid")))
                    {
                        arr.getJSONObject(j).put("level", arr.getJSONObject(j).getInt("level") + wifiScanList.get(i).level);
                        arr.getJSONObject(j).put("raz", arr.getJSONObject(j).getInt("raz") + 1);
                    }else
                    {
                        JSONObject obj = new JSONObject();
                            obj.put("ssid", wifiScanList.get(i).SSID);
                            obj.put("bssid", wifiScanList.get(i).BSSID);
                            obj.put("level", wifiScanList.get(i).level);
                            obj.put("frequency", wifiScanList.get(i).frequency);
                            obj.put("timestamp",System.currentTimeMillis());                    //Pobiera obecny czas jaki jest wyświetlony na device
                            obj.put("raz",1);
                            arr.put(obj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void averageLevel()
    {
        for (int i = 0; i < arr.length(); i++) {
            try {
                arr.getJSONObject(i).put("level", arr.getJSONObject(i).getInt("level") / arr.getJSONObject(i).getInt("raz"));
                arr.getJSONObject(i).remove("raz");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

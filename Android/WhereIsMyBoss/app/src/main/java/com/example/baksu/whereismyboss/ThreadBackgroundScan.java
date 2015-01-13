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
            try
            {
                sniffer.clearArr();
                for (int i=1 ; i<=7; i++) {
                    System.out.println(sniffer.getListToSend());
                    sniffer.oneScan();
                    System.out.println("Po skanie : " + sniffer.getListToSend());
                    backgroundThread.sleep(300);
                }
                sniffer.averageLevel();
                backgroundThread.sleep(500);
                serverTransmission.sendList(sniffer.getListToSend(), building,floor,room);
                backgroundThread.sleep(30*1000);

                //sniffer.startScan();                      // stare skanowanie zostawione jak by średnia jednak nie działała
                //backgroundThread.sleep(500);
                //serverTransmission.sendList(sniffer.getListToSend(), building,floor,room);
                //backgroundThread.sleep(30*1000);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}

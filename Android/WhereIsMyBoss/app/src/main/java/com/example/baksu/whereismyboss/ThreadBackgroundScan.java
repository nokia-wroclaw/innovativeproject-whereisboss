package com.example.baksu.whereismyboss;

import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by Baksu on 2014-11-24.
 * Klasa odpowiedzialna za działanie skanowania w tle
 */
public class ThreadBackgroundScan implements Runnable {

    Thread backgroundThread;
    private boolean running = true;
    private WifiManager wifiManager;
    private Sniffer sniffer;
    private String room;

    public ThreadBackgroundScan(WifiManager wM, String room)
    {
        this.wifiManager = wM;
        sniffer = new Sniffer(wifiManager);
        this.room = room;
    }

    public void start() {
        if( backgroundThread == null ) {
            backgroundThread = new Thread( this );
            backgroundThread.start();
        }
        Log.i("Watek wystartował","");
    }

    public void stop() {
        running = false;
        backgroundThread.interrupted();
        Log.i("Watek się zatrzymal","");
    }

    public void run() {
        while(running){
            try
            {
                sniffer.startScan();
                backgroundThread.sleep(500);
                ActivityLogin.getServerTransmission().sendList(sniffer.getListToSend(), ActivityLogin.getWifiInfo().getMacAddress(),room);
                backgroundThread.sleep(30*1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}

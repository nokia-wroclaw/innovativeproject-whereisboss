package com.example.baksu.whereismyboss;

import android.app.ActivityManager;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by Baksu on 2014-11-30.
 */
public class ReportPositionThread implements Runnable
{
    Thread backgroundThread;
    private boolean running = true;
    private WifiManager wifiManager;
    private Sniffer sniffer;

    public ReportPositionThread(WifiManager wM)
    {
        this.wifiManager = wM;
        sniffer = new Sniffer(wifiManager);
    }

    public void start() {
        if( backgroundThread == null ) {
            backgroundThread = new Thread( this );
            backgroundThread.start();
        }
        Log.i("Watek wystartował", "");
    }

    public void stop() {
        running = false;
        Log.i("Watek się zatrzymal","");
    }

    public void run() {
        while(running){
            try
            {
                sniffer.startScan();
                backgroundThread.sleep(500);
               // MainActivity.getServerTransmission().sendList(sniffer.getListToSend(),MainActivity.getWifiInfo().getMacAddress(),room);
                backgroundThread.sleep(10000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}

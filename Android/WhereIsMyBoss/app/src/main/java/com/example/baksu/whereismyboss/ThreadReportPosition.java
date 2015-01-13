package com.example.baksu.whereismyboss;

import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by Baksu on 2014-11-30.
 */
public class ThreadReportPosition implements Runnable
{
    Thread backgroundThread;
    private boolean running = true;
    private WifiManager wifiManager;
    private Sniffer sniffer;
    private ServerTransmission serverTransmission;

    public ThreadReportPosition(WifiManager wM, ServerTransmission serverTransmission)
    {
        this.wifiManager = wM;
        sniffer = new Sniffer(wifiManager);
        this.serverTransmission = serverTransmission;
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
        backgroundThread.interrupted();
        Log.i("Watek się zatrzymal","");
    }

    public void run() {
        while(running){
            try
            {
                sniffer.startScan();
                backgroundThread.sleep(500);
                serverTransmission.sendReportPosition(sniffer.getListToSend());
                backgroundThread.sleep(180*1000); // ma być 180 * 1000
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}

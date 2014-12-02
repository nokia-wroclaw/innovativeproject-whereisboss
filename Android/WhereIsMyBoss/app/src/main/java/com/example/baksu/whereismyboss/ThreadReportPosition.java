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

    public ThreadReportPosition(WifiManager wM)
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
        backgroundThread.interrupted();
        Log.i("Watek się zatrzymal","");
    }

    public void run() {
        while(running){
            try
            {
                sniffer.startScan();
                backgroundThread.sleep(500);
                ActivityLogin.getServerTransmission().snedReportPosision(sniffer.getListToSend());
                backgroundThread.sleep(180*1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}

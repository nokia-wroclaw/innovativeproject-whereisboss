package com.example.baksu.whereismyboss;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

/**
 * Created by Baksu on 2014-12-02.
 */
public class ThreadBuildingDownloads implements Runnable {

    private ServerTransmission servTrans;
    private Thread thread;
   // private Handler handler;
    private boolean running = false;
    private Context context;

    public ThreadBuildingDownloads(ServerTransmission serverTransmission,Context context)
    {
        this.servTrans = serverTransmission;
        //this.handler = handler;
        this.context = context;
    }

    public void start()
    {
        if( thread == null ) {
            thread = new Thread( this );
        }
        if(!running) {
            thread.start();
        }
    }

    @Override
    public void run() {
        servTrans.downloadBuilding();
        while(servTrans.getBuildings() == null)             //ToDO: Dodac wyjątek jeśli ktoś nie ma żadnego budynku przypisanego
        {
            try {
                thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Intent loading = new Intent(context, ActivityScan.class);
        loading.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(loading);

        thread.interrupt();
    }
}

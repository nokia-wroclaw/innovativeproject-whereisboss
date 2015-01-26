package com.example.baksu.whereismyboss;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import org.json.JSONObject;

/**
 * Created by Baksu on 2015-01-21.
 */
public class ThreadSearch implements Runnable {

    private Thread thread;
    private ServerTransmission serverTransmission;
    private String name;
    private Context context;
    private Handler handler;
    private Message msg = Message.obtain();

    public ThreadSearch (ServerTransmission serverTransmission, String name,  Context context, Handler handler)
    {
        this.serverTransmission = serverTransmission;
        this.name = name;
        this.context = context;
        this.handler = handler;
    }

    /**
     * Metoda odpowiedzialna za wystartowanie wątku
     */
    public void start() {
        if( thread == null ) {
            thread = new Thread( this );
            thread.start();
        }
    }

    @Override
    public void run() {
        serverTransmission.search(name);

        JSONObject response = serverTransmission.getSearch();
        while(response  == null){
            try {
                thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            response = serverTransmission.getSearch();
        }

        if(response != null){
            msg.obj = "ready";
            msg.setTarget(handler);
            msg.sendToTarget();
        }

        thread.interrupt();
    }
}

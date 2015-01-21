package com.example.baksu.whereismyboss;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

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
            System.out.println("Udało się poprawnie ściągnąc mapę");
            // serverTransmission.createCookie();
            //Intent loading = new Intent(context, ActivityMain.class); //TODO wpisać tutaj odświerzenie mapy
            //loading.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(loading);
        /*}else if(response == 1){
            msg.obj = "Brak podanego użytkownika w bazie";
            msg.setTarget(handler);
            msg.sendToTarget();
        }else if(response == 2){
            msg.obj = "Błędne hasło";
            msg.setTarget(handler);
            msg.sendToTarget();*/
        }

        thread.interrupt();
    }
}

package com.example.baksu.whereismyboss;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Baksu on 2014-12-02.
 */
public class ThreadLogin implements Runnable {

    private ServerTransmission servTrans;
    private Thread thread;
    private String login;
    private String pass;
    private Context context;
    private boolean running = false;

    public ThreadLogin (ServerTransmission serverTransmission, String login, String pass, Context context)
    {
        this.servTrans = serverTransmission;
        this.login = login;
        this.pass = pass;
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
        servTrans.loginToServer(login, pass);
        int response = servTrans.getResponseLogin();
        while(response  == 20){
            try {
                thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            response = servTrans.getResponseLogin();
        }

        if(response == 0){
            Intent loading = new Intent(context, ActivityMain.class);
            loading.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(loading);
        }else if(response == 1){
            Toast.makeText(context, "Brak podanego użytkownika w bazie", Toast.LENGTH_LONG).show();
        }else if(response == 2){
            Toast.makeText(context, "Błędne hasło", Toast.LENGTH_LONG).show();
        }

        thread.interrupt();
    }
}

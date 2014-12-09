package com.example.baksu.whereismyboss;

import android.app.Activity;
import android.content.*;
import android.content.ServiceConnection;
import android.net.wifi.WifiInfo;
import android.os.Bundle;

import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.os.Message;

public class ActivityLogin extends Activity {

    private ThreadLogin threadLogin;
    private static ServerTransmission serverTransmission;
    private static Handler handler;

    //Obiekty GUI
    private TextView login;
    private TextView pass;
    private ProgressBar loading;
    private RelativeLayout mainLayout;

    WifiManager wifiManager;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Intent service = new Intent(this, ServerTransmission.class);
        this.startService(service);
        bindService(service, bService, this.BIND_AUTO_CREATE);
        //serverTransmission = new ServerTransmission();
        //serverTransmission.startConnection();


// Znalezienie komponentów na GUI
        loading = (ProgressBar) findViewById(R.id.loading_spinner);
        mainLayout = (RelativeLayout)findViewById(R.id.myRalaticeLayout);
        login = (TextView)findViewById(R.id.loginServer);
        pass = (TextView)findViewById(R.id.passServer);


        wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);


      // loading = new Intent(getApplicationContext(), LoadingActivity.class);

    }


    public void onDestroy()
    {
        super.onDestroy();
        this.unbindService(bService);
    }

    public void onPause()
    {
        super.onPause();
    }

    public void onResume()
    {
        loading.setVisibility(View.INVISIBLE);
        super.onRestart();
    }

    /**
     * Metoda odpowiedzialna za obsługę przycisku
     * @param v
     */
    public void bntClick(View v)
    {
        switch(v.getId())
        {
            case R.id.bntLoginServer: bntLogin(); break;
        }
    }

    /**
    * Metoda odpowiedzialna za obsługiwanie logowania użytkownika na serwer
    */
    private void bntLogin()
    {
        // Dwie linijki odpowiedzialne za chowanie klawiatury po przyciśnieciu Login
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);

        loading.setVisibility(View.VISIBLE);

        serverTransmission.startConnection();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String message = (String) msg.obj;
                Toast.makeText(ActivityLogin.this,message, Toast.LENGTH_LONG).show();
                loading.setVisibility(View.INVISIBLE);
            }
        };

        threadLogin = new ThreadLogin(serverTransmission,login.getText().toString(), pass.getText().toString(),getApplicationContext(),handler);
        threadLogin.start();
    }


    public static ServerTransmission getServerTransmission()
    {
        return serverTransmission;
    }

    ServiceConnection bService = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder binder) {
            ServerTransmission.MyBinder b = (ServerTransmission.MyBinder) binder;
            serverTransmission = b.getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            serverTransmission = null;
        }
    };
}


package com.example.baksu.whereismyboss;

import android.app.Activity;
import android.net.wifi.WifiInfo;
import android.os.Bundle;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityLogin extends Activity {

    private ThreadLogin threadLogin;
    private static ServerTransmission serverTransmission;
    private static WifiInfo info;
    WifiManager wifiManager;

    //Obiekty GUI
    private TextView login;
    private TextView pass;
    private ProgressBar loading;
    private RelativeLayout mainLayout;


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        serverTransmission = new ServerTransmission();
        serverTransmission.startConnection();



// Znalezienie komponentów na GUI
        loading = (ProgressBar) findViewById(R.id.loading_spinner);
        mainLayout = (RelativeLayout)findViewById(R.id.myRalaticeLayout);
        login = (TextView)findViewById(R.id.loginServer);
        pass = (TextView)findViewById(R.id.passServer);


        wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        info = wifiManager.getConnectionInfo();


      // loading = new Intent(getApplicationContext(), LoadingActivity.class);

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

/*
    private void bntStartScan()
    {
        threadScan = new ThreadBackgroundScan(wifiManager,rooms.getSelectedItem().toString());
        threadScan.start();
        Toast.makeText(context, "Skanowanie rozpoczęte", Toast.LENGTH_LONG).show();
    }

    private void bntStopScan()
    {
        threadScan.stop();
        Toast.makeText(context, "Skanowanie zostało przerwane", Toast.LENGTH_LONG).show();
    }
*/

    /**
    * Metoda odpowiedzialna za obsługiwanie logowania użytkownika na serwer
    */
    private void bntLogin()
    {
        // Dwie linijki odpowiedzialne za chowanie klawiatury po przyciśnieciu Login
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);

        loading.setVisibility(View.VISIBLE);

        threadLogin = new ThreadLogin(serverTransmission,login.getText().toString(), pass.getText().toString(),getApplicationContext());
        threadLogin.start();
    }

/*
    private void bntReportPos()
    {
        threadReport = new ThreadReportPosition(wifiManager);
        threadReport.start();
        Toast.makeText(context, "Reportowanie rozpoczęte", Toast.LENGTH_LONG).show();
    }

    private void bntStopReportPos()
    {
        threadReport.stop();
        Toast.makeText(context, "Reportowanie zostało przerwane", Toast.LENGTH_LONG).show();
    }
    */

    public static ServerTransmission getServerTransmission()
    {
        return serverTransmission;
    }

    public static WifiInfo getWifiInfo()
    {
        return info;
    }

}


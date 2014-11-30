package com.example.baksu.whereismyboss;

import android.app.Activity;
import android.net.wifi.WifiInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Message;
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

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends Activity {

    private BackgroundScanThread scanThread;
    private static ServerTransmission serverTransmission;
    private static WifiInfo info;
    WifiManager wifiManager;
    private Context context;
    private String userLogin;

    //Obiekty GUI
    private TextView login;
    private TextView pass;
    private ListView list;
    private ProgressBar loading;
    private RelativeLayout mainLayout;
    private Button bntLogIn;
    private Button bntStartScan;
    private Button bntStopScan;
    private Button bntLogOut;
    private Button bntReport;
    private Spinner floors;
    private Spinner rooms;


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
//        rooms = (Spinner)findViewById(R.id.roomList);
// Znalezienie komponentów na GUI
        loading = (ProgressBar) findViewById(R.id.loading_spinner);
        mainLayout = (RelativeLayout)findViewById(R.id.myRalaticeLayout);
        bntLogIn = (Button)findViewById(R.id.bntLoginServer);
        bntStartScan = (Button) findViewById(R.id.bntStartScan);
        bntStopScan = (Button) findViewById(R.id.bntStopScan);
        bntLogOut = (Button) findViewById(R.id.bntLogoutServer);
        //bntReport = (Button) findViewById(R.id.bntReportPos);
        login = (TextView)findViewById(R.id.loginServer);
        pass = (TextView)findViewById(R.id.passServer);
        floors = (Spinner)findViewById(R.id.floors);
        rooms = (Spinner)findViewById(R.id.rooms);


        serverTransmission = new ServerTransmission();
        wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        info = wifiManager.getConnectionInfo();

    }

    public void onPause()
    {
        super.onPause();
    }

    public void onResume()
    {
        super.onRestart();
    }

    /**
    * Metoda odpowiedzialna za klikanie przycisków
     */
    public void bntClick(View v)
    {
        switch(v.getId())
        {
            case R.id.bntLoginServer: bntLogin(); break;
            case R.id.bntStartScan: bntStartScan(); break;
            case R.id.bntStopScan: bntStopScan(); break;
            case R.id.bntLogoutServer: bntLogOutServer(); break;
         //   case R.id.bntReportPos: bntReportPos(); break;
        }
    }

    /**
    * Metoda odpowiedzialna za obsługę przycisku rozpoczęcia skanowania
     */
    public void bntStartScan()
    {
        scanThread = new BackgroundScanThread(wifiManager,rooms.getSelectedItem().toString());
        scanThread.start();
        Toast.makeText(context, "Skanowanie rozpoczęte", Toast.LENGTH_LONG).show();
    }

    /**
    * Metoda odpowiedzialna za obsługę przycisku zakończenia skanowania
     */
    public void bntStopScan()
    {
        scanThread.stop();
        Toast.makeText(context, "Skanowanie zostało przerwane", Toast.LENGTH_LONG).show();
    }

    /**
    * Metoda odpowiedzialna za obsługiwanie logowania użytkownika na serwer
    */
    public void bntLogin()
    {
        // Dwie linijki odpowiedzialne za chowanie klawiatury po przyciśnieciu Login
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
        //loading.setVisibility(View.VISIBLE);

        int response = 20;
        //Ropoczęcie połączenia z serwerem;
        serverTransmission.startConnection();
        userLogin = login.getText().toString();
        serverTransmission.loginToServer(userLogin, pass.getText().toString());


        while((response = serverTransmission.getResponseLogin()) == 20){        //TODO:  zmienić na asynchroniczne łączenie

        }
      //  loading.setVisibility(View.INVISIBLE);
        Log.e("Po", Integer.toString(response));
        if (response == 0) {
            // Wyświetlenie komunikatu oraz pokazanie i ukrycie przycisków
            Toast.makeText(context, "Połączenie nawiazane", Toast.LENGTH_LONG).show();
            login.setVisibility(View.INVISIBLE);
            pass.setVisibility(View.INVISIBLE);
            bntLogIn.setVisibility(View.INVISIBLE);
            bntStopScan.setVisibility(View.VISIBLE);
            bntStartScan.setVisibility(View.VISIBLE);
            bntLogOut.setVisibility(View.VISIBLE);
            floors.setVisibility(View.VISIBLE);
            rooms.setVisibility(View.VISIBLE);
        //Pobranie planu budynku
            serverTransmission.downloadBuilding(userLogin);
            while(serverTransmission.getRooms() == null) {               //Petla oczekujaca na odebranie informacji o pokojach
            }
            rooms.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,serverTransmission.getRooms()));

        }else if (response == 1) {
            Toast.makeText(context, "Brak podanego użytkownika w bazie", Toast.LENGTH_LONG).show();
        }else if (response == 2) {
            Toast.makeText(context, "Błędne hasło", Toast.LENGTH_LONG).show();
        }
    }

    /**
    * Metoda odpowiedzialna za obłsugę rozłączania się z serwerem
     */
    public void bntLogOutServer()
    {
        serverTransmission.endConnection();
        Toast.makeText(context, "Zostałeś wylogowany", Toast.LENGTH_LONG).show();
        login.setVisibility(View.VISIBLE);
        pass.setVisibility(View.VISIBLE);
        bntLogIn.setVisibility(View.VISIBLE);
        bntStopScan.setVisibility(View.INVISIBLE);
        bntStartScan.setVisibility(View.INVISIBLE);
        bntLogOut.setVisibility(View.INVISIBLE);
        floors.setVisibility(View.INVISIBLE);
        rooms.setVisibility(View.INVISIBLE);

        userLogin = null;
        login.setText("");
        pass.setText("");
    }

    /**
     * Metoda odpowiedzialna za wysyłanie obecnej pozycji użytkownika co jakiś czas.
     */
    public void bntReportPos()
    {

    }

    public static ServerTransmission getServerTransmission()
    {
        return serverTransmission;
    }

    public static WifiInfo getWifiInfo()
    {
        return info;
    }

}


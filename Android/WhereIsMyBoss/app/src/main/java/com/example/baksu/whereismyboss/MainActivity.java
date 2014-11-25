package com.example.baksu.whereismyboss;

import android.app.Activity;
import android.net.wifi.WifiInfo;
import android.os.Bundle;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private BackgroundScanThread scanThread;
    private static ServerTransmission serverTransmission;
    private static WifiInfo info;
    WifiManager wifiManager;
    private Context context;

    //Obiekty GUI
    private TextView login;
    private TextView pass;
    private ListView list;
    private Spinner rooms;
    private ProgressBar loading;
    private RelativeLayout mainLayout;
    private Button bntLogIn;
    private Button bntStartScan;
    private Button bntStopScan;
    private Button bntLogOut;


    public void onCreate(Bundle savedInstanceState) {
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
        login = (TextView)findViewById(R.id.loginServer);
        pass = (TextView)findViewById(R.id.passServer);


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

    public void bntClick(View v)
    {
        switch(v.getId())
        {
            case R.id.bntLoginServer: bntLogin(); break;
            case R.id.bntStartScan: bntStartScan(); break;
            case R.id.bntStopScan: bntStopScan(); break;
//            case R.id.bntGetRoom: bntGetRoom(); break;
//
        }
    }

    public static ServerTransmission getServerTransmission()
    {
        return serverTransmission;
    }

    public static WifiInfo getWifiInfo()
    {
        return info;
    }

    public void bntStartScan()
    {
        scanThread = new BackgroundScanThread(wifiManager,rooms.getSelectedItem().toString());
        scanThread.start();
    }

    public void bntStopScan()
    {
        scanThread.stop();
    }

    public void bntSend()
    {
       // serverTransmission.sendList(sniff.getListToSend(), info.getMacAddress(), rooms.getSelectedItem().toString());
    }

    public void bntGetRoom()
    {
        serverTransmission.downloadRoom();
        while(serverTransmission.getRooms() == null) {               //Petla oczekujaca na odebranie informacji o pokojach
        }
        rooms.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,serverTransmission.getRooms()));
    }

    public void bntLogin()
    {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
        loading.setVisibility(View.VISIBLE);

        int response;
        serverTransmission.createConnect();
        serverTransmission.startConnection();

        //Log.e(login.getText().toString(), pass.getText().toString());

        serverTransmission.loginToServer(login.getText().toString(), pass.getText().toString());
        response = serverTransmission.getResponseLogin();
      //  Log.e("Przed", Integer.toString(response));

        while((response = serverTransmission.getResponseLogin()) == 20){
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        loading.setVisibility(View.INVISIBLE);
      //  Log.e("Po", Integer.toString(response));
        if (response == 0) {
            Toast.makeText(context, "Połączenie nawiazane", Toast.LENGTH_LONG).show();
            login.setVisibility(View.INVISIBLE);
            pass.setVisibility(View.INVISIBLE);
            bntLogIn.setVisibility(View.INVISIBLE);
            bntStopScan.setVisibility(View.VISIBLE);
            bntStopScan.setVisibility(View.VISIBLE);
            bntLogOut.setVisibility(View.VISIBLE);
        }

        if (response == 1)
            Toast.makeText(context, "Brak podanego użytkownika w bazie", Toast.LENGTH_LONG).show();

        if (response == 2)
            Toast.makeText(context, "Błędne hasło", Toast.LENGTH_LONG).show();

//        login.setVisibility(View.INVISIBLE);
//        pass.setVisibility(View.INVISIBLE);
    }

}


package com.example.baksu.whereismyboss;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Baksu on 2014-12-02.
 */
public class ActivityScan extends Activity {

    private Spinner spinBuilding;
    private Spinner spinFloor;
    private Spinner spinRoom;
    private ServerTransmission serverTransmission;
    private ThreadBackgroundScan threadScan;
    private Context context;
    private WifiManager wifiManager;

    List<Building> buildings;

    public int pos1,pos2;                   //TODO: wszystko trzy zmienne do zmiany
    ArrayAdapter<String> adp1,adp2,adp3;
    List<String> l1,l2,l3;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_activity);

        context = getApplicationContext();
        wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);

        spinBuilding = (Spinner)findViewById(R.id.spinBuildings);
        spinFloor = (Spinner)findViewById(R.id.spinFloors);
        spinRoom = (Spinner)findViewById(R.id.spinRooms);

        Intent service = new Intent(this, ServerTransmission.class);
        bindService(service, bService, this.BIND_AUTO_CREATE);

    }

    public void bntClick(View v)
    {
        switch(v.getId())
        {
            case R.id.bntGetBuildings: test(); break;
            case R.id.bntStartScan: startScan(); break;
            case R.id.bntStopScan: stopScan(); break;
        }
    }

    private void startScan()
    {
        threadScan = new ThreadBackgroundScan(wifiManager,rooms.getSelectedItem().toString());
        threadScan.start();
        Toast.makeText(context, "Skanowanie rozpoczęte", Toast.LENGTH_LONG).show();
    }

    private void stopScan()
    {
        threadScan.stop();
        Toast.makeText(context, "Skanowanie zostało przerwane", Toast.LENGTH_LONG).show();
    }

    public void test()          //TODO: zmienić żeby ta funkcja odpalała się nie pod przyciskiem ale odrazu po onCreate
    {
        Log.e("Pytanie activ", serverTransmission.tescik);
        buildings = serverTransmission.getBuildings();

        l1=new ArrayList<String>();
        for(int i=0 ; i< buildings.size(); i++)
        {
            l1.add(buildings.get(i).name);
        }
        adp1=new ArrayAdapter<String> (this,android.R.layout.simple_dropdown_item_1line,l1);
        adp1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinBuilding.setAdapter(adp1);

        spinBuilding.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pos1 = i;

                add1();

                spinFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        pos2 = i;

                        add2();
                    }

                    private void add2()
                    {

                        l3 = new ArrayList<String>(buildings.get(pos1).floors.get(pos2).rooms);

                        adp3=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line,l3);
                        spinRoom.setAdapter(adp3);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            private void add1()
            {
                l2 = new ArrayList<String>();
                for(int i = 0 ; i<buildings.get(pos1).floors.size(); i++)
                {
                    l2.add(buildings.get(pos1).floors.get(i).name);
                }
                adp2=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line,l2);
                spinFloor.setAdapter(adp2);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }



/*    public void test()
    {

        test[0][0][0] = "budynek 1";
        test[1][0][0] = "budynek 2";
        test[2][0][0] = "budynek 3";

        test[0][1][0] = "floor 11";
        test[0][1][1] = "room111";
        test[0][1][2] = "room112";
        test[0][1][3] = "room113";

        test[0][2][0] = "floor 12";
        test[0][2][1] = "room121";
        test[0][2][2] = "room122";
        test[0][2][3] = "room123";

        test[0][3][0] = "floor 13";
        test[1][1][0] = "floor 21";
        test[1][2][0] = "floor 22";
        test[1][3][0] = "floor 23";
        test[2][1][0] = "floor 31";
        test[2][2][0] = "floor 32";
        test[2][3][0] = "floor 33";




        l1=new ArrayList<String>();
        l1.add(test[0][0][0]);
        l1.add(test[1][0][0]);
        l1.add(test[2][0][0]);

        adp1=new ArrayAdapter<String> (this,android.R.layout.simple_dropdown_item_1line,l1);
        adp1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinBuilding.setAdapter(adp1);

        spinBuilding.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pos1 = i;

                add1();

                spinFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        pos2 = i;

                        add2();
                    }

                    private void add2()
                    {
                        l3 = new ArrayList<String>();
                        for(int i = 1 ; i<=3; i++)
                        {
                            l3.add(test[pos1][pos2][i]);
                        }
                        adp3=new ArrayAdapter<String>(getApplicationContext(),
                                android.R.layout.simple_dropdown_item_1line,l3);
                        spinRoom.setAdapter(adp3);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            private void add1()
            {
                l2 = new ArrayList<String>();
                for(int i = 1 ; i<=3; i++)
                {
                    l2.add(test[pos1][i][0]);
                }
                adp2=new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_dropdown_item_1line,l2);
                spinFloor.setAdapter(adp2);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }*/

    @Override
    public void onBackPressed() {
        this.finish();
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

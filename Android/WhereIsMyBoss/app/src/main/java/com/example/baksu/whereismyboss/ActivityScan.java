package com.example.baksu.whereismyboss;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiInfo;
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
    private String building;
    private String floor;
    private String room;

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

    @Override
    public void onBackPressed() {
        this.finish();
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
        building = null;
        floor = null;
        room = null;
        int i=0,j=0,k=0;
        while (building == null)
        {
            if(buildings.get(i).name.equals(spinBuilding.getSelectedItem().toString()))
                building = buildings.get(i).id;
            else
                i++;
        }
        while (floor == null)
        {
            if(buildings.get(i).floors[j].name.equals(spinFloor.getSelectedItem().toString()))
                floor = buildings.get(i).floors[j].id;
            else
                j++;
        }
        while (room == null)
        {
            if(buildings.get(i).floors[j].rooms[k].name.equals(spinRoom.getSelectedItem().toString()))
                room = buildings.get(i).floors[j].rooms[k].id;
            else
                k++;
        }


        threadScan = new ThreadBackgroundScan(wifiManager,serverTransmission,building,floor,room);
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
                        l3 = new ArrayList<String>();
                        for(int i = 0 ; i<buildings.get(pos1).floors[pos2].rooms.length; i++)
                        {
                            l3.add(buildings.get(pos1).floors[pos2].rooms[i].name);
                        }

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
                for(int i = 0 ; i<buildings.get(pos1).floors.length; i++)
                {
                    l2.add(buildings.get(pos1).floors[i].name);
                }
                adp2=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line,l2);
                spinFloor.setAdapter(adp2);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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

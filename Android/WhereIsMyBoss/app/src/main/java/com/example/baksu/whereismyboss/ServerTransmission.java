package com.example.baksu.whereismyboss;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Baksu on 2014-11-11.
 */
public class ServerTransmission extends Service
{
    private final IBinder mBinder = new MyBinder();
    private Socket socket;
    private int response;
    private List<Building> buildings;
    private Floor[] floors;
    private Room[] rooms;

    /**
    * Konstruktor odpowiedzialny za stworzenie połączenia z serwerem
     */
    public ServerTransmission()
    {
        try
        {
            //this.socket = IO.socket("http://stormy-shore-9392.herokuapp.com/");
            //this.socket = IO.socket("https://whereisboss.herokuapp.com");
           this.socket = IO.socket("https://whereisbosstest.herokuapp.com");
          
          //  socket.connect();

        }catch(URISyntaxException e)
        {
              //ToDo: dodać wyjatek
        }
    }

    public int onStartCommand()
    {
        try
        {
            //this.socket = IO.socket("https://whereisboss.herokuapp.com");
            this.socket = IO.socket("https://whereisbosstest.herokuapp.com");
            //this.socket = IO.socket("http://stormy-shore-9392.herokuapp.com/");
        }catch(URISyntaxException e)
        {
        //TODO: Dodać jakiś wyjątek
        }

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        ServerTransmission getService() {
            return ServerTransmission.this;
        }
    }

    /**
    * Rozpoczęcie połączenia z serwerem
     */
    public void startConnection()           // Rozpoczęcie połączenia z serwerem
    {
        socket.connect();
    }

    /**
    * Zakończenie połączenia z serwerem
    */
    public void endConnection()
    {
        socket.disconnect();
    }

    /**
     * Metoda odpowiedzialna za przesyłanie wszystkich access pointów
     * @param list
     * @param building
     * @param floor
     * @param room
     */
    public void sendList(JSONArray list, String building, String floor, String room)
    {
        list.put(building);
        list.put(floor);
        list.put(room);
        socket.emit("sendAP", list);
    }

    public void sendReportPosition(JSONArray list)
    {
        socket.emit("setPosition",list);
    }

/**
 * Funkcja odpowiedzialna za pobieranie wszystkich danych o budynkach
 */
    public void downloadBuilding()
    {
        buildings = null;

        socket.emit("getAll","t3");            //TODO: Zminić, żeby nie wpisywać loginu !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        socket.on("getAll", new Emitter.Listener() {
            @Override

            public void call(Object... args) {
                JSONArray info = (JSONArray)args[0];
                JSONArray floorsJA = null;
                JSONArray roomsJA = null;
                buildings = new ArrayList<Building>();
                
                try {
                    for(int i = 0; i < info.length(); i++)
                    {
                        floorsJA = (JSONArray) info.getJSONObject(i).get("floors");
                        floors = new Floor[floorsJA.length()];
                        for(int j = 0; j < floorsJA.length(); j++)
                        {
                            roomsJA = (JSONArray) floorsJA.getJSONObject(j).get("rooms");
                            rooms = new Room[roomsJA.length()];
                            for(int k = 0 ; k < roomsJA.length(); k++)
                            {
                                Log.e("name",roomsJA.getJSONObject(k).getString("room"));
                                Log.e("id",roomsJA.getJSONObject(k).getString("_id"));
                                rooms[k] = new Room(roomsJA.getJSONObject(k).getString("room"),roomsJA.getJSONObject(k).getString("_id"));
                            }
                            floors[j] = new Floor(floorsJA.getJSONObject(j).getString("floor"),floorsJA.getJSONObject(j).getString("_id"),rooms);
                        }
                        buildings.add(new Building(info.getJSONObject(i).getString("building"),info.getJSONObject(i).getString("_id"),floors));

                        /*building = info.getJSONObject(0).getString("building");
                        roomsJA = (JSONArray)info.getJSONObject(0).get("rooms");
                        List<String> rooms =  new ArrayList<String>();
                        for (int j=0; j < roomsJA.length() ;j++)
                        {
                            rooms.add(roomsJA.getString(j));
                        }
                        room = new Room();
                        floor = new Floor(info.getJSONObject(0).getString("floor"),rooms);

                        buildings.add(new Building(building,floor));*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



                /*try {
                    rooms = floor.getJSONArray("rooms");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ServerTransmission.this.rooms = new String[rooms.length()];

                for(int i = 0; i < rooms.length(); i++)
                {
                    try {
                        ServerTransmission.this.rooms[i] = rooms.getJSONObject(i).getString("name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }*/

        });
    }
    /*
    *Metoda odpowiedzialana za logowanie do serwera
     */
    public void loginToServer(String login, String pass)
    {
        response = 20;
        JSONArray log = new JSONArray();
        log.put(login);
        log.put(pass);

        socket.emit("LogIn",log);
        socket.on("LogIn", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                response = (Integer)args[0];
            }
        });
    }

    public int getResponseLogin()
    {
        return response;
    }

    public List<Building>getBuildings()
    {
        return buildings;
    }

}

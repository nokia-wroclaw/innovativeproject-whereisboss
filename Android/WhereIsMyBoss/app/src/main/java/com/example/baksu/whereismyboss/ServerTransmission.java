package com.example.baksu.whereismyboss;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.engineio.client.Transport;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Manager;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieManager;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private String cookie;
    private JSONObject search;
    //private final String host = "https://whereisbosstest.herokuapp.com";
   private final String host = "https://whereisboss.herokuapp.com";

    /**
    * Konstruktor odpowiedzialny za stworzenie połączenia z serwerem
     */
    public ServerTransmission()
    {
        cookie = "hi";
        try
        {
           this.socket = IO.socket(host);
          
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
            this.socket = IO.socket(host);
        }catch(URISyntaxException e)
        {
        //TODO: Dodać jakiś wyjątek
        }

        return Service.START_NOT_STICKY;
    }

    public void destroy()
    {
            stopSelf();
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
    public void startConnection()
    {
        createCookie();
        socket.connect();
    }

    public void createCookie()
    {
        socket.io().on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Transport transport = (Transport) args[0];
                transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        @SuppressWarnings("unchecked")
                        Map<String, String> headers = (Map<String, String>) args[0];
                        // set header
                        //Log.e("robi sie to ? : ",cookie);
                        headers.put("Set-Cookie", cookie);
                    }
                }).on(Transport.EVENT_RESPONSE_HEADERS, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        @SuppressWarnings("unchecked")
                        Map<String, String> headers = (Map<String, String>) args[0];
                        // get header
                        System.out.println(cookie);
                        if (cookie != null && !headers.isEmpty() && cookie.equals("hi")) {
                            cookie = headers.get("Set-Cookie");
                        }
                        if(!headers.isEmpty()){
                            if(!cookie.equals(headers.get("Set-Cookie"))){
                                System.out.println("Sesja została przerwana, powrót do activity logowania");
                            }
                        }
                        //System.out.println("Wypisało header: ");
                             System.out.println("Wypisało header: " + headers.toString());
//                        Log.e("Set-Cookie", cookie);
                    }
                });
            }
        });
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

        socket.emit("getAll");
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
                               // Log.e("name",roomsJA.getJSONObject(k).getString("room"));
                               // Log.e("id",roomsJA.getJSONObject(k).getString("_id"));
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

    public void search(String name){
        search = null;
        socket.emit("SearchAndroid",name);
        socket.on("SearchAndroid", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                search = (JSONObject)args[0];
            }
        });
    }

    public void logout(){
        System.out.println("zostales wylogowany");
        endConnection();
        Intent intent = new Intent(this, ActivityLogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public int getResponseLogin()
    {
        return response;
    }

    public List<Building>getBuildings()
    {
        return buildings;
    }

    public JSONObject getSearch(){
        return search;
    }

}

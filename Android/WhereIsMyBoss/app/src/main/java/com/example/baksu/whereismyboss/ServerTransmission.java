package com.example.baksu.whereismyboss;

import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Baksu on 2014-11-11.
 */
public class ServerTransmission
{
    private String rooms[] = null;
    private String floors[] = null;
    Socket socket;
    private String text = "brak polaczaenia";
    private int response;

    /*
    * Konstruktor odpowiedzialny za stworzenie połączenia z serwerem
     */
    public ServerTransmission()
    {
        try
        {
           this.socket = IO.socket("https://whereisboss.herokuapp.com");
           //this.socket = IO.socket("https://whereisbosstest.herokuapp.com");

        }catch(URISyntaxException e)
        {
               text = "jakis dziwny blad";
        }
    }
/*
* Rozpoczęcie połączenia z serwerem
 */
    public void startConnection()           // Rozpoczęcie połączenia z serwerem
    {
        socket.connect();
    }
/*
* Zakończenie połączenia z serwerem
 */
    public void endConnection()
    {
        socket.disconnect();
    }

    public void sendList(JSONArray list, String mac, String room)              //Funkcja odpowiedzialna za przesłanie wszystkich access pointów
    {
        list.put(mac);
        list.put(room);
        socket.emit("foo", list);
    }

    public void snedReportPosision(JSONArray list)
    {
        socket.emit("setPosition",list);
    }

/*
* Funkcja odpowiedzialna za pobieranie wszystkich danych o budynku
 */
    public void downloadBuilding(String login)
    {
        rooms = null;
        socket.emit("getDataTEST",login);              //TODO Zmienić potem na normalne getData
        socket.on("getData", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray rooms = new JSONArray();
                JSONObject floor = (JSONObject)args[0];
                try {
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
                }

            }
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

    public String[] getRooms()
    {
        return rooms;
    }

    public String[] getFloors()
    {
        return floors;
    }

    public String getText()
    {
        return text;
    }

}

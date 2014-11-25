package com.example.baksu.whereismyboss;

import android.os.AsyncTask;
import android.os.Debug;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.*;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * Created by Baksu on 2014-11-11.
 */
public class ServerTransmission
{
    private String lista[] = null;
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

    public void createConnect(){                                        //nie wiem czy ta funkcja jest potrzebna ?
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args)
            {
                text = "wyslane";
            }
        }).on("event", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                text = "dostałem: " + args[0];
                //socket.disconnect();
            }
        }).on(socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                text = "rozłączylo";
            }
        });
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

    public void downloadRoom()                                  // Funckaj odpowiedzialna za pobieranie pokojów
    {
        socket.emit("getDataTEST","test");              //TODO Zmienić potem na normalne getData
        socket.on("getData", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray rooms = null;
                JSONObject floor = (JSONObject)args[0];
                try {
                    rooms = floor.getJSONArray("rooms");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                lista = new String[rooms.length()];

                for(int i = 0; i < rooms.length(); i++)
                {
                    try {
                        lista[i] = rooms.getJSONObject(i).getString("name");
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
    public int loginToServer(String login, String pass)
    {
        response = 20;
        socket.emit("LogIn",login,pass);
        socket.on("LogIn", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                response = (Integer)args[0];
            }
        });

        return response;
    }

    public int getResponseLogin()
    {
        return response;
    }

    public String[] getRooms()
    {
        return lista;
    }

    public String getText()
    {
        return text;
    }

}

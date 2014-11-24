package com.example.baksu.whereismyboss;

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

    public ServerTransmission()                     //Stworzenie socketa z serwerem
    {
        try

        {
           this.socket = IO.socket("https://whereisboss.herokuapp.com");
           //this.socket = IO.socket("https://whereisbosstest.herokuapp.com");

        }catch(URISyntaxException e) {
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

    public void startConnection()           // Rozpoczęcie połączenia z serwerem
    {
        socket.connect();
    }

    public String getText()
    {
        return text;
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

    public String[] getRooms()
    {
        return lista;
    }

}

package com.example.baksu.whereismyboss;

import android.os.Debug;

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

    Socket socket;
    private String text = "brak polaczaenia";

    public ServerTransmission()
    {
        try
        {
            this.socket = IO.socket("https://intense-plateau-3634.herokuapp.com");

        }catch(URISyntaxException e) {
               text = "jakis dziwny blad";
        }
    }

    public void createConnect(){
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

    public void startConnection()
    {
        socket.connect();
    }

    public String getText()
    {
        return text;
    }

    public void sendList(JSONArray list)
    {
        list.put("Pokoj 3");
        socket.emit("foo", list);
    }

}

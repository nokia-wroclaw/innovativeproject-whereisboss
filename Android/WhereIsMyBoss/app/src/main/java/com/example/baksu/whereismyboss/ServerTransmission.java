package com.example.baksu.whereismyboss;

import android.os.Debug;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
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
            this.socket = IO.socket("https://whereisboss.herokuapp.com");

        }catch(URISyntaxException e) {
               text = "jakis dziwny blad";
        }
    }

    public void createConnect(final String[][] list){
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args)
            {
                JSONObject obj = new JSONObject();
                try
                {
                    obj.put("ssid", list[0] );
                    obj.put("bssid", list[1] );
                    obj.put("level", list[2] );
                    obj.put("frequency", list[3] );
                  //  obj.put("timestamp", list[4] );
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
                socket.emit("foo", obj);
            }
        }).on("event", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                text = "dostałem: " + args[0];
                socket.disconnect();
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

}

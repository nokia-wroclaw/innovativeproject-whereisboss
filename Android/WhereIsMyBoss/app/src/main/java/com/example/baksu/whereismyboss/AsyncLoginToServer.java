package com.example.baksu.whereismyboss;

import android.os.AsyncTask;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;

/**
 * Created by Baksu on 2014-12-01.
 */

public class AsyncLoginToServer extends AsyncTask<Void, Void, Integer>
{
    private Socket socket;
    String TAG = "Tag:";
    private int response = 20;
    private JSONArray log;

    public AsyncLoginToServer(Socket socket, JSONArray log)
    {
        this.socket = socket;
        this.log = log;
    }

    protected Integer doInBackground(Void... params)
    {
        socket.emit("LogIn",log);
        socket.on("LogIn", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                response = (Integer)args[0];
            }
        });

        while (response != 1){

        }

        return response;
    }

    protected void onPostExecute(Integer result) {
        if (result == 1) {
            Log.i(TAG, "onPostExecute: udało się ściągnąć.");

        }
    }
}
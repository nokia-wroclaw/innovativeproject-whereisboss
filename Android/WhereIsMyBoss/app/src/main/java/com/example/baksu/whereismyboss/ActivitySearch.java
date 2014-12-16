package com.example.baksu.whereismyboss;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ImageView;

/**
 * Created by Baksu on 2014-12-13.
 */
public class ActivitySearch extends Activity {

    private ServerTransmission serverTransmission;
    private Context context;
    ImageView map_image;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        context = getApplicationContext();
        map_image = (ImageView)findViewById(R.id.main_imagemap);

        Intent service = new Intent(this, ServerTransmission.class);
        bindService(service, bService, this.BIND_AUTO_CREATE);

    }

    /**
     * Metoda odpowiedzialna za obsługę przyciusku back
     */
    @Override
    public void onBackPressed() {
        serverTransmission.endConnection();
        this.unbindService(bService);
        this.finish();
    }

    /**
     * Metoda odpowiedzialna za podpiecie serwisu do Activity
     */
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

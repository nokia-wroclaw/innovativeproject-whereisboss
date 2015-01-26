package com.example.baksu.whereismyboss;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

/**
 * Created by Baksu on 2014-12-13.
 */
public class ActivitySearch extends Activity {

    private ServerTransmission serverTransmission;
    private Context context;
    ImageView imageView;
    private String string = "https://s3.amazonaws.com/whereisboss-assets/54bf6f4244a96b0300d33cbe.jpeg";
    Drawable drawable =null;

    static ProgressDialog pd;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);



        pd = new ProgressDialog(this);
        pd.setMessage("Downloading map");
        imageView = (ImageView) findViewById(R.id.main_imagemap);

        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                new DownloadImageTask(imageView).execute(string);


                ShowMap.data = "{\"rooms\":[{\"name\":\"RED\",\"coordinates\":[[54,237],[52,387],[229,392],[228,236]]},{\"name\":\"YELLOW\",\"coordinates\":[[228,236],[230,393],[457,392],[456,235]]},{\"name\":\"BLUE\",\"coordinates\":[[456,235],[457,392],[633,393],[633,234]]}]}";
                ShowMap.room = "BLUE";


            }
        });


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

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    public static Bitmap mIcon11;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        ActivitySearch.pd.show();
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        ActivitySearch.pd.dismiss();
        bmImage.setImageBitmap(result);
    }
}

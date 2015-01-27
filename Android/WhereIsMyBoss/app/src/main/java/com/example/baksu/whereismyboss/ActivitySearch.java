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
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private JSONObject dataJSON;  //parametr
    public static ProgressDialog pd;
    private ThreadSearch thredSearch;
    private EditText nameEdit;
    private Handler handler;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        pd = new ProgressDialog(this);
        pd.setMessage("Downloading map");
        imageView = (ImageView) findViewById(R.id.main_imagemap);
        nameEdit = (EditText) findViewById(R.id.nameSearch);

        Intent service = new Intent(this, ServerTransmission.class);
        bindService(service, bService, this.BIND_AUTO_CREATE);
    }

    /**
     * Metoda odpowiedzialna za obsługę przyciusku back
     */
    @Override
    public void onBackPressed() {
        this.finish();
    }

    public void onDestroy()
    {
        super.onDestroy();
        serverTransmission.destroy();
        this.unbindService(bService);
    }

    public void bntClick(View v)
    {
        switch(v.getId())
        {
            case R.id.bntSearch: bntSearch(); break;
            case R.id.bntLogout: bntLogout(); break;
        }
    }

    public void bntSearch(){

            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    String message = (String) msg.obj;
                    if(message.equals("ready")){
                        showMap();
                    }else{
                        Toast.makeText(ActivitySearch.this,"ERROR", Toast.LENGTH_LONG).show();
                    }
                }
            };

            thredSearch = new ThreadSearch(serverTransmission,nameEdit.getText().toString(),context,handler);
            thredSearch.start();

    }

    public void bntLogout()
    {
        serverTransmission.logout();
    }

    public void showMap(){
        try {
            dataJSON = serverTransmission.getSearch();
            //String jsonString  = "{\"building\" : \"Lab\",\"floor\" : \"0\",\"mapa\" : \"https://s3.amazonaws.com/whereisboss-assets/54bf6f4244a96b0300d33cbe.jpeg\",\"rooms\" :[{\"_id\":\"54bf6f6444a96b0300d33cbf\",\"coordinates\":[[54,237],[52,387],[229,392],[228,236]],\"room\":\"Lab 1\"},{\"_id\":\"54bf6f6444a96b0300d33cc0\",\"coordinates\":[[228,236],[230,393],[457,392],[456,235]],\"room\":\"Lab 2\"},{\"_id\":\"54bf6f6444a96b0300d33cc1\",\"coordinates\":[[456,235],[457,392],[633,393],[633,234]],\"room\":\"Lab 3\"}],\"location\" : \"54bf6f6444a96b0300d33cc0\"}";

            //JSONObject dataJSON =  new JSONObject(jsonString); //zbedne
            JSONArray array = dataJSON.getJSONArray("rooms");
            JSONObject[] rooms = new JSONObject[array.length()];
            ShowMap.array = array;

            String location = dataJSON.getString("location");
            ShowMap.room = location;

            String mapa = dataJSON.getString("mapa");
            String buildingString = dataJSON.getString("building");
            String floorString = dataJSON.getString("floor");

            TextView building = (TextView) findViewById(R.id.building);
            building.setText("Building:   " + buildingString);
            TextView floor = (TextView) findViewById(R.id.floor);
            floor.setText("Floor:   " +floorString);

            new DownloadImageTask(imageView).execute(mapa);

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
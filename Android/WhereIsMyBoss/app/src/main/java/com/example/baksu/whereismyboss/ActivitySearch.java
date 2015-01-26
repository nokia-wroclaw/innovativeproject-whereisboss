package com.example.baksu.whereismyboss;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.BitmapFactory;

/**
 * Created by Baksu on 2014-12-13.
 */
public class ActivitySearch extends Activity {

    private ServerTransmission serverTransmission;
    private Context context;
    ImageView map_image;
    ImageView image;
    private TextView text;
    WebView myWebView;
    Bitmap bitMap;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);


        image = (ImageView) findViewById(R.id.main_imagemap);


        String image_url = "https://s3.amazonaws.com/whereisboss-assets/54bf6f4244a96b0300d33cbe.jpeg";
        SmartImageView myImage = (SmartImageView) this.findViewById(R.id.my_image);
        myImage.setImageUrl(image_url);
        myImage.setImageContact(contactAddressBookId);





       /* myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl(ShowMap.url);

        Bitmap btmp = myWebView.getDrawingCache(true);
        image.setImageBitmap(btmp);


       // myWebView.getSettings().setBuiltInZoomControls(true);
       // myWebView.setVerticalScrollBarEnabled(true);
      //  myWebView.setHorizontalScrollBarEnabled(true);

         Toast.makeText(ActivitySearch.this, ShowMap.url, Toast.LENGTH_SHORT).show();
       // myWebView.setScrollContainer(true);

        image = (ImageView)findViewById(R.id.main_imagemap);

        Toast.makeText(ActivitySearch.this, ShowMap.data, Toast.LENGTH_SHORT).show();


        Intent service = new Intent(this, ServerTransmission.class);
        bindService(service, bService, this.BIND_AUTO_CREATE);





        text = (TextView) findViewById(R.id.editText);
        final Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                // Toast.makeText(ActivitySearch.this, text.getText().toString(), Toast.LENGTH_SHORT).show();
                String user = text.getText().toString();

                if(user.equals("q")) {


                    ShowMap.data = "{\"rooms\":[{\"name\":\"RED\",\"coordinates\":[[342,38],[342,207],[578,206],[580,37]]},{\"name\":\"YELLOW\",\"coordinates\":[[682,427],[683,530],[831,526],[828,426]]},{\"name\":\"BLUE\",\"coordinates\":[[345,350],[344,471],[467,472],[468,348]]}]}";
                    //ShowMap.data = "{\"building\":\"Lab\",\"floor\":\"0\",\"mapa\":\"https://s3.amazonaws.com/whereisboss-assets/54bf6f4244a96b0300d33cbe.jpeg\",\"rooms\":[{\"_id\":\"54bf6f6444a96b0300d33cbf\",\"coordinates\":[[54,237],[52,387],[229,392],[228,236]],\"room\":\"Lab 1\"},{\"_id\":\"54bf6f6444a96b0300d33cc0\",\"coordinates\":[[228,236],[230,393],[457,392],[456,235]],\"room\":\"Lab 2\"},{\"_id\":\"54bf6f6444a96b0300d33cc1\",\"coordinates\":[[456,235],[457,392],[633,393],[633,234]],\"room\":\"Lab 3\"}]\"location\":\"Lab 2\"}";
                    ShowMap.room = "RED";
                    // {\"building\":\"Lab\",\"floor\":\"0\",\"mapa\":\"https://s3.amazonaws.com/whereisboss-assets/54bf6f4244a96b0300d33cbe.jpeg\",\"rooms\":[{\"_id\":\"54bf6f6444a96b0300d33cbf\",\"coordinates\":[[54,237],[52,387],[229,392],[228,236]],\"room\":\"Lab 1\"},{\"_id\":\"54bf6f6444a96b0300d33cc0\",\"coordinates\":[[228,236],[230,393],[457,392],[456,235]],\"room\":\"Lab 2\"},{\"_id\":\"54bf6f6444a96b0300d33cc1\",\"coordinates\":[[456,235],[457,392],[633,393],[633,234]],\"room\":\"Lab 3\"}]\"location\":\"Lab 2\"}";

                    // ShowMap showMap = (ShowMap) findViewById(R.id.main_imagemap);
                    // showMap.setVisibility(View.GONE);
                    ShowMap.url = "https://s3.amazonaws.com/whereisboss-assets/54bf6f4244a96b0300d33cbe.jpeg";
                    //myWebView = (WebView) findViewById(R.id.webview);
                    //  myWebView.loadUrl(url);

                    //map_image = (ImageView) findViewById(R.id.main_imagemap);
                    //myWebView.loadUrl(url);
                   Intent refresh = new Intent(ActivitySearch.this, ActivitySearch.class);
                      finish();
                      startActivity(getIntent());







                    // setContentView(R.layout.search_activity);
                    //  ShowMap showMap = (ShowMap) findViewById(R.id.main_imagemap);
                    //  map_image = (ImageView)findViewById(R.id.main_imagemap);
                    //  Intent refresh = new Intent(ActivitySearch.this, ActivitySearch.class);
                    //  finish();
                    //  startActivity(getIntent());


                    //  Canvas canvas = new Canvas();
                    //  showMap.onDraw(canvas);
                    //context = getApplicationContext();
                    // map_image = (ImageView)findViewById(R.id.main_imagemap);
                    //Toast.makeText(ActivitySearch.this, "wszedl", Toast.LENGTH_SHORT).show();
                    // ShowMap.bool = true;
                    // Intent refresh = new Intent(ActivitySearch.this, ActivitySearch.class);
                    //startActivityForResult(refresh, GlobalDataStore.STATIC_INTEGER_VALUE);
                    //  finish();
                    //  startActivity(getIntent());



                }

                else {


                }




            }

        });*/







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

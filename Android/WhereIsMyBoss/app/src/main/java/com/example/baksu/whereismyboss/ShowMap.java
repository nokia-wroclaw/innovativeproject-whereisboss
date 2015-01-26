package com.example.baksu.whereismyboss;

/**
 * Created by Baksu on 2014-12-13.
 */
import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.graphics.Typeface;


public class ShowMap extends ImageView {

    //parametry wejsciowe
    public static String data;
    public static String room;
    public static JSONArray array;

    public ShowMap(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public ShowMap(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public ShowMap(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int maxX;
        int minX;
        int maxY;
        int minY;

        String coordinatesString;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        Path polyPath = new Path();

        p.setStyle(Paint.Style.STROKE);
        super.onDraw(canvas);
        try {

            JSONObject[] rooms = new JSONObject[array.length()];

            for (int i=0; i < array.length(); i++) {
                p.setStrokeWidth(4);
                maxX = 0;
                minX = 0;
                maxY = 0;
                minY = 0;

                //wyluskanie stringa samych wspolrzednych, dla kazdego pokoju osobno
                rooms[i] = array.getJSONObject(i);
                coordinatesString = rooms[i].getString("coordinates");
                coordinatesString = rooms[i].getString("coordinates");
                coordinatesString = coordinatesString.substring(2, coordinatesString.length()-2);
                String[] coordinatesArray = coordinatesString.split(",");

                //obrobka tych wspolrzednych, aby pozostala sama liczba
                for (int j=0; j < coordinatesArray.length; j++) {
                    if(coordinatesArray[j].startsWith("[")) {
                        coordinatesArray[j] = coordinatesArray[j].substring(1);
                    }
                    else if(coordinatesArray[j].endsWith("]")) {
                        coordinatesArray[j] = coordinatesArray[j].substring(0,coordinatesArray[j].length()-1);
                    }
                }
                //pozycjonowanie napisu oraz zaznaczanie obrysu obszarow
                maxX = Integer.parseInt(coordinatesArray[0]);
                minX = Integer.parseInt(coordinatesArray[0]);
                maxY = Integer.parseInt(coordinatesArray[1]);
                minY = Integer.parseInt(coordinatesArray[1]);
                for (int j=0; j < coordinatesArray.length; j++) {
                    if(j==0) {
                        polyPath.moveTo(Integer.parseInt(coordinatesArray[j]),Integer.parseInt(coordinatesArray[j+1]));
                    }
                    else {
                        polyPath.lineTo(Integer.parseInt(coordinatesArray[j]),Integer.parseInt(coordinatesArray[j+1]));

                        if( Integer.parseInt(coordinatesArray[j])> maxX ){
                            maxX = Integer.parseInt(coordinatesArray[j]);
                        }
                        if( Integer.parseInt(coordinatesArray[j]) < minX ){
                            minX = Integer.parseInt(coordinatesArray[j]);
                        }
                        if( Integer.parseInt(coordinatesArray[j+1]) > maxY ){
                            maxY = Integer.parseInt(coordinatesArray[j+1]);
                        }
                        if( Integer.parseInt(coordinatesArray[j+1])< minY ){
                            minY = Integer.parseInt(coordinatesArray[j+1]);
                        }
                    }
                    j++;
                }
                p.setColor(0xdd222222);
                p.setStyle(Paint.Style.FILL);
                p.setStrokeWidth(1);
                p.setTextSize(18);
                p.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                canvas.drawText(rooms[i].getString("room"), (minX + maxX)/2 - ((maxX-minX)/4), (minY + maxY)/2,p);

                p.setStyle(Paint.Style.STROKE);
                p.setStrokeWidth(3);
                polyPath.lineTo(Integer.parseInt(coordinatesArray[0]),Integer.parseInt(coordinatesArray[1]));
                canvas.drawPath(polyPath, p);
                polyPath.close();
            }
        }
        catch(Throwable t) {}

        //zakreslanie wyszczegolnionego pomieszczenia w parametrze funkcji
        Paint p2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        Path polyPath2 = new Path();
        try {

            JSONObject[] rooms = new JSONObject[array.length()];

            for (int i=0; i < array.length(); i++) {
                p2.setStrokeWidth(4);
                p2.setStyle(Paint.Style.FILL);

                rooms[i] = array.getJSONObject(i);
                coordinatesString = rooms[i].getString("coordinates");
                coordinatesString = rooms[i].getString("coordinates");
                coordinatesString = coordinatesString.substring(2, coordinatesString.length()-2);
                String[] coordinatesArray = coordinatesString.split(",");

                for (int j=0; j < coordinatesArray.length; j++) {

                    if(coordinatesArray[j].startsWith("[")) {
                        coordinatesArray[j] = coordinatesArray[j].substring(1);
                    }
                    else if(coordinatesArray[j].endsWith("]")) {
                        coordinatesArray[j] = coordinatesArray[j].substring(0,coordinatesArray[j].length()-1);
                    }
                }
                if(rooms[i].getString("_id").equals(room)) {
                    for (int j=0; j < coordinatesArray.length; j++) {
                        if(j==0) {
                            polyPath2.moveTo(Integer.parseInt(coordinatesArray[j]),Integer.parseInt(coordinatesArray[j+1]));
                        }
                        else {
                            polyPath2.lineTo(Integer.parseInt(coordinatesArray[j]),Integer.parseInt(coordinatesArray[j+1]));
                        }
                        j++;
                    }
                    p2.setColor(0x55661100);
                    polyPath2.lineTo(Integer.parseInt(coordinatesArray[0]),Integer.parseInt(coordinatesArray[1]));
                    canvas.drawPath(polyPath2, p2);
                    polyPath.close();
                }

            }
        }
        catch(Throwable t) {

        }
    }
}

package com.example.baksu.whereismyboss;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Baksu on 2014-12-02.
 */
public class ActivityScan extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_activity);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}

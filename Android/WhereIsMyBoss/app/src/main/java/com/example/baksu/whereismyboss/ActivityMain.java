package com.example.baksu.whereismyboss;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Baksu on 2014-12-02.
 */
public class ActivityMain extends Activity {

    private ServerTransmission servTrans;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        servTrans = (ServerTransmission) getIntent().getSerializableExtra("ServTrans");
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    public void bntClick(View v)
    {
        switch(v.getId())
        {
            case R.id.bntScan: bntScan(); break;
            case R.id.bntReport: bntReport(); break;
            case R.id.bntSearch: bntSearch(); break;
            //TODO: Dorobić przycisk logout
        }
    }

    public void bntScan()
    {

    }

    public void bntReport()
    {

    }

    public void bntSearch()
    {

    }
}

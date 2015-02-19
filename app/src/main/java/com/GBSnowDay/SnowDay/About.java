package com.GBSnowDay.SnowDay;


import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

public class About extends Activity {
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        PackageInfo versionInfo = getPackageInfo();
        TextView txtVersion = (TextView) findViewById(R.id.txtVersion);
        txtVersion.setText(" v" + versionInfo.versionName);
        //Show the up arrow in the action bar
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private PackageInfo getPackageInfo() {
        PackageInfo pi = null;
        try {
            pi = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi;
    }

    @Override
    public boolean onNavigateUp() {
       super.onNavigateUp();
        //Return to the previous activity
        finish();
        return true;
    }
}

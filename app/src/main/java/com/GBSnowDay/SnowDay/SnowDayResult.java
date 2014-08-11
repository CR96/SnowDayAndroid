package com.GBSnowDay.SnowDay;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

public class SnowDayResult extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snow_day_result);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.snow_day_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void radarToggle(View view) {
        WebView webRadar = (WebView) findViewById(R.id.webRadar);
        Button btnRadar = (Button) findViewById(R.id.btnRadar);
        if (webRadar.getVisibility() == View.GONE) {
            webRadar.setEnabled(true);
            webRadar.setVisibility(View.VISIBLE);
            webRadar.loadUrl("http://radar.weather.gov/Conus/Loop/centgrtlakes_loop.gif");
            webRadar.getSettings().setLoadWithOverviewMode(true);
            webRadar.getSettings().setUseWideViewPort(true);
            btnRadar.setText(R.string.radarhide);
        }else{
            webRadar.setVisibility(View.GONE);
            webRadar.setEnabled(false);
            btnRadar.setText(R.string.radarshow);
            }
        }

    }


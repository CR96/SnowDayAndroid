package com.GBSnowDay.SnowDay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.GBSnowDay.SnowDay.DateResult;
import com.GBSnowDay.SnowDay.R;
import com.GBSnowDay.SnowDay.adapter.CustomAdapter;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    /*Copyright 2014-2016 Corey Rowe

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.*/

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "";
    private static final String TWITTER_SECRET = "";

    //Declare all views
    RadioButton optToday;
    RadioButton optTomorrow;
    RecyclerView lstInfo;
    Spinner lstDays;
    Button btnCalculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Crashlytics(), new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        //Declare views
        optToday = (RadioButton) findViewById(R.id.optToday);
        optTomorrow = (RadioButton) findViewById(R.id.optTomorrow);
        lstInfo = (RecyclerView) findViewById(R.id.lstInfo);
        lstDays = (Spinner) findViewById(R.id.lstDays);
        btnCalculate = (Button) findViewById(R.id.btnCalculate);

        DateResult dateResult = new DateResult(MainActivity.this);

        boolean todayValid = dateResult.getTodayValid();
        boolean tomorrowValid = dateResult.getTomorrowValid();

        if (!todayValid && !tomorrowValid) {
            optToday.setEnabled(false);
            optToday.setChecked(false);
            optTomorrow.setEnabled(false);
            optTomorrow.setChecked(false);
        } else if (!todayValid) {
            optToday.setEnabled(false);
            optToday.setChecked(false);
        } else if (!tomorrowValid) {
            optTomorrow.setEnabled(false);
            optTomorrow.setChecked(false);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        lstInfo.setLayoutManager(layoutManager);
        lstInfo.setAdapter(new CustomAdapter(
                dateResult.getEventPresent(),
                dateResult.getBobcats(),
                dateResult.getInfoList()));

        //Listen for optToday or optTomorrow changes
        //Don't allow the calculation to run if "Select a day" is selected
        optToday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (lstDays.getSelectedItemId() != 0) {
                    btnCalculate.setEnabled(true);
                }
            }
        });
        optTomorrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (lstDays.getSelectedItemId() != 0) {
                    btnCalculate.setEnabled(true);
                }
            }
        });

        //Listen for lstDays changes
        //Don't allow the calculation to be run if "Select a day" is selected or no radio button
        //is selected
        lstDays.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (id == 0) {
                    btnCalculate.setEnabled(false);
                } else if (!optToday.isChecked() && !optTomorrow.isChecked()) {
                    btnCalculate.setEnabled(false);
                } else {
                    btnCalculate.setEnabled(true);
                }
                special();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Required empty method
            }
        });

        //Listen for button click
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Date setup

                int dayrun = -1;

                if (optToday.isChecked()) {
                    //Set dayrun to 0 (Today)
                    dayrun = 0;

                } else if (optTomorrow.isChecked()) {
                    //Set dayrun to 1 (Tomorrow)
                    dayrun = 1;
                }

                //Have the user input past snow days
                int days = lstDays.getSelectedItemPosition() - 1;

                if (dayrun == 0) {
                    Answers.getInstance().logCustom(new CustomEvent("Ran Calculation: Today"));
                }else if (dayrun == 1) {
                    Answers.getInstance().logCustom(new CustomEvent("Ran Calculation: Tomorrow"));
                }

                //Switch to result activity
                //Pass values of 'dayrun' and 'days' to new activity
                Intent result = new Intent(getApplicationContext(), ResultActivity.class);
                result.putExtra("dayrun", dayrun);
                result.putExtra("days", days);
                startActivity(result);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.snow_day, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about) {
            //Show the about activity
            Intent about = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(about);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void special() {
        ArrayList<Integer> daysarray = new ArrayList<>();
        daysarray.add((int) lstDays.getSelectedItemId());
        int[] specialarray = {0, 3, 7, 1, 7, 3, 1, 2, 1};
        if (daysarray.toString().equals(Arrays.toString(specialarray))) {
            ArrayList<String> special = new ArrayList<>();
            special.add(0, getString(R.string.special));
            special.add(1, "");
            lstInfo.setAdapter(new CustomAdapter(false, false, special));
        }
    }
}




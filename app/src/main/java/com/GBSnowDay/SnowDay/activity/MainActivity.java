/*
 * Copyright 2014-2018 Corey Rowe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

import com.GBSnowDay.SnowDay.R;
import com.GBSnowDay.SnowDay.adapter.CustomAdapter;
import com.GBSnowDay.SnowDay.model.EventModel;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import java.util.ArrayList;
import java.util.Arrays;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    //Declare all views
    private RadioButton optToday;
    private RadioButton optTomorrow;
    private RecyclerView lstInfo;
    private Spinner lstDays;
    private Button btnCalculate;

    private ArrayList<Integer> daysArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        //Declare views
        optToday = findViewById(R.id.optToday);
        optTomorrow = findViewById(R.id.optTomorrow);
        lstInfo = findViewById(R.id.lstInfo);
        lstDays = findViewById(R.id.lstDays);
        btnCalculate = findViewById(R.id.btnCalculate);

        EventModel eventModel = new EventModel(MainActivity.this);

        boolean todayValid = eventModel.getTodayValid();
        boolean tomorrowValid = eventModel.getTomorrowValid();

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
                eventModel.getEventPresent(),
                eventModel.getBobcats(),
                eventModel.getInfoList()));

        //Listen for optToday or optTomorrow changes
        //Don't allow the calculation to run if "Select a day" is selected
        optToday.setOnClickListener(view -> {
            if (lstDays.getSelectedItemId() != 0) {
                btnCalculate.setEnabled(true);
            }
        });
        optTomorrow.setOnClickListener(view -> {
            if (lstDays.getSelectedItemId() != 0) {
                btnCalculate.setEnabled(true);
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
        btnCalculate.setOnClickListener(view -> {
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
        daysArray.add((int) lstDays.getSelectedItemId());
        int[] specialarray = {0, 3, 7, 1, 7, 3, 1, 2, 1};
        if (daysArray.toString().equals(Arrays.toString(specialarray))) {
            ArrayList<String> special = new ArrayList<>();
            special.add(0, getString(R.string.special));
            special.add(1, "");
            lstInfo.setAdapter(new CustomAdapter(false, false, special));
        }
    }
}




package com.GBSnowDay.SnowDay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SnowDay extends Activity {

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
            Intent about = new Intent(getApplicationContext(), About.class);
            startActivity(about);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Declare all views
    RadioButton optToday;
    RadioButton optTomorrow;
    ListView lstInfo;
    Spinner lstDays;
    Button btnCalculate;

    //Declare variables
    public String today;
    public String tomorrow;
    public Date date;
    public Format formatter;

    public List<String> infoList = new ArrayList<>();

    public int days;
    public int dayrun;

    //Figure out what tomorrow is
    //Saturday = 0, Sunday = 1

    Calendar calendar = Calendar.getInstance();
    int weekday = calendar.get(Calendar.DAY_OF_WEEK);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snow_day);

        //Declare views
        optToday = (RadioButton) findViewById(R.id.optToday);
        optTomorrow = (RadioButton) findViewById(R.id.optTomorrow);
        lstInfo = (ListView) findViewById(R.id.lstInfo);
        lstDays = (Spinner) findViewById(R.id.lstDays);
        btnCalculate = (Button) findViewById(R.id.btnCalculate);

        //Make sure the user doesn't try to run the program on the weekend or during school hours
        setDate();
        checkWeekend();
        checkTime();

        //Set the content of the ListView
        System.out.println(infoList);
        ArrayAdapter<String> infoadapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, infoList);
        lstInfo.setAdapter(infoadapter);

        //Listen for optToday or optTomorrow changes
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
        lstDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (lstDays.getSelectedItemId() == 0) {
                    btnCalculate.setEnabled(false);
                }else if (!optToday.isChecked() && !optTomorrow.isChecked()) {
                    btnCalculate.setEnabled(false);
                }else{
                    btnCalculate.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Listen for button click
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                System.out.println("btnCalculate clicked");
                btnCalculate.setEnabled(false);
                Calculate();

                //Switch to SnowDayResult activity
                //Pass value of 'days' to new activity
                Intent result = new Intent(getApplicationContext(), SnowDayResult.class);
                result.putExtra("dayrun", dayrun);
                result.putExtra("days", days);
                startActivity(result);
            }
        });
    }


    public void Calculate() {
        /**
         * This method will predict the possibility of a snow day for Grand Blanc Community Schools.
         * Created by Corey Rowe, July 2014 - port of original Swing GUI.
         * Factors:
         * Predicted snowfall and time of arrival (not yet implemented)
         * Predicted ice accumulation (not yet implemented)
         * Predicted wind chill (below -20F?) (not yet implemented)
         * Number of snow days accrued (more = smaller chance) (not yet implemented)
         * Schools currently closed (data from WJRT) (not yet implemented)
         * Schools in higher tiers (5 is highest) will increase the snow day chance.
         * Obviously return 100% if GB is already closed.
         */

        //Date setup

        if (optToday.isChecked()) {
            //Set dayrun to 0 (Today)
            dayrun = 0;

        } else if (optTomorrow.isChecked()) {
            //Set dayrun to 1 (Tomorrow)
            dayrun = 1;
        }

        //Determine the date
        date = calendar.getTime();
        formatter = new SimpleDateFormat("MMM dd yyyy");
        today = formatter.format(date);
        calendar.add(Calendar.DATE, 1);
        date = calendar.getTime();
        formatter = new SimpleDateFormat("MMM dd yyyy");
        tomorrow = formatter.format(date);

        //Set calculation to today or tomorrow
        if (dayrun == 0) {
            btnCalculate.setText(this.getString(R.string.DayRun) + " " + this.getString(R.string.today));
        } else if (dayrun == 1) {
            btnCalculate.setText(this.getString(R.string.DayRun) + " " + this.getString(R.string.tomorrow));
        }

        //Have the user input past snow days
        days = lstDays.getSelectedItemPosition() - 1;
    }

    private void setDate() {
        //Set the current month, day, and year
        String date = "Current Date: " + new SimpleDateFormat("MMMM dd yyyy").format(calendar.getTime());
        infoList.add(0, date);

    }

    private void checkWeekend() {
        //Friday is 6
        //Saturday is 7
        //Sunday is 1

        if (weekday == 6) {
            infoList.add(1, getString(R.string.SaturdayTomorrow));
            optTomorrow.setEnabled(false);
            optToday.setChecked(true);
        } else if (weekday == 7) {
            infoList.add(1, getString(R.string.SaturdayToday));
            optToday.setEnabled(false);
            optTomorrow.setEnabled(false);
            lstDays.setEnabled(false);
        } else if (weekday == 1) {
            infoList.add(1, getString(R.string.SundayToday));
            optToday.setEnabled(false);
            optTomorrow.setChecked(true);
        }
    }

    private void checkTime() {
        if (calendar.get(Calendar.HOUR_OF_DAY) >= 7 && calendar.get(Calendar.HOUR_OF_DAY) < 16 && weekday != 7 && weekday != 1) {
            //Time is between 7AM and 4PM.
            optToday.setEnabled(false);
            infoList.add(2, getString(R.string.SchoolOpen));
            dayrun = 1;
        } else if (calendar.get(Calendar.HOUR_OF_DAY) >= 16 && weekday != 7 && weekday != 1) {
            optToday.setEnabled(false);
            //Time is after 4PM.
            infoList.add(2, getString(R.string.GBDismissed));
            dayrun = 1;
        }
    }
}



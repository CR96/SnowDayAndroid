package com.GBSnowDay.SnowDay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SnowDay extends Activity {

    //Declare all views
    RadioButton optToday;
    RadioButton optTomorrow;
    TextView txtInfo;
    Spinner lstDays;
    Button btnCalculate;



    //Declare variables
    public String today;
    public String tomorrow;
    public Date date;
    public Format formatter;

    public String[] orgNameLine;
    public String[] statusLine;

    public int days;
    public int dayrun;

    //Figure out what tomorrow is
    //Saturday = 0, Sunday = 1

    Calendar calendar = Calendar.getInstance();
    int weekday = calendar.get(Calendar.DAY_OF_WEEK);
    int month = calendar.get(Calendar.MONTH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("We're live!");
        System.out.println("Creating activity_snow_day");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snow_day);

        //Declare views
        optToday = (RadioButton) findViewById(R.id.optToday);
        optTomorrow = (RadioButton) findViewById(R.id.optTomorrow);
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        lstDays = (Spinner) findViewById(R.id.lstDays);
        btnCalculate = (Button) findViewById(R.id.btnCalculate);


        //Make sure the user doesn't try to run the program on the weekend or during school hours
        //checkWeekend();
        checkTime();
        //Listen for optToday or optTomorrow changes
        optToday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            System.out.println("optToday checked");
            if (lstDays.getSelectedItemId() != 0) {
                btnCalculate.setEnabled(true);
                //btnCalculate.setBackgroundColor(Color.rgb(52, 181, 229));
            }
            }
        });
        optTomorrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                System.out.println("optTomorrow checked");
                if (lstDays.getSelectedItemId() != 0) {
                    btnCalculate.setEnabled(true);
                    //btnCalculate.setBackgroundColor(Color.rgb(52, 181, 229));
                }
            }
        });

        //Listen for lstDays changes
        lstDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (lstDays.getSelectedItemId() == 0) {
                    btnCalculate.setEnabled(false);
                    //btnCalculate.setBackgroundColor(Color.rgb(2, 154, 204));
                }else if (!optToday.isChecked() && !optTomorrow.isChecked()) {
                    btnCalculate.setEnabled(false);
                    //btnCalculate.setBackgroundColor(Color.rgb(2, 154, 204));
                }else{
                    btnCalculate.setEnabled(true);
                    //btnCalculate.setBackgroundColor(Color.rgb(52, 181, 229));
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
                //btnCalculate.setBackgroundColor(Color.rgb(2, 154, 204));
                Calculate();
                //Switch to SnowDayResult activity
                //Pass value of 'days' to new activity

                Intent result = new Intent(getApplicationContext(), SnowDayResult.class);
                result.putExtra("dayrun", dayrun);
                result.putExtra("days", days);
                System.out.println("Switching to activity_snow_day_result");
                startActivity(result);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.snow_day, menu);
        System.out.println("onCreateOptionsMenu doesn't do anything yet");
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        System.out.println("onOptionsItemSelected doesn't do anything yet");
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        System.out.println("Calculation has started");
        //Call a reset to clear any previous data
        Reset();

        //Date setup
        System.out.println("Checking selected day");
        if (optToday.isChecked()) {
            dayrun = 0;
            System.out.println("dayrun set to 0 (Today)");
        } else if (optTomorrow.isChecked()) {
            System.out.println("dayrun set to 1 (Tomorrow)");
            dayrun = 1;
        }

        System.out.println("Determining date");
        date = calendar.getTime();
        formatter = new SimpleDateFormat("MMM dd yyyy");
        today = formatter.format(date);
        calendar.add(Calendar.DATE, 1);
        date = calendar.getTime();
        formatter = new SimpleDateFormat("MMM dd yyyy");
        tomorrow = formatter.format(date);

        //Set calculation to today or tomorrow
        if (dayrun == 0) {
            txtInfo.setText(txtInfo.getText() + "\n" + this.getString(R.string.DayRun) + " " + this.getString(R.string.today));
            System.out.println("Today is " + today);
        } else if (dayrun == 1) {
            txtInfo.setText(txtInfo.getText() + "\n" + this.getString(R.string.DayRun) + " " + this.getString(R.string.tomorrow));
            System.out.println("Tomorrow is " + tomorrow);
        }

        //Have the user input past snow days
        days = lstDays.getSelectedItemPosition() - 1;
        System.out.println("User says " + days + " snow days have occurred.");
    }

    private void Reset() {
        System.out.println("Resetting SnowDay variables");
        //Reset variables
        today = "";
        tomorrow = "";
        txtInfo.setText("");
    }

    private void checkWeekend() {
        System.out.println("Checking the Weekend...");
        //Friday is 6
        //Saturday is 7
        //Sunday is 1

        if (weekday == 6) {
            System.out.println("Today is Friday (6).");
            txtInfo.setText(R.string.SaturdayTomorrow);
            optTomorrow.setEnabled(false);
            optToday.setChecked(true);
        } else if (weekday == 7) {
            System.out.println("Today is Saturday (7).");
            txtInfo.setText(R.string.SaturdayToday);
            optToday.setEnabled(false);
            optTomorrow.setEnabled(false);
            lstDays.setEnabled(false);
        } else if (weekday == 1) {
            System.out.println("Today is Sunday (1).");
            txtInfo.setText(R.string.SundayToday);
            optToday.setEnabled(false);
            optTomorrow.setChecked(true);
        }
    }

    private void checkTime() {
        System.out.println("Checking the time...");
        if (calendar.get(Calendar.HOUR_OF_DAY) >= 7 && calendar.get(Calendar.HOUR_OF_DAY) < 16 && weekday != 7 && weekday != 1) {
            System.out.println("Time is between 7AM and 4PM.");
            System.out.println("The school's already open.");
            optToday.setEnabled(false);
            //txtGB.setText("Grand Blanc: OPEN");
            txtInfo.setText(txtInfo.getText() + this.getString(R.string.SchoolOpen));
            dayrun = 1;
        } else if (calendar.get(Calendar.HOUR_OF_DAY) >= 16 && weekday != 7 && weekday != 1) {
            //txtGB.setText("Grand Blanc: Dismissed");
            //txtGB.setBackgroundColor(Color.YELLOW);
            optToday.setEnabled(false);
            System.out.println("Time is after 4PM");
            System.out.println("School's already out for today!");
            txtInfo.setText(txtInfo.getText() + this.getString(R.string.GBDismissed));
            dayrun = 1;
        }
    }












}



package com.GBSnowDay.SnowDay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import java.util.Arrays;
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

    public List<String> infoList = new ArrayList<String>();
    public List<Integer> daysarray = new ArrayList<Integer>();
    public int infoCount = 1;
    public int dayscount = 0;
    public boolean todayValid;
    public boolean tomorrowValid;

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
        checkDate();

        if (todayValid || tomorrowValid) {
            checkWeekend();
            checkTime();
        }

        //Set the content of the ListView
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
                } else if (!optToday.isChecked() && !optTomorrow.isChecked()) {
                    btnCalculate.setEnabled(false);
                } else {
                    btnCalculate.setEnabled(true);
                }
                special();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Listen for button click
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
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

        //Have the user input past snow days
        days = lstDays.getSelectedItemPosition() - 1;
    }

    private void checkDate() {

        todayValid = true;
        tomorrowValid = true;
        //Set the current month, day, and year
        SimpleDateFormat currentDate = new SimpleDateFormat("MMMM dd yyyy");
        String date = currentDate.format(calendar.getTime());
        String dateFormatted = "Current Date: " + currentDate.format(calendar.getTime());
        infoList.add(0, dateFormatted);

        //Check for days school is not in session (such as Winter Break, development days, etc.)
        //Dates are read as a String instead of calendar integers so they can be easily modified.

        if (date.equals("December 09 2014") || date.equals("February 03 2015")
                || date.equals("May 05 2015")) {
            infoList.add(infoCount, "REMINDER: Tomorrow is a Late Start.");
            infoCount++;
        } else if (date.equals("December 10 2014") || date.equals("February 04 2015")
                || date.equals("May 06 2015")) {
            infoList.add(infoCount, "REMINDER: Today is a Late Start.");
            infoCount++;
        } else if (date.equals("December 21 2014")) {
            infoList.add(infoCount, "Winter Break begins tomorrow.");
            infoCount++;
            tomorrowValid = false;
        } else if (date.equals("December 22 2014") || date.equals("December 23 2014")
                || date.equals("December 24 2014") || date.equals("December 25 2014")
                || date.equals("December 26 2014") || date.equals("December 27 2014")
                || date.equals("December 28 2014") || date.equals("December 29 2014")
                || date.equals("December 30 2014") || date.equals("December 31 2014")
                || date.equals("January 01 2015") || date.equals("January 02 2015")) {
            //Winter Break
            if (date.equals("December 25 2014")) {
                infoList.add(infoCount, "Merry Christmas!");
                infoCount++;
            } else if (date.equals("January 01 2015")) {
                infoList.add(infoCount, "Happy New Year!");
                infoCount++;
            }

            infoList.add(infoCount, "Enjoy your winter break!");
            infoCount++;
            todayValid = false;
            tomorrowValid = false;
        } else if (date.equals("January 18 2015")) {
            infoList.add(infoCount, "Tomorrow is MLK Day. School is not in session.");
            infoCount++;
            todayValid = false;
            tomorrowValid = false;
        } else if (date.equals("January 19 2015")) {
            //MLK Day
            infoList.add(infoCount, "Happy MLK Day! School is not in session.");
            infoList.add(infoCount + 1, "Tomorrow is Teacher Records Day. School is not in session.");
            infoCount += 2;
            todayValid = false;
            //Teacher records day is the following day
            tomorrowValid = false;
        } else if (date.equals("January 20 2015")) {
            infoList.add(infoCount, "Teacher Records Day. School is not in session.");
            infoCount++;
            todayValid = false;
        } else if (date.equals("February 15 2015")) {
            infoList.add(infoCount, "Tomorrow is President's Day. School is not in session.");
            infoCount++;
            todayValid = false;
            tomorrowValid = false;
        } else if (date.equals("February 16 2015")) {
            infoList.add(infoCount, "Happy President's Day! School is not in session.");
            infoCount++;
            todayValid = false;
        } else if (date.equals("March 10 2015")) {
            infoList.add(infoCount, "REMINDER: Tomorrow is a Half Day for elementary and middle school students.");
            infoCount++;
        } else if (date.equals("March 11 2015") || date.equals("March 12 2015")
                || date.equals("March 13 2015")) {
            infoList.add(infoCount, "REMINDER: Half Day for elementary and middle school students");
            infoCount++;
        } else if (date.equals("April 01 2015")) {
            infoList.add(infoCount, "REMINDER: Tomorrow is a Half Day.");
            infoCount++;
        } else if (date.equals("April 02 2015")) {
            infoList.add(infoCount, "REMINDER: Today is a Half Day.");
            infoList.add(infoCount, "Spring Break begins tomorrow.");
            infoCount++;
            tomorrowValid = false;
        } else if (date.equals("April 03 2015") || date.equals("April 04 2015")
                || date.equals("April 05 2015") || date.equals("April 06 2015")
                || date.equals("April 07 2015") || date.equals("April 08 2015")
                || date.equals("April 09 2015") || date.equals("April 10 2015")) {
            //Spring Break
            if (date.equals("April 05 2015")) {
                infoList.add(infoCount, "Happy Easter!");
                infoCount++;
            }

            infoList.add(infoCount, "Enjoy your Spring Break!");
            infoCount++;
            todayValid = false;
            tomorrowValid = false;
        }else if (date.equals("April 22 2015")) {
            infoList.add(infoCount, "Tomorrow is a Professional Development Day. School is not in session.");
            infoCount++;
            tomorrowValid = false;
        }else if (date.equals("April 23 2015")) {
            infoList.add(infoCount,"Staff Professional Development Day. School is not in session.");
            infoCount++;
            todayValid = false;
        }else if (date.equals("April 24 2015")) {
            infoList.add(infoCount, "Tomorrow is Memorial Day. School is not in session.");
            infoCount++;
            tomorrowValid = false;
        }else if (date.equals("April 25 2015")) {
            infoList.add(infoCount, "Happy Memorial Day! School is not in session.");
            infoCount++;
            todayValid = false;
        }else if (date.equals("May 19 2015")) {
            infoList.add(infoCount, "Congratulations Senior Class of 2015!");
            infoCount++;
        }else if (date.equals("June 11 2015")) {
            infoList.add(infoCount, "Today is the last day of school!");
            infoCount++;
            tomorrowValid = false;
        }else if (calendar.MONTH >= 5 && calendar.MONTH <= 7 && calendar.DAY_OF_MONTH > 11) {
            infoList.add(infoCount, "Enjoy your Summer!");
            infoCount++;
            todayValid = false;
            tomorrowValid = false;
        }


        //Determine if the program should be run
        if (!tomorrowValid && !todayValid) {
            optToday.setEnabled(false);
            optToday.setTextColor(Color.GRAY);
            optTomorrow.setEnabled(false);
            optTomorrow.setTextColor(Color.GRAY);
            lstDays.setEnabled(false);
        }else if (!tomorrowValid) {
            optTomorrow.setEnabled(false);
            optTomorrow.setTextColor(Color.GRAY);
        }else if (!todayValid) {
            optToday.setEnabled(false);
            optToday.setTextColor(Color.GRAY);
        }
    }

    private void special() {
        daysarray.add(dayscount, (int) lstDays.getSelectedItemId());
        dayscount++;
        int[] specialarray = {0, 3, 7, 1, 7, 3, 1, 2, 1};
        if (daysarray.toString().equals(Arrays.toString(specialarray))) {
            List<String> special = new ArrayList<String>();
            special.add(0, getString(R.string.special));
            ArrayAdapter<String> specialadapter = new ArrayAdapter<String>(getApplicationContext(),
                    R.layout.infolist, special);
            lstInfo.setAdapter(specialadapter);
        }
    }

    private void checkWeekend() {
        //Friday is 6
        //Saturday is 7
        //Sunday is 1

        if (weekday == 6) {
            infoList.add(infoCount, getString(R.string.SaturdayTomorrow));
            optTomorrow.setEnabled(false);
            optTomorrow.setTextColor(Color.GRAY);
            optToday.setChecked(true);
            infoCount++;
        } else if (weekday == 7) {
            infoList.add(infoCount, getString(R.string.SaturdayToday));
            optToday.setEnabled(false);
            optToday.setTextColor(Color.GRAY);
            optTomorrow.setEnabled(false);
            optTomorrow.setTextColor(Color.GRAY);
            infoCount++;
        } else if (weekday == 1) {
            infoList.add(infoCount, getString(R.string.SundayToday));
            optToday.setEnabled(false);
            optToday.setTextColor(Color.GRAY);
            optTomorrow.setChecked(true);
            infoCount++;
        }
    }

    private void checkTime() {
        if (calendar.get(Calendar.HOUR_OF_DAY) >= 7 && calendar.get(Calendar.HOUR_OF_DAY) < 16 && weekday != 7 && weekday != 1) {
            //Time is between 7AM and 4PM.
            optToday.setEnabled(false);
            optToday.setTextColor(Color.GRAY);
            infoList.add(infoCount, getString(R.string.SchoolOpen));
            infoCount++;
            dayrun = 1;
        } else if (calendar.get(Calendar.HOUR_OF_DAY) >= 16 && weekday != 7 && weekday != 1) {
            optToday.setEnabled(false);
            optToday.setTextColor(Color.GRAY);
            //Time is after 4PM.
            infoList.add(infoCount, getString(R.string.GBDismissed));
            infoCount++;
            dayrun = 1;
        }
    }
}



package com.GBSnowDay.SnowDay;

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

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;

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

    //Declare variables
    String datetoday;
    String datetomorrow;
    String textdate;

    boolean todayValid;
    boolean tomorrowValid;
    static boolean eventPresent;
    static boolean bobcats;

    int days;
    int dayrun;

    //Declare lists that will be used in ListAdapters
    List<String> infoList = new ArrayList<>();
    List<Integer> daysarray = new ArrayList<>();

    //Figure out what tomorrow is
    //Saturday = 6, Sunday = 7

    DateTime dt = new DateTime();
    int weekday = dt.getDayOfWeek();

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

        //Make sure the user doesn't try to run the program on the weekend or on specific dates
        checkDate();

        //Only run checkWeekend() if today or tomorrow is still valid
        if (todayValid || tomorrowValid) {
            checkWeekend();
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        lstInfo.setLayoutManager(layoutManager);
        lstInfo.setAdapter(new CustomAdapter(infoList));

        //Listen for optToday or optTomorrow changes
        //Don't allow the calculation to run if "Select a day" is selected
        optToday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (lstDays.getSelectedItemId() != 0) {
                    btnCalculate.setEnabled(true);
                    btnCalculate.setAlpha(1f);
                }
            }
        });
        optTomorrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (lstDays.getSelectedItemId() != 0) {
                    btnCalculate.setEnabled(true);
                    btnCalculate.setAlpha(1f);
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
                    btnCalculate.setAlpha(.25f);
                } else if (!optToday.isChecked() && !optTomorrow.isChecked()) {
                    btnCalculate.setEnabled(false);
                    btnCalculate.setAlpha(.25f);
                } else {
                    btnCalculate.setEnabled(true);
                    btnCalculate.setAlpha(1f);
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
                //Start the calculation
                Calculate();

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
                result.putExtra("datetoday", datetoday);
                result.putExtra("datetomorrow", datetomorrow);
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

    public void Calculate() {

        //Date setup

        if (optToday.isChecked()) {
            //Set dayrun to 0 (Today)
            dayrun = 0;

        } else if (optTomorrow.isChecked()) {
            //Set dayrun to 1 (Tomorrow)
            dayrun = 1;
        }

        //Have the user input past snow days
        days = lstDays.getSelectedItemPosition() - 1;
    }

    public void checkDate() {

        //These are set to false if the calculation cannot be run on that day
        todayValid = true;
        tomorrowValid = true;

        //Set the current month, day, and year
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        datetoday = sdt.format(calendar.getTime());

        SimpleDateFormat currentDate = new SimpleDateFormat("MMMM dd yyyy", Locale.US);
        textdate = currentDate.format(calendar.getTime());

        calendar.add(Calendar.DATE, 1);

        datetomorrow = sdt.format(calendar.getTime());

        infoList.add(0, "Current Date: " + textdate);


        /*Check for days school is not in session (such as Winter Break, development days, etc.)
        Uses a mixture of SimpleDateFormat for simple string comparison and JodaTime for more
        complicated arguments*/

        if (dt.getMonthOfYear() == 6 && dt.getDayOfMonth() > 14) {
            //Summer break (June)
            infoList.add(getString(R.string.Summer));
            eventPresent = true;
            todayValid = false;
            tomorrowValid = false;
        } else if (dt.getMonthOfYear() > 6 && dt.getMonthOfYear() <= 8) {
            //Summer break (July and August)
            infoList.add(getString(R.string.Summer));
            eventPresent = true;
            todayValid = false;
            tomorrowValid = false;
        } else if (dt.getMonthOfYear() == 9 && dt.getDayOfMonth() < 5) {
            //Summer break (September)
            infoList.add(getString(R.string.Summer));
            eventPresent = true;
            todayValid = false;
            tomorrowValid = false;
        } else if (textdate.equals("September 05 2016")) {
            infoList.add(getString(R.string.YearStart));
            eventPresent = true;
            todayValid = false;
        } else if (textdate.equals("October 07 2016")) {
            infoList.add(getString(R.string.HC));
            eventPresent = true;
        } else if (textdate.equals("October 11 2016") || textdate.equals("December 06 2016")
                || textdate.equals("January 31 2017") || textdate.equals("May 02 2017")) {
            infoList.add(getString(R.string.LSTomorrow));
            eventPresent = true;
        } else if (textdate.equals("October 12 2016") || textdate.equals("December 07 2016")
                || textdate.equals("February 01 2017") || textdate.equals("May 03 2017")) {
            infoList.add(getString(R.string.LSToday));
            eventPresent = true;
        } else if (textdate.equals("November 24 2016")) {
            infoList.add(getString(R.string.Thanksgiving));
            eventPresent = true;
            todayValid = false;
            tomorrowValid = false;
        } else if (textdate.equals("November 24 2016") || textdate.equals("November 25 2016")) {
            infoList.add(getString(R.string.ThanksgivingRecess));
            eventPresent = true;
            todayValid = false;
        } else if (textdate.equals("December 21 2016")) {
            infoList.add(getString(R.string.WinterBreakTomorrow));
            eventPresent = true;
            tomorrowValid = false;
        } else if (textdate.equals("December 22 2016") || textdate.equals("December 23 2016")
                || textdate.equals("December 24 2016") || textdate.equals("December 25 2016")
                || textdate.equals("December 26 2016") || textdate.equals("December 27 2016")
                || textdate.equals("December 28 2016") || textdate.equals("December 29 2016")
                || textdate.equals("December 30 2016") || textdate.equals("December 31 2016")
                || textdate.equals("January 01 2017") || textdate.equals("January 02 2017")) {
            //Winter Break
            if (textdate.equals("December 25 2016")) {
                infoList.add(getString(R.string.Christmas));
                eventPresent = true;
                todayValid = false;
                tomorrowValid = false;
            } else if (textdate.equals("January 01 2017")) {
                infoList.add(getString(R.string.NewYear));
                eventPresent = true;
                todayValid = false;
                tomorrowValid = false;
            } else if (textdate.equals("January 02 2017")) {
                eventPresent = true;
                todayValid = false;
            } else {
                eventPresent = true;
                todayValid = false;
                tomorrowValid = false;
            }
            infoList.add(getString(R.string.WinterBreak));
        } else if (textdate.equals("January 15 2017")) {
            infoList.add(getString(R.string.MLKTomorrow) + getString(R.string.NoSessionTomorrow));
            eventPresent = true;
            todayValid = false;
            tomorrowValid = false;
        } else if (textdate.equals("January 16 2017")) {
            //MLK Day
            infoList.add(getString(R.string.MLK) + getString(R.string.NoSessionToday));
            eventPresent = true;
            todayValid = false;
        } else if (textdate.equals("January 22 2017")) {
            infoList.add(getString(R.string.RecordsTomorrow) + getString(R.string.NoSessionTomorrow));
            eventPresent = true;
            tomorrowValid = false;
        } else if (textdate.equals("January 23 2017")) {
            infoList.add(getString(R.string.Records) + getString(R.string.NoSessionToday));
            eventPresent = true;
            todayValid = false;
        //Lincoln's birthday is on a Saturday in 2017.
        /*}else if (textdate.equals("February 11 2017")) {
            infoList.add(getString(R.string.LincolnTomorrow) + getString(R.string.NoSessionTomorrow));
            eventPresent = true;
            tomorrowValid = false;
        } else if (textdate.equals("February 12 2017")) {
            infoList.add(getString(R.string.Lincoln) + getString(R.string.NoSessionToday));
            eventPresent = true;
            todayValid = false;*/
        } else if (textdate.equals("February 16 2017")) {
            //This is the Thursday leading into "President's Weekend"
            infoList.add(getString(R.string.TomorrowGeneric));
            eventPresent = true;
            tomorrowValid = false;
        } else if (textdate.equals("February 17 2017")) {
            //Friday of "President's Weekend"
            infoList.add(getString(R.string.TodayGeneric));
            eventPresent = true;
            todayValid = false;
        } else if (textdate.equals("February 19 2017")) {
            infoList.add(getString(R.string.PresidentTomorrow) + getString(R.string.NoSessionTomorrow));
            eventPresent = true;
            todayValid = false;
            tomorrowValid = false;
        } else if (textdate.equals("February 20 2017")) {
            infoList.add(getString(R.string.President) + getString(R.string.NoSessionToday));
            eventPresent = true;
            todayValid = false;
        } else if (textdate.equals("November 15 2016") || textdate.equals("March 07 2017")) {
            infoList.add(getString(R.string.HalfDayConferenceMSTomorrow));
            eventPresent = true;
        } else if (textdate.equals("November 16 2016") || textdate.equals("November 17 2016")
                || textdate.equals("March 08 2017") || textdate.equals("March 09 2017")) {
            infoList.add(getString(R.string.HalfDayConferenceMSTodayTomorrow));
            eventPresent = true;
        } else if (textdate.equals("November 18 2016") || textdate.equals("March 10 2017")) {
            infoList.add(getString(R.string.HalfDayConferenceMSToday));
            eventPresent = true;
        } else if (textdate.equals("October 20 2016")) {
            infoList.add(getString(R.string.HalfDayHSTomorrow));
            eventPresent = true;
        } else if (textdate.equals("October 21 2016")) {
            infoList.add(getString(R.string.HalfDayHSToday));
            eventPresent = true;
        } else if (textdate.equals("October 06 2016") || textdate.equals("November 22 2016")
                || textdate.equals("March 30 2017")) {
            infoList.add(getString(R.string.HalfDayTomorrow));
            eventPresent = true;
        } else if (textdate.equals("October 07 2016") || textdate.equals("November 23 2016")
                || textdate.equals("March 31 2017")) {
            if (textdate.equals("November 23 2017")) {
                infoList.add(getString(R.string.ThanksgivingRecessTomorrow));
                eventPresent = true;
                tomorrowValid = false;
            }

            infoList.add(getString(R.string.HalfDay));
            eventPresent = true;
        } else if (textdate.equals("April 13 2017")) {
            infoList.add(getString(R.string.GoodFridayTomorrow) + getString(R.string.NoSessionTomorrow));
            eventPresent = true;
            tomorrowValid = false;
        } else if (textdate.equals("April 14 2017")) {
            infoList.add(getString(R.string.GoodFriday) + getString(R.string.NoSessionToday));
            eventPresent = true;
            todayValid = false;
        } else if (textdate.equals("April 16 2017")) {
            infoList.add(getString(R.string.Easter));
            eventPresent = true;
            todayValid = false;
        } else if (textdate.equals("March 31 2017")) {
            infoList.add(getString(R.string.HalfDay));
            infoList.add(getString(R.string.SpringBreakTomorrow));
            eventPresent = true;
            tomorrowValid = false;
        } else if (textdate.equals("April 01 2017") || textdate.equals("April 02 2017")
                || textdate.equals("April 03 2017") || textdate.equals("April 04 2017")
                || textdate.equals("April 05 2017") || textdate.equals("April 06 2017")
                || textdate.equals("April 07 2017")) {
            //Spring Break

            infoList.add(getString(R.string.SpringBreak));
            eventPresent = true;
            todayValid = false;
            tomorrowValid = false;
        } else if (textdate.equals("November 07 2016")) {
            infoList.add(getString(R.string.PDDTomorrow) + getString(R.string.NoSessionTomorrow));
            infoList.add("Don't forget to vote tomorrow!");
            eventPresent = true;
            tomorrowValid = false;
        } else if (textdate.equals("November 08 2016")) {
            infoList.add(getString(R.string.PDD) + getString(R.string.NoSessionToday));
            infoList.add("Don't forget to vote today!");
            eventPresent = true;
            todayValid = false;
        } else if (textdate.equals("May 28 2017")) {
            infoList.add(getString(R.string.MemorialDayTomorrow) + getString(R.string.NoSessionTomorrow));
            eventPresent = true;
            tomorrowValid = false;
        } else if (textdate.equals("May 29 2017")) {
            infoList.add(getString(R.string.MemorialDay) + getString(R.string.NoSessionToday));
            eventPresent = true;
            todayValid = false;
        } else if (textdate.equals("June 01 2017")) {
            infoList.add(getString(R.string.Senior));
            eventPresent = true;
            bobcats = true;
        } else if (textdate.equals("June 14 2017")) {
            infoList.add(getString(R.string.YearEnd));
            eventPresent = true;
            tomorrowValid = false;
        }

        //Determine if the calculation should be available
        if (!tomorrowValid && !todayValid) {
            optToday.setEnabled(false);
            optToday.setChecked(false);
            optTomorrow.setEnabled(false);
            optTomorrow.setChecked(false);
        } else if (!tomorrowValid) {
            optTomorrow.setEnabled(false);
            optTomorrow.setChecked(false);
        } else if (!todayValid) {
            optToday.setEnabled(false);
            optToday.setChecked(false);
        }
    }

    private void special() {
        daysarray.add((int) lstDays.getSelectedItemId());
        int[] specialarray = {0, 3, 7, 1, 7, 3, 1, 2, 1};
        if (daysarray.toString().equals(Arrays.toString(specialarray))) {
            List<String> special = new ArrayList<>();
            special.add(0, getString(R.string.special));
            special.add(1, "");
            lstInfo.setAdapter(new CustomAdapter(special));
        }
    }

    public void checkWeekend() {
        //Friday is 5
        //Saturday is 6
        //Sunday is 7

        if (weekday == 5) {
            infoList.add(getString(R.string.SaturdayTomorrow));
            optTomorrow.setEnabled(false);
            optTomorrow.setChecked(false);
            eventPresent = true;
        } else if (weekday == 6) {
            infoList.add(getString(R.string.SaturdayToday));
            optToday.setEnabled(false);
            optToday.setChecked(false);
            optTomorrow.setEnabled(false);
            optTomorrow.setChecked(false);
            eventPresent = true;
        } else if (weekday == 7) {
            infoList.add(getString(R.string.SundayToday));
            optToday.setEnabled(false);
            optToday.setChecked(false);
            eventPresent = true;
        }
    }
}




package com.GBSnowDay.SnowDay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

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
            //Show the about activity
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
    String today;
    String tomorrow;
    Date date;
    Format formatter;

    int dayscount = 0;
    boolean todayValid;
    boolean tomorrowValid;
    boolean reminder;

    int days;
    int dayrun;

    //UI values for onSaveInstanceState
    boolean optTodayState;
    boolean optTomorrowState;
    int lstDaysState;

    //Declare lists that will be used in ListAdapters
    List<String> infoList = new ArrayList<String>();
    List<Integer> daysarray = new ArrayList<Integer>();
    int infoCount = 1;

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

        //Restore savedInstanceState if the activity was destroyed.
        if (savedInstanceState != null){
            optToday.setSelected(optTodayState);
            optTomorrow.setSelected(optTomorrowState);
            lstDays.setSelection(lstDaysState);
        }

        //Make sure the user doesn't try to run the program on the weekend or on specific dates
        checkDate();

        //Only run checkWeekend() if today or tomorrow is still valid
        if (todayValid || tomorrowValid) {
            checkWeekend();
        }

        //Set the content of the ListView
        CustomAdapter mAdapter = new CustomAdapter();
        for (int i = 0; i < infoList.size(); i++) {
            mAdapter.addItem(infoList.get(i));
        }

        lstInfo.setAdapter(mAdapter);

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
                btnCalculate.setEnabled(false);
                //Start the calculation
                Calculate();

                //Switch to SnowDayResult activity
                //Pass values of 'dayrun' and 'days' to new activity
                Intent result = new Intent(getApplicationContext(), SnowDayResult.class);
                result.putExtra("dayrun", dayrun);
                result.putExtra("days", days);
                startActivity(result);
            }
        });
    }

    protected void onSaveInstanceState(Bundle outState) {
        //Save the UI settings if the activity is destroyed.
        optTodayState = optToday.isSelected();
        optTomorrowState = optTomorrow.isSelected();
        lstDaysState = lstDays.getSelectedItemPosition();
    }

    //Adapter class
    private class CustomAdapter extends BaseAdapter {
        private static final int TYPE_ITEM = 0;

        private ArrayList<String> mData = new ArrayList<String>();
        private LayoutInflater mInflater;

        public CustomAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void addItem(final String item) {
            mData.add(item);
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            return TYPE_ITEM;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public String getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            int type = getItemViewType(position);
            holder = new ViewHolder();
            switch (type) {
                case TYPE_ITEM:
                    if (reminder && position == 1) {
                        //If there is a reminder / event, make it blue
                        convertView = mInflater.inflate(R.layout.itemreminder, null);
                        holder.textView = (TextView) convertView.findViewById(R.id.text);
                    } else {
                        convertView = mInflater.inflate(R.layout.itemlist, null);
                        holder.textView = (TextView) convertView.findViewById(R.id.text);
                    }
                    break;
            }
            convertView.setTag(holder);
            holder.textView.setText(mData.get(position));
            return convertView;
        }
    }

    public static class ViewHolder {
        public TextView textView;
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

        //These are set to false if the calculation cannot be run on that day
        todayValid = true;
        tomorrowValid = true;

        //Set the current month, day, and year
        SimpleDateFormat currentDate = new SimpleDateFormat("MMMM dd yyyy");
        String date = currentDate.format(calendar.getTime());
        String dateFormatted = "Current Date: " + currentDate.format(calendar.getTime());
        infoList.add(0, dateFormatted);

        /*Check for days school is not in session (such as Winter Break, development days, etc.)
        Dates are read as a String from SimpleDateFormat instead of calendar integers so they can
        be easily modified without messing with the date/time API.*/

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
            infoList.add(infoCount, "Tomorrow is MLK Day. School will not be in session.");
            infoCount++;
            todayValid = false;
            tomorrowValid = false;
        } else if (date.equals("January 19 2015")) {
            //MLK Day
            infoList.add(infoCount, "Happy MLK Day! School is not in session.");
            infoList.add(infoCount + 1, "Tomorrow is Teacher Records Day. School will not be in session.");
            infoCount += 2;
            todayValid = false;
            //Special case: teacher records day is the following day
            tomorrowValid = false;
        } else if (date.equals("January 20 2015")) {
            infoList.add(infoCount, "Teacher Records Day. School is not in session.");
            infoCount++;
            todayValid = false;
        } else if (date.equals("February 15 2015")) {
            infoList.add(infoCount, "Tomorrow is President's Day. School will not be not in session.");
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
        } else if (date.equals("April 22 2015")) {
            infoList.add(infoCount, "Tomorrow is a Professional Development Day. School will not be in session.");
            infoCount++;
            tomorrowValid = false;
        } else if (date.equals("April 23 2015")) {
            infoList.add(infoCount, "Staff Professional Development Day. School is not in session.");
            infoCount++;
            todayValid = false;
        } else if (date.equals("April 24 2015")) {
            infoList.add(infoCount, "Tomorrow is Memorial Day. School will not be in session.");
            infoCount++;
            tomorrowValid = false;
        } else if (date.equals("April 25 2015")) {
            infoList.add(infoCount, "Happy Memorial Day! School is not in session.");
            infoCount++;
            todayValid = false;
        } else if (date.equals("May 19 2015")) {
            //As a senior, it has to be said...
            infoList.add(infoCount, "Congratulations Senior Class of 2015!");
            infoCount++;
        } else if (date.equals("June 11 2015")) {
            infoList.add(infoCount, "Today is the last day of school!");
            infoCount++;
            tomorrowValid = false;
        } else if (calendar.MONTH == 5 && calendar.DAY_OF_MONTH > 11) {
            //Summer break (June)
            infoList.add(infoCount, "Enjoy your Summer!");
            infoCount++;
            todayValid = false;
            tomorrowValid = false;
        } else if (calendar.MONTH > 5 && calendar.MONTH < 8) {
            //Summer break (July and August)
            infoList.add(infoCount, "Enjoy your Summer!");
            infoCount++;
            todayValid = false;
            tomorrowValid = false;
        } else if (calendar.MONTH == 9) {
            //Summmer break (September)
            //Conditions for the first day of the 2015-16 year
            //To be determined
        }

        //If items were added...
        reminder = infoCount > 1;


        //Determine if the calculation should be available
        if (!tomorrowValid && !todayValid) {
            optToday.setEnabled(false);
            optToday.setTextColor(Color.GRAY);
            optTomorrow.setEnabled(false);
            optTomorrow.setTextColor(Color.GRAY);
        } else if (!tomorrowValid) {
            optTomorrow.setEnabled(false);
            optTomorrow.setTextColor(Color.GRAY);
        } else if (!todayValid) {
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
}




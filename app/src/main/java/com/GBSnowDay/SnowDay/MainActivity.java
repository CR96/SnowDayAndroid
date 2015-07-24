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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;

public class MainActivity extends Activity {
    /*Copyright 2014 Corey Rowe

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.*/

    //Declare all views
    RadioButton optToday;
    RadioButton optTomorrow;
    ListView lstInfo;
    Spinner lstDays;
    Button btnCalculate;

    //Declare variables
    String date;

    int dayscount = 0;
    boolean todayValid;
    boolean tomorrowValid;
    boolean reminder;
    boolean bobcats;

    int days;
    int dayrun;

    //Declare lists that will be used in ListAdapters
    List<String> infoList = new ArrayList<String>();
    List<Integer> daysarray = new ArrayList<Integer>();
    int infoCount = 1;

    //Figure out what tomorrow is
    //Saturday = 6, Sunday = 7

    DateTime dt = new DateTime();
    int weekday = dt.getDayOfWeek();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Declare views
        optToday = (RadioButton) findViewById(R.id.optToday);
        optTomorrow = (RadioButton) findViewById(R.id.optTomorrow);
        lstInfo = (ListView) findViewById(R.id.lstInfo);
        lstDays = (Spinner) findViewById(R.id.lstDays);
        btnCalculate = (Button) findViewById(R.id.btnCalculate);

        //Show the EULA if it hasn't been displayed
        new EULADialog(this).show();

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
                //Start the calculation
                Calculate();

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
                    if (bobcats && position == 1) {

                        convertView = mInflater.inflate(R.layout.item_bobcats, null);
                        holder.textView = (TextView) convertView.findViewById(R.id.text);
                    }else if (reminder && position == 1) {
                        //If there is a reminder / event, make it blue
                        convertView = mInflater.inflate(R.layout.item_reminder, null);
                        holder.textView = (TextView) convertView.findViewById(R.id.text);
                    } else {
                        convertView = mInflater.inflate(R.layout.item_center, null);
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

        //Have the user input past snow days
        days = lstDays.getSelectedItemPosition() - 1;
    }

    public void checkDate() {

        //These are set to false if the calculation cannot be run on that day
        todayValid = true;
        tomorrowValid = true;

        //Set the current month, day, and year
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMMM dd yyyy", Locale.US);
        date = currentDate.format(calendar.getTime());

        infoList.add(0, "Current Date: " + date);

        /*Check for days school is not in session (such as Winter Break, development days, etc.)
        Uses a mixture of SimpleDateFormat for simple string comparison and JodaTime for more
        complicated arguments*/

        if (dt.getMonthOfYear() == 6 && dt.getDayOfMonth() > 15) {
            //Summer break (June)
            infoList.add(infoCount, getString(R.string.Summer));
            infoCount++;
            todayValid = false;
            tomorrowValid = false;
        } else if (dt.getMonthOfYear() > 6 && dt.getMonthOfYear() <= 8) {
            //Summer break (July and August)
            infoList.add(infoCount, getString(R.string.Summer));
            infoCount++;
            todayValid = false;
            tomorrowValid = false;
        } else if (dt.getMonthOfYear() == 9 && dt.getDayOfMonth() < 7) {
            //Summer break (September)
            infoList.add(infoCount, getString(R.string.Summer));
            infoCount++;
            todayValid = false;
            tomorrowValid = false;
        }else if (date.equals("September 07 2015")) {
            infoList.add(infoCount, getString(R.string.YearStart));
            infoCount++;
            todayValid = false;
        }else if (date.equals("September 25 2015")) {
            infoList.add(infoCount, getString(R.string.HC));
            infoCount++;
        }else if (date.equals("October 20 2015") || date.equals("December 08 2015")
                || date.equals("February 02 2016") || date.equals("May 03 2016")) {
            infoList.add(infoCount, getString(R.string.LSTomorrow));
            infoCount++;
        } else if (date.equals("October 21 2015") || date.equals("December 09 2015")
                || date.equals("February 03 2016") || date.equals("May 04 2016")) {
            infoList.add(infoCount, getString(R.string.LSToday));
            infoCount++;
        }else if (date.equals("November 26 2015")) {
            infoList.add(infoCount, getString(R.string.Thanksgiving));
            infoCount++;
            todayValid = false;
            tomorrowValid = false;
        }else if (date.equals("November 26 2015") || date.equals("November 27 2015")) {
            infoList.add(infoCount, getString(R.string.ThanksgivingRecess));
            infoCount++;
            todayValid = false;
        } else if (date.equals("December 22 2015")) {
            infoList.add(infoCount, getString(R.string.WinterBreakTomorrow));
            infoCount++;
            tomorrowValid = false;
        } else if (date.equals("December 23 2015") || date.equals("December 24 2015")
                || date.equals("December 25 2015") || date.equals("December 26 2015") || date.equals("December 27 2014")
                || date.equals("December 27 2015") || date.equals("December 28 2015")
                || date.equals("December 29 2015") || date.equals("December 30 2015")
                || date.equals("December 31 2015") || date.equals("January 01 2016")) {
            //Winter Break
            if (date.equals("December 25 2015")) {
                infoList.add(infoCount, getString(R.string.Christmas));
                infoCount++;
            } else if (date.equals("January 01 2016")) {
                infoList.add(infoCount, getString(R.string.NewYear));
                infoCount++;
            }

            infoList.add(infoCount, getString(R.string.WinterBreak));
            infoCount++;
            todayValid = false;
            tomorrowValid = false;
        } else if (date.equals("January 17 2016")) {
            infoList.add(infoCount, getString(R.string.MLKTomorrow) + getString(R.string.NoSessionTomorrow));
            infoCount++;
            todayValid = false;
            tomorrowValid = false;
        } else if (date.equals("January 18 2016")) {
            //MLK Day
            infoList.add(infoCount, getString(R.string.MLK) + getString(R.string.NoSessionToday));
            infoCount++;
            todayValid = false;
        }else if (date.equals("January 24 2016")) {
            infoList.add(infoCount, getString(R.string.RecordsTomorrow) + getString(R.string.NoSessionTomorrow));
            infoCount++;
            tomorrowValid = false;
        } else if (date.equals("January 25 2016")) {
            infoList.add(infoCount, getString(R.string.Records) + getString(R.string.NoSessionToday));
            infoCount++;
            todayValid = false;
        }else if (date.equals("February 11 2016")) {
            infoList.add(infoCount, getString(R.string.LincolnTomorrow) + getString(R.string.NoSessionTomorrow));
            infoCount++;
            tomorrowValid = false;
        } else if (date.equals("February 12 2016")) {
            infoList.add(infoCount, getString(R.string.Lincoln) + getString(R.string.NoSessionToday));
            infoCount++;
            todayValid = false;
        } else if (date.equals("February 14 2016")) {
            infoList.add(infoCount, getString(R.string.PresidentTomorrow) + getString(R.string.NoSessionTomorrow));
            infoCount++;
            todayValid = false;
            tomorrowValid = false;
        } else if (date.equals("February 15 2016")) {
            infoList.add(infoCount, getString(R.string.President) + getString(R.string.NoSessionToday));
            infoCount++;
            todayValid = false;
        } else if (date.equals("November 10 2015") || date.equals("March 08 2016")) {
            infoList.add(infoCount, getString(R.string.HalfDayConferenceMSTomorrow));
            infoCount++;
        }else if (date.equals("November 11 2015") || date.equals("November 12 2015")
            || date.equals("March 09 2016") || date.equals("March 10 2016")) {
            infoList.add(infoCount, getString(R.string.HalfDayConferenceMSTodayTomorrow));
            infoCount++;
        } else if (date.equals("November 13 2015") || date.equals("March 11 2016")) {
            infoList.add(infoCount, getString(R.string.HalfDayConferenceMSTomorrow));
            infoCount++;
        } else if (date.equals("November 24 2015") || date.equals("September 24 2015")
            || date.equals("October 08 2015")
            || date.equals("March 31 2016")) {
            infoList.add(infoCount, getString(R.string.HalfDayTomorrow));
            infoCount++;
        }else if (date.equals("November 25 2015") || date.equals("September 25 2015")
                || date.equals("October 09 2015")) {
            if (date.equals("November 25 2015")) {
                infoList.add(infoCount, getString(R.string.ThanksgivingRecessTomorrow));
                infoCount++;
                tomorrowValid = false;
            }

            infoList.add(infoCount, getString(R.string.HalfDay));
            infoCount++;
        }else if (date.equals("March 24 2016")) {
            infoList.add(infoCount, getString(R.string.GoodFridayTomorrow) + getString(R.string.NoSessionTomorrow));
            infoCount++;
            tomorrowValid = false;
        }else if (date.equals("March 25 2016")) {
            infoList.add(infoCount, getString(R.string.GoodFriday) + getString(R.string.NoSessionToday));
            infoCount++;
            todayValid = false;
        }else if (date.equals("March 27 2016")) {
            infoList.add(infoCount, getString(R.string.Easter));
            infoCount++;
            todayValid = false;
        } else if (date.equals("April 01 2016")) {
            infoList.add(infoCount, getString(R.string.HalfDay));
            infoList.add(infoCount + 1, getString(R.string.SpringBreakTomorrow));
            infoCount+=2;
            tomorrowValid = false;
        } else if (date.equals("April 02 2016") || date.equals("April 03 2016")
                || date.equals("April 04 2016") || date.equals("April 05 2016")
                || date.equals("April 06 2016") || date.equals("April 07 2016")
                || date.equals("April 08 2016")) {
            //Spring Break

            infoList.add(infoCount, getString(R.string.SpringBreak));
            infoCount++;
            todayValid = false;
            tomorrowValid = false;
        } else if (date.equals("November 02 2015") || date.equals("April 27 2016")) {
            infoList.add(infoCount, getString(R.string.PDDTomorrow) + getString(R.string.NoSessionTomorrow));
            infoCount++;
            tomorrowValid = false;
        } else if (date.equals("November 03 2015") || date.equals("April 28 2016")) {
            infoList.add(infoCount, getString(R.string.PDD) + getString(R.string.NoSessionToday));
            infoCount++;
            todayValid = false;
        } else if (date.equals("May 29 2016")) {
            infoList.add(infoCount, getString(R.string.MemorialDayTomorrow) + getString(R.string.NoSessionTomorrow));
            infoCount++;
            tomorrowValid = false;
        } else if (date.equals("May 30 2016")) {
            infoList.add(infoCount, getString(R.string.MemorialDay) + getString(R.string.NoSessionToday));
            infoCount++;
            todayValid = false;
        } else if (date.equals("June 02 2016")) {
            infoList.add(infoCount, getString(R.string.Senior));
            infoCount++;
            bobcats = true;
        } else if (date.equals("June 15 2016")) {
            infoList.add(infoCount, getString(R.string.YearEnd));
            infoCount++;
            tomorrowValid = false;
        }

        //If items were added...
        reminder = infoCount > 1;


        //Determine if the calculation should be available
        if (!tomorrowValid && !todayValid) {
            optToday.setEnabled(false);
            optToday.setChecked(false);
            optToday.setTextColor(Color.GRAY);
            optTomorrow.setEnabled(false);
            optTomorrow.setChecked(false);
            optTomorrow.setTextColor(Color.GRAY);
        } else if (!tomorrowValid) {
            optTomorrow.setEnabled(false);
            optTomorrow.setChecked(false);
            optTomorrow.setTextColor(Color.GRAY);
        } else if (!todayValid) {
            optToday.setEnabled(false);
            optToday.setChecked(false);
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
                    R.layout.item, special);
            lstInfo.setAdapter(specialadapter);
        }
    }

    public void checkWeekend() {
        //Friday is 5
        //Saturday is 6
        //Sunday is 7

        if (weekday == 5) {
            infoList.add(infoCount, getString(R.string.SaturdayTomorrow));
            optTomorrow.setEnabled(false);
            optTomorrow.setChecked(false);
            optTomorrow.setTextColor(Color.GRAY);
            infoCount++;
        } else if (weekday == 6) {
            infoList.add(infoCount, getString(R.string.SaturdayToday));
            optToday.setEnabled(false);
            optToday.setChecked(false);
            optToday.setTextColor(Color.GRAY);
            optTomorrow.setEnabled(false);
            optTomorrow.setChecked(false);
            optTomorrow.setTextColor(Color.GRAY);
            infoCount++;
        } else if (weekday == 7) {
            infoList.add(infoCount, getString(R.string.SundayToday));
            optToday.setEnabled(false);
            optToday.setChecked(false);
            optToday.setTextColor(Color.GRAY);
            infoCount++;
        }
    }
}




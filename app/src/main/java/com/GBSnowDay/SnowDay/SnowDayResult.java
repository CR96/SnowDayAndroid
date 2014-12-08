package com.GBSnowDay.SnowDay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;


public class SnowDayResult extends Activity {

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
    ListView lstClosings;
    TextView txtTier1;
    TextView txtTier2;
    TextView txtTier3;
    TextView txtTier4;

    ListView lstGB;
    ListView lstWJRT;
    ListView lstNWS;

    TextView txtPercent;

    ListView lstWeather;
    WebView webRadar;
    Button btnRadar;
    ProgressBar progCalculate;

    TabHost tabHost;

    //Variable declaration
    String orgName;
    String status;
    String schooltext;
    String weathertext;

    String[] orgNameLine;
    String[] statusLine;

    //Declare lists that will be used in ListAdapters
    List<String> GBInfo = new ArrayList<String>();
    List<String> closings = new ArrayList<String>();
    List<String> wjrtInfo = new ArrayList<String>();
    List<String> weather = new ArrayList<String>();
    List<String> nwsInfo = new ArrayList<String>();

    int GBCount = 1;
    int weatherCount = 0;
    int wjrtCount = 0;
    int nwsCount = 0;

    DateTime dt = new DateTime();

    int days;
    int dayrun;
    String date;
    String dateTomorrow;

    //Individual components of the calculation
    int schoolpercent;
    int weathertoday;
    int weathertomorrow;
    int weatherpercent;
    int percent;

    //Levels of school closings (near vs. far)
    int tier1 = 0;
    int tier2 = 0;
    int tier3 = 0;
    int tier4 = 0;


    //For the ending animation
    int percentscroll;

    //Every school this program searches for: true = closed, false = open (default)
    boolean GBAcademy;
    boolean GISD;
    boolean HolyFamily;
    boolean WPAcademy;

    boolean Durand; //Check for "Durand Senior Center"
    boolean Holly;  //Check for "Holly Academy"
    boolean Lapeer; //Check for "Chatfield School-Lapeer", "Greater Lapeer Transit Authority",
    // "Lapeer CMH Day Programs", "Lapeer Co. Ed-Tech Center", "Lapeer County Ofices", "
    // Lapeer District Library", "Lapeer Senior Center", and "St. Paul Lutheran-Lapeer"
    boolean Owosso; //Check for "Owosso Senior Center", "Baker College-Owosso", and "St. Paul Catholic-Owosso"

    boolean Beecher;
    boolean Clio; //Check for "Clio Area Senior Center", "Clio City Hall", and "Cornerstone Clio"
    boolean Davison; //Check for "Davison Senior Center", "Faith Baptist School-Davison", and "Montessori Academy-Davison"
    boolean Fenton; //Check for "Lake Fenton", "Fenton City Hall", and "Fenton Montessori Academy"
    boolean Flushing; //Check for "Flushing Senior Citizens Center" and "St. Robert-Flushing"
    boolean Genesee; //Check for "Freedom Work-Genesee Co.", "Genesee Christian-Burton",
    // "Genesee Co. Mobile Meals", "Genesee Hlth Sys Day Programs", "Genesee Stem Academy", and "Genesee I.S.D."
    boolean Kearsley;
    boolean LKFenton;
    boolean Linden; //Check for "Linden Charter Academy"
    boolean Montrose; //Check for "Montrose Senior Center"
    boolean Morris;  //Check for "Mt Morris Twp Administration" and "St. Mary's-Mt. Morris"
    boolean SzCreek; //Check for "Swartz Creek Area Senior Ctr." and "Swartz Creek Montessori"

    boolean Atherton;
    boolean Bendle;
    boolean Bentley;
    boolean Carman; //Check for "Carman-Ainsworth Senior Ctr."
    boolean Flint; //Thankfully this is listed as "Flint Community Schools" -
    // otherwise there would be 25 exceptions to check for.
    boolean Goodrich;

    boolean GB; //Check for "Freedom Work-Grand Blanc", "Grand Blanc Academy", "Grand Blanc City Offices",
    // "Grand Blanc Senior Center", and "Holy Family-Grand Blanc"

    //True is GB is already open (GB is false, time is during or after school hours)
    boolean GBOpen;

    //Every weather warning this program searches for
    boolean SigWeather;
    boolean WinterAdvisory;
    boolean WinterWatch;
    boolean LakeSnowAdvisory;
    boolean LakeSnowWatch;
    boolean Rain;
    boolean Drizzle;
    boolean Fog;
    boolean WindChillAdvisory;
    boolean WindChillWatch;
    boolean BlizzardWatch;
    boolean WinterWarn;
    boolean LakeSnowWarn;
    boolean IceStorm;
    boolean WindChillWarn;
    boolean BlizzardWarn;

    //Scraper status
    boolean WJRTActive = true;
    boolean NWSActive = true;

    /*Used for catching IOExceptions / NullPointerExceptions if there are connectivity issues
    or a webpage is down*/
    boolean WJRTFail;
    boolean NWSFail;

    //Custom adapter
    private ClosingsAdapter closingsAdapter;
    private WeatherAdapter weatherAdapter;
    private GBAdapter gbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snow_day_result);

        //Create TabHost
        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        //Tab 1 - Percent and information
        TabHost.TabSpec specs = tabHost.newTabSpec("tab1");
        specs.setContent(R.id.tab1);
        specs.setIndicator(getString(R.string.Percent));
        tabHost.addTab(specs);

        //Tab 2 - ABC 12 closings
        specs = tabHost.newTabSpec("tab2");
        specs.setContent(R.id.tab2);
        specs.setIndicator(getString(R.string.Closings));
        tabHost.addTab(specs);

        //Tab 3 - Weather warnings and radar
        specs = tabHost.newTabSpec("tab3");
        specs.setContent(R.id.tab3);
        specs.setIndicator(getString(R.string.Weather));
        tabHost.addTab(specs);

        //Declare views
        lstClosings = (ListView) findViewById(R.id.lstClosings);

        //Add the 27 fixed values so they can be set out of sequence
        closings.add(0, "");
        closings.add(1, "");
        closings.add(2, "");
        closings.add(3, "");
        closings.add(4, "");
        closings.add(5, "");
        closings.add(6, "");
        closings.add(7, "");
        closings.add(8, "");
        closings.add(9, "");
        closings.add(10, "");
        closings.add(11, "");
        closings.add(12, "");
        closings.add(13, "");
        closings.add(14, "");
        closings.add(15, "");
        closings.add(16, "");
        closings.add(17, "");
        closings.add(18, "");
        closings.add(19, "");
        closings.add(20, "");
        closings.add(21, "");
        closings.add(22, "");
        closings.add(23, "");
        closings.add(24, "");
        closings.add(25, "");
        closings.add(26, "");

        txtTier1 = (TextView) findViewById(R.id.txtTier1);
        txtTier2 = (TextView) findViewById(R.id.txtTier2);
        txtTier3 = (TextView) findViewById(R.id.txtTier3);
        txtTier4 = (TextView) findViewById(R.id.txtTier4);

        txtPercent = (TextView) findViewById(R.id.txtPercent);
        lstGB = (ListView) findViewById(R.id.lstGB);
        lstWJRT = (ListView) findViewById(R.id.lstWJRT);
        lstNWS = (ListView) findViewById(R.id.lstNWS);

        //Add the first GBInfo value so it can be set out of sequence
        GBInfo.add(0, "");

        //Add the first weather value so it can be set out of sequence
        weather.add(0, "");

        lstWeather = (ListView) findViewById(R.id.lstWeather);
        webRadar = (WebView) findViewById(R.id.webRadar);
        btnRadar = (Button) findViewById(R.id.btnRadar);
        progCalculate = (ProgressBar) findViewById(R.id.progCalculate);

        //Start the calculation
        Calculate();
    }

    //GBAdapter class
    private class GBAdapter extends BaseAdapter {
        private static final int TYPE_ITEM = 0;
        private static final int TYPE_SEPARATOR = 1;
        private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

        private ArrayList<String> mData = new ArrayList<String>();
        private LayoutInflater mInflater;

        private TreeSet<Integer> mSeparatorsSet = new TreeSet<Integer>();

        public GBAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void addItem(final String item) {
            mData.add(item);
            notifyDataSetChanged();
        }

        public void addSeparatorItem(final String item) {
            mData.add(item);
            //Save separator position
            mSeparatorsSet.add(mData.size() - 1);
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
        }

        @Override
        public int getViewTypeCount() {
            return TYPE_MAX_COUNT;
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
            /*No 'if (convertView == null)' statement to prevent view recycling
            (views must remain fixed)*/
            switch (type) {
                case TYPE_ITEM:
                    if (GB && position == 0) {
                        //If GB is closed, make it red.
                        convertView = mInflater.inflate(R.layout.itemredcenter, null);
                        holder.textView = (TextView) convertView.findViewById(R.id.text);
                        break;
                    } else if (GB && position == 1) {
                        convertView = mInflater.inflate(R.layout.itembluecenter, null);
                        holder.textView = (TextView) convertView.findViewById(R.id.text);
                        break;
                    }else{
                        convertView = mInflater.inflate(R.layout.itemlistcenter, null);
                        holder.textView = (TextView) convertView.findViewById(R.id.text);
                        break;
                    }
            }
            convertView.setTag(holder);
            holder.textView.setText(mData.get(position));
            return convertView;
        }
    }

    //Closings adapter class
    private class ClosingsAdapter extends BaseAdapter {
        private static final int TYPE_ITEM = 0;
        private static final int TYPE_SEPARATOR = 1;
        private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

        private ArrayList<String> mData = new ArrayList<String>();
        private LayoutInflater mInflater;

        private TreeSet<Integer> mSeparatorsSet = new TreeSet<Integer>();

        public ClosingsAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void addItem(final String item) {
            mData.add(item);
            notifyDataSetChanged();
        }

        public void addSeparatorItem(final String item) {
            mData.add(item);
            //Save separator position
            mSeparatorsSet.add(mData.size() - 1);
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
        }

        @Override
        public int getViewTypeCount() {
            return TYPE_MAX_COUNT;
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
            /*No 'if (convertView == null)' statement to prevent view recycling
            (views must remain fixed)*/
            switch (type) {
                case TYPE_ITEM:
                    if (Atherton && position == 2 || Bendle && position == 3
                            || Bentley && position == 4 || Carman && position == 5
                            || Flint && position == 6 || Goodrich && position == 7
                            || Beecher && position == 9 || Clio && position == 10
                            || Davison && position == 11 || Fenton && position == 12
                            || Flushing && position == 13 || Genesee && position == 14
                            || Kearsley && position == 15 || LKFenton && position == 16
                            || Linden && position == 17 || Montrose && position == 18
                            || Morris && position == 19 || SzCreek && position == 20
                            || Durand && position == 22 || Holly && position == 23
                            || Lapeer && position == 24 || Owosso && position == 25
                            || GBAcademy && position == 27 || GISD && position == 28
                            || HolyFamily && position == 29 || WPAcademy && position == 30) {
                        //If the school is closed, make it orange.
                        convertView = mInflater.inflate(R.layout.itemorange, null);
                        holder.textView = (TextView)convertView.findViewById(R.id.text);
                        break;
                    }else{
                        convertView = mInflater.inflate(R.layout.itemlist, null);
                        holder.textView = (TextView)convertView.findViewById(R.id.text);
                        break;
                    }
                case TYPE_SEPARATOR:
                    //Set the text separators ("Districts near Grand Blanc", etc.)
                    if (position == 0) {
                        convertView = mInflater.inflate(R.layout.itemlightseparator, null);
                        holder.textView = (TextView)convertView.findViewById(R.id.textSeparator);
                        break;
                    }else{
                        convertView = mInflater.inflate(R.layout.itemseparator, null);
                        holder.textView = (TextView) convertView.findViewById(R.id.textSeparator);
                        break;
                    }
            }
            convertView.setTag(holder);
            holder.textView.setText(mData.get(position));
            return convertView;
        }
    }

    //WeatherAdapter class
    private class WeatherAdapter extends BaseAdapter {
        private static final int TYPE_ITEM = 0;
        private static final int TYPE_SEPARATOR = 1;
        private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

        private ArrayList<String> mData = new ArrayList<String>();
        private LayoutInflater mInflater;

        private TreeSet<Integer> mSeparatorsSet = new TreeSet<Integer>();

        public WeatherAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void addItem(final String item) {
            mData.add(item);
            notifyDataSetChanged();
        }

        public void addSeparatorItem(final String item) {
            mData.add(item);
            //Save separator position
            mSeparatorsSet.add(mData.size() - 1);
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
        }

        @Override
        public int getViewTypeCount() {
            return TYPE_MAX_COUNT;
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
            /*No 'if (convertView == null)' statement to prevent view recycling
            (views must remain fixed)*/
            switch (type) {
                case TYPE_ITEM:
                    //Color the weather warning based on severity.
                    if (weather.get(position - 1).contains(getString(R.string.SigWeather))
                        || weather.get(position - 1).contains(getString(R.string.WinterAdvisory))
                        || weather.get(position - 1).contains(getString(R.string.LakeSnowAdvisory))
                        || weather.get(position - 1).contains(getString(R.string.Rain))
                        || weather.get(position - 1).contains(getString(R.string.Drizzle))
                        || weather.get(position - 1).contains(getString(R.string.Fog))
                        || weather.get(position - 1).contains(getString(R.string.WindChillAdvisory))) {
                        convertView = mInflater.inflate(R.layout.itemblue, null);
                        holder.textView = (TextView)convertView.findViewById(R.id.text);
                    }else if (weather.get(position - 1).contains(getString(R.string.WinterWatch))
                        || weather.get(position - 1).contains(getString(R.string.LakeSnowWatch))
                        || weather.get(position - 1).contains(getString(R.string.WindChillWatch))
                        || weather.get(position - 1).contains(getString(R.string.BlizzardWatch))){
                        convertView = mInflater.inflate(R.layout.itemorange, null);
                        holder.textView = (TextView)convertView.findViewById(R.id.text);
                    }else if (weather.get(position - 1).contains(getString(R.string.WinterWarn))
                        || weather.get(position - 1).contains(getString(R.string.LakeSnowWarn))
                        || weather.get(position - 1).contains(getString(R.string.IceStorm))
                        || weather.get(position - 1).contains(getString(R.string.WindChillWarn))
                        || weather.get(position - 1).contains(getString(R.string.BlizzardWarn))) {
                        convertView = mInflater.inflate(R.layout.itemred, null);
                        holder.textView = (TextView)convertView.findViewById(R.id.text);
                    }else{
                        convertView = mInflater.inflate(R.layout.itemlist, null);
                        holder.textView =(TextView)convertView.findViewById(R.id.text);
                    }
                    break;
                case TYPE_SEPARATOR:
                        convertView = mInflater.inflate(R.layout.itemseparator, null);
                        holder.textView = (TextView) convertView.findViewById(R.id.textSeparator);
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

    public void radarToggle(View view) {
        //Show / hide and configure the WebView-based radar
        if (webRadar.getVisibility() == View.GONE) {
            webRadar.setEnabled(true);
            webRadar.setVisibility(View.VISIBLE);
            webRadar.loadUrl("http://radar.weather.gov/Conus/Loop/centgrtlakes_loop.gif");
            webRadar.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            btnRadar.setText(getString(R.string.radarhide));
            if (NWSFail) {
                //Hide the ListView displaying the error message
                lstNWS.setVisibility(View.GONE);
            }else{
                //Hide the ListView displaying the weather information
                lstWeather.setVisibility(View.GONE);
            }
        } else {
            webRadar.setVisibility(View.GONE);
            webRadar.setEnabled(false);
            btnRadar.setText(getString(R.string.radarshow));
            if (NWSFail) {
                //Show the ListView displaying the error message
                lstNWS.setVisibility(View.VISIBLE);
            }else{
                //Show the ListView displaying the weather information
                lstWeather.setVisibility(View.VISIBLE);
            }
        }
    }

    private void Calculate() {
        /**
         * This application will predict the possibility of a snow day for Grand Blanc Community Schools.
         * Created by Corey Rowe, July 2014 - redesign of original Swing GUI.
         * Factors:
         * Weather warnings from the National Weather Service (includes snowfall, ice, and wind chill)
         * Number of past snow days (more = smaller chance)
         * Schools currently closed (data from WJRT)
         * Schools in higher tiers (4 is highest) will increase the snow day chance.
         * Obviously return 100% if GB is already closed.
         */

        //Read variables from SnowDay class
        Intent result = getIntent();
        dayrun = result.getIntExtra("dayrun", dayrun);
        days = result.getIntExtra("days", days);
        date = result.getStringExtra("date");
        dateTomorrow = result.getStringExtra("dateTomorrow");

        /**WJRT SCHOOL CLOSINGS SCRAPER**/
        new WJRTScraper().execute();

        /**NATIONAL WEATHER SERVICE SCRAPER**/
        new WeatherScraper().execute();

        //Final Percent Calculator
        new PercentCalculate().execute();


    }

    private class WJRTScraper extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... nothing) {
            Document schools = null;
            //Scrape School Closings from WJRT with Jsoup.

            /**WJRT SCHOOL CLOSINGS SCRAPER**/
            //Scrape School Closings from WJRT with Jsoup.
            //Run scraper in an Async task.

            //This is the current listings page.

            try {
                schools = Jsoup.connect("http://ftpcontent2.worldnow.com/wjrt/school/closings.htm").get();
                //Attempt to parse input
                for (Element row : schools.select("td[bgcolor]")) {
                    //Reading closings - name of institution and status
                    orgName = orgName + "\n" + (row.select("font.orgname").first().text());
                    status = status + "\n" + (row.select("font.status").first().text());
                }

                //Checking for null pointers not caught by NullPointerException
                if (orgName == null || status == null) {
                    //orgName or status is null.
                    schooltext = schools.text();
                    //This shows in place of the table (as plain text) if no schools or institutions are closed.
                    if (schooltext.contains("no active records")) {
                        //No schools are closed.
                        wjrtInfo.add(wjrtCount, getString(R.string.NoClosings));
                        wjrtCount++;
                        WJRTFail = false;
                    } else {
                        //Webpage layout was not recognized.
                        wjrtInfo.add(wjrtCount, getString(R.string.WJRTParseError));
                        wjrtInfo.add(wjrtCount + 1, getString(R.string.ErrorContact));
                        wjrtCount += 2;
                        WJRTFail = true;

                    }

                    //orgName and status have no content.
                    //Set dummy content so the scraper doesn't fail with a NullPointerException.
                    orgName = "DummyLine1\nDummyLine2\nDummyLine3";
                    status = "DummyLine1\nDummyLine2\nDummyLine3";

                }

            } catch (IOException e) {
                //Connectivity issues
                wjrtInfo.add(wjrtCount, getString(R.string.WJRTConnectionError) + " " + getString(R.string.NoConnection));
                wjrtCount++;
                WJRTFail = true;

            } catch (NullPointerException e) {
                //Webpage layout was not recognized.
                wjrtInfo.add(wjrtCount, getString(R.string.WJRTParseError));
                wjrtInfo.add(wjrtCount + 1, getString(R.string.ErrorContact));
                wjrtCount += 2;
                WJRTFail = true;
            }

            //Only run if WJRTFail is false to avoid NullPointerExceptions
            if (!WJRTFail) {
                //Splitting orgName and status strings by line break.
                //Saving to orgNameLine and statusLine.
                //This will create string arrays that can be parsed by for loops.
                orgNameLine = orgName.split("\n");
                statusLine = status.split("\n");


                //Sanity check - make sure Grand Blanc isn't already closed before predicting
                checkGBClosed();

                //Decide whether to check for today's closings or tomorrow's closings.

                if (dayrun == 0) {
                    //Check closings for today
                    checkClosingsToday();

                } else if (dayrun == 1) {
                    //Check closings for tomorrow
                    checkClosingsTomorrow();

                }
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            //WJRT scraper has finished.
            WJRTActive = false;
        }

    }

    private void checkGBClosed() {
        //Checking if GB is closed.
        for (int i = 1; i < orgNameLine.length; i++) {
            //If GB hasn't been found...
            if (!GB) {
                if (orgNameLine[i].contains("Grand Blanc") && !orgNameLine[i].contains("Academy")
                        && !orgNameLine[i].contains("Freedom") && !orgNameLine[i].contains("Offices")
                        && !orgNameLine[i].contains("City") && !orgNameLine[i].contains("Senior")
                        && !orgNameLine[i].contains("Holy") && statusLine[i].contains("Closed Today")
                        && dayrun == 0) {

                    GBInfo.set(0, getString(R.string.GBClosedToday));
                    GBInfo.add(GBCount, getString(R.string.SnowDay));
                    GBCount++;
                    //GB Found (today)
                    GB = true;
                    break;
                } if (orgNameLine[i].contains("Grand Blanc") && !orgNameLine[i].contains("Academy")
                        && !orgNameLine[i].contains("Freedom") && !orgNameLine[i].contains("Offices")
                        && !orgNameLine[i].contains("City") && !orgNameLine[i].contains("Senior")
                        && !orgNameLine[i].contains("Holy") && !orgNameLine[i].contains("only")
                        && statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {

                    GBInfo.set(0, getString(R.string.GBClosedTomorrow));
                    GBInfo.add(GBCount, getString(R.string.SnowDay));
                    GBCount++;
                    //GB Found (tomorrow)
                    GB = true;
                    break;
                }

            }
        }

        if (!GB) {
            //If GB is still false, GB is open
            GBInfo.set(0, getString(R.string.GB) + getString(R.string.Open));
            if (dt.getHourOfDay() >= 7 && dt.getHourOfDay() < 16 && dayrun == 0) {
                //Time is between 7AM and 4PM. School is already in session.
                GBInfo.add(GBCount, getString(R.string.SchoolOpen));
                GBOpen = true;
                GBCount++;
            }else if (dt.getHourOfDay() >= 16 && dayrun == 0) {
                //Time is after 4PM. School is already out.
                GBInfo.add(GBCount, getString(R.string.GB) + getString(R.string.Dismissed));
                GBOpen = true;
                GBCount++;
            }

        }
    }

    private void checkClosingsToday() {
        for (int i = 1; i < orgNameLine.length; i++) {
            if (!(Atherton)) {
                if (orgNameLine[i].contains("Atherton") && statusLine[i].contains("Closed Today")) {
                    closings.set(1, "Atherton: CLOSED");
                    tier4++;
                    Atherton = true;
                } else {
                    closings.set(1, "Atherton: OPEN");
                }
            }
            if (!(Bendle)) {
                if (orgNameLine[i].contains("Bendle") && statusLine[i].contains("Closed Today")) {
                    closings.set(2, "Bendle: CLOSED");
                    tier4++;
                    Bendle = true;
                } else {
                    closings.set(2, "Bendle: OPEN");
                }
            }
            if (!(Bentley)) {
                if (orgNameLine[i].contains("Bentley") && statusLine[i].contains("Closed Today")) {
                    closings.set(3, "Bentley: CLOSED");
                    tier4++;
                    Bentley = true;
                } else {
                    closings.set(3, "Bentley: OPEN");
                }
            }
            if (!(Carman)) {
                if (orgNameLine[i].contains("Carman-Ainsworth") && !orgNameLine[i].contains("Senior")
                        && statusLine[i].contains("Closed Today")) {
                    closings.set(4, "Carman-Ainsworth: CLOSED");
                    tier4++;
                    Carman = true;
                } else {
                    closings.set(4, "Carman-Ainsworth: OPEN");
                }
            }
            if (!(Flint)) {
                if (orgNameLine[i].contains("Flint Community Schools")
                        && statusLine[i].contains("Closed Today")) {
                    closings.set(5, "Flint: CLOSED");
                    tier4++;
                    Flint = true;
                } else {
                    closings.set(5, "Flint: OPEN");
                }
            }
            if (!(Goodrich)) {
                if (orgNameLine[i].contains("Goodrich") && statusLine[i].contains("Closed Today")) {
                    closings.set(6, "Goodrich: CLOSED");
                    tier4++;
                    Goodrich = true;
                } else {
                    closings.set(6, "Goodrich: OPEN");
                }
            }
            if (!(Beecher)) {
                if (orgNameLine[i].contains("Beecher") && statusLine[i].contains("Closed Today")) {
                    closings.set(7, "Beecher: CLOSED");
                    tier3++;
                    Beecher = true;
                } else {
                    closings.set(7, "Beecher: OPEN");
                }
            }
            if (!(Clio)) {
                if (orgNameLine[i].contains("Clio") && !orgNameLine[i].contains("Senior")
                        && !orgNameLine[i].contains("City") && !orgNameLine[i].contains("Cornerstone")
                        && statusLine[i].contains("Closed Today")) {
                    closings.set(8, "Clio: CLOSED");
                    tier3++;
                    Clio = true;
                } else {
                    closings.set(8, "Clio: OPEN");
                }
            }
            if (!(Davison)) {
                if (orgNameLine[i].contains("Davison") && !orgNameLine[i].contains("Senior")
                        && !orgNameLine[i].contains("Faith") && !orgNameLine[i].contains("Montessori")
                        && statusLine[i].contains("Closed Today")) {
                    closings.set(9, "Davison: CLOSED");
                    tier3++;
                    Davison = true;
                } else {
                    closings.set(9, "Davison: OPEN");
                }
            }
            if (!(Fenton)) {
                if (orgNameLine[i].contains("Fenton") && !orgNameLine[i].contains("Lake")
                        && !orgNameLine[i].contains("City") && !orgNameLine[i].contains("Montessori")
                        && statusLine[i].contains("Closed Today")) {
                    closings.set(10, "Fenton: CLOSED");
                    tier3++;
                    Fenton = true;
                } else {
                    closings.set(10, "Fenton: OPEN");
                }
            }
            if (!(Flushing)) {
                if (orgNameLine[i].contains("Flushing") && !orgNameLine[i].contains("Senior")
                        && !orgNameLine[i].contains("Robert")
                        && statusLine[i].contains("Closed Today")) {
                    closings.set(11, "Flushing: CLOSED");
                    tier3++;
                    Flushing = true;
                } else {
                    closings.set(11, "Flushing: OPEN");
                }
            }
            if (!(Genesee)) {
                if (orgNameLine[i].contains("Genesee") && !orgNameLine[i].contains("Freedom")
                        && !orgNameLine[i].contains("Christian") && !orgNameLine[i].contains("Mobile")
                        && !orgNameLine[i].contains("Programs") && !orgNameLine[i].contains("Hlth")
                        && !orgNameLine[i].contains("Sys") && !orgNameLine[i].contains("Stem")
                        && !orgNameLine[i].contains("I.S.D.") && statusLine[i].contains("Closed Today")) {
                    closings.set(12, "Genesee: CLOSED");
                    tier3++;
                    Genesee = true;
                } else {
                    closings.set(12, "Genesee: OPEN");
                }
            }
            if (!(Kearsley)) {
                if (orgNameLine[i].contains("Kearsley") && statusLine[i].contains("Closed Today")) {
                    closings.set(13, "Kearsley: CLOSED");
                    tier3++;
                    Kearsley = true;
                } else {
                    closings.set(13, "Kearsley: OPEN");
                }
            }
            if (!(LKFenton)) {
                if (orgNameLine[i].contains("Lake Fenton") && statusLine[i].contains("Closed Today")) {
                    closings.set(14, "Lake Fenton: CLOSED");
                    tier3++;
                    LKFenton = true;
                } else {
                    closings.set(14, "Lake Fenton: OPEN");
                }
            }
            if (!(Linden)) {
                if (orgNameLine[i].contains("Linden") && !orgNameLine[i].contains("Charter")
                        && statusLine[i].contains("Closed Today")) {
                    closings.set(15, "Linden: CLOSED");
                    tier3++;
                    Linden = true;
                } else {
                    closings.set(15, "Linden: OPEN");
                }
            }
            if (!(Montrose)) {
                if (orgNameLine[i].contains("Montrose") && !orgNameLine[i].contains("Senior")
                        && statusLine[i].contains("Closed Today")) {
                    closings.set(16, "Montrose: CLOSED");
                    tier3++;
                    Montrose = true;
                } else {
                    closings.set(16, "Montrose: OPEN");
                }
            }
            if (!(Morris)) {
                if (orgNameLine[i].contains("Mt. Morris") && !orgNameLine[i].contains("Administration")
                        && !orgNameLine[i].contains("Twp") && !orgNameLine[i].contains("Mary")
                        && statusLine[i].contains("Closed Today")) {
                    closings.set(17, "Mount Morris: CLOSED");
                    tier3++;
                    Morris = true;
                } else {
                    closings.set(17, "Mount Morris: OPEN");
                }
            }
            if (!(SzCreek)) {
                if (orgNameLine[i].contains("Swartz Creek") && !orgNameLine[i].contains("Senior")
                        && !orgNameLine[i].contains("Montessori")
                        && statusLine[i].contains("Closed Today")) {
                    closings.set(18, "Swartz Creek: CLOSED");
                    tier3++;
                    SzCreek = true;
                } else {
                    closings.set(18, "Swartz Creek: OPEN");
                }
            }
            if (!(Durand)) {
                if (orgNameLine[i].contains("Durand") && !orgNameLine[i].contains("Senior")
                        && statusLine[i].contains("Closed Today")) {
                    closings.set(19, "Durand: CLOSED");
                    tier2++;
                    Durand = true;
                } else {
                    closings.set(19, "Durand: OPEN");
                }
            }
            if (!(Holly)) {
                if (orgNameLine[i].contains("Holly") && !orgNameLine[i].contains("Academy")
                        && statusLine[i].contains("Closed Today")) {
                    closings.set(20, "Holly: CLOSED");
                    tier2++;
                    Holly = true;
                } else {
                    closings.set(20, "Holly: OPEN");
                }
            }
            if (!(Lapeer)) {
                if (orgNameLine[i].contains("Lapeer") && !orgNameLine[i].contains("Chatfield")
                        && !orgNameLine[i].contains("Transit") && !orgNameLine[i].contains("CMH")
                        && !orgNameLine[i].contains("Tech") && !orgNameLine[i].contains("Offices")
                        && !orgNameLine[i].contains("Library") && !orgNameLine[i].contains("Senior")
                        && !orgNameLine[i].contains("Paul") && statusLine[i].contains("Closed Today")) {
                    closings.set(21, "Lapeer: CLOSED");
                    tier2++;
                    Lapeer = true;
                } else {
                    closings.set(21, "Lapeer: OPEN");
                }
            }
            if (!(Owosso)) {
                if (orgNameLine[i].contains("Owosso") && !orgNameLine[i].contains("Senior")
                        && !orgNameLine[i].contains("Baker") && !orgNameLine[i].contains("Paul")
                        && statusLine[i].contains("Closed Today")) {
                    closings.set(22, "Owosso: CLOSED");
                    tier2++;
                    Owosso = true;
                } else {
                    closings.set(22, "Owosso: OPEN");
                }
            }
            if (!(GBAcademy)) {
                if (orgNameLine[i].contains("Grand Blanc Academy")
                        && statusLine[i].contains("Closed Today")) {
                    closings.set(23, "Grand Blanc Academy: CLOSED");
                    tier1++;
                    GBAcademy = true;
                } else {
                    closings.set(23, "Grand Blanc Academy: OPEN");
                }
            }
            if (!(GISD)) {
                if (orgNameLine[i].contains("Genesee I.S.D.") && statusLine[i].contains("Closed Today")) {
                    closings.set(24, "Genesee I.S.D.: CLOSED");
                    tier1++;
                    GISD = true;
                } else {
                    closings.set(24, "Genesee I.S.D.: OPEN");
                }
            }
            if (!(HolyFamily)) {
                if (orgNameLine[i].contains("Holy Family") && statusLine[i].contains("Closed Today")) {
                    closings.set(25, "Holy Family: CLOSED");
                    tier1++;
                    HolyFamily = true;
                } else {
                    closings.set(25, "Holy Family: OPEN");
                }
            }
            if (!(WPAcademy)) {
                if (orgNameLine[i].contains("Woodland Park Academy")
                        && statusLine[i].contains("Closed Today")) {
                    closings.set(26, "Woodland Park Academy: CLOSED");
                    tier1++;
                    WPAcademy = true;
                } else {
                    closings.set(26, "Woodland Park Academy: OPEN");
                }
            }
        }
    }

    private void checkClosingsTomorrow() {
        for (int i = 1; i < orgNameLine.length; i++) {
            if (!(Atherton)) {
                if (orgNameLine[i].contains("Atherton") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(1, "Atherton: CLOSED");
                    tier4++;
                    Atherton = true;
                } else {
                    closings.set(1, "Atherton: OPEN");
                }
            }
            if (!(Bendle)) {
                if (orgNameLine[i].contains("Bendle") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(2, "Bendle: CLOSED");
                    tier4++;
                    Bendle = true;
                } else {
                    closings.set(2, "Bendle: OPEN");
                }
            }
            if (!(Bentley)) {
                if (orgNameLine[i].contains("Bentley") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(3, "Bentley: CLOSED");
                    tier4++;
                    Bentley = true;
                } else {
                    closings.set(3, "Bentley: OPEN");
                }
            }
            if (!(Carman)) {
                if (orgNameLine[i].contains("Carman-Ainsworth") && !orgNameLine[i].contains("Senior")
                        && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(4, "Carman-Ainsworth: CLOSED");
                    tier4++;
                    Carman = true;
                } else {
                    closings.set(4, "Carman-Ainsworth: OPEN");
                }
            }
            if (!(Flint)) {
                if (orgNameLine[i].contains("Flint Community Schools")
                        && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(5, "Flint: CLOSED");
                    tier4++;
                    Flint = true;
                } else {
                    closings.set(5, "Flint: OPEN");
                }
            }
            if (!(Goodrich)) {
                if (orgNameLine[i].contains("Goodrich") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(6, "Goodrich: CLOSED");
                    tier4++;
                    Goodrich = true;
                } else {
                    closings.set(6, "Goodrich: OPEN");
                }
            }
            if (!(Beecher)) {
                if (orgNameLine[i].contains("Beecher") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(7, "Beecher: CLOSED");
                    tier3++;
                    Beecher = true;
                } else {
                    closings.set(7, "Beecher: OPEN");
                }
            }
            if (!(Clio)) {
                if (orgNameLine[i].contains("Clio") && !orgNameLine[i].contains("Senior")
                        && !orgNameLine[i].contains("City") && !orgNameLine[i].contains("Cornerstone")
                        && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(8, "Clio: CLOSED");
                    tier3++;
                    Clio = true;
                } else {
                    closings.set(8, "Clio: OPEN");
                }
            }
            if (!(Davison)) {
                if (orgNameLine[i].contains("Davison") && !orgNameLine[i].contains("Senior")
                        && !orgNameLine[i].contains("Faith") && !orgNameLine[i].contains("Montessori")
                        && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(9, "Davison: CLOSED");
                    tier3++;
                    Davison = true;
                } else {
                    closings.set(9, "Davison: OPEN");
                }
            }
            if (!(Fenton)) {
                if (orgNameLine[i].contains("Fenton") && !orgNameLine[i].contains("Lake")
                        && !orgNameLine[i].contains("City") && !orgNameLine[i].contains("Montessori")
                        && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(10, "Fenton: CLOSED");
                    tier3++;
                    Fenton = true;
                } else {
                    closings.set(10, "Fenton: OPEN");
                }
            }
            if (!(Flushing)) {
                if (orgNameLine[i].contains("Flushing") && !orgNameLine[i].contains("Senior")
                        && !orgNameLine[i].contains("Robert") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(11, "Flushing: CLOSED");
                    tier3++;
                    Flushing = true;
                } else {
                    closings.set(11, "Flushing: OPEN");
                }
            }
            if (!(Genesee)) {
                if (orgNameLine[i].contains("Genesee") && !orgNameLine[i].contains("Freedom")
                        && !orgNameLine[i].contains("Christian") && !orgNameLine[i].contains("Mobile")
                        && !orgNameLine[i].contains("Programs") && !orgNameLine[i].contains("Hlth")
                        && !orgNameLine[i].contains("Sys") && !orgNameLine[i].contains("Stem")
                        && !orgNameLine[i].contains("I.S.D.") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(12, "Genesee: CLOSED");
                    tier3++;
                    Genesee = true;
                } else {
                    closings.set(12, "Genesee: OPEN");
                }
            }
            if (!(Kearsley)) {
                if (orgNameLine[i].contains("Kearsley") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(13, "Kearsley: CLOSED");
                    tier3++;
                    Kearsley = true;
                } else {
                    closings.set(13, "Kearsley: OPEN");
                }
            }
            if (!(LKFenton)) {
                if (orgNameLine[i].contains("Lake Fenton") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(14, "Lake Fenton: CLOSED");
                    tier3++;
                    LKFenton = true;
                } else {
                    closings.set(14, "Lake Fenton: OPEN");
                }
            }
            if (!(Linden)) {
                if (orgNameLine[i].contains("Linden") && !orgNameLine[i].contains("Charter")
                        && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(15, "Linden: CLOSED");
                    tier3++;
                    Linden = true;
                } else {
                    closings.set(15, "Linden: OPEN");
                }
            }
            if (!(Montrose)) {
                if (orgNameLine[i].contains("Montrose") && !orgNameLine[i].contains("Senior")
                        && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(16, "Montrose: CLOSED");
                    tier3++;
                    Montrose = true;
                } else {
                    closings.set(16, "Montrose: OPEN");
                }
            }
            if (!(Morris)) {
                if (orgNameLine[i].contains("Mt. Morris") && !orgNameLine[i].contains("Administration")
                        && !orgNameLine[i].contains("Twp") && !orgNameLine[i].contains("Mary")
                        && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(17, "Mount Morris: CLOSED");
                    tier3++;
                    Morris = true;
                } else {
                    closings.set(17, "Mount Morris: OPEN");
                }
            }
            if (!(SzCreek)) {
                if (orgNameLine[i].contains("Swartz Creek") && !orgNameLine[i].contains("Senior")
                        && !orgNameLine[i].contains("Montessori")
                        && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(18, "Swartz Creek: CLOSED");
                    tier3++;
                    SzCreek = true;
                } else {
                    closings.set(18, "Swartz Creek: OPEN");
                }
            }
            if (!(Durand)) {
                if (orgNameLine[i].contains("Durand") && !orgNameLine[i].contains("Senior")
                        && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(19, "Durand: CLOSED");
                    tier2++;
                    Durand = true;
                } else {
                    closings.set(19, "Durand: OPEN");
                }
            }
            if (!(Holly)) {
                if (orgNameLine[i].contains("Holly") && !orgNameLine[i].contains("Academy")
                        && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(20, "Holly: CLOSED");
                    tier2++;
                    Holly = true;
                } else {
                    closings.set(20, "Holly: OPEN");
                }
            }
            if (!(Lapeer)) {
                if (orgNameLine[i].contains("Lapeer") && !orgNameLine[i].contains("Chatfield")
                        && !orgNameLine[i].contains("Transit") && !orgNameLine[i].contains("CMH")
                        && !orgNameLine[i].contains("Tech") && !orgNameLine[i].contains("Offices")
                        && !orgNameLine[i].contains("Library") && !orgNameLine[i].contains("Senior")
                        && !orgNameLine[i].contains("Paul") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(21, "Lapeer: CLOSED");
                    tier2++;
                    Lapeer = true;
                } else {
                    closings.set(21, "Lapeer: OPEN");
                }
            }
            if (!(Owosso)) {
                if (orgNameLine[i].contains("Owosso") && !orgNameLine[i].contains("Senior")
                        && !orgNameLine[i].contains("Baker") && !orgNameLine[i].contains("Paul")
                        && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(22, "Owosso: CLOSED");
                    tier2++;
                    Owosso = true;
                } else {
                    closings.set(22, "Owosso: OPEN");
                }
            }
            if (!(GBAcademy)) {
                if (orgNameLine[i].contains("Grand Blanc Academy")
                        && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(23, "Grand Blanc Academy: CLOSED");
                    tier1++;
                    GBAcademy = true;
                } else {
                    closings.set(23, "Grand Blanc Academy: OPEN");
                }
            }
            if (!(GISD)) {
                if (orgNameLine[i].contains("Genesee I.S.D.")
                        && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(24, "Genesee I.S.D.: CLOSED");
                    tier1++;
                    GISD = true;
                } else {
                    closings.set(24, "Genesee I.S.D.: OPEN");
                }
            }
            if (!(HolyFamily)) {
                if (orgNameLine[i].contains("Holy Family")
                        && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(25, "Holy Family: CLOSED");
                    tier1++;
                    HolyFamily = true;
                } else {
                    closings.set(25, "Holy Family: OPEN");
                }
            }
            if (!(WPAcademy)) {
                if (orgNameLine[i].contains("Woodland Park Academy")
                        && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(26, "Woodland Park Academy: CLOSED");
                    tier1++;
                    WPAcademy = true;
                } else {
                    closings.set(26, "Woodland Park Academy: OPEN");
                }
            }
        }
    }


    private class WeatherScraper extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... nothing) {
            /**NATIONAL WEATHER SERVICE SCRAPER**/
            //Change the percentage based on current storm/wind/temperature warnings.

            Document weatherdoc = null;

            //Live html
            try {
                weatherdoc = Jsoup.connect("http://forecast.weather.gov/afm/PointClick.php?lat=42.92580&lon=-83.61870").get();
                //"Searching for elements in class 'warn'
                Elements weatherWarn = weatherdoc.getElementsByClass("warn");
                //Saving elements to searchable string weathertext
                weathertext = weatherWarn.toString();

                if (weathertext.equals("")) {
                    //weathertext is empty.
                    //Searching for element 'hazards_content'
                    //This element should always be present even if no hazards are present.
                    Element weatherNull = weatherdoc.getElementById("hazards_content");

                    if (weatherNull.toString().contains("No Hazards in Effect")) {
                        //Webpage parsed correctly: no hazards present.
                        weather.set(0, getString(R.string.NoWeather));
                        NWSFail = false;
                    }
                } else {
                    //Hazards found. Use the data
                    getWeather();
                }
            } catch (IOException e) {
                //Connectivity issues
                nwsInfo.add(nwsCount, getString(R.string.WeatherConnectionError) + " " + getString(R.string.NoConnection));
                nwsCount++;
                NWSFail = true;
            } catch (NullPointerException e) {
                //Webpage layout not recognized.
                nwsInfo.add(nwsCount, getString(R.string.WeatherParseError));
                nwsInfo.add(nwsCount + 1, getString(R.string.ErrorContact));
                nwsCount += 2;
                NWSFail = true;
            }


            return null;
        }

        protected void onPostExecute(Void result) {
            //Weather scraper has finished.
            NWSActive = false;
        }
    }

    private void getWeather() {
        /*Only the highest weatherpercent is stored (not cumulative).
        Watches affect tomorrow's calculation.
        Advisories and Warnings affect today's calculation.*/

        if (weathertext.contains("Significant Weather Advisory")) {
            //Significant Weather Advisory - 15% weatherpercent
            SigWeather = true;
            weathertoday = 15;
        }
        if (weathertext.contains("Winter Weather Advisory")) {
            //Winter Weather Advisory - 30% weatherpercent
            WinterAdvisory = true;
            weathertoday = 30;
        }
        if (weathertext.contains("Lake-Effect Snow Advisory")) {
            //Lake Effect Snow Advisory - 40% weatherpercent
            LakeSnowAdvisory = true;
            weathertoday = 40;
        }
        if (weathertext.contains("Freezing Rain Advisory")) {
            //Freezing Rain - 40% weatherpercent
            Rain = true;
            weathertoday = 40;
        }
        if (weathertext.contains("Freezing Drizzle Advisory")) {
            //Freezing Drizzle - 40% weatherpercent
            Drizzle = true;
            weathertoday = 40;
        }
        if (weathertext.contains("Freezing Fog Advisory")) {
            //Freezing Fog - 40% weatherpercent
            Fog = true;
            weathertoday = 40;
        }
        if (weathertext.contains("Wind Chill Advisory")) {
            //Wind Chill Advisory - 40% weatherpercent
            WindChillAdvisory = true;
            weathertoday = 40;
        }
        if (weathertext.contains("Ice Storm Warning")) {
            //Ice Storm Warning - 70% weatherpercent
            IceStorm = true;
            weathertoday = 70;
        }
        if (weathertext.contains("Wind Chill Watch")) {
            //Wind Chill Watch - 70% weatherpercent
            WindChillWatch = true;
            weathertomorrow = 70;
        }
        if (weathertext.contains("Wind Chill Warning")) {
            //Wind Chill Warning - 70% weatherpercent
            WindChillWarn = true;
            weathertoday = 70;
        }
        if (weathertext.contains("Winter Storm Watch")) {
            //Winter Storm Watch - 80% weatherpercent
            WinterWatch = true;
            weathertomorrow = 80;
        }
        if (weathertext.contains("Winter Storm Warning")) {
            //Winter Storm Warning - 80% weatherpercent
            WinterWarn = true;
            weathertoday = 80;
        }
        if (weathertext.contains("Lake-Effect Snow Watch")) {
            //Lake Effect Snow Watch - 80% weatherpercent
            LakeSnowWatch = true;
            weathertomorrow = 80;
        }
        if (weathertext.contains("Lake-Effect Snow Warning")) {
            //Lake Effect Snow Warning - 80% weatherpercent
            LakeSnowWarn = true;
            weathertoday = 80;
        }
        if (weathertext.contains("Blizzard Watch")) {
            //Blizzard Watch - 90% weatherpercent
            BlizzardWatch = true;
            weathertomorrow = 90;
        }
        if (weathertext.contains("Blizzard Warning")) {
            //Blizzard Warning - 90% weatherpercent
            BlizzardWarn = true;
            weathertoday = 90;
        }

        //If none of the above warnings are present
        if (weathertoday == 0 && weathertomorrow == 0) {
            weather.set(0, getString(R.string.NoWeather));
        }

        //Set entries in the list in order of decreasing category (warn -> watch -> advisory)
        if (BlizzardWarn) {
            weather.add(weatherCount, getString(R.string.BlizzardWarn));
            weatherCount++;
        }if (LakeSnowWarn) {
            weather.add(weatherCount, getString(R.string.LakeSnowWarn));
            weatherCount++;
        }if (WinterWarn) {
            weather.add(weatherCount, getString(R.string.WinterWarn));
            weatherCount++;
        }if (WindChillWarn) {
            weather.add(weatherCount, getString(R.string.WindChillWarn));
            weatherCount++;
        }if (IceStorm) {
            weather.add(weatherCount, getString(R.string.IceStorm));
            weatherCount++;
        }if (BlizzardWatch){
            weather.add(weatherCount, getString(R.string.BlizzardWatch));
            weatherCount++;
        }if (LakeSnowWatch) {
            weather.add(weatherCount, getString(R.string.LakeSnowWatch));
            weatherCount++;
        }if (WinterWatch) {
            weather.add(weatherCount, getString(R.string.WinterWatch));
            weatherCount++;
        }if (WindChillWatch) {
            weather.add(weatherCount, getString(R.string.WindChillWatch));
            weatherCount++;
        }if (LakeSnowAdvisory) {
            weather.add(weatherCount, getString(R.string.LakeSnowAdvisory));
            weatherCount++;
        }if (WinterAdvisory) {
            weather.add(weatherCount, getString(R.string.WinterAdvisory));
            weatherCount++;
        }if (WindChillAdvisory) {
            weather.add(weatherCount, getString(R.string.WindChillAdvisory));
            weatherCount++;
        }if (Rain) {
            weather.add(weatherCount, getString(R.string.Rain));
            weatherCount++;
        }if (Drizzle) {
            weather.add(weatherCount, getString(R.string.Drizzle));
            weatherCount++;
        }if (Fog) {
            weather.add(weatherCount, getString(R.string.Fog));
            weatherCount++;
        }if (SigWeather) {
            weather.add(weatherCount, getString(R.string.SigWeather));
            weatherCount++;
        }
    }

    private class PercentCalculate extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... nothing) {

            //Give the scrapers time to act before displaying the percent

            while (WJRTActive || NWSActive) {
                try {
                    //Wait for scrapers to finish before continuing
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //Set the schoolpercent
            if (tier4 > 2) {
                //3+ schools near GB are closed. 80% schoolpercent.
                schoolpercent += 80;
            } if (tier3 > 2) {
                //3+ schools in Genesee County are closed. 60% schoolpercent.
                schoolpercent += 60;
            } if (tier2 > 2) {
                //3+ schools in nearby counties are closed. 40% schoolpercent.
                schoolpercent += 40;
            } if (tier1 > 2) {
                //3+ academies are closed. 20% schoolpercent.
                schoolpercent += 20;
            }

            //Set the weatherpercent
            if (dayrun == 0) {
                weatherpercent = weathertoday;
            }else if (dayrun == 1) {
                weatherpercent = weathertomorrow;
            }

            //Calculate the total percent.
            //Set the percent to the higher percent.
            if (weatherpercent > schoolpercent) {
                percent = weatherpercent;
            } else if (schoolpercent > weatherpercent) {
                percent = schoolpercent;
            }

            //Reduce the percent chance by three for every snow day entered.
            percent -= (days * 3);

            //No negative percents.
            if (percent < 0) {
                percent = 0;
            }

            //Don't allow a chance above 90%.
            if (percent > 90) {
                percent = 90;
            }

            //Negate the above results for special cases
            if (GB) {
                //WJRTScraper reports Grand Blanc is closed. Override percentage, set to 100%
                percent = 100;
            }else if (GBOpen) {
                //GB is false and the time is during or after school hours. 0% chance.
                percent = 0;
            }

            percentscroll = 0;

            runOnUiThread(new Runnable() {
                public void run() {
                    progCalculate.setVisibility(View.GONE);
                    txtPercent.setText("0%");
                }
            });

            //Animate txtPercent
            if (WJRTFail && NWSFail) {
                //Both scrapers failed. A percentage cannot be determined.
                //Don't set the percent.
                GBInfo.set(0, getString(R.string.CalculateError));
                runOnUiThread(new Runnable() {
                    public void run() {
                        txtPercent.setText("--");
                    }
                });
            } else {
                try {
                    for (int i = 0; i < percent; i++) {
                        Thread.sleep(10);
                        if (percentscroll >= 0 && percentscroll <= 20) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    txtPercent.setTextColor(Color.RED);
                                }
                            });
                        } if (percentscroll > 20 && percentscroll <= 60) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    txtPercent.setTextColor(Color.rgb(255, 165, 0));
                                }
                            });
                        } if (percentscroll > 60 && percentscroll <= 80) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    txtPercent.setTextColor(Color.GREEN);
                                }
                            });
                        } if (percentscroll > 80) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    txtPercent.setTextColor(Color.BLUE);
                                }
                            });
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                                txtPercent.setText((percentscroll) + "%");
                            }
                        });
                        percentscroll++;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btnRadar.setVisibility(View.VISIBLE);
                }
            });


            //Set the content of the information ListView
            if (WJRTFail || NWSFail) {
                //Network communication issues
                GBInfo.add(GBCount, getString(R.string.NoNetwork));
                GBCount++;
            }



            gbAdapter = new GBAdapter();
            for (int i = 0; i < GBInfo.size(); i++) {
                gbAdapter.addItem(GBInfo.get(i));
            }

            lstGB.setAdapter(gbAdapter);

            //Set up the ListView adapter that displays school closings

            if (!WJRTFail) {
                //WJRT has not failed.
                closingsAdapter = new ClosingsAdapter();
                if (dayrun == 0) {
                    closingsAdapter.addSeparatorItem("Closings for " + date);
                }else if (dayrun == 1) {
                    closingsAdapter.addSeparatorItem("Closings for " + dateTomorrow);
                }

                closingsAdapter.addSeparatorItem(getString(R.string.tier4));
                for (int i = 1; i < closings.size(); i++) {
                    closingsAdapter.addItem(closings.get(i));
                    if (i == 6) {
                        closingsAdapter.addSeparatorItem(getString(R.string.tier3));
                    } if (i == 18) {
                        closingsAdapter.addSeparatorItem(getString(R.string.tier2));
                    } if (i == 22) {
                        closingsAdapter.addSeparatorItem(getString(R.string.tier1));
                    }
                }

                lstClosings.setAdapter(closingsAdapter);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lstClosings.setVisibility(View.VISIBLE);
                    }
                });
            } else {
                //WJRT has failed.
                ArrayAdapter<String> WJRTadapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, wjrtInfo);
                lstWJRT.setAdapter(WJRTadapter);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lstWJRT.setVisibility(View.VISIBLE);
                    }
                });

            }

            //Set up the ListView adapter that displays weather warnings
            if (!NWSFail) {
                //NWS has not failed.

                //Remove blank entries
                for (int i = 0; i < weather.size(); i++) {
                    if (weather.get(i).equals("")) {
                        weather.remove(i);
                    }
                }

                weatherAdapter = new WeatherAdapter();
                weatherAdapter.addSeparatorItem(getString(R.string.NWS));
                for (int i = 0; i < weather.size(); i++) {
                    weatherAdapter.addItem(weather.get(i));
                }

                lstWeather.setAdapter(weatherAdapter);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lstWeather.setVisibility(View.VISIBLE);
                    }
                });
            } else {
                //NWS has failed.
                ArrayAdapter<String> NWSadapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, nwsInfo);
                lstNWS.setAdapter(NWSadapter);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lstNWS.setVisibility(View.VISIBLE);
                    }
                });

            }
        }
    }
}







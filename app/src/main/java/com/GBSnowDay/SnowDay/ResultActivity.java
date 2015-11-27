package com.GBSnowDay.SnowDay;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ResultActivity extends AppCompatActivity {

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


    String[] orgNameLine;
    String[] statusLine;
    String[] weatherwarn;

    //Declare lists that will be used in ListAdapters
    List<String> GBInfo = new ArrayList<>();
    List<String> closings = new ArrayList<>();
    List<String> wjrtInfo = new ArrayList<>();
    List<String> weather = new ArrayList<>();
    List<String> nwsInfo = new ArrayList<>();

    int GBCount = 1;
    int weatherCount = 0;
    int wjrtCount = 0;
    int nwsCount = 0;

    DateTime dt = new DateTime();

    int days;
    int dayrun;

    //Individual components of the calculation
    int schoolpercent;
    int weathertoday;
    int weathertomorrow;
    int weatherpercent;
    int percent;

    //Levels of school closings (near vs. far)
    int tier1today = 0;
    int tier2today = 0;
    int tier3today = 0;
    int tier4today = 0;
    
    int tier1tomorrow = 0;
    int tier2tomorrow = 0;
    int tier3tomorrow = 0;
    int tier4tomorrow = 0;

    //Every school this program searches for: true = closed, false = open (default)
    static boolean GBAcademy;
    static boolean GISD;
    static boolean HolyFamily;
    static boolean WPAcademy;

    static boolean Durand; //Check for "Durand Senior Center"
    static boolean Holly;  //Check for "Holly Academy"
    static boolean Lapeer; //Check for "Chatfield School-Lapeer", "Greater Lapeer Transit Authority",
    // "Lapeer CMH Day Programs", "Lapeer Co. Ed-Tech Center", "Lapeer County Ofices", "
    // Lapeer District Library", "Lapeer Senior Center", and "St. Paul Lutheran-Lapeer"
    static boolean Owosso; //Check for "Owosso Senior Center", "Baker College-Owosso", "Owosso Social Security Office",
    // and "St. Paul Catholic-Owosso"

    static boolean Beecher;
    static boolean Clio; //Check for "Clio Area Senior Center", "Clio City Hall", and "Cornerstone Clio"
    static boolean Davison; //Check for "Davison Senior Center", "Faith Baptist School-Davison", "Montessori Academy-Davison",
    // and "Ross Medical Education-Davison"
    static boolean Fenton; //Check for "Lake Fenton", "Fenton City Hall", and "Fenton Montessori Academy"
    static boolean Flushing; //Check for "Flushing Senior Citizens Center" and "St. Robert-Flushing"
    static boolean Genesee; //Check for "Freedom Work-Genesee Co.", "Genesee Christian-Burton",
    // "Genesee Co. Mobile Meals", "Genesee Hlth Sys Day Programs", "Genesee Stem Academy", and "Genesee I.S.D."
    static boolean Kearsley;
    static boolean LKFenton;
    static boolean Linden; //Check for "Linden Charter Academy"
    static boolean Montrose; //Check for "Montrose Senior Center"
    static boolean Morris;  //Check for "Mt Morris Twp Administration" and "St. Mary's-Mt. Morris"
    static boolean SzCreek; //Check for "Swartz Creek Area Senior Ctr." and "Swartz Creek Montessori"

    static boolean Atherton;
    static boolean Bendle;
    static boolean Bentley;
    static boolean Carman; //Check for "Carman-Ainsworth Senior Ctr."
    static boolean Flint; //Thankfully this is listed as "Flint Community Schools" -
    // otherwise there would be 25 exceptions to check for.
    static boolean Goodrich;

    static boolean GB; //Check for "Freedom Work-Grand Blanc", "Grand Blanc Academy", "Grand Blanc City Offices",
    // "Grand Blanc Senior Center", and "Holy Family-Grand Blanc"

    //True is GB is already open (GB is false, time is during or after school hours)
    static boolean GBOpen;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        //Read variables from MainActivity class
        Intent result = getIntent();
        dayrun = result.getIntExtra("dayrun", dayrun);
        days = result.getIntExtra("days", days);

        //Create TabHost
        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        //Tab 1 - Percent and information
        TabHost.TabSpec specs = tabHost.newTabSpec("tab1");
        specs.setContent(R.id.tab1);
        specs.setIndicator(getString(R.string.tab1));
        tabHost.addTab(specs);

        //Tab 2 - ABC 12 closings
        specs = tabHost.newTabSpec("tab2");
        specs.setContent(R.id.tab2);
        specs.setIndicator(getString(R.string.tab2));
        tabHost.addTab(specs);

        //Tab 3 - Weather warnings and radar
        specs = tabHost.newTabSpec("tab3");
        specs.setContent(R.id.tab3);
        specs.setIndicator(getString(R.string.tab3));
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

    @Override
    public boolean onNavigateUp() {
        super.onNavigateUp();
        //Return to the previous activity
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.closings) {
            //Open URL with warnings listed
            String url = "http://www.abc12.com/closings";
            Intent i = new Intent(Intent.ACTION_VIEW);
            Uri u = Uri.parse(url);
            Context context = this;

            try {
                //Start the activity
                i.setData(u);
                startActivity(i);
            }catch (ActivityNotFoundException e) {
                //Raise on activity not found
                Toast.makeText(context, "No browser found.", Toast.LENGTH_SHORT).show();
            }
        }
        if (id == R.id.weather) {
            //Open URL with warnings listed
            String url = "http://mobile.weather.gov/index.php?lat=42.92515852864426&lon=-83.63534793945507";
            Intent i = new Intent(Intent.ACTION_VIEW);
            Uri u = Uri.parse(url);
            Context context = this;

            try {
                //Start the activity
                i.setData(u);
                startActivity(i);
            }catch (ActivityNotFoundException e) {
                //Raise on activity not found
                Toast.makeText(context, "No browser found.", Toast.LENGTH_SHORT).show();
            }
        }
        if (id == R.id.action_about) {
            //Show the about activity
            Intent about = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(about);
            return true;
        }
        return super.onOptionsItemSelected(item);
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

        /**WJRT SCHOOL CLOSINGS SCRAPER**/
        new WJRTScraper().execute();

        /**NATIONAL WEATHER SERVICE SCRAPER**/
        new WeatherScraper().execute();

        //Final Percent Calculator
        new PercentCalculate().execute();


    }

    private class WJRTScraper extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... nothing) {
            Document schools;
            //Scrape School Closings from WJRT with Jsoup.

            /**WJRT SCHOOL CLOSINGS SCRAPER**/
            //Scrape School Closings from WJRT with Jsoup.
            //Run scraper in an Async task.

            //This is the current listings page.

            try {
                schools = Jsoup.connect("http://gray.ftp.clickability.com/wjrt/school/closings.htm").get();
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
                        wjrtCount ++;
                        WJRTFail = true;

                        Crashlytics.log(getString(R.string.WJRTParseError));

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

                Crashlytics.logException(e);

            } catch (NullPointerException e) {
                //Webpage layout was not recognized.
                wjrtInfo.add(wjrtCount, getString(R.string.WJRTParseError));
                wjrtCount ++;
                WJRTFail = true;

                Crashlytics.logException(e);
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


                //Check school closings
                checkClosings();

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
                        && !orgNameLine[i].contains("Holy")) {
                    GBInfo.set(0, getString(R.string.GB) + statusLine[i]);
                    if (statusLine[i].contains("Closed Today") && dayrun == 0
                            || statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                        GBInfo.add(GBCount, getString(R.string.SnowDay));
                        GBCount++;
                        //GB Found
                        GB = true;
                    }
                    break;
                }
            }
        }

        if (!GB) {
            //If GB is still false, GB is open
            if (dayrun == 0) {
                GBInfo.set(0, getString(R.string.GB) + getString(R.string.OpenToday));
            }else if (dayrun == 1) {
                GBInfo.set(0, getString(R.string.GB) + getString(R.string.OpenTomorrow));
            }
            if (dt.getHourOfDay() >= 7 && dt.getHourOfDay() < 16 && dayrun == 0) {
                //Time is between 7AM and 4PM. School is already in session.
                GBInfo.add(GBCount, getString(R.string.SchoolOpen));
                GBCount++;
                GBOpen = true;
            } else if (dt.getHourOfDay() >= 16 && dayrun == 0) {
                //Time is after 4PM. School is already out.
                GBInfo.add(GBCount, getString(R.string.Dismissed));
                GBCount++;
                GBOpen = true;
            }
        }
    }

    private void checkClosings() {
        /*General structure:
        ->Checks for the presence of a school name
        -->If the school appears in the list, show its status entry and set its boolean to "true"
        --->If the status entry contains "Closed Today" and the calculation is being run for 'today', 
            increase that tier's 'today' count
        --->If the status entry contains "Closed Tomorrow" and the calculation is being run for 'tomorrow',
            increase that tier's 'tomorrow' count*/

        for (int i = 1; i < orgNameLine.length; i++) {
            if (!(Atherton)) {
                if (orgNameLine[i].contains("Atherton")) {
                    closings.set(1, getString(R.string.Atherton) + statusLine[i]);
                    if (statusLine[i].contains("Closed Today") && dayrun == 0) {
                        tier4today++;
                    }else if (statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                        tier4tomorrow++;
                    }
                    Atherton = true;
                } else {
                    closings.set(1, getString(R.string.Atherton) + getString(R.string.Open));
                }
            }
            if (!(Bendle)) {
                if (orgNameLine[i].contains("Bendle")) {
                    closings.set(2, getString(R.string.Bendle) + statusLine[i]);
                    if (statusLine[i].contains("Closed Today") && dayrun == 0) {
                        tier4today++;
                    }else if (statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                        tier4tomorrow++;
                    }
                    Bendle = true;
                } else {
                    closings.set(2, getString(R.string.Bendle) + getString(R.string.Open));
                }
            }
            if (!(Bentley)) {
                if (orgNameLine[i].contains("Bentley")) {
                    closings.set(3, getString(R.string.Bentley) + statusLine[i]);
                    if (statusLine[i].contains("Closed Today") && dayrun == 0) {
                        tier4today++;
                    }else if (statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                        tier4tomorrow++;
                    }
                    Bentley = true;
                } else {
                    closings.set(3, getString(R.string.Bentley) + getString(R.string.Open));
                }
            }
            if (!(Carman)) {
                if (orgNameLine[i].contains("Carman-Ainsworth") && !orgNameLine[i].contains("Senior")) {
                    closings.set(4, getString(R.string.Carman) + statusLine[i]);
                    if (statusLine[i].contains("Closed Today") && dayrun == 0) {
                        tier4today++;
                    }else if (statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                        tier4tomorrow++;
                    }
                    Carman = true;
                } else {
                    closings.set(4, getString(R.string.Carman) + getString(R.string.Open));
                }
            }
            if (!(Flint)) {
                if (orgNameLine[i].contains("Flint Community Schools")) {
                    closings.set(5, getString(R.string.Flint) + statusLine[i]);
                    if (statusLine[i].contains("Closed Today") && dayrun == 0) {
                        tier4today++;
                    }else if (statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                        tier4tomorrow++;
                    }
                    Flint = true;
                } else {
                    closings.set(5, getString(R.string.Flint) + getString(R.string.Open));
                }
            }
            if (!(Goodrich)) {
                if (orgNameLine[i].contains("Goodrich")) {
                    closings.set(6, getString(R.string.Goodrich) + statusLine[i]);
                    if (statusLine[i].contains("Closed Today") && dayrun == 0) {
                        tier4today++;
                    }else if (statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                        tier4tomorrow++;
                    }
                    Goodrich = true;
                } else {
                    closings.set(6, getString(R.string.Goodrich) + getString(R.string.Open));
                }
            }
            if (!(Beecher)) {
                if (orgNameLine[i].contains("Beecher")) {
                    closings.set(7, getString(R.string.Beecher) + statusLine[i]);
                    if (statusLine[i].contains("Closed Today") && dayrun == 0) {
                        tier3today++;
                    }else if (statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                        tier3tomorrow++;
                    }
                    Beecher = true;
                } else {
                    closings.set(7, getString(R.string.Beecher) + getString(R.string.Open));
                }
            }
            if (!(Clio)) {
                if (orgNameLine[i].contains("Clio") && !orgNameLine[i].contains("Senior")
                        && !orgNameLine[i].contains("City") && !orgNameLine[i].contains("Cornerstone")) {
                    closings.set(8, getString(R.string.Clio) + statusLine[i]);
                    if (statusLine[i].contains("Closed Today") && dayrun == 0) {
                        tier3today++;
                    }else if (statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                        tier3tomorrow++;
                    }
                    Clio = true;
                } else {
                    closings.set(8, getString(R.string.Clio) + getString(R.string.Open));
                }
            }
            if (!(Davison)) {
                if (orgNameLine[i].contains("Davison") && !orgNameLine[i].contains("Senior")
                        && !orgNameLine[i].contains("Faith") && !orgNameLine[i].contains("Medical")
                        && !orgNameLine[i].contains("Montessori")) {
                    closings.set(9, getString(R.string.Davison) + statusLine[i]);
                    if (statusLine[i].contains("Closed Today") && dayrun == 0) {
                        tier3today++;
                    }else if (statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                        tier3tomorrow++;
                    }
                    Davison = true;
                } else {
                    closings.set(9, getString(R.string.Davison) + getString(R.string.Open));
                }
            }
            if (!(Fenton)) {
                if (orgNameLine[i].contains("Fenton") && !orgNameLine[i].contains("Lake")
                        && !orgNameLine[i].contains("City") && !orgNameLine[i].contains("Montessori")) {
                    closings.set(10, getString(R.string.Fenton) + statusLine[i]);
                    if (statusLine[i].contains("Closed Today") && dayrun == 0) {
                        tier3today++;
                    }else if (statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                        tier3tomorrow++;
                    }
                    Fenton = true;
                } else {
                    closings.set(10, getString(R.string.Fenton) + getString(R.string.Open));
                }
            }
            if (!(Flushing)) {
                if (orgNameLine[i].contains("Flushing") && !orgNameLine[i].contains("Senior")
                        && !orgNameLine[i].contains("Robert")) {
                    closings.set(11, getString(R.string.Flushing) + statusLine[i]);
                    if (statusLine[i].contains("Closed Today") && dayrun == 0) {
                        tier3today++;
                    }else if (statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                        tier3tomorrow++;
                    }
                    Flushing = true;
                } else {
                    closings.set(11, getString(R.string.Flushing) + getString(R.string.Open));
                }
            }
            if (!(Genesee)) {
                if (orgNameLine[i].contains("Genesee") && !orgNameLine[i].contains("Freedom")
                        && !orgNameLine[i].contains("Christian") && !orgNameLine[i].contains("Library")
                        && !orgNameLine[i].contains("Mobile") && !orgNameLine[i].contains("Programs")
                        && !orgNameLine[i].contains("Hlth") && !orgNameLine[i].contains("Sys")
                        && !orgNameLine[i].contains("Stem") && !orgNameLine[i].contains("I.S.D.")) {
                    closings.set(12, getString(R.string.Genesee) + statusLine[i]);
                    if (statusLine[i].contains("Closed Today") && dayrun == 0) {
                        tier3today++;
                    }else if (statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                        tier3tomorrow++;
                    }
                    Genesee = true;
                } else {
                    closings.set(12, getString(R.string.Genesee) + getString(R.string.Open));
                }
            }
            if (!(Kearsley)) {
                if (orgNameLine[i].contains("Kearsley")) {
                    closings.set(13, getString(R.string.Kearsley) + statusLine[i]);
                    if (statusLine[i].contains("Closed Today") && dayrun == 0) {
                        tier3today++;
                    }else if (statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                        tier3tomorrow++;
                    }
                    Kearsley = true;
                } else {
                    closings.set(13, getString(R.string.Kearsley) + getString(R.string.Open));
                }
            }
            if (!(LKFenton)) {
                if (orgNameLine[i].contains("Lake Fenton")) {
                    closings.set(14, getString(R.string.LKFenton) + statusLine[i]);
                    if (statusLine[i].contains("Closed Today") && dayrun == 0) {
                        tier3today++;
                    }else if (statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                        tier3tomorrow++;
                    }
                    LKFenton = true;
                } else {
                    closings.set(14, getString(R.string.LKFenton) + getString(R.string.Open));
                }
            }
            if (!(Linden)) {
                if (orgNameLine[i].contains("Linden") && !orgNameLine[i].contains("Charter")) {
                    closings.set(15, getString(R.string.Linden) + statusLine[i]);
                    if (statusLine[i].contains("Closed Today") && dayrun == 0) {
                        tier3today++;
                    }else if (statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                        tier3tomorrow++;
                    }
                    Linden = true;
                } else {
                    closings.set(15, getString(R.string.Linden) + getString(R.string.Open));
                }
            }
            if (!(Montrose)) {
                if (orgNameLine[i].contains("Montrose") && !orgNameLine[i].contains("Senior")) {
                    closings.set(16, getString(R.string.Montrose) + statusLine[i]);
                    if (statusLine[i].contains("Closed Today") && dayrun == 0) {
                        tier3today++;
                    } else if (statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                        tier3tomorrow++;
                    }
                    Montrose = true;
                } else {
                    closings.set(16, getString(R.string.Montrose) + getString(R.string.Open));
                }
            }
            if (!(Morris)) {
                if (orgNameLine[i].contains("Mt. Morris") && !orgNameLine[i].contains("Administration")
                        && !orgNameLine[i].contains("Twp") && !orgNameLine[i].contains("Mary")) {
                    closings.set(17, getString(R.string.Morris) + statusLine[i]);
                    if (statusLine[i].contains("Closed Today") && dayrun == 0) {
                        tier3today++;
                    }else if (statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                        tier3tomorrow++;
                    }
                    Morris= true;
                } else {
                    closings.set(17, getString(R.string.Morris) + getString(R.string.Open));
                }
            }
            if (!(SzCreek)) {
                if (orgNameLine[i].contains("Swartz Creek") && !orgNameLine[i].contains("Senior")
                        && !orgNameLine[i].contains("Montessori")) {
                    closings.set(18, getString(R.string.SzCreek) + statusLine[i]);
                    if (statusLine[i].contains("Closed Today") && dayrun == 0) {
                        tier3today++;
                    }else if (statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                        tier3tomorrow++;
                    }
                    SzCreek = true;
                } else {
                    closings.set(18, getString(R.string.SzCreek) + getString(R.string.Open));
                }
            }
            if (!(Durand)) {
                if (orgNameLine[i].contains("Durand") && !orgNameLine[i].contains("Senior")) {
                    closings.set(19, getString(R.string.Durand) + statusLine[i]);
                    if (statusLine[i].contains("Closed Today") && dayrun == 0) {
                        tier2today++;
                    }else if (statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                        tier2tomorrow++;
                    }
                    Durand = true;
                } else {
                    closings.set(19, getString(R.string.Durand) + getString(R.string.Open));
                }
            }
            if (!(Holly)) {
                if (orgNameLine[i].contains("Holly") && !orgNameLine[i].contains("Academy")) {
                    closings.set(20, getString(R.string.Holly) + statusLine[i]);
                    if (statusLine[i].contains("Closed Today") && dayrun == 0) {
                        tier2today++;
                    }else if (statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                        tier2tomorrow++;
                    }
                    Holly = true;
                } else {
                    closings.set(20, getString(R.string.Holly) + getString(R.string.Open));
                }
            }
            if (!(Lapeer)) {
                if (orgNameLine[i].contains("Lapeer") && !orgNameLine[i].contains("Chatfield")
                        && !orgNameLine[i].contains("Transit") && !orgNameLine[i].contains("CMH")
                        && !orgNameLine[i].contains("Tech") && !orgNameLine[i].contains("Offices")
                        && !orgNameLine[i].contains("Library") && !orgNameLine[i].contains("Senior")
                        && !orgNameLine[i].contains("Paul")) {
                    closings.set(21, getString(R.string.Lapeer) + statusLine[i]);
                    if (statusLine[i].contains("Closed Today") && dayrun == 0) {
                        tier2today++;
                    }else if (statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                        tier2tomorrow++;
                    }
                    Lapeer = true;
                } else {
                    closings.set(21, getString(R.string.Lapeer) + getString(R.string.Open));
                }
            }
            if (!(Owosso)) {
                if (orgNameLine[i].contains("Owosso") && !orgNameLine[i].contains("Senior")
                        && !orgNameLine[i].contains("Baker") && !orgNameLine[i].contains("Paul")
                        && !orgNameLine[i].contains("Security")) {
                    closings.set(22, getString(R.string.Owosso) + statusLine[i]);
                    if (statusLine[i].contains("Closed Today") && dayrun == 0) {
                        tier2today++;
                    }else if (statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                        tier2tomorrow++;
                    }
                    Owosso = true;
                } else {
                    closings.set(22, getString(R.string.Owosso) + getString(R.string.Open));
                }
            }
            if (!(GBAcademy)) {
                if (orgNameLine[i].contains("Grand Blanc Academy")) {
                    closings.set(23, getString(R.string.GBAcademy) + statusLine[i]);
                    if (statusLine[i].contains("Closed Today") && dayrun == 0) {
                        tier1today++;
                    }else if (statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                        tier1tomorrow++;
                    }
                    GBAcademy = true;
                } else {
                    closings.set(23, getString(R.string.GBAcademy) + getString(R.string.Open));
                }
            }
            if (!(GISD)) {
                if (orgNameLine[i].contains("Genesee I.S.D.")) {
                    closings.set(24, getString(R.string.GISD) + statusLine[i]);
                    if (statusLine[i].contains("Closed Today") && dayrun == 0) {
                        tier1today++;
                    }else if (statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                        tier1tomorrow++;
                    }
                    GISD = true;
                } else {
                    closings.set(24, getString(R.string.GISD) + getString(R.string.Open));
                }
            }
            if (!(HolyFamily)) {
                if (orgNameLine[i].contains("Holy Family")) {
                    closings.set(25, getString(R.string.HolyFamily) + statusLine[i]);
                    if (statusLine[i].contains("Closed Today") && dayrun == 0) {
                        tier1today++;
                    }else if (statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                        tier1tomorrow++;
                    }
                    HolyFamily = true;
                } else {
                    closings.set(25, getString(R.string.HolyFamily) + getString(R.string.Open));
                }
            }
            if (!(WPAcademy)) {
                if (orgNameLine[i].contains("Woodland Park Academy")) {
                    closings.set(26, getString(R.string.WPAcademy) + statusLine[i]);
                    if (statusLine[i].contains("Closed Today") && dayrun == 0) {
                        tier1today++;
                    }else if (statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                        tier1tomorrow++;
                    }
                    WPAcademy = true;
                } else {
                    closings.set(26, getString(R.string.WPAcademy) + getString(R.string.Open));
                }
            }
        }
    }

    private class WeatherScraper extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... nothing) {
            /**NATIONAL WEATHER SERVICE SCRAPER**/
            //Change the percentage based on current storm/wind/temperature warnings.

            Document weatherdoc;

            //Live html
            try {
                 weatherdoc = Jsoup.connect("http://alerts.weather.gov/cap/wwaatmget.php?x=MIZ061&y=0").get();
                //Saving to searchable string array weatherwarn
                  weatherwarn=weatherdoc.toString().split("<title>");
                  getWeather();
            }catch (IOException e) {
                //Connectivity issues
                nwsInfo.add(nwsCount, getString(R.string.WeatherConnectionError) + " " + getString(R.string.NoConnection));
                nwsCount++;
                NWSFail = true;

                Crashlytics.logException(e);
            } catch (NullPointerException e) {
                //Webpage layout not recognized.
                nwsInfo.add(nwsCount, getString(R.string.WeatherParseError));
                nwsCount ++;
                NWSFail = true;

                Crashlytics.logException(e);
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
        for (int i = 1; i < weatherwarn.length; i++) {
            if (weatherwarn[i].contains("Significant Weather Advisory")) {
                //Significant Weather Advisory - 15% weatherpercent
                SigWeather = true;
                weathertoday = 15;
            }
            if (weatherwarn[i].contains("Winter Weather Advisory")) {
                //Winter Weather Advisory - 30% weatherpercent
                WinterAdvisory = true;
                weathertoday = 30;
            }
            if (weatherwarn[i].contains("Lake-Effect Snow Advisory")) {
                //Lake Effect Snow Advisory - 40% weatherpercent
                LakeSnowAdvisory = true;
                weathertoday = 40;

            }
            if (weatherwarn[i].contains("Freezing Rain Advisory")) {
                //Freezing Rain - 40% weatherpercent
                Rain = true;
                weathertoday = 40;
            }
            if (weatherwarn[i].contains("Freezing Drizzle Advisory")) {
                //Freezing Drizzle - 40% weatherpercent
                Drizzle = true;
                weathertoday = 40;
            }
            if (weatherwarn[i].contains("Freezing Fog Advisory")) {
                //Freezing Fog - 40% weatherpercent
                Fog = true;
                weathertoday = 40;
            }
            if (weatherwarn[i].contains("Wind Chill Advisory")) {
                //Wind Chill Advisory - 40% weatherpercent
                WindChillAdvisory = true;
                weathertoday = 40;
            }
            if (weatherwarn[i].contains("Ice Storm Warning")) {
                //Ice Storm Warning - 70% weatherpercent
                IceStorm = true;
                weathertoday = 70;
            }
            if (weatherwarn[i].contains("Wind Chill Watch")) {
                //Wind Chill Watch - 70% weatherpercent
                WindChillWatch = true;
                weathertomorrow = 70;
            }
            if (weatherwarn[i].contains("Wind Chill Warning")) {
                //Wind Chill Warning - 70% weatherpercent
                WindChillWarn = true;
                weathertoday = 70;
            }
            if (weatherwarn[i].contains("Winter Storm Watch")) {
                //Winter Storm Watch - 80% weatherpercent
                WinterWatch = true;
                weathertomorrow = 80;
            }
            if (weatherwarn[i].contains("Winter Storm Warning")) {
                //Winter Storm Warning - 80% weatherpercent
                WinterWarn = true;
                weathertoday = 80;
            }
            if (weatherwarn[i].contains("Lake-Effect Snow Watch")) {
                //Lake Effect Snow Watch - 80% weatherpercent
                LakeSnowWatch = true;
                weathertomorrow = 80;
            }
            if (weatherwarn[i].contains("Lake-Effect Snow Warning")) {
                //Lake Effect Snow Warning - 80% weatherpercent
                LakeSnowWarn = true;
                weathertoday = 80;
            }
            if (weatherwarn[i].contains("Blizzard Watch")) {
                //Blizzard Watch - 90% weatherpercent
                BlizzardWatch = true;
                weathertomorrow = 90;
            }
            if (weatherwarn[i].contains("Blizzard Warning")) {
                //Blizzard Warning - 90% weatherpercent
                BlizzardWarn = true;
                weathertoday = 90;
            }
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
            if (tier1today > 2 && dayrun == 0
                    || tier1tomorrow > 2 && dayrun == 1) {
                //3+ academies are closed. 20% schoolpercent.
                schoolpercent = 20;
            }
            if (tier2today > 2 && dayrun == 0
                    || tier2tomorrow > 2 && dayrun == 1) {
                //3+ schools in nearby counties are closed. 40% schoolpercent.
                schoolpercent = 40;
            }
            if (tier3today > 2 && dayrun == 0
                    || tier3tomorrow > 2 && dayrun == 1) {
                //3+ schools in Genesee County are closed. 60% schoolpercent.
                schoolpercent = 60;
            }
            if (tier4today > 2 && dayrun == 0
                || tier4tomorrow > 2 && dayrun == 1) {
                //3+ schools near GB are closed. 80% schoolpercent.
                schoolpercent = 80;
                if (Carman) {
                    //Carman is closed along with three close schools. 90% schoolpercent.
                    schoolpercent = 90;
                }
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

                final Resources res = getResources();

                try {
                    for (int percentscroll = 0; percentscroll <= percent; percentscroll++) {
                        Thread.sleep(10);
                        if (percentscroll >= 0 && percentscroll <= 20) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    txtPercent.setTextColor(ContextCompat.getColor(ResultActivity.this, R.color.red));
                                }
                            });
                        } if (percentscroll > 20 && percentscroll <= 60) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    txtPercent.setTextColor(ContextCompat.getColor(ResultActivity.this, R.color.orange));
                                }
                            });
                        } if (percentscroll > 60 && percentscroll <= 80) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    txtPercent.setTextColor(ContextCompat.getColor(ResultActivity.this, R.color.green));
                                }
                            });
                        } if (percentscroll > 80) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    txtPercent.setTextColor(ContextCompat.getColor(ResultActivity.this, R.color.colorAccent));
                                }
                            });
                        }
                        final int finalPercentscroll = percentscroll;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                txtPercent.setText((finalPercentscroll) + "%");
                            }
                        });
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

            //Set the content of the information ListView
            if (WJRTFail || NWSFail) {
                //Network communication issues
                GBInfo.add(GBCount, getString(R.string.NoNetwork));
                GBCount++;
            }


            GBAdapter gbAdapter = new GBAdapter(ResultActivity.this);
            for (int i = 0; i < GBInfo.size(); i++) {
                gbAdapter.addItem(GBInfo.get(i));
            }

            lstGB.setAdapter(gbAdapter);

            //Set up the ListView adapter that displays school closings

            if (!WJRTFail) {
                //WJRT has not failed.
                ClosingsAdapter closingsAdapter = new ClosingsAdapter(ResultActivity.this);
                closingsAdapter.addSeparatorItem(getString(R.string.WJRTClosings));
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
                ArrayAdapter<String> WJRTadapter = new ArrayAdapter<>(getApplicationContext(),
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

                WeatherAdapter weatherAdapter = new WeatherAdapter(ResultActivity.this, weather);
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
                ArrayAdapter<String> NWSadapter = new ArrayAdapter<>(getApplicationContext(),
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







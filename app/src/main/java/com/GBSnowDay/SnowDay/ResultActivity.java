package com.GBSnowDay.SnowDay;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;


public class ResultActivity extends AppCompatActivity {

    /*Copyright 2014-2015 Corey Rowe

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

    ViewPager viewPager;
    TabLayout tabLayout;

    String schooltext;


    List<String> orgName = new ArrayList<>();
    List<String> status = new ArrayList<>();

    //Declare lists that will be used in ListAdapters
    List<String> GBInfo = new ArrayList<>();
    List<String> closings = new ArrayList<>();
    List<String> wjrtInfo = new ArrayList<>();
    List<String> nwsInfo = new ArrayList<>();

    List<String> weatherWarn = new ArrayList<>();
    List<String> weatherSummary = new ArrayList<>();
    List<String> weatherExpire = new ArrayList<>();
    List<String> weatherLink = new ArrayList<>();

    DateTime dt = new DateTime();

    int days;
    int dayrun;

    String datetoday;
    String datetomorrow;

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
    boolean GBAcademy;
    boolean GISD;
    boolean HolyFamily;
    boolean WPAcademy;

    boolean Durand; //Check for "Durand Senior Center"
    boolean Holly;  //Check for "Holly Academy"
    boolean Lapeer; //Check for "Lapeer County CMH", "Lapeer Vocational Tech.", "Lapeer Team Work",
    // "Lapeer Senior Center", "Lapeer Co. Education Technology Center", "Lapeer Co. Intermed. Special Ed",
    // "Lapeer Growth and Opportunity, Inc.", "Lapeer District Library", "Lapeer County Offices", "NEMSCA-Lapeer Head Start",
    // "Greater Lapeer Transportation Authority", "Foster Grandparents-Lapeer, Genesee, Shiawassee", "Davenport University-Lapeer",
    // "MSU Extension Service-Lapeer Co.", "Community Connections-Lapeer", and "Chatfield School-Lapeer"
    boolean Owosso; //Check for "Owosso Christian School", "Owosso Senior Center",
    // "Owosso Seventh-day Adventist School", and "Social Security Administration-Owosso"

    boolean Beecher;
    boolean Clio; //Check for "Clio Area Christian School", Clio Area Senior Center",
    // "Clio City Hall", and "Cornerstone Clio"
    boolean Davison; //Check for "Davison Senior Center", "Faith Baptist School-Davison", "Montessori Academy-Davison",
    // and "Ross Medical Education-Davison"
    boolean Fenton; //Check for "Lake Fenton", "Fenton City Hall", "Fenton Academy of Cosmetology",
    // and "Fenton Montessori Academy"
    boolean Flushing; //Check for "Flushing Senior Citizens Center" and "St. Robert-Flushing"
    boolean Genesee; //Check for "Genesee I.S.D.", "Genesee Health System Day Programs", "Genesee Health System",
    // "Genesee Health Plan", "Genesee Academy", "Genesee Area Skill Center", "Genesee Christian School",
    // "Genesee County Free Medical Clinic", "Genesee District Library", "Genesee County Mobile Meal Program",
    // "Genesee STEM Academy", "Genesee Co Circuit Court", "Genesee County Government", "Genesee County Literacy Coalition",
    // "Flint Genesee Job Corps", "Leadership Genesee", "Freedom Work Genesee Co.", "Youth Leadership Genesee",
    // "67th District Court-Genesee Co.", "MSU Extension Service-Genesee Co.",
    // "Genesee Christian-Burton", and "Foster Grandparents-Lapeer, Genesee, Shiawassee"
    boolean Kearsley;
    boolean LKFenton;
    boolean Linden; //Check for "Linden Charter Academy"
    boolean Montrose; //Check for "Montrose Senior Center"
    boolean Morris;  //Check for "Mt Morris Twp Administration" and "St. Mary Church Religious Ed-Mt. Morris"
    boolean SzCreek; //Check for "Swartz Creek Area Senior Center" and "Swartz Creek Montessori"

    boolean Atherton;
    boolean Bendle;
    boolean Bentley;
    boolean Carman; //Check for "Carman-Ainsworth Senior Center"
    boolean Flint; //Thankfully this is listed as "Flint Community Schools" -
    // otherwise there would be a lot of exceptions to check for.
    boolean Goodrich;

    boolean GB; //Check for "Grand Blanc Senior Center", "Grand Blanc Academy", "Grand Blanc Road Montessori",
    // "Grand Blanc Gymnastics Co.", and "Freedom Work Grand Blanc"

    boolean GBOpen; //True if GB is already open (GB = false and time is during or after school hours)

    boolean GBMessage; //Grand Blanc has a message (e.g. "Early Dismissal") but isn't actually closed.

    //Don't try to show weather warning information if no warnings are present
    boolean WeatherWarningsPresent;

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

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
            viewPager.setPagingEnabled(false);
        }

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (viewPager != null) {
            tabLayout.setVisibility(View.INVISIBLE);
            tabLayout.setupWithViewPager(viewPager);

            //Make sure tab content stays in memory
            viewPager.setOffscreenPageLimit(3);
        }

        //Read variables from MainActivity class
        Intent result = getIntent();
        dayrun = result.getIntExtra("dayrun", dayrun);
        days = result.getIntExtra("days", days);
        datetoday = result.getStringExtra("datetoday");
        datetomorrow = result.getStringExtra("datetomorrow");

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


        //Add the first GBInfo value so it can be set out of sequence
        GBInfo.add(0, "");

        //Start the calculation
        Calculate();

    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new PercentFragment(), getString(R.string.tab1));
        adapter.addFragment(new ClosingsFragment(), getString(R.string.tab2));
        adapter.addFragment(new WeatherFragment(), getString(R.string.tab3));
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
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
        if (id == android.R.id.home) {
            //Return to the previous activity
            finish();
        }
        return super.onOptionsItemSelected(item);
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
            Document schools = null;
            //Scrape School Closings from WJRT with Jsoup.

            /**WJRT SCHOOL CLOSINGS SCRAPER**/
            //Scrape School Closings from WJRT with Jsoup.
            //Run scraper in an Async task.

            try {
                //This is the current listings page.
                schools = Jsoup.connect("http://abc12.com/closings").get();
                //Attempt to parse input

                Element table = schools.select("table").last();
                Elements rows = table.select("tr");

                for (int i = 1; i < rows.size(); i++) { //Skip header row
                    Element row = rows.get(i);
                    orgName.add(row.select("td").get(0).text());
                    status.add(row.select("td").get(1).text());
                }

            } catch (IOException e) {

                //Connectivity issues
                wjrtInfo.add(getString(R.string.WJRTConnectionError) + " " + getString(R.string.NoConnection));
                WJRTFail = true;

                Crashlytics.logException(e);

            } catch (NullPointerException | IndexOutOfBoundsException e) {

                if (schools != null) {
                    schooltext = schools.text();
                }else{
                    schooltext = "";
                }

                //This shows in place of the table (as plain text) if no schools or institutions are closed.
                if (schooltext.contains("no closings or delays")) {
                    //No schools are closed.
                    WJRTFail = false;

                    //Add a blank entry so checkClosings() still works correctly
                    orgName.add(" ");

                } else {
                    //Webpage layout was not recognized.
                    wjrtInfo.add(getString(R.string.WJRTParseError));
                    WJRTFail = true;

                    Crashlytics.logException(e);
                }
            }

            //Only run if WJRTFail is false to avoid NullPointerExceptions
            if (!WJRTFail) {
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
        //Checking if Grand Blanc is closed.
        for (int i = 0; i < orgName.size(); i++) {
            //If Grand Blanc hasn't been found...
            if (!GB) {
                if (orgName.get(i).contains("Grand Blanc") && !orgName.get(i).contains("Academy")
                        && !orgName.get(i).contains("Freedom") && !orgName.get(i).contains("Offices")
                        && !orgName.get(i).contains("City") && !orgName.get(i).contains("Senior")
                        && !orgName.get(i).contains("Holy") && !orgName.get(i).contains("Montessori")
                        && !orgName.get(i).contains("Gym")) {
                    GBInfo.set(0, getString(R.string.GB) + status.get(i));
                    if (status.get(i).contains("Closed Today") && dayrun == 0
                            || status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
                        GBInfo.add(getString(R.string.SnowDay));
                        //GB is closed.
                        GB = true;
                    }else{
                        //GB at least has a message.
                        GBMessage = true;
                    }
                    break;
                }
            }
        }

        if (!GB) {
            //Grand Blanc is open.
            if (!GBMessage) {
                GBInfo.set(0, getString(R.string.GB) + getString(R.string.NotClosed));
            }

            if (dayrun == 0) {
                if (dt.getHourOfDay() >= 7 && dt.getHourOfDay() < 16) {
                    //Time is between 7AM and 4PM. School is already in session.
                    GBInfo.add(getString(R.string.SchoolOpen));
                    GBOpen = true;
                } else if (dt.getHourOfDay() >= 16) {
                    //Time is after 4PM. School is already out.
                    GBInfo.add(getString(R.string.Dismissed));
                    GBOpen = true;
                }
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

        for (int i = 0; i < orgName.size(); i++) {
            if (!(Atherton)) {
                if (orgName.get(i).contains("Atherton")) {
                    closings.set(1, getString(R.string.Atherton) + status.get(i));
                    if (status.get(i).contains("Closed Today") && dayrun == 0) {
                        tier4today++;
                    }else if (status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
                        tier4tomorrow++;
                    }
                    Atherton = true;
                } else {
                    closings.set(1, getString(R.string.Atherton) + getString(R.string.Open));
                }
            }
            if (!(Bendle)) {
                if (orgName.get(i).contains("Bendle")) {
                    closings.set(2, getString(R.string.Bendle) + status.get(i));
                    if (status.get(i).contains("Closed Today") && dayrun == 0) {
                        tier4today++;
                    }else if (status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
                        tier4tomorrow++;
                    }
                    Bendle = true;
                } else {
                    closings.set(2, getString(R.string.Bendle) + getString(R.string.Open));
                }
            }
            if (!(Bentley)) {
                if (orgName.get(i).contains("Bentley")) {
                    closings.set(3, getString(R.string.Bentley) + status.get(i));
                    if (status.get(i).contains("Closed Today") && dayrun == 0) {
                        tier4today++;
                    }else if (status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
                        tier4tomorrow++;
                    }
                    Bentley = true;
                } else {
                    closings.set(3, getString(R.string.Bentley) + getString(R.string.Open));
                }
            }
            if (!(Carman)) {
                if (orgName.get(i).contains("Carman-Ainsworth") && !orgName.get(i).contains("Senior")) {
                    closings.set(4, getString(R.string.Carman) + status.get(i));
                    if (status.get(i).contains("Closed Today") && dayrun == 0) {
                        tier4today++;
                    }else if (status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
                        tier4tomorrow++;
                    }
                    Carman = true;
                } else {
                    closings.set(4, getString(R.string.Carman) + getString(R.string.Open));
                }
            }
            if (!(Flint)) {
                if (orgName.get(i).contains("Flint Community Schools")) {
                    closings.set(5, getString(R.string.Flint) + status.get(i));
                    if (status.get(i).contains("Closed Today") && dayrun == 0) {
                        tier4today++;
                    }else if (status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
                        tier4tomorrow++;
                    }
                    Flint = true;
                } else {
                    closings.set(5, getString(R.string.Flint) + getString(R.string.Open));
                }
            }
            if (!(Goodrich)) {
                if (orgName.get(i).contains("Goodrich")) {
                    closings.set(6, getString(R.string.Goodrich) + status.get(i));
                    if (status.get(i).contains("Closed Today") && dayrun == 0) {
                        tier4today++;
                    }else if (status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
                        tier4tomorrow++;
                    }
                    Goodrich = true;
                } else {
                    closings.set(6, getString(R.string.Goodrich) + getString(R.string.Open));
                }
            }
            if (!(Beecher)) {
                if (orgName.get(i).contains("Beecher")) {
                    closings.set(7, getString(R.string.Beecher) + status.get(i));
                    if (status.get(i).contains("Closed Today") && dayrun == 0) {
                        tier3today++;
                    }else if (status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
                        tier3tomorrow++;
                    }
                    Beecher = true;
                } else {
                    closings.set(7, getString(R.string.Beecher) + getString(R.string.Open));
                }
            }
            if (!(Clio)) {
                if (orgName.get(i).contains("Clio") && !orgName.get(i).contains("Christian")
                        && !orgName.get(i).contains("Senior") && !orgName.get(i).contains("City")
                        && !orgName.get(i).contains("Cornerstone")) {
                    closings.set(8, getString(R.string.Clio) + status.get(i));
                    if (status.get(i).contains("Closed Today") && dayrun == 0) {
                        tier3today++;
                    }else if (status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
                        tier3tomorrow++;
                    }
                    Clio = true;
                } else {
                    closings.set(8, getString(R.string.Clio) + getString(R.string.Open));
                }
            }
            if (!(Davison)) {
                if (orgName.get(i).contains("Davison") && !orgName.get(i).contains("Senior")
                        && !orgName.get(i).contains("Faith") && !orgName.get(i).contains("Medical")
                        && !orgName.get(i).contains("Montessori")) {
                    closings.set(9, getString(R.string.Davison) + status.get(i));
                    if (status.get(i).contains("Closed Today") && dayrun == 0) {
                        tier3today++;
                    }else if (status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
                        tier3tomorrow++;
                    }
                    Davison = true;
                } else {
                    closings.set(9, getString(R.string.Davison) + getString(R.string.Open));
                }
            }
            if (!(Fenton)) {
                if (orgName.get(i).contains("Fenton") && !orgName.get(i).contains("Lake")
                        && !orgName.get(i).contains("City") && !orgName.get(i).contains("Academy")
                        && !orgName.get(i).contains("Montessori")) {
                    closings.set(10, getString(R.string.Fenton) + status.get(i));
                    if (status.get(i).contains("Closed Today") && dayrun == 0) {
                        tier3today++;
                    }else if (status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
                        tier3tomorrow++;
                    }
                    Fenton = true;
                } else {
                    closings.set(10, getString(R.string.Fenton) + getString(R.string.Open));
                }
            }
            if (!(Flushing)) {
                if (orgName.get(i).contains("Flushing") && !orgName.get(i).contains("Senior")
                        && !orgName.get(i).contains("Robert")) {
                    closings.set(11, getString(R.string.Flushing) + status.get(i));
                    if (status.get(i).contains("Closed Today") && dayrun == 0) {
                        tier3today++;
                    }else if (status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
                        tier3tomorrow++;
                    }
                    Flushing = true;
                } else {
                    closings.set(11, getString(R.string.Flushing) + getString(R.string.Open));
                }
            }
            if (!(Genesee)) {
                if (orgName.get(i).contains("Genesee") && !orgName.get(i).contains("Freedom")
                        && !orgName.get(i).contains("Christian") && !orgName.get(i).contains("Library")
                        && !orgName.get(i).contains("Mobile") && !orgName.get(i).contains("Programs")
                        && !orgName.get(i).contains("Health") && !orgName.get(i).contains("Medical")
                        && !orgName.get(i).contains("Academy") && !orgName.get(i).contains("Skill")
                        && !orgName.get(i).contains("Sys") && !orgName.get(i).contains("STEM")
                        && !orgName.get(i).contains("Court") && !orgName.get(i).contains("County")
                        && !orgName.get(i).contains("Job") && !orgName.get(i).contains("Leadership")
                        && !orgName.get(i).contains("Freedom") && !orgName.get(i).contains("MSU")
                        && !orgName.get(i).contains("I.S.D.") && !orgName.get(i).contains("Foster")) {
                    closings.set(12, getString(R.string.Genesee) + status.get(i));
                    if (status.get(i).contains("Closed Today") && dayrun == 0) {
                        tier3today++;
                    }else if (status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
                        tier3tomorrow++;
                    }
                    Genesee = true;
                } else {
                    closings.set(12, getString(R.string.Genesee) + getString(R.string.Open));
                }
            }
            if (!(Kearsley)) {
                if (orgName.get(i).contains("Kearsley")) {
                    closings.set(13, getString(R.string.Kearsley) + status.get(i));
                    if (status.get(i).contains("Closed Today") && dayrun == 0) {
                        tier3today++;
                    }else if (status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
                        tier3tomorrow++;
                    }
                    Kearsley = true;
                } else {
                    closings.set(13, getString(R.string.Kearsley) + getString(R.string.Open));
                }
            }
            if (!(LKFenton)) {
                if (orgName.get(i).contains("Lake Fenton")) {
                    closings.set(14, getString(R.string.LKFenton) + status.get(i));
                    if (status.get(i).contains("Closed Today") && dayrun == 0) {
                        tier3today++;
                    }else if (status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
                        tier3tomorrow++;
                    }
                    LKFenton = true;
                } else {
                    closings.set(14, getString(R.string.LKFenton) + getString(R.string.Open));
                }
            }
            if (!(Linden)) {
                if (orgName.get(i).contains("Linden") && !orgName.get(i).contains("Charter")) {
                    closings.set(15, getString(R.string.Linden) + status.get(i));
                    if (status.get(i).contains("Closed Today") && dayrun == 0) {
                        tier3today++;
                    }else if (status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
                        tier3tomorrow++;
                    }
                    Linden = true;
                } else {
                    closings.set(15, getString(R.string.Linden) + getString(R.string.Open));
                }
            }
            if (!(Montrose)) {
                if (orgName.get(i).contains("Montrose") && !orgName.get(i).contains("Senior")) {
                    closings.set(16, getString(R.string.Montrose) + status.get(i));
                    if (status.get(i).contains("Closed Today") && dayrun == 0) {
                        tier3today++;
                    } else if (status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
                        tier3tomorrow++;
                    }
                    Montrose = true;
                } else {
                    closings.set(16, getString(R.string.Montrose) + getString(R.string.Open));
                }
            }
            if (!(Morris)) {
                if (orgName.get(i).contains("Mt. Morris") && !orgName.get(i).contains("Administration")
                        && !orgName.get(i).contains("Twp") && !orgName.get(i).contains("Mary")) {
                    closings.set(17, getString(R.string.Morris) + status.get(i));
                    if (status.get(i).contains("Closed Today") && dayrun == 0) {
                        tier3today++;
                    }else if (status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
                        tier3tomorrow++;
                    }
                    Morris = true;
                } else {
                    closings.set(17, getString(R.string.Morris) + getString(R.string.Open));
                }
            }
            if (!(SzCreek)) {
                if (orgName.get(i).contains("Swartz Creek") && !orgName.get(i).contains("Senior")
                        && !orgName.get(i).contains("Montessori")) {
                    closings.set(18, getString(R.string.SzCreek) + status.get(i));
                    if (status.get(i).contains("Closed Today") && dayrun == 0) {
                        tier3today++;
                    }else if (status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
                        tier3tomorrow++;
                    }
                    SzCreek = true;
                } else {
                    closings.set(18, getString(R.string.SzCreek) + getString(R.string.Open));
                }
            }
            if (!(Durand)) {
                if (orgName.get(i).contains("Durand") && !orgName.get(i).contains("Senior")) {
                    closings.set(19, getString(R.string.Durand) + status.get(i));
                    if (status.get(i).contains("Closed Today") && dayrun == 0) {
                        tier2today++;
                    }else if (status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
                        tier2tomorrow++;
                    }
                    Durand = true;
                } else {
                    closings.set(19, getString(R.string.Durand) + getString(R.string.Open));
                }
            }
            if (!(Holly)) {
                if (orgName.get(i).contains("Holly") && !orgName.get(i).contains("Academy")) {
                    closings.set(20, getString(R.string.Holly) + status.get(i));
                    if (status.get(i).contains("Closed Today") && dayrun == 0) {
                        tier2today++;
                    }else if (status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
                        tier2tomorrow++;
                    }
                    Holly = true;
                } else {
                    closings.set(20, getString(R.string.Holly) + getString(R.string.Open));
                }
            }
            if (!(Lapeer)) {
                if (orgName.get(i).contains("Lapeer") && !orgName.get(i).contains("Chatfield")
                        && !orgName.get(i).contains("Greater") && !orgName.get(i).contains("CMH")
                        && !orgName.get(i).contains("Tech") && !orgName.get(i).contains("Team")
                        && !orgName.get(i).contains("Center") && !orgName.get(i).contains("Special")
                        && !orgName.get(i).contains("Growth") && !orgName.get(i).contains("Offices")
                        && !orgName.get(i).contains("Library") && !orgName.get(i).contains("Head")
                        && !orgName.get(i).contains("Senior") && !orgName.get(i).contains("Foster")
                        && !orgName.get(i).contains("Davenport") && !orgName.get(i).contains("MSU")
                        && !orgName.get(i).contains("Paul") && !orgName.get(i).contains("Connections")) {
                    closings.set(21, getString(R.string.Lapeer) + status.get(i));
                    if (status.get(i).contains("Closed Today") && dayrun == 0) {
                        tier2today++;
                    }else if (status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
                        tier2tomorrow++;
                    }
                    Lapeer = true;
                } else {
                    closings.set(21, getString(R.string.Lapeer) + getString(R.string.Open));
                }
            }
            if (!(Owosso)) {
                if (orgName.get(i).contains("Owosso") && !orgName.get(i).contains("Christian")
                        && !orgName.get(i).contains("Senior") && !orgName.get(i).contains("Adventist")
                        && !orgName.get(i).contains("Baker") && !orgName.get(i).contains("Paul")
                        && !orgName.get(i).contains("Security")) {
                    closings.set(22, getString(R.string.Owosso) + status.get(i));
                    if (status.get(i).contains("Closed Today") && dayrun == 0) {
                        tier2today++;
                    }else if (status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
                        tier2tomorrow++;
                    }
                    Owosso = true;
                } else {
                    closings.set(22, getString(R.string.Owosso) + getString(R.string.Open));
                }
            }
            if (!(GBAcademy)) {
                if (orgName.get(i).contains("Grand Blanc Academy")) {
                    closings.set(23, getString(R.string.GBAcademy) + status.get(i));
                    if (status.get(i).contains("Closed Today") && dayrun == 0) {
                        tier1today++;
                    }else if (status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
                        tier1tomorrow++;
                    }
                    GBAcademy = true;
                } else {
                    closings.set(23, getString(R.string.GBAcademy) + getString(R.string.Open));
                }
            }
            if (!(GISD)) {
                if (orgName.get(i).contains("Genesee I.S.D.")) {
                    closings.set(24, getString(R.string.GISD) + status.get(i));
                    if (status.get(i).contains("Closed Today") && dayrun == 0) {
                        tier1today++;
                    }else if (status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
                        tier1tomorrow++;
                    }
                    GISD = true;
                } else {
                    closings.set(24, getString(R.string.GISD) + getString(R.string.Open));
                }
            }
            if (!(HolyFamily)) {
                if (orgName.get(i).contains("Holy Family")) {
                    closings.set(25, getString(R.string.HolyFamily) + status.get(i));
                    if (status.get(i).contains("Closed Today") && dayrun == 0) {
                        tier1today++;
                    }else if (status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
                        tier1tomorrow++;
                    }
                    HolyFamily = true;
                } else {
                    closings.set(25, getString(R.string.HolyFamily) + getString(R.string.Open));
                }
            }
            if (!(WPAcademy)) {
                if (orgName.get(i).contains("Woodland Park Academy")) {
                    closings.set(26, getString(R.string.WPAcademy) + status.get(i));
                    if (status.get(i).contains("Closed Today") && dayrun == 0) {
                        tier1today++;
                    }else if (status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
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

                Elements title = weatherdoc.select("title");
                Elements summary = weatherdoc.select("summary");
                Elements expiretime = weatherdoc.select("cap|expires");
                Elements link = weatherdoc.select("link");


                if (title != null) {
                    for (int i = 0; i < title.size(); i++) {
                        weatherWarn.add(title.get(i).text().replace(" by NWS", ""));
                    }

                    if (!weatherWarn.get(1).contains("no active")) {
                        //Weather warnings are present.
                        WeatherWarningsPresent = true;
                    }
                }
                if (expiretime != null) {
                    for (int i = 0; i < expiretime.size(); i++) {
                        weatherExpire.add(expiretime.get(i).text());
                    }
                }

                if (summary != null) {
                    for (int i = 0; i < summary.size(); i++) {
                        weatherSummary.add(summary.get(i).text() + "...");
                    }
                }

                if (weatherLink != null) {
                    for (int i = 0; i < link.size(); i++) {
                        weatherLink.add(link.get(i).attr("href"));
                    }
                }

                getWeather();

            }catch (IOException e) {
                //Connectivity issues
                nwsInfo.add(getString(R.string.WeatherConnectionError) + " " + getString(R.string.NoConnection));
                NWSFail = true;

                Crashlytics.logException(e);
            } catch (NullPointerException e) {
                //Webpage layout not recognized.
                nwsInfo.add(getString(R.string.WeatherParseError));
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
        Calculation is affected based on when warning expires.*/
        for (int i = 0; i < weatherWarn.size(); i++) {
            if (weatherWarn.get(i).contains("Significant Weather Advisory")) {
                //Significant Weather Advisory - 15% weatherpercent
                checkWarningTime(i, 15);
            }
            if (weatherWarn.get(i).contains("Winter Weather Advisory")) {
                //Winter Weather Advisory - 30% weatherpercent
                checkWarningTime(i, 30);
            }
            if (weatherWarn.get(i).contains("Lake-Effect Snow Advisory")) {
                //Lake Effect Snow Advisory - 40% weatherpercent
                checkWarningTime(i, 40);
            }
            if (weatherWarn.get(i).contains("Freezing Rain Advisory")) {
                //Freezing Rain - 40% weatherpercent
                checkWarningTime(i, 40);
            }
            if (weatherWarn.get(i).contains("Freezing Drizzle Advisory")) {
                //Freezing Drizzle - 40% weatherpercent
                checkWarningTime(i, 40);
            }
            if (weatherWarn.get(i).contains("Freezing Fog Advisory")) {
                //Freezing Fog - 40% weatherpercent
                checkWarningTime(i, 40);
            }
            if (weatherWarn.get(i).contains("Wind Chill Advisory")) {
                //Wind Chill Advisory - 40% weatherpercent
                checkWarningTime(i, 40);
            }
            if (weatherWarn.get(i).contains("Ice Storm Warning")) {
                //Ice Storm Warning - 70% weatherpercent
                checkWarningTime(i, 70);
            }
            if (weatherWarn.get(i).contains("Wind Chill Watch")) {
                //Wind Chill Watch - 70% weatherpercent
                checkWarningTime(i, 70);
            }
            if (weatherWarn.get(i).contains("Wind Chill Warning")) {
                //Wind Chill Warning - 70% weatherpercent
                checkWarningTime(i, 70);
            }
            if (weatherWarn.get(i).contains("Winter Storm Watch")) {
                //Winter Storm Watch - 80% weatherpercent
                checkWarningTime(i, 80);
            }
            if (weatherWarn.get(i).contains("Winter Storm Warning")) {
                //Winter Storm Warning - 80% weatherpercent
                checkWarningTime(i, 80);
            }
            if (weatherWarn.get(i).contains("Lake-Effect Snow Watch")) {
                //Lake Effect Snow Watch - 80% weatherpercent
                checkWarningTime(i, 80);
            }
            if (weatherWarn.get(i).contains("Lake-Effect Snow Warning")) {
                //Lake Effect Snow Warning - 80% weatherpercent
                checkWarningTime(i, 80);
            }
            if (weatherWarn.get(i).contains("Blizzard Watch")) {
                //Blizzard Watch - 90% weatherpercent
                checkWarningTime(i, 90);
            }
            if (weatherWarn.get(i).contains("Blizzard Warning")) {
                //Blizzard Warning - 90% weatherpercent
                checkWarningTime(i, 90);
            }
        }
    }

    private void checkWarningTime(int i, int w) {
        if (weatherExpire.get(i - 1).substring(0, 10).equals(datetoday)) {
            weathertoday = w;
        }

        if (weatherExpire.get(i - 1).substring(0, 10).equals(datetomorrow)) {
            weathertoday = w;
            weathertomorrow = w;
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
            }else if (GBOpen){
                //GB is open and the time is during or after school hours. 0% chance.
                percent = 0;
            }

            runOnUiThread(new Runnable() {
                public void run() {
                    PercentFragment.txtPercent.setText("0%");
                }
            });

            //Animate txtPercent
            if (WJRTFail && NWSFail) {
                //Both scrapers failed. A percentage cannot be determined.
                //Don't set the percent.
                GBInfo.add(getString(R.string.CalculateError));
                runOnUiThread(new Runnable() {
                    public void run() {
                        PercentFragment.txtPercent.setText("--");
                        crossFadePercent();
                        enableTabs();
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        crossFadePercent();
                        countUp(PercentFragment.txtPercent, 0);
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //Set the content of the information ListView
            if (WJRTFail || NWSFail) {
                //Network communication issues
                GBInfo.add(getString(R.string.NoNetwork));
            }


            GBAdapter gbAdapter = new GBAdapter(ResultActivity.this, GB, GBMessage);
            for (int i = 0; i < GBInfo.size(); i++) {
                if (!GBInfo.get(i).isEmpty()) {
                    gbAdapter.addItem(GBInfo.get(i));
                }
            }

            PercentFragment.lstGB.setAdapter(gbAdapter);

            //Set up the ListView adapter that displays school closings

            if (!WJRTFail) {
                //WJRT has not failed.
                ClosingsAdapter closingsAdapter = new ClosingsAdapter(ResultActivity.this);
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

                ClosingsFragment.lstClosings.setAdapter(closingsAdapter);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ClosingsFragment.lstClosings.setVisibility(View.VISIBLE);
                    }
                });
            } else {
                //WJRT has failed.
                ArrayAdapter<String> WJRTadapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, wjrtInfo);
                ClosingsFragment.lstWJRT.setAdapter(WJRTadapter);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ClosingsFragment.lstWJRT.setVisibility(View.VISIBLE);
                    }
                });

            }

            //Set up the ListView adapter that displays weather warnings
            if (!NWSFail) {
                //NWS has not failed.

                //Remove blank entries
                for (int i = 0; i < weatherWarn.size(); i++) {
                    if (weatherWarn.get(i).equals("")) {
                        weatherWarn.remove(i);
                    }
                }

                WeatherAdapter weatherAdapter = new WeatherAdapter(ResultActivity.this, weatherWarn);
                weatherAdapter.addSeparatorItem(weatherWarn.get(0));
                for (int i = 1; i < weatherWarn.size(); i++) {
                    weatherAdapter.addItem(weatherWarn.get(i));
                }

                WeatherFragment.lstWeather.setAdapter(weatherAdapter);

                WeatherFragment.lstWeather.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Don't show a message for the list separator
                                if (position > 0 && WeatherWarningsPresent) {
                                    try {
                                        new WeatherDialog(ResultActivity.this, weatherWarn.get(position), weatherSummary.get(position - 1), weatherLink.get(position)).show();
                                    }catch (NullPointerException | IndexOutOfBoundsException e) {
                                        Toast.makeText(ResultActivity.this, getString(R.string.WarningParseError), Toast.LENGTH_SHORT).show();
                                        Crashlytics.logException(e);
                                    }
                                }
                            }
                        });

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                WeatherFragment.lstWeather.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                });
            } else {
                //NWS has failed.
                ArrayAdapter<String> NWSadapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, nwsInfo);
                WeatherFragment.lstNWS.setAdapter(NWSadapter);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        WeatherFragment.lstNWS.setVisibility(View.VISIBLE);
                    }
                });
            }
        }
    }

    private void crossFadePercent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            PercentFragment.txtPercent.setAlpha(0f);
            PercentFragment.txtPercent.animate()
                    .alpha(1f)
                    .setDuration(250);

            PercentFragment.progCalculate.animate()
                    .alpha(0f)
                    .setDuration(250)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            PercentFragment.progCalculate.clearAnimation();
                            PercentFragment.progCalculate.setVisibility(View.GONE);
                        }
                    });
        }else{
            Animation fade_out = AnimationUtils.loadAnimation(ResultActivity.this, android.R.anim.fade_out);
            fade_out.setFillAfter(true);
            fade_out.setDuration(250);
            fade_out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    PercentFragment.progCalculate.clearAnimation();
                    PercentFragment.progCalculate.setVisibility(View.GONE);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            PercentFragment.progCalculate.startAnimation(fade_out);

            Animation fade_in = AnimationUtils.loadAnimation(ResultActivity.this, android.R.anim.fade_in);
            fade_in.setDuration(250);
            fade_in.setFillAfter(true);
            PercentFragment.txtPercent.startAnimation(fade_in);
        }
    }

    @SuppressLint("SetTextI18n")
    private void countUp(final TextView tv, final int count) {
        if (count > percent) {
            if (percent > 0) {
                PercentFragment.txtPercent.startAnimation(AnimationUtils.loadAnimation(ResultActivity.this, R.anim.overshoot));
            }
            enableTabs();
            return;
        }
        tv.setText(String.valueOf(count) + "%");

        if (count >= 0 && count <= 20) {
            runOnUiThread(new Runnable() {
                public void run() {
                    PercentFragment.txtPercent.setTextColor(ContextCompat.getColor(ResultActivity.this, R.color.red));
                }
            });
        } if (count > 20 && count <= 60) {
            runOnUiThread(new Runnable() {
                public void run() {
                    PercentFragment.txtPercent.setTextColor(ContextCompat.getColor(ResultActivity.this, R.color.orange));
                }
            });
        } if (count > 60 && count <= 80) {
            runOnUiThread(new Runnable() {
                public void run() {
                    PercentFragment.txtPercent.setTextColor(ContextCompat.getColor(ResultActivity.this, R.color.green));
                }
            });
        } if (count > 80) {
            runOnUiThread(new Runnable() {
                public void run() {
                    PercentFragment.txtPercent.setTextColor(ContextCompat.getColor(ResultActivity.this, R.color.colorAccent));
                }
            });
        }

        Animation alpha = AnimationUtils.loadAnimation(ResultActivity.this, R.anim.alpha);
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation anim) {
                countUp(tv, count + 1);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        tv.startAnimation(alpha);

    }

    private void enableTabs() {
        //Enable the tabs
        tabLayout.startAnimation(AnimationUtils.loadAnimation(ResultActivity.this, R.anim.slide_in));
        tabLayout.setVisibility(View.VISIBLE);
        viewPager.setPagingEnabled(true);

        PercentFragment.lstGB.startAnimation(AnimationUtils.loadAnimation(ResultActivity.this, R.anim.slide_in));
        PercentFragment.lstGB.setVisibility(View.VISIBLE);
    }


    private class ClosingsAdapter extends BaseAdapter {

        private static final int TYPE_ITEM = 0;
        private static final int TYPE_SEPARATOR = 1;
        private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

        private ArrayList<String> mData = new ArrayList<>();
        private LayoutInflater mInflater;

        private TreeSet<Integer> mSeparatorsSet = new TreeSet<>();

        public ClosingsAdapter(Context context) {
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            ViewHolder holder;
            int type = getItemViewType(position);
            holder = new ViewHolder();
            /*No 'if (convertView == null)' statement to prevent view recycling
            (views must remain fixed)*/
            switch (type) {
                case TYPE_ITEM:
                    if (Atherton && position == 1 || Bendle && position == 2
                            || Bentley && position == 3 || Carman && position == 4
                            || Flint && position == 5 || Goodrich && position == 6
                            || Beecher && position == 8 || Clio && position == 9
                            || Davison && position == 10 || Fenton && position == 11
                            || Flushing && position == 12 || Genesee && position == 13
                            || Kearsley && position == 14 || LKFenton && position == 15
                            || Linden && position == 16 || Montrose && position == 17
                            || Morris && position == 18 || SzCreek && position == 19
                            || Durand && position == 21 || Holly && position == 22
                            || Lapeer && position == 23 || Owosso && position == 24
                            || GBAcademy && position == 26 || GISD && position == 27
                            || HolyFamily && position == 28 || WPAcademy && position == 29) {
                        //If the school is closed, make it orange.
                        convertView = mInflater.inflate(R.layout.item_orange, parent, false);
                        holder.textView = (TextView)convertView.findViewById(R.id.text);
                        break;
                    }else{
                        convertView = mInflater.inflate(R.layout.item, parent, false);
                        holder.textView = (TextView)convertView.findViewById(R.id.text);
                        break;
                    }
                case TYPE_SEPARATOR:
                    //Set the text separators ("Districts near Grand Blanc", etc.)
                    convertView = mInflater.inflate(R.layout.separator, parent, false);
                    holder.textView = (TextView) convertView.findViewById(R.id.textSeparator);
                    break;

            }
            convertView.setTag(holder);
            holder.textView.setText(mData.get(position));
            return convertView;
        }

        public class ViewHolder {
            public TextView textView;
        }
    }
}







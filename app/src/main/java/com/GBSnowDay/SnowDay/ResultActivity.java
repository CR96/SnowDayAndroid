package com.GBSnowDay.SnowDay;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ResultActivity extends AppCompatActivity {

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

    //Declare all views
    PercentFragment percentFragment = new PercentFragment();
    ClosingsFragment closingsFragment = new ClosingsFragment();
    WeatherFragment weatherFragment = new WeatherFragment();

    ViewPager viewPager;
    TabLayout tabLayout;

    String schooltext;


    List<String> orgName = new ArrayList<>();
    List<String> status = new ArrayList<>();

    List<String> displayedOrgNames = new ArrayList<>();
    List<String> displayedStatuses = new ArrayList<>();

    //Declare lists that will be displayed in RecyclerViews
    List<String> GBText = new ArrayList<>();
    List<String> GBSubtext = new ArrayList<>();

    List<String> wjrtInfo = new ArrayList<>();
    List<String> nwsInfo = new ArrayList<>();

    List<String> weatherWarn = new ArrayList<>();
    List<String> weatherWarnTime = new ArrayList<>();
    List<String> weatherSummary = new ArrayList<>();
    List<String> weatherExpire = new ArrayList<>();
    List<String> weatherLink = new ArrayList<>();

    DateTime dt = new DateTime();

    int days;
    int dayrun;

    String weekdaytoday;
    String weekdaytomorrow;

    String datetoday;
    String datetomorrow;

    //Individual components of the calculation
    int schoolpercent;
    int weatherpercent;
    int percent;

    //Levels of school closings (near vs. far)
    int tier1 = 0;
    int tier2 = 0;
    int tier3 = 0;
    int tier4 = 0;

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
    static boolean WeatherWarningsPresent;

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

        //Start the calculation
        Calculate();

    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(percentFragment, getString(R.string.tab1));
        adapter.addFragment(closingsFragment, getString(R.string.tab2));
        adapter.addFragment(weatherFragment, getString(R.string.tab3));
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

                //Get the day of the week as a string.
                weekdaytoday = dt.dayOfWeek().getAsText();

                //Get tomorrow's weekday as a string.
                DateTime tomorrow = new DateTime();

                weekdaytomorrow = tomorrow.plusDays(1).dayOfWeek().getAsText();

                //This is the current listings page.
                schools = Jsoup.connect("http://abc12.com/closings").timeout(10000).get();
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
                Resources res = getResources();

                //Sanity check - make sure Grand Blanc isn't already closed before predicting
                GB = isClosed(
                        res.getStringArray(R.array.checks_gb),
                        getString(R.string.GB),
                        true,
                        -1);

                if (GB) {
                    GBText.add(getString(R.string.SnowDay));
                    GBSubtext.add(null);
                }else{
                    if (dayrun == 0) {
                        if (dt.getHourOfDay() >= 7 && dt.getHourOfDay() < 16) {
                            //Time is between 7AM and 4PM. School is already in session.
                            GBText.add(getString(R.string.SchoolOpen));
                            GBSubtext.add(null);
                            GBOpen = true;
                        } else if (dt.getHourOfDay() >= 16) {
                            //Time is after 4PM. School is already out.
                            GBText.add(getString(R.string.Dismissed));
                            GBSubtext.add(null);
                            GBOpen = true;
                        }
                    }
                }

                //Check school closings
                String[] tier1 = res.getStringArray(R.array.name_t1);
                String[] tier2 = res.getStringArray(R.array.name_t2);
                String[] tier3 = res.getStringArray(R.array.name_t3);
                String[] tier4 = res.getStringArray(R.array.name_t4);

                //Tier 4
                Atherton = isClosed(
                        res.getStringArray(R.array.checks_atherton),
                        tier4[0],
                        false,
                        4);
                Bendle = isClosed(
                        res.getStringArray(R.array.checks_bendle),
                        tier4[1],
                        false,
                        4);
                Bentley = isClosed(
                        res.getStringArray(R.array.checks_bentley),
                        tier4[2],
                        false,
                        4);
                Carman = isClosed(
                        res.getStringArray(R.array.checks_carman),
                        tier4[3],
                        false,
                        4);
                Flint = isClosed(
                        res.getStringArray(R.array.checks_flint),
                        tier4[4],
                        false,
                        4);
                Goodrich = isClosed(
                        res.getStringArray(R.array.checks_goodrich),
                        tier4[5],
                        false,
                        4);

                //Tier 3
                Beecher = isClosed(
                        res.getStringArray(R.array.checks_beecher),
                        tier3[0],
                        false,
                        3);
                Clio = isClosed(
                        res.getStringArray(R.array.checks_clio),
                        tier3[1],
                        false,
                        3);
                Davison = isClosed(
                        res.getStringArray(R.array.checks_davison),
                        tier3[2],
                        false,
                        3);
                Fenton = isClosed(
                        res.getStringArray(R.array.checks_fenton),
                        tier3[3],
                        false,
                        3);
                Flushing = isClosed(
                        res.getStringArray(R.array.checks_flushing),
                        tier3[4],
                        false,
                        3);
                Genesee = isClosed(
                        res.getStringArray(R.array.checks_genesee),
                        tier3[5],
                        false,
                        3);
                Kearsley = isClosed(
                        res.getStringArray(R.array.checks_kearsley),
                        tier3[6],
                        false,
                        3);
                LKFenton = isClosed(
                        res.getStringArray(R.array.checks_lkfenton),
                        tier3[7],
                        false,
                        3);
                Linden = isClosed(
                        res.getStringArray(R.array.checks_linden),
                        tier3[8],
                        false,
                        3);
                Montrose = isClosed(
                        res.getStringArray(R.array.checks_montrose),
                        tier3[9],
                        false,
                        3);
                Morris = isClosed(
                        res.getStringArray(R.array.checks_morris),
                        tier3[10],
                        false,
                        3);
                SzCreek = isClosed(
                        res.getStringArray(R.array.checks_szcreek),
                        tier3[11],
                        false,
                        3);

                //Tier 2
                Durand = isClosed(
                        res.getStringArray(R.array.checks_durand),
                        tier2[0],
                        false,
                        2);
                Holly = isClosed(
                        res.getStringArray(R.array.checks_holly),
                        tier2[1],
                        false,
                        2);
                Lapeer = isClosed(
                        res.getStringArray(R.array.checks_lapeer),
                        tier2[2],
                        false,
                        2);
                Owosso = isClosed(
                        res.getStringArray(R.array.checks_owosso),
                        tier2[3],
                        false,
                        2);

                //Tier 1
                GBAcademy = isClosed(
                        res.getStringArray(R.array.checks_gbacademy),
                        tier1[0],
                        false,
                        1);
                GISD = isClosed(
                        res.getStringArray(R.array.checks_gisd),
                        tier1[1],
                        false,
                        1);
                HolyFamily = isClosed(
                        res.getStringArray(R.array.checks_holyfamily),
                        tier1[2],
                        false,
                        1);
                WPAcademy = isClosed(
                        res.getStringArray(R.array.checks_wpacademy),
                        tier1[3],
                        false,
                        1);
            }

            //Set the schoolpercent
            if (tier1 > 2) {
                //3+ academies are closed. 20% schoolpercent.
                schoolpercent = 20;
            }
            if (tier2 > 2) {
                //3+ schools in nearby counties are closed. 40% schoolpercent.
                schoolpercent = 40;
            }
            if (tier3 > 2) {
                //3+ schools in Genesee County are closed. 60% schoolpercent.
                schoolpercent = 60;
            }
            if (tier4 > 2) {
                //3+ schools near GB are closed. 80% schoolpercent.
                schoolpercent = 80;
                if (Carman) {
                    //Carman is closed along with 2+ close schools. 90% schoolpercent.
                    schoolpercent = 90;
                }
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            //WJRT scraper has finished.
            WJRTActive = false;
        }

    }

    /** Checks if a specified school or organization is closed or has a message.
     * @param checks The array of potential false positives to be checked
     * @param schoolName The name of the school as present in the array populated by {@link WJRTScraper}
     * @param isGrandBlanc Whether the school is Grand Blanc or another school
     * @param tier The tier the school belongs to (-1 for Grand Blanc)
     * @return The status of the school
     */
    private boolean isClosed(
            String[] checks,
            String schoolName,
            boolean isGrandBlanc,
            int tier) {

        boolean schoolFound = false;
        boolean result = false;

        for (int i = 0; i < orgName.size(); i++) {
            if (orgName.get(i).contains(schoolName)) {
                if (!isFalsePositive(checks, orgName.get(i))) {
                    schoolFound = true;
                    if (isGrandBlanc) {
                        GBMessage = true;
                        GBText.add(schoolName);
                        GBSubtext.add(status.get(i));
                    } else {
                        displayedOrgNames.add(schoolName);
                        displayedStatuses.add(status.get(i));
                    }

                    if (status.get(i).contains("Closed " + weekdaytoday) && dayrun == 0
                            || status.get(i).contains("Closed Today") && dayrun == 0
                            || status.get(i).contains("Closed " + weekdaytomorrow) && dayrun == 1
                            || status.get(i).contains("Closed Tomorrow") && dayrun == 1) {
                        if (isGrandBlanc) {
                            result = true;
                        } else {
                            switch (tier) {
                                case 1:
                                    tier1++;
                                case 2:
                                    tier2++;
                                case 3:
                                    tier3++;
                                case 4:
                                    tier4++;
                                default:
                            }
                            result = true;
                        }
                    }
                }
            }

            if (schoolFound) {break;}
        }

        if (isGrandBlanc && !schoolFound) {
            GBText.add(schoolName);
            GBSubtext.add(getString(R.string.Open));
        }else if (!schoolFound){
            displayedOrgNames.add(schoolName);
            displayedStatuses.add(getString(R.string.Open));
        }

        return result;
    }

    private boolean isFalsePositive(String[] checks, String org) {

        for (String check : checks) {
            if (org.contains(check)) {
                return true;
            }
        }
        return false;
    }

    private class WeatherScraper extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... nothing) {
            /**NATIONAL WEATHER SERVICE SCRAPER**/
            //Change the percentage based on current storm/wind/temperature warnings.

            Document weatherdoc;

            //Live html
            try {
                weatherdoc = Jsoup.connect("http://alerts.weather.gov/cap/wwaatmget.php?x=MIZ061&y=0").timeout(10000).get();

                Elements title = weatherdoc.select("title");
                Elements summary = weatherdoc.select("summary");
                Elements expiretime = weatherdoc.select("cap|expires");
                Elements link = weatherdoc.select("link");


                if (title != null) {
                    for (int i = 0; i < title.size(); i++) {
                        int stringend = title.get(i).text().indexOf("issued");
                        if (stringend != -1) {
                            weatherWarn.add(title.get(i).text().substring(0, stringend));
                        } else {
                            weatherWarn.add(title.get(i).text());
                        }
                    }

                    if (!weatherWarn.get(1).contains("no active")) {
                        //Weather warnings are present.
                        WeatherWarningsPresent = true;
                    }
                }
                if (expiretime != null) {
                    SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.US);
                    SimpleDateFormat output = new SimpleDateFormat("MMMM dd 'at' h:mm a", Locale.US);
                    Date date;
                    String readableDate;
                    for (int i = 0; i < expiretime.size(); i++) {
                        weatherExpire.add(expiretime.get(i).text());
                        date = input.parse(weatherExpire.get(i));
                        readableDate = output.format(date);
                        weatherWarnTime.add("Expires " + readableDate);
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

            } catch (IOException e) {
                //Connectivity issues
                nwsInfo.add(getString(R.string.WeatherConnectionError) + " " + getString(R.string.NoConnection));
                NWSFail = true;

                Crashlytics.logException(e);
            } catch (NullPointerException | ParseException e) {
                //Webpage layout not recognized.
                nwsInfo.add(getString(R.string.WeatherParseError));
                NWSFail = true;

                Crashlytics.logException(e);
            }

            if (!NWSFail) {
                //Significant Weather Advisory
                checkWeatherWarning(getString(R.string.SigWeather), 15);

                //Winter Weather Advisory
                checkWeatherWarning(getString(R.string.WinterAdvisory), 30);

                //Lake Effect Snow Advisory
                checkWeatherWarning(getString(R.string.LakeSnowAdvisory), 40);

                //Freezing Rain
                checkWeatherWarning(getString(R.string.Rain), 40);

                //Freezing Drizzle
                checkWeatherWarning(getString(R.string.Drizzle), 40);

                //Freezing Fog
                checkWeatherWarning(getString(R.string.Fog), 40);

                //Wind Chill Advisory
                checkWeatherWarning(getString(R.string.WindChillAdvisory), 40);

                //Ice Storm Warning
                checkWeatherWarning(getString(R.string.IceStorm), 70);

                //Wind Chill Watch
                checkWeatherWarning(getString(R.string.WindChillWatch), 70);

                //Wind Chill Warning
                checkWeatherWarning(getString(R.string.WindChillWarn), 70);

                //Winter Storm Watch
                checkWeatherWarning(getString(R.string.WinterWatch), 80);

                //Winter Storm Warning
                checkWeatherWarning(getString(R.string.WinterWarn), 80);

                //Lake Effect Snow Watch
                checkWeatherWarning(getString(R.string.LakeSnowWatch), 80);

                //Lake Effect Snow Warning
                checkWeatherWarning(getString(R.string.LakeSnowWarn), 80);

                //Blizzard Watch
                checkWeatherWarning(getString(R.string.BlizzardWatch), 90);

                //Blizzard Warning
                checkWeatherWarning(getString(R.string.BlizzardWarn), 90);
            }

            return null;

        }

        protected void onPostExecute(Void result) {
            //Weather scraper has finished.
            NWSActive = false;
        }
    }


    /**Check for the presence of weather warnings.
     * Only the highest weather percent is stored (not cumulative).
     * Calculation is affected based on when warning expires.
     * @param warn The string identifying the warning to search for
     * @param weight The value weatherpercent is set to if the warning is found (
     */
    private void checkWeatherWarning(String warn, int weight) {
        for (int i = 0; i < weatherWarn.size(); i++) {
            if (weatherWarn.get(i).contains(warn)) {
                if (weatherExpire.get(i - 1).substring(0, 10).equals(datetoday) && dayrun == 0) {
                    weatherpercent = weight;
                }

                if (weatherExpire.get(i - 1).substring(0, 10).equals(datetomorrow)) {
                    weatherpercent = weight;
                }
            }
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
                    percentFragment.txtPercent.setText("0%");
                }
            });

            //Animate txtPercent
            if (WJRTFail && NWSFail) {
                //Both scrapers failed. A percentage cannot be determined.
                //Don't set the percent.
                GBText.add(getString(R.string.CalculateError));
                GBSubtext.add(getString(R.string.NoConnection));
                runOnUiThread(new Runnable() {
                    public void run() {
                        percentFragment.txtPercent.setText("--");
                        crossFadePercent();
                        enableTabs();
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        crossFadePercent();
                        countUp(percentFragment.txtPercent, 0);
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            GBAdapter gbAdapter = new GBAdapter(GBText, GBSubtext, GB, GBMessage);

            LinearLayoutManager GBManager = new LinearLayoutManager(ResultActivity.this);
            GBManager.setStackFromEnd(true);
            percentFragment.lstGB.setLayoutManager(GBManager);

            percentFragment.lstGB.setAdapter(gbAdapter);

            //Set up the RecyclerView adapter that displays school closings

            if (!WJRTFail) {
                //WJRT has not failed.

                RecyclerView.LayoutManager ClosingsManager = new LinearLayoutManager(ResultActivity.this);
                closingsFragment.lstClosings.setLayoutManager(ClosingsManager);

                //Add section headers
                displayedOrgNames.add(0, getString(R.string.tier4));
                displayedStatuses.add(0, null);

                displayedOrgNames.add(7, getString(R.string.tier3));
                displayedStatuses.add(7, null);

                displayedOrgNames.add(20, getString(R.string.tier2));
                displayedStatuses.add(20, null);

                displayedOrgNames.add(25, getString(R.string.tier1));
                displayedStatuses.add(25, null);

                ClosingsAdapter closingsAdapter = new ClosingsAdapter(
                        displayedOrgNames, displayedStatuses);

                closingsFragment.lstClosings.setAdapter(closingsAdapter);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closingsFragment.lstClosings.setVisibility(View.VISIBLE);
                    }
                });
            } else {
                //WJRT has failed.
                ArrayAdapter<String> WJRTadapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, wjrtInfo);
                closingsFragment.lstWJRT.setAdapter(WJRTadapter);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closingsFragment.lstWJRT.setVisibility(View.VISIBLE);
                    }
                });

            }

            //Set up the RecyclerView adapter that displays weather warnings
            if (!NWSFail) {
                //NWS has not failed.

                RecyclerView.LayoutManager WeatherManager = new LinearLayoutManager(ResultActivity.this);
                WeatherAdapter weatherAdapter = new WeatherAdapter(
                        weatherWarn,
                        weatherWarnTime,
                        weatherSummary,
                        weatherLink);

                weatherFragment.lstWeather.setLayoutManager(WeatherManager);
                weatherFragment.lstWeather.setAdapter(weatherAdapter);
            } else {
                //NWS has failed.
                ArrayAdapter<String> NWSadapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, nwsInfo);
                weatherFragment.lstNWS.setAdapter(NWSadapter);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        weatherFragment.lstNWS.setVisibility(View.VISIBLE);
                    }
                });
            }
        }
    }

    private void crossFadePercent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            percentFragment.txtPercent.setAlpha(0f);
            percentFragment.txtPercent.animate()
                    .alpha(1f)
                    .setDuration(250);

            percentFragment.progCalculate.animate()
                    .alpha(0f)
                    .setDuration(250)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            percentFragment.progCalculate.clearAnimation();
                            percentFragment.progCalculate.setVisibility(View.GONE);
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
                    percentFragment.progCalculate.clearAnimation();
                    percentFragment.progCalculate.setVisibility(View.GONE);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            percentFragment.progCalculate.startAnimation(fade_out);

            Animation fade_in = AnimationUtils.loadAnimation(ResultActivity.this, android.R.anim.fade_in);
            fade_in.setDuration(250);
            fade_in.setFillAfter(true);
            percentFragment.txtPercent.startAnimation(fade_in);
        }
    }

    @SuppressLint("SetTextI18n")
    private void countUp(final TextView tv, final int count) {
        if (count > percent) {
            if (percent > 0) {
                percentFragment.txtPercent.startAnimation(AnimationUtils.loadAnimation(ResultActivity.this, R.anim.overshoot));
            }
            enableTabs();
            return;
        }
        tv.setText(String.valueOf(count) + "%");

        if (count >= 0 && count <= 20) {
            runOnUiThread(new Runnable() {
                public void run() {
                    percentFragment.txtPercent.setTextColor(ContextCompat.getColor(ResultActivity.this, R.color.red));
                }
            });
        } if (count > 20 && count <= 60) {
            runOnUiThread(new Runnable() {
                public void run() {
                    percentFragment.txtPercent.setTextColor(ContextCompat.getColor(ResultActivity.this, R.color.orange));
                }
            });
        } if (count > 60 && count <= 80) {
            runOnUiThread(new Runnable() {
                public void run() {
                    percentFragment.txtPercent.setTextColor(ContextCompat.getColor(ResultActivity.this, R.color.green));
                }
            });
        } if (count > 80) {
            runOnUiThread(new Runnable() {
                public void run() {
                    percentFragment.txtPercent.setTextColor(ContextCompat.getColor(ResultActivity.this, R.color.colorAccent));
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

        percentFragment.lstGB.startAnimation(AnimationUtils.loadAnimation(ResultActivity.this, R.anim.slide_in));
        percentFragment.lstGB.setVisibility(View.VISIBLE);
    }


    public class ClosingsAdapter extends RecyclerView.Adapter<ClosingsAdapter.ViewHolder> {
        private Context mContext;
        private List<String> mOrgList;
        private List<String> mStatusList;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public CardView mCardView;
            public ViewHolder(CardView v) {
                super(v);
                mCardView = v;
            }
        }

        public ClosingsAdapter(List<String> data1, List<String> data2) {
            mOrgList = data1;
            mStatusList = data2;
        }

        @Override
        public ClosingsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
            mContext = parent.getContext();

            View v = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_closings, parent, false);
            return new ViewHolder((CardView) v);
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            LinearLayout layout = (LinearLayout) holder.mCardView.getChildAt(0);
            TextView txtOrg = (TextView) layout.getChildAt(0);
            TextView txtStatus = (TextView) layout.getChildAt(1);

            txtOrg.setText(mOrgList.get(position));
            txtStatus.setText(mStatusList.get(position));
            if (position == 0 || position == 7 || position == 20 || position == 25) {
                //Make section headers blue.
                holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBackground));
                holder.mCardView.setCardElevation(0);
                holder.mCardView.setContentPadding(16, 32, 0, 0);
                txtStatus.setVisibility(View.GONE);
            }else if (Atherton && position == 1 || Bendle && position == 2
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
                holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.orange));
                holder.mCardView.setCardElevation(16);
            }else{
                holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                holder.mCardView.setCardElevation(0);
            }
        }

        @Override
        public int getItemCount() {
            return mOrgList.size();
        }
    }
}







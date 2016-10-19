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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


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

    ClosingsScraper closingsScraper;
    WeatherScraper weatherScraper;

    ArrayList<String> orgNames = new ArrayList<>();
    ArrayList<String> orgStatuses = new ArrayList<>();

    ArrayList<String> displayedOrgNames = new ArrayList<>();
    ArrayList<String> displayedOrgStatuses = new ArrayList<>();

    //Declare lists that will be displayed in RecyclerViews
    ArrayList<String> GBText = new ArrayList<>();
    ArrayList<String> GBSubtext = new ArrayList<>();

    ArrayList<String> warningTitles = new ArrayList<>();
    ArrayList<String> warningExpireTimes = new ArrayList<>();
    ArrayList<String> warningReadableTimes = new ArrayList<>();
    ArrayList<String> warningSummaries = new ArrayList<>();
    ArrayList<String> warningLinks = new ArrayList<>();

    DateTime today = new DateTime();
    SimpleDateFormat sdfInput;

    int days;
    int dayrun;

    String weekdaytoday;
    String weekdaytomorrow;

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
    boolean weatherWarningsPresent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

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
            tabLayout.setupWithViewPager(viewPager);

            //Make sure tab content stays in memory
            viewPager.setOffscreenPageLimit(3);
        }

        //Read variables from MainActivity class
        Intent result = getIntent();
        dayrun = result.getIntExtra("dayrun", dayrun);
        days = result.getIntExtra("days", days);

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
            //Open URL with closings listed
            String url = getString(R.string.ClosingsURL);
            Intent i = new Intent(Intent.ACTION_VIEW);
            Uri u = Uri.parse(url);
            Context context = this;

            try {
                //Start the activity
                i.setData(u);
                startActivity(i);
            }catch (ActivityNotFoundException e) {
                //Raise on activity not found
                Toast.makeText(context, getString(R.string.NoBrowser), Toast.LENGTH_SHORT).show();
            }
        }
        if (id == R.id.weather) {
            //Open URL with warnings listed
            String url = getString(R.string.WeatherExternalLink);
            Intent i = new Intent(Intent.ACTION_VIEW);
            Uri u = Uri.parse(url);
            Context context = this;

            try {
                //Start the activity
                i.setData(u);
                startActivity(i);
            }catch (ActivityNotFoundException e) {
                //Raise on activity not found
                Toast.makeText(context, getString(R.string.NoBrowser), Toast.LENGTH_SHORT).show();
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

        closingsScraper = new ClosingsScraper(this, new ClosingsScraper.AsyncResponse() {
            @Override
            public void processFinish(ClosingsScraper.ClosingsData closingsData) {
                if (closingsScraper.isCancelled()) {
                    //Closings scraper has failed.
                    closingsFragment.txtClosingsInfo.setText(closingsData.error);
                    closingsFragment.txtClosingsInfo.setVisibility(View.VISIBLE);

                    GBText.add(closingsData.error);
                    GBSubtext.add(getString(R.string.CalculateWithoutClosings));
                }else{
                    orgNames.addAll(closingsData.orgNames);
                    orgStatuses.addAll(closingsData.orgStatuses);
                    parseClosings();

                    //Set up the RecyclerView adapter that displays school closings
                    RecyclerView.LayoutManager ClosingsManager = new LinearLayoutManager(ResultActivity.this);
                    closingsFragment.lstClosings.setLayoutManager(ClosingsManager);

                    //Add section headers
                    displayedOrgNames.add(0, getString(R.string.tier4));
                    displayedOrgStatuses.add(0, null);

                    displayedOrgNames.add(7, getString(R.string.tier3));
                    displayedOrgStatuses.add(7, null);

                    displayedOrgNames.add(20, getString(R.string.tier2));
                    displayedOrgStatuses.add(20, null);

                    displayedOrgNames.add(25, getString(R.string.tier1));
                    displayedOrgStatuses.add(25, null);

                    ClosingsAdapter closingsAdapter = new ClosingsAdapter(
                            displayedOrgNames, displayedOrgStatuses);

                    closingsFragment.lstClosings.setAdapter(closingsAdapter);
                    closingsFragment.lstClosings.setVisibility(View.VISIBLE);
                }
            }
        });

        closingsScraper.execute();

        weatherScraper = new WeatherScraper(this, new WeatherScraper.AsyncResponse() {
            @Override
            public void processFinish(WeatherScraper.WeatherData weatherData) {
                if (weatherScraper.isCancelled()) {
                    //Weather scraper has failed.
                    weatherFragment.txtWeatherInfo.setText(weatherData.error);
                    weatherFragment.txtWeatherInfo.setVisibility(View.VISIBLE);

                    GBText.add(weatherData.error);
                    GBSubtext.add(getString(R.string.CalculateWithoutWeather));
                }else{
                    warningTitles.addAll(weatherData.warningTitles);
                    warningExpireTimes.addAll(weatherData.warningExpireTimes);
                    warningReadableTimes.addAll(weatherData.warningReadableTimes);
                    warningSummaries.addAll(weatherData.warningSummaries);
                    warningLinks.addAll(weatherData.warningLinks);
                    weatherWarningsPresent = weatherData.weatherWarningsPresent;
                    parseWeather();

                    //Set up the RecyclerView adapter that displays weather warnings
                    RecyclerView.LayoutManager WeatherManager = new LinearLayoutManager(ResultActivity.this);
                    WeatherAdapter weatherAdapter = new WeatherAdapter(
                            warningTitles,
                            warningReadableTimes,
                            warningSummaries,
                            warningLinks,
                            weatherWarningsPresent);

                    weatherFragment.lstWeather.setLayoutManager(WeatherManager);
                    weatherFragment.lstWeather.setAdapter(weatherAdapter);
                }
            }
        });

        weatherScraper.execute();

        //Final Percent Calculator
        new PercentCalculate().execute();


    }

    private void parseClosings() {

            //Get the day of the week as a string.
            weekdaytoday = today.dayOfWeek().getAsText();

            //Get tomorrow's weekday as a string.
            DateTime tomorrow = new DateTime();

            weekdaytomorrow = tomorrow.plusDays(1).dayOfWeek().getAsText();

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
            } else {
                if (dayrun == 0) {
                    if (today.getHourOfDay() >= 7 && today.getHourOfDay() < 16) {
                        //Time is between 7AM and 4PM. School is already in session.
                        GBText.add(getString(R.string.SchoolOpen));
                        GBSubtext.add(null);
                        GBOpen = true;
                    } else if (today.getHourOfDay() >= 16) {
                        //Time is after 4PM. School is already out.
                        GBText.add(getString(R.string.Dismissed));
                        GBSubtext.add(null);
                        GBOpen = true;
                    }
                }
            }

            //Check school closings
            String[] tier1schools = res.getStringArray(R.array.name_t1);
            String[] tier2schools = res.getStringArray(R.array.name_t2);
            String[] tier3schools = res.getStringArray(R.array.name_t3);
            String[] tier4schools = res.getStringArray(R.array.name_t4);

            //Tier 4
            Atherton = isClosed(
                    res.getStringArray(R.array.checks_atherton),
                    tier4schools[0],
                    false,
                    4);
            Bendle = isClosed(
                    res.getStringArray(R.array.checks_bendle),
                    tier4schools[1],
                    false,
                    4);
            Bentley = isClosed(
                    res.getStringArray(R.array.checks_bentley),
                    tier4schools[2],
                    false,
                    4);
            Carman = isClosed(
                    res.getStringArray(R.array.checks_carman),
                    tier4schools[3],
                    false,
                    4);
            Flint = isClosed(
                    res.getStringArray(R.array.checks_flint),
                    tier4schools[4],
                    false,
                    4);
            Goodrich = isClosed(
                    res.getStringArray(R.array.checks_goodrich),
                    tier4schools[5],
                    false,
                    4);

            //Tier 3
            Beecher = isClosed(
                    res.getStringArray(R.array.checks_beecher),
                    tier3schools[0],
                    false,
                    3);
            Clio = isClosed(
                    res.getStringArray(R.array.checks_clio),
                    tier3schools[1],
                    false,
                    3);
            Davison = isClosed(
                    res.getStringArray(R.array.checks_davison),
                    tier3schools[2],
                    false,
                    3);
            Fenton = isClosed(
                    res.getStringArray(R.array.checks_fenton),
                    tier3schools[3],
                    false,
                    3);
            Flushing = isClosed(
                    res.getStringArray(R.array.checks_flushing),
                    tier3schools[4],
                    false,
                    3);
            Genesee = isClosed(
                    res.getStringArray(R.array.checks_genesee),
                    tier3schools[5],
                    false,
                    3);
            Kearsley = isClosed(
                    res.getStringArray(R.array.checks_kearsley),
                    tier3schools[6],
                    false,
                    3);
            LKFenton = isClosed(
                    res.getStringArray(R.array.checks_lkfenton),
                    tier3schools[7],
                    false,
                    3);
            Linden = isClosed(
                    res.getStringArray(R.array.checks_linden),
                    tier3schools[8],
                    false,
                    3);
            Montrose = isClosed(
                    res.getStringArray(R.array.checks_montrose),
                    tier3schools[9],
                    false,
                    3);
            Morris = isClosed(
                    res.getStringArray(R.array.checks_morris),
                    tier3schools[10],
                    false,
                    3);
            SzCreek = isClosed(
                    res.getStringArray(R.array.checks_szcreek),
                    tier3schools[11],
                    false,
                    3);

            //Tier 2
            Durand = isClosed(
                    res.getStringArray(R.array.checks_durand),
                    tier2schools[0],
                    false,
                    2);
            Holly = isClosed(
                    res.getStringArray(R.array.checks_holly),
                    tier2schools[1],
                    false,
                    2);
            Lapeer = isClosed(
                    res.getStringArray(R.array.checks_lapeer),
                    tier2schools[2],
                    false,
                    2);
            Owosso = isClosed(
                    res.getStringArray(R.array.checks_owosso),
                    tier2schools[3],
                    false,
                    2);

            //Tier 1
            GBAcademy = isClosed(
                    res.getStringArray(R.array.checks_gbacademy),
                    tier1schools[0],
                    false,
                    1);
            GISD = isClosed(
                    res.getStringArray(R.array.checks_gisd),
                    tier1schools[1],
                    false,
                    1);
            HolyFamily = isClosed(
                    res.getStringArray(R.array.checks_holyfamily),
                    tier1schools[2],
                    false,
                    1);
            WPAcademy = isClosed(
                    res.getStringArray(R.array.checks_wpacademy),
                    tier1schools[3],
                    false,
                    1);

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
    }

    /** Checks if a specified school or organization is closed or has a message.
     * @param checks The array of potential false positives to be checked
     * @param schoolName The name of the school as present in the array populated by {@link ClosingsScraper}
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

        for (int i = 0; i < orgNames.size(); i++) {
            if (orgNames.get(i).contains(schoolName)) {
                if (!isFalsePositive(checks, orgNames.get(i))) {
                    schoolFound = true;
                    if (isGrandBlanc) {
                        GBMessage = true;
                        GBText.add(schoolName);
                        GBSubtext.add(orgStatuses.get(i));
                    } else {
                        displayedOrgNames.add(schoolName);
                        displayedOrgStatuses.add(orgStatuses.get(i));
                    }

                    if (orgStatuses.get(i).contains("Closed " + weekdaytoday) && dayrun == 0
                            || orgStatuses.get(i).contains("Closed Today") && dayrun == 0
                            || orgStatuses.get(i).contains("Closed " + weekdaytomorrow) && dayrun == 1
                            || orgStatuses.get(i).contains("Closed Tomorrow") && dayrun == 1) {
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
            displayedOrgStatuses.add(getString(R.string.Open));
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
    
    private void parseWeather() {
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

    /**Check for the presence of weather warnings.
     * Only the highest weather percent is stored (not cumulative).
     * Calculation is affected based on when warning expires.
     * @param warn The string identifying the warning to search for
     * @param weight The value weatherpercent is set to if the warning is found
     * @throws ParseException if date format is unrecognized
     */
    private void checkWeatherWarning(String warn, int weight) {
        DateTime warningDate = null;
        DateTime tomorrow;
        for (int i = 0; i < warningTitles.size(); i++) {
            if (warningTitles.get(i).contains(warn)) {
                try {
                    warningDate = new DateTime(sdfInput.parse(warningExpireTimes.get(i - 1)));
                } catch (ParseException e) {
                    // TODO: Handle this exception
                }
                tomorrow = today.plusDays(1);
                if ((warningDate.isEqual(today) || warningDate.isAfter(today))
                    && (dayrun == 0)) {
                    weatherpercent = weight;
                }else if ((warningDate.isEqual(tomorrow) || warningDate.isAfter(tomorrow))
                    && (dayrun == 1)) {
                    weatherpercent = weight;
                }
            }
        }
    }

    private class PercentCalculate extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... nothing) {

            //Give the scrapers time to act before displaying the percent

            while (closingsScraper.getStatus() == Status.RUNNING
                    || weatherScraper.getStatus() == Status.RUNNING) {
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
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //Animate txtPercent
            if (closingsScraper.isCancelled()
                    && weatherScraper.isCancelled()) {
                //Both scrapers failed. A percentage cannot be determined.
                //Don't set the percent.
                GBText.add(getString(R.string.CalculateError));
                GBSubtext.add(getString(R.string.NoConnection));
                percentFragment.progCalculate.clearAnimation();
                percentFragment.progCalculate.setImageDrawable(
                        ContextCompat.getDrawable(
                                ResultActivity.this,
                                R.drawable.ic_error_outline_white));
                percentFragment.lstGB.startAnimation(AnimationUtils.loadAnimation(ResultActivity.this, R.anim.slide_in));
                percentFragment.lstGB.setVisibility(View.VISIBLE);
            } else {
                crossFadePercent();
                countUp(percentFragment.txtPercent, 0);
            }

            GBAdapter gbAdapter = new GBAdapter(GBText, GBSubtext, GB, GBMessage);

            LinearLayoutManager GBManager = new LinearLayoutManager(ResultActivity.this);
            GBManager.setStackFromEnd(true);
            percentFragment.lstGB.setLayoutManager(GBManager);

            percentFragment.lstGB.setAdapter(gbAdapter);
        }
    }

    private void crossFadePercent() {
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
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setElevation(0);
        }

        tabLayout.setVisibility(View.VISIBLE);
        viewPager.setPagingEnabled(true);

        percentFragment.lstGB.startAnimation(AnimationUtils.loadAnimation(ResultActivity.this, R.anim.slide_in));
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







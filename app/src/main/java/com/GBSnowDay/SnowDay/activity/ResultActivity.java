package com.GBSnowDay.SnowDay.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.GBSnowDay.SnowDay.model.ClosingsModel;
import com.GBSnowDay.SnowDay.R;
import com.GBSnowDay.SnowDay.model.WeatherModel;
import com.GBSnowDay.SnowDay.adapter.ClosingsAdapter;
import com.GBSnowDay.SnowDay.adapter.GBAdapter;
import com.GBSnowDay.SnowDay.adapter.WeatherAdapter;
import com.GBSnowDay.SnowDay.fragment.ClosingsFragment;
import com.GBSnowDay.SnowDay.fragment.PercentFragment;
import com.GBSnowDay.SnowDay.fragment.WeatherFragment;
import com.GBSnowDay.SnowDay.network.ClosingsScraper;
import com.GBSnowDay.SnowDay.network.WeatherScraper;
import com.GBSnowDay.SnowDay.view.ViewPager;

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

    ClosingsModel mClosingsModel;

    //Declare lists that will be displayed in RecyclerViews
    ArrayList<String> GBText = new ArrayList<>();
    ArrayList<String> GBSubtext = new ArrayList<>();

    int days;
    int dayrun;

    //Individual components of the calculation
    int schoolPercent;
    int weatherPercent;
    int percent;

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

        closingsScraper = new ClosingsScraper(this, dayrun, new ClosingsScraper.AsyncResponse() {
            @Override
            public void processFinish(ClosingsModel closingsModel) {
                mClosingsModel = closingsModel;
                if (closingsScraper.isCancelled()) {
                    //Closings scraper has failed.
                    closingsFragment.txtClosingsInfo.setText(mClosingsModel.error);
                    closingsFragment.txtClosingsInfo.setVisibility(View.VISIBLE);

                    GBText.add(mClosingsModel.error);
                    GBSubtext.add(getString(R.string.CalculateWithoutClosings));
                } else {
                    //Set the school percent
                    schoolPercent = mClosingsModel.schoolPercent;

                    GBText.addAll(mClosingsModel.GBText);
                    GBSubtext.addAll(mClosingsModel.GBSubtext);

                    //Set up the RecyclerView adapter that displays school closings
                    RecyclerView.LayoutManager ClosingsManager = new LinearLayoutManager(ResultActivity.this);
                    closingsFragment.lstClosings.setLayoutManager(ClosingsManager);

                    //Add section headers
                    mClosingsModel.displayedOrgNames.add(0, getString(R.string.tier4));
                    mClosingsModel.displayedOrgStatuses.add(0, null);

                    mClosingsModel.displayedOrgNames.add(7, getString(R.string.tier3));
                    mClosingsModel.displayedOrgStatuses.add(7, null);

                    mClosingsModel.displayedOrgNames.add(20, getString(R.string.tier2));
                    mClosingsModel.displayedOrgStatuses.add(20, null);

                    mClosingsModel.displayedOrgNames.add(25, getString(R.string.tier1));
                    mClosingsModel.displayedOrgStatuses.add(25, null);

                    ClosingsAdapter closingsAdapter = new ClosingsAdapter(mClosingsModel);

                    closingsFragment.lstClosings.setAdapter(closingsAdapter);
                    closingsFragment.lstClosings.setVisibility(View.VISIBLE);
                }
            }
        });

        closingsScraper.execute();

        weatherScraper = new WeatherScraper(this, dayrun, new WeatherScraper.AsyncResponse() {
            @Override
            public void processFinish(WeatherModel weatherModel) {
                if (weatherScraper.isCancelled()) {
                    //Weather scraper has failed.
                    weatherFragment.txtWeatherInfo.setText(weatherModel.error);
                    weatherFragment.txtWeatherInfo.setVisibility(View.VISIBLE);

                    GBText.add(weatherModel.error);
                    GBSubtext.add(getString(R.string.CalculateWithoutWeather));
                }else{
                    //Set the weather percent
                    weatherPercent = weatherModel.weatherPercent;

                    //Set up the RecyclerView adapter that displays weather warnings
                    RecyclerView.LayoutManager WeatherManager = new LinearLayoutManager(ResultActivity.this);
                    WeatherAdapter weatherAdapter = new WeatherAdapter(weatherModel);

                    weatherFragment.lstWeather.setLayoutManager(WeatherManager);
                    weatherFragment.lstWeather.setAdapter(weatherAdapter);
                }
            }
        });

        weatherScraper.execute();

        //Final Percent Calculator
        new PercentCalculate().execute();
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
            if (weatherPercent > schoolPercent) {
                percent = weatherPercent;
            } else if (schoolPercent > weatherPercent) {
                percent = schoolPercent;
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
            if (mClosingsModel.GB) {
                //WJRTScraper reports Grand Blanc is closed. Override percentage, set to 100%
                percent = 100;
            } else if (mClosingsModel.GBOpen) {
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
                GBText.clear();
                GBSubtext.clear();

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

            GBAdapter gbAdapter = new GBAdapter(
                    GBText,
                    GBSubtext,
                    mClosingsModel.GB,
                    mClosingsModel.GBMessage);

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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(percentFragment, getString(R.string.tab1));
        adapter.addFragment(closingsFragment, getString(R.string.tab2));
        adapter.addFragment(weatherFragment, getString(R.string.tab3));
        viewPager.setAdapter(adapter);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        void addFragment(Fragment fragment, String title) {
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
}
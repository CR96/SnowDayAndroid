package com.GBSnowDay.SnowDay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;


public class SnowDayResult extends Activity {

    //Declare all views
    TextView txtGB;
    TextView txtCarman;
    TextView txtAtherton;
    TextView txtBendle;
    TextView txtBentley;
    TextView txtFlint;
    TextView txtGoodrich;
    TextView txtBeecher;
    TextView txtClio;
    TextView txtDavison;
    TextView txtFenton;
    TextView txtFlushing;
    TextView txtGenesee;
    TextView txtKearsley;
    TextView txtLKFenton;
    TextView txtLinden;
    TextView txtMontrose;
    TextView txtMorris;
    TextView txtSzCreek;
    TextView txtDurand;
    TextView txtHolly;
    TextView txtLapeer;
    TextView txtOwosso;
    TextView txtGBAcademy;
    TextView txtGISD;
    TextView txtHolyFamily;
    TextView txtWPAcademy;
    
    TextView txtTier1;
    TextView txtTier2;
    TextView txtTier3;
    TextView txtTier4;

    TextView txtInfo;
    TextView txtPercent;
    TextView txtWeather;

    WebView webRadar;
    Button btnRadar;
    ProgressBar progCalculate;

    TabHost tabHost;

    //Variable declaration
    public String orgName;
    public String status;
    public String schooltext;
    public String weathertext;
    public String weathercheck;

    public String[] orgNameLine;
    public String[] statusLine;

    public int days;
    public int schoolpercent = 0;
    public int weatherpercent = 0;
    public int percent;
    public int percentscroll;
    public int dayrun = 0;
    public int tier1 = 0;
    public int tier2 = 0;
    public int tier3 = 0;
    public int tier4 = 0;
    public int tier5 = 0;

    public boolean schoolNull;
    public boolean nullWeather;

    public boolean GBAcademy;
    public boolean HolyFamily;
    public boolean WPAcademy;
    public boolean GISD;
    public boolean Durand; //Check for "Durand Senior Center"
    public boolean Holly;  //Check for "Holly Academy"
    public boolean Lapeer; //Check for "Chatfield School-Lapeer", "Greater Lapeer Transit Authority", "Lapeer CMH Day Programs",
    //"Lapeer Co. Ed-Tech Center", "Lapeer County Ofices", "Lapeer District Library", "Lapeer Senior Center", and "St. Paul Lutheran-Lapeer"
    public boolean Owosso; //Check for "Owosso Senior Center", "Baker College-Owosso", and "St. Paul Catholic-Owosso"
    public boolean Beecher;
    public boolean Clio; //Check for "Clio Area Senior Center", "Clio City Hall", and "Cornerstone Clio"
    public boolean Davison; //Check for "Davison Senior Center", "Faith Baptist School-Davison", and "Montessori Academy-Davison"
    public boolean Fenton; //Check for "Lake Fenton", "Fenton City Hall", and "Fenton Montessori Academy"
    public boolean Flushing; //Check for "Flushing Senior Citizens Center" and "St. Robert-Flushing"
    public boolean Genesee; //Check for "Freedom Work-Genesee Co.", "Genesee Christian-Burton", "Genesee Co. Mobile Meals", "Genesee Hlth Sys Day Programs", "Genesee Stem Academy", and "Genesee I.S.D."
    public boolean Kearsley;
    public boolean LKFenton;
    public boolean Linden; //Check for "Linden Charter Academy"
    public boolean Montrose; //Check for "Montrose Senior Center"
    public boolean Morris;  //Check for "Mt Morris Twp Administration" and "St. Mary's-Mt. Morris"
    public boolean SzCreek; //Check for "Swartz Creek Area Senior Ctr." and "Swartz Creek Montessori"
    public boolean Atherton;
    public boolean Bendle;
    public boolean Bentley;
    public boolean Flint; //Thankfully this is listed as "Flint Community Schools" - otherwise there would be 25 exceptions to check for.
    public boolean Goodrich;
    public boolean Carman; //Check for "Carman-Ainsworth Senior Ctr."
    public boolean GB; //Check for "Freedom Work-Grand Blanc", "Grand Blanc Academy", "Grand Blanc City Offices", "Grand Blanc Senior Center", and "Holy Family-Grand Blanc"

    public boolean WJRTActive;
    public boolean NWSActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            System.out.println("Hello from activity_snow_day_result!");
            System.out.println("Setting up views");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_snow_day_result);

            //Create TabHost
            tabHost = (TabHost) findViewById(R.id.tabHost);
            tabHost.setup();

            TabHost.TabSpec specs = tabHost.newTabSpec("tag1");
            specs.setContent(R.id.tab1);
            specs.setIndicator("Percent");
            tabHost.addTab(specs);

            specs = tabHost.newTabSpec("tag2");
            specs.setContent(R.id.tab2);
            specs.setIndicator("Closings");
            tabHost.addTab(specs);

            specs = tabHost.newTabSpec("tag3");
            specs.setContent(R.id.tab3);
            specs.setIndicator("Weather");
            tabHost.addTab(specs);

            //Declare views
            txtGB = (TextView) findViewById(R.id.txtGB);
            txtCarman = (TextView) findViewById(R.id.txtCarman);
            txtAtherton = (TextView) findViewById(R.id.txtAtherton);
            txtBendle = (TextView) findViewById(R.id.txtBendle);
            txtBentley = (TextView) findViewById(R.id.txtBentley);
            txtFlint = (TextView) findViewById(R.id.txtFlint);
            txtGoodrich = (TextView) findViewById(R.id.txtGoodrich);
            txtBeecher = (TextView) findViewById(R.id.txtBeecher);
            txtClio = (TextView) findViewById(R.id.txtClio);
            txtDavison = (TextView) findViewById(R.id.txtDavison);
            txtFenton = (TextView) findViewById(R.id.txtFenton);
            txtFlushing = (TextView) findViewById(R.id.txtFlushing);
            txtGenesee = (TextView) findViewById(R.id.txtGenesee);
            txtKearsley = (TextView) findViewById(R.id.txtKearsley);
            txtLKFenton = (TextView) findViewById(R.id.txtLKFenton);
            txtLinden = (TextView) findViewById(R.id.txtLinden);
            txtMontrose = (TextView) findViewById(R.id.txtMontrose);
            txtMorris = (TextView) findViewById(R.id.txtMorris);
            txtSzCreek = (TextView) findViewById(R.id.txtSzCreek);
            txtDurand = (TextView) findViewById(R.id.txtDurand);
            txtHolly = (TextView) findViewById(R.id.txtHolly);
            txtLapeer = (TextView) findViewById(R.id.txtLapeer);
            txtOwosso = (TextView) findViewById(R.id.txtOwosso);
            txtGBAcademy = (TextView) findViewById(R.id.txtGBAcademy);
            txtGISD = (TextView) findViewById(R.id.txtGISD);
            txtHolyFamily = (TextView) findViewById(R.id.txtHolyFamily);
            txtWPAcademy = (TextView) findViewById(R.id.txtWPAcademy);
        
            txtTier1 = (TextView) findViewById(R.id.txtTier1);
            txtTier2 = (TextView) findViewById(R.id.txtTier2);
            txtTier3 = (TextView) findViewById(R.id.txtTier3);
            txtTier4 = (TextView) findViewById(R.id.txtTier4);

            txtPercent = (TextView) findViewById(R.id.txtPercent);
            txtWeather = (TextView) findViewById(R.id.txtWeather);
            txtInfo = (TextView) findViewById(R.id.txtInfo);
            webRadar = (WebView) findViewById(R.id.webRadar);
            btnRadar = (Button) findViewById(R.id.btnRadar);
            progCalculate = (ProgressBar) findViewById(R.id.progCalculate);
        

            Calculate();
        }


    public void radarToggle(View view) {
        if (webRadar.getVisibility() == View.GONE) {
            webRadar.setEnabled(true);
            webRadar.setVisibility(View.VISIBLE);
            webRadar.loadUrl("http://radar.weather.gov/Conus/Loop/centgrtlakes_loop.gif");
            webRadar.getSettings().setLoadWithOverviewMode(true);
            webRadar.getSettings().setUseWideViewPort(true);
            btnRadar.setText(R.string.radarhide);
            txtWeather.setVisibility(View.GONE);
        } else {
            webRadar.setVisibility(View.GONE);
            webRadar.setEnabled(false);
            btnRadar.setText(R.string.radarshow);
            txtWeather.setVisibility(View.VISIBLE);
        }
    }

    private void Calculate() {
        System.out.println("Continuing calculation");
        //Read dayrun and days from SnowDay class
        Intent result = getIntent();
        dayrun = result.getIntExtra("dayrun", dayrun);
        days = result.getIntExtra("days", days);
        System.out.println("Variables dayrun and days received from SnowDay class.");
        System.out.println("Dayrun is " + dayrun);
        System.out.println("Days are " + days);

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

        //Call a reset to clear any previous data
        Reset();


        /**WJRT SCHOOL CLOSINGS SCRAPER**/
        new WJRTScraper().execute();

        //Next Test: Weather!

        /**NATIONAL WEATHER SERVICE SCRAPER**/
        new WeatherScraper().execute();

        //Final Percent Calculator
        new PercentCalculate().execute();



    }

    private void Reset() {
        System.out.println("Resetting SnowDayResult variables");
        //Reset AsyncTask status
        WJRTActive = false;
        NWSActive = false;

        txtInfo.setVisibility(View.GONE);
        progCalculate.setVisibility(View.VISIBLE);

        //Reset variables
        schoolpercent = 0;
        weatherpercent = 0;
        percent = 0;
        tier1 = 0;
        tier2 = 0;
        tier3 = 0;
        tier4 = 0;
        tier5 = 0;

        schoolNull = false;
        nullWeather = true;

        GBAcademy = false;
        WPAcademy = false;
        HolyFamily = false;
        GISD = false;
        Durand = false;
        Holly = false;
        Lapeer = false;
        Owosso = false;
        Beecher = false;
        Clio = false;
        Davison = false;
        Fenton = false;
        Flushing = false;
        Genesee = false;
        Kearsley = false;
        LKFenton = false;
        Linden = false;
        Montrose = false;
        Morris = false;
        SzCreek = false;
        Atherton = false;
        Bendle = false;
        Bentley = false;
        Flint = false;
        Goodrich = false;
        Carman = false;
        GB = false;

        txtPercent.setText("");
        txtGBAcademy.setText((R.string.GBAcademy));
        txtGBAcademy.setTextColor(Color.WHITE);
        txtGBAcademy.setBackgroundColor(Color.rgb(55, 60, 65));
        txtGBAcademy.setVisibility(View.GONE);
        txtGISD.setText(R.string.GISD);
        txtGISD.setTextColor(Color.WHITE);
        txtGISD.setBackgroundColor(Color.rgb(55, 60, 65));
        txtGISD.setVisibility(View.GONE);
        txtHolyFamily.setText(R.string.HolyFamily);
        txtHolyFamily.setTextColor(Color.WHITE);
        txtHolyFamily.setBackgroundColor(Color.rgb(55, 60, 65));
        txtHolyFamily.setVisibility(View.GONE);
        txtWPAcademy.setText(R.string.WPAcademy);
        txtWPAcademy.setTextColor(Color.WHITE);
        txtWPAcademy.setBackgroundColor(Color.rgb(55, 60, 65));
        txtWPAcademy.setVisibility(View.GONE);
        txtBeecher.setText(R.string.Beecher);
        txtBeecher.setTextColor(Color.WHITE);
        txtBeecher.setBackgroundColor(Color.rgb(55, 60, 65));
        txtBeecher.setVisibility(View.GONE);
        txtClio.setText(R.string.Clio);
        txtClio.setTextColor(Color.WHITE);
        txtClio.setBackgroundColor(Color.rgb(55, 60, 65));
        txtClio.setVisibility(View.GONE);
        txtDavison.setText(R.string.Davison);
        txtDavison.setTextColor(Color.WHITE);
        txtDavison.setBackgroundColor(Color.rgb(55, 60, 65));
        txtDavison.setVisibility(View.GONE);
        txtFenton.setText(R.string.Fenton);
        txtFenton.setTextColor(Color.WHITE);
        txtFenton.setBackgroundColor(Color.rgb(55, 60, 65));
        txtFenton.setVisibility(View.GONE);
        txtFlushing.setText(R.string.Flushing);
        txtFlushing.setTextColor(Color.WHITE);
        txtFlushing.setBackgroundColor(Color.rgb(55, 60, 65));
        txtFlushing.setVisibility(View.GONE);
        txtGenesee.setText(R.string.Genesee);
        txtGenesee.setTextColor(Color.WHITE);
        txtGenesee.setBackgroundColor(Color.rgb(55, 60, 65));
        txtGenesee.setVisibility(View.GONE);
        txtKearsley.setText(R.string.Kearsley);
        txtKearsley.setTextColor(Color.WHITE);
        txtKearsley.setBackgroundColor(Color.rgb(55, 60, 65));
        txtKearsley.setVisibility(View.GONE);
        txtLKFenton.setText(R.string.LKFenton);
        txtLKFenton.setTextColor(Color.WHITE);
        txtLKFenton.setBackgroundColor(Color.rgb(55, 60, 65));
        txtLKFenton.setVisibility(View.GONE);
        txtLinden.setText(R.string.Linden);
        txtLinden.setTextColor(Color.WHITE);
        txtLinden.setBackgroundColor(Color.rgb(55, 60, 65));
        txtLinden.setVisibility(View.GONE);
        txtMontrose.setText(R.string.Montrose);
        txtMontrose.setTextColor(Color.WHITE);
        txtMontrose.setBackgroundColor(Color.rgb(55, 60, 65));
        txtMontrose.setVisibility(View.GONE);
        txtMorris.setText(R.string.Morris);
        txtMorris.setTextColor(Color.WHITE);
        txtMorris.setBackgroundColor(Color.rgb(55, 60, 65));
        txtMorris.setVisibility(View.GONE);
        txtSzCreek.setText(R.string.SzCreek);
        txtSzCreek.setTextColor(Color.WHITE);
        txtSzCreek.setBackgroundColor(Color.rgb(55, 60, 65));
        txtSzCreek.setVisibility(View.GONE);
        txtAtherton.setText(R.string.Atherton);
        txtAtherton.setTextColor(Color.WHITE);
        txtAtherton.setBackgroundColor(Color.rgb(55, 60, 65));
        txtAtherton.setVisibility(View.GONE);
        txtBendle.setText(R.string.Bendle);
        txtBendle.setTextColor(Color.WHITE);
        txtBendle.setBackgroundColor(Color.rgb(55, 60, 65));
        txtBendle.setVisibility(View.GONE);
        txtBentley.setText(R.string.Bentley);
        txtBentley.setTextColor(Color.WHITE);
        txtBentley.setBackgroundColor(Color.rgb(55, 60, 65));
        txtBentley.setVisibility(View.GONE);
        txtFlint.setText(R.string.Flint);
        txtFlint.setTextColor(Color.WHITE);
        txtFlint.setBackgroundColor(Color.rgb(55, 60, 65));
        txtFlint.setVisibility(View.GONE);
        txtGoodrich.setText(R.string.Goodrich);
        txtGoodrich.setTextColor(Color.WHITE);
        txtGoodrich.setBackgroundColor(Color.rgb(55, 60, 65));
        txtGoodrich.setVisibility(View.GONE);
        txtDurand.setText(R.string.Durand);
        txtDurand.setTextColor(Color.WHITE);
        txtDurand.setBackgroundColor(Color.rgb(55, 60, 65));
        txtDurand.setVisibility(View.GONE);
        txtHolly.setText(R.string.Holly);
        txtHolly.setTextColor(Color.WHITE);
        txtHolly.setBackgroundColor(Color.rgb(55, 60, 65));
        txtHolly.setVisibility(View.GONE);
        txtLapeer.setText(R.string.Lapeer);
        txtLapeer.setTextColor(Color.WHITE);
        txtLapeer.setBackgroundColor(Color.rgb(55, 60, 65));
        txtLapeer.setVisibility(View.GONE);
        txtOwosso.setText(R.string.Owosso);
        txtOwosso.setTextColor(Color.WHITE);
        txtOwosso.setBackgroundColor(Color.rgb(55, 60, 65));
        txtOwosso.setVisibility(View.GONE);
        txtCarman.setText(R.string.Carman);
        txtCarman.setTextColor(Color.WHITE);
        txtCarman.setBackgroundColor(Color.rgb(55, 60, 65));
        txtCarman.setVisibility(View.GONE);
        txtGB.setText(R.string.GB);
        txtGB.setTextColor(Color.WHITE);
        txtGB.setBackgroundColor(Color.rgb(55, 60, 65));
        txtGB.setVisibility(View.GONE);
        
        txtTier1.setVisibility(View.GONE);
        txtTier2.setVisibility(View.GONE);
        txtTier3.setVisibility(View.GONE);
        txtTier4.setVisibility(View.GONE);
        
        txtWeather.setVisibility(View.GONE);
        btnRadar.setVisibility(View.GONE);
        
    }

    private class WJRTScraper extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... nothing) {
            System.out.println("Starting WJRT Scraper");
            WJRTActive = true;
            Document schools = null;
            //Scrape School Closings from WJRT with Jsoup.

            /**WJRT SCHOOL CLOSINGS SCRAPER**/
            //Scrape School Closings from WJRT with Jsoup.
            //Run scraper in an Async task.

            //This is the current listings page.

            try {
                schools = Jsoup.connect("http://ftpcontent2.worldnow.com/wjrt/school/closings.htm").get();
            } catch (IOException e) {
                System.out.println(R.string.NoConnection);
            }

            System.out.println("Attempting to parse input file");
            int loopnum = 1; //for debugging purposes only
            for (Element row : schools.select("td[bgcolor]")) {
                System.out.println("Reading closings (" + (loopnum) + ")");
                orgName = orgName + "\n" + (row.select("font.orgname").first().text());
                status = status + "\n" + (row.select("font.status").first().text());
                loopnum++;
            }
            System.out.println("Loop exited.");

            System.out.println("Checking for null pointers...");
            if (orgName == null || status == null) {
                System.out.println("orgName or status is null.");
                schooltext = schools.text();
                //This shows in place of the table (as plain text) if no schools or institutions are closed.
                if (schooltext.contains("no active records")) {
                    System.out.println("No schools are closed.");
                    runOnUiThread(new Runnable() {
                        public void run() {
                            txtInfo.setText(txtInfo.getText() + "\n" + R.string.NoClosings);
                        }
                    });

                } else {
                    System.out.println("Webpage layout was not recognized.");
                    runOnUiThread(new Runnable() {
                        public void run() {
                            txtInfo.setText(txtInfo.getText() + "\n" + R.string.WJRTError + "\n" + R.string.ErrorContact);
                        }
                    });
                }
                orgName = "DummyLine1\nDummyLine2\nDummyLine3";
                status = "DummyLine1\nDummyLine2\nDummyLine3";
            }

            System.out.println("Splitting orgName and status strings by line break.");
            System.out.println("Saving to orgNameLine and statusLine.");
            System.out.println("This will create string arrays that can be parsed by for loops.");
            orgNameLine = orgName.split("\n");
            statusLine = status.split("\n");

            //The first test: School Closings!
            //Decide whether to check for today's closings or tomorrow's closings.
           if (dayrun == 0) {
                System.out.println("Will check closings for today.");
                checkClosingsToday();

           } else if (dayrun == 1) {
                System.out.println("Will check closings for tomorrow.");
                checkClosingsTomorrow();

            }

            //Sanity check - make sure GB isn't actually closed before predicting
            System.out.println("Checking to see if Grand Blanc is already closed.");
            checkGBClosed();

            return null;
        }

        protected void onPostExecute(Void result) {
            WJRTActive = false;
            System.out.println("WJRTScraper finished.");
        }

    }

    private void checkClosingsToday() {
        //Entire method must be run on UI thread to function.
        //It's just a big if/else in a for loop. It isn't as intensive as it looks.
        runOnUiThread(new Runnable() {
            public void run() {

                for (int i = 1; i < orgNameLine.length; i++) {
                    if (!(GBAcademy)) {
                        if (orgNameLine[i].contains("Grand Blanc Academy") && statusLine[i].contains("Closed Today")) {
                            txtGBAcademy.setText("Grand Blanc Academy: CLOSED");
                            //lstClosings.
                            txtGBAcademy.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtGBAcademy.setTextColor(Color.WHITE);
                            System.out.println("Closures Detected Correctly");
                            tier1++;
                            GBAcademy = true;
                        } else {
                            txtGBAcademy.setText("Grand Blanc Academy: OPEN");
                        }
                    }
                    if (!(GISD)) {
                        if (orgNameLine[i].contains("Genesee I.S.D.") && statusLine[i].contains("Closed Today")) {
                            txtGISD.setText("Genesee I.S.D.: CLOSED");
                            txtGISD.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtGISD.setTextColor(Color.WHITE);
                            tier1++;
                            GISD = true;
                        } else {
                            txtGISD.setText("Genesee I.S.D.: OPEN");
                        }
                    }
                    if (!(HolyFamily)) {
                        if (orgNameLine[i].contains("Holy Family") && statusLine[i].contains("Closed Today")) {
                            txtHolyFamily.setText("Holy Family: CLOSED");
                            txtHolyFamily.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtHolyFamily.setTextColor(Color.WHITE);
                            tier1++;
                            HolyFamily = true;
                        } else {
                            txtHolyFamily.setText("Holy Family: OPEN");
                        }
                    }
                    if (!(WPAcademy)) {
                        if (orgNameLine[i].contains("Woodland Park Academy") && statusLine[i].contains("Closed Today")) {
                            txtWPAcademy.setText("Woodland Park Academy: CLOSED");
                            txtWPAcademy.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtWPAcademy.setTextColor(Color.WHITE);
                            tier1++;
                            WPAcademy = true;
                        } else {
                            txtWPAcademy.setText("Woodland Park Academy: OPEN");
                        }
                    }
                    if (!(Durand)) {
                        if (orgNameLine[i].contains("Durand") && !orgNameLine[i].contains("Senior") && statusLine[i].contains("Closed Today")) {
                            txtDurand.setText("Durand: CLOSED");
                            txtDurand.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtDurand.setTextColor(Color.WHITE);
                            tier2++;
                            Durand = true;
                        } else {
                            txtDurand.setText("Durand: OPEN");
                        }
                    }
                    if (!(Holly)) {
                        if (orgNameLine[i].contains("Holly") && !orgNameLine[i].contains("Academy") && statusLine[i].contains("Closed Today")) {
                            txtHolly.setText("Holly: CLOSED");
                            txtHolly.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtHolly.setTextColor(Color.WHITE);
                            tier2++;
                            Holly = true;
                        } else {
                            txtHolly.setText("Holly: OPEN");
                        }
                    }
                    if (!(Lapeer)) {
                        if (orgNameLine[i].contains("Lapeer") && !orgNameLine[i].contains("Chatfield") && !orgNameLine[i].contains("Transit") && !orgNameLine[i].contains("CMH") && !orgNameLine[i].contains("Tech") && !orgNameLine[i].contains("Offices") && !orgNameLine[i].contains("Library") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Paul") && statusLine[i].contains("Closed Today")) {
                            txtLapeer.setText("Lapeer: CLOSED");
                            txtLapeer.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtLapeer.setTextColor(Color.WHITE);
                            tier2++;
                            Lapeer = true;
                        } else {
                            txtLapeer.setText("Lapeer: OPEN");
                        }
                    }
                    if (!(Owosso)) {
                        if (orgNameLine[i].contains("Owosso") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Baker") && !orgNameLine[i].contains("Paul") && statusLine[i].contains("Closed Today")) {
                            txtOwosso.setText("Owosso: CLOSED");
                            txtOwosso.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtOwosso.setTextColor(Color.WHITE);
                            tier2++;
                            Owosso = true;
                        } else {
                            txtOwosso.setText("Owosso: OPEN");
                        }
                    }
                    if (!(Beecher)) {
                        if (orgNameLine[i].contains("Beecher") && statusLine[i].contains("Closed Today")) {
                            txtBeecher.setText("Beecher: CLOSED");
                            txtBeecher.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtBeecher.setTextColor(Color.WHITE);
                            tier2++;
                            Beecher = true;
                        } else {
                            txtBeecher.setText("Beecher: OPEN");
                        }
                    }
                    if (!(Clio)) {
                        if (orgNameLine[i].contains("Clio") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("City") && !orgNameLine[i].contains("Cornerstone") && statusLine[i].contains("Closed Today")) {
                            txtClio.setText("Clio: CLOSED");
                            txtClio.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtClio.setTextColor(Color.WHITE);
                            tier3++;
                            Clio = true;
                        } else {
                            txtClio.setText("Clio: OPEN");
                        }
                    }
                    if (!(Davison)) {
                        if (orgNameLine[i].contains("Davison") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Faith") && !orgNameLine[i].contains("Montessori") && statusLine[i].contains("Closed Today")) {
                            txtDavison.setText("Davison: CLOSED");
                            txtDavison.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtDavison.setTextColor(Color.WHITE);
                            tier3++;
                            Davison = true;
                        } else {
                            txtDavison.setText("Davison: OPEN");
                        }
                    }
                    if (!(Fenton)) {
                        if (orgNameLine[i].contains("Fenton") && !orgNameLine[i].contains("Lake") && !orgNameLine[i].contains("City") && !orgNameLine[i].contains("Montessori") && statusLine[i].contains("Closed Today")) {
                            txtFenton.setText("Fenton: CLOSED");
                            txtFenton.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtFenton.setTextColor(Color.WHITE);
                            tier3++;
                            Fenton = true;
                        } else {
                            txtFenton.setText("Fenton: OPEN");
                        }
                    }
                    if (!(Flushing)) {
                        if (orgNameLine[i].contains("Flushing") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Robert") && statusLine[i].contains("Closed Today")) {
                            txtFlushing.setText("Flushing: CLOSED");
                            txtFlushing.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtFlushing.setTextColor(Color.WHITE);
                            tier3++;
                            Flushing = true;
                        } else {
                            txtFlushing.setText("Flushing: OPEN");
                        }
                    }
                    if (!(Genesee)) {
                        if (orgNameLine[i].contains("Genesee") && !orgNameLine[i].contains("Freedom") && !orgNameLine[i].contains("Christian") && !orgNameLine[i].contains("Mobile") && !orgNameLine[i].contains("Programs") && !orgNameLine[i].contains("Hlth") && !orgNameLine[i].contains("Sys") && !orgNameLine[i].contains("Stem") && !orgNameLine[i].contains("I.S.D.") && statusLine[i].contains("Closed Today")) {
                            txtGenesee.setText("Genesee: CLOSED");
                            txtGenesee.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtGenesee.setTextColor(Color.WHITE);
                            tier3++;
                            Genesee = true;
                        } else {
                            txtGenesee.setText("Genesee: OPEN");
                        }
                    }
                    if (!(Kearsley)) {
                        if (orgNameLine[i].contains("Kearsley") && statusLine[i].contains("Closed Today")) {
                            txtKearsley.setText("Kearsley: CLOSED");
                            txtKearsley.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtKearsley.setTextColor(Color.WHITE);
                            tier3++;
                            Kearsley = true;
                        } else {
                            txtKearsley.setText("Kearsley: OPEN");
                        }
                    }
                    if (!(LKFenton)) {
                        if (orgNameLine[i].contains("Lake Fenton") && statusLine[i].contains("Closed Today")) {
                            txtLKFenton.setText("Lake Fenton: CLOSED");
                            txtLKFenton.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtLKFenton.setTextColor(Color.WHITE);
                            tier3++;
                            LKFenton = true;
                        } else {
                            txtLKFenton.setText("Lake Fenton: OPEN");
                        }
                    }
                    if (!(Linden)) {
                        if (orgNameLine[i].contains("Linden") && !orgNameLine[i].contains("Charter") && statusLine[i].contains("Closed Today")) {
                            txtLinden.setText("Linden: CLOSED");
                            txtLinden.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtLinden.setTextColor(Color.WHITE);
                            tier3++;
                            Linden = true;
                        } else {
                            txtLinden.setText("Linden: OPEN");
                        }
                    }
                    if (!(Montrose)) {
                        if (orgNameLine[i].contains("Montrose") && !orgNameLine[i].contains("Senior") && statusLine[i].contains("Closed Today")) {
                            txtMontrose.setText("Montrose: CLOSED");
                            txtMontrose.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtMontrose.setTextColor(Color.WHITE);
                            tier3++;
                            Montrose = true;
                        } else {
                            txtMontrose.setText("Montrose: OPEN");
                        }
                    }
                    if (!(Morris)) {
                        if (orgNameLine[i].contains("Mt. Morris") && !orgNameLine[i].contains("Administration") && !orgNameLine[i].contains("Twp") && !orgNameLine[i].contains("Mary") && statusLine[i].contains("Closed Today")) {
                            txtMorris.setText("Mount Morris: CLOSED");
                            txtMorris.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtMorris.setTextColor(Color.WHITE);
                            tier3++;
                            Morris = true;
                        } else {
                            txtMorris.setText("Mount Morris: OPEN");
                        }
                    }
                    if (!(SzCreek)) {
                        if (orgNameLine[i].contains("Swartz Creek") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Montessori") && statusLine[i].contains("Closed Today")) {
                            txtSzCreek.setText("Swartz Creek: CLOSED");
                            txtSzCreek.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtSzCreek.setTextColor(Color.WHITE);
                            tier3++;
                            SzCreek = true;
                        } else {
                            txtSzCreek.setText("Swartz Creek: OPEN");
                        }
                    }
                    if (!(Atherton)) {
                        if (orgNameLine[i].contains("Atherton") && statusLine[i].contains("Closed Today")) {
                            txtAtherton.setText("Atherton: CLOSED");
                            txtAtherton.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtAtherton.setTextColor(Color.WHITE);
                            tier4++;
                            Atherton = true;
                        } else {
                            txtAtherton.setText("Atherton: OPEN");
                        }
                    }
                    if (!(Bendle)) {
                        if (orgNameLine[i].contains("Bendle") && statusLine[i].contains("Closed Today")) {
                            txtBendle.setText("Bendle: CLOSED");
                            txtBendle.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtBendle.setTextColor(Color.WHITE);
                            tier4++;
                            Bendle = true;
                        } else {
                            txtBendle.setText("Bendle: OPEN");
                        }
                    }
                    if (!(Bentley)) {
                        if (orgNameLine[i].contains("Bentley") && statusLine[i].contains("Closed Today")) {
                            txtBentley.setText("Bentley: CLOSED");
                            txtBentley.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtBentley.setTextColor(Color.WHITE);
                            tier4++;
                            Bentley = true;
                        } else {
                            txtBentley.setText("Bentley: OPEN");
                        }
                    }
                    if (!(Flint)) {
                        if (orgNameLine[i].contains("Flint Community Schools") && statusLine[i].contains("Closed Today")) {
                            txtFlint.setText("Flint: CLOSED");
                            txtFlint.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtFlint.setTextColor(Color.WHITE);
                            tier4++;
                            Flint = true;
                        } else {
                            txtFlint.setText("Flint: OPEN");
                        }
                    }
                    if (!(Goodrich)) {
                        if (orgNameLine[i].contains("Goodrich") && statusLine[i].contains("Closed Today")) {
                            txtGoodrich.setText("Goodrich: CLOSED");
                            txtGoodrich.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtGoodrich.setTextColor(Color.WHITE);
                            tier4++;
                            Goodrich = true;
                        } else {
                            txtGoodrich.setText("Goodrich: OPEN");
                        }
                    }
                    if (!(Carman)) {
                        if (orgNameLine[i].contains("Carman-Ainsworth") && !orgNameLine[i].contains("Senior") && statusLine[i].contains("Closed Today")) {
                            txtCarman.setText("Carman-Ainsworth: CLOSED");
                            txtCarman.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtCarman.setTextColor(Color.WHITE);
                            tier5++;
                            Carman = true;
                        } else {
                            txtCarman.setText("Carman-Ainsworth: OPEN");
                        }
                    }
                }

            }
        });
    }


    private void checkClosingsTomorrow() {
        //Entire method must be run on UI thread to function.
        //It's just a big if/else in a for loop. It isn't as intensive as it looks.
        runOnUiThread(new Runnable() {
            public void run() {

                for (int i = 1; i < orgNameLine.length; i++) {
                    if (!(GBAcademy)) {
                        if (orgNameLine[i].contains("Grand Blanc Academy") && statusLine[i].contains("Closed Tomorrow")) {
                            txtGBAcademy.setText("Grand Blanc Academy: CLOSED");
                            //lstClosings.
                            txtGBAcademy.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtGBAcademy.setTextColor(Color.WHITE);
                            System.out.println("Closures Detected Correctly");
                            tier1++;
                            GBAcademy = true;
                        } else {
                            txtGBAcademy.setText("Grand Blanc Academy: OPEN");
                        }
                    }
                    if (!(GISD)) {
                        if (orgNameLine[i].contains("Genesee I.S.D.") && statusLine[i].contains("Closed Tomorrow")) {
                            txtGISD.setText("Genesee I.S.D.: CLOSED");
                            txtGISD.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtGISD.setTextColor(Color.WHITE);
                            tier1++;
                            GISD = true;
                        } else {
                            txtGISD.setText("Genesee I.S.D.: OPEN");
                        }
                    }
                    if (!(HolyFamily)) {
                        if (orgNameLine[i].contains("Holy Family") && statusLine[i].contains("Closed Tomorrow")) {
                            txtHolyFamily.setText("Holy Family: CLOSED");
                            txtHolyFamily.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtHolyFamily.setTextColor(Color.WHITE);
                            tier1++;
                            HolyFamily = true;
                        } else {
                            txtHolyFamily.setText("Holy Family: OPEN");
                        }
                    }
                    if (!(WPAcademy)) {
                        if (orgNameLine[i].contains("Woodland Park Academy") && statusLine[i].contains("Closed Tomorrow")) {
                            txtWPAcademy.setText("Woodland Park Academy: CLOSED");
                            txtWPAcademy.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtWPAcademy.setTextColor(Color.WHITE);
                            tier1++;
                            WPAcademy = true;
                        } else {
                            txtWPAcademy.setText("Woodland Park Academy: OPEN");
                        }
                    }
                    if (!(Durand)) {
                        if (orgNameLine[i].contains("Durand") && !orgNameLine[i].contains("Senior") && statusLine[i].contains("Closed Tomorrow")) {
                            txtDurand.setText("Durand: CLOSED");
                            txtDurand.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtDurand.setTextColor(Color.WHITE);
                            tier2++;
                            Durand = true;
                        } else {
                            txtDurand.setText("Durand: OPEN");
                        }
                    }
                    if (!(Holly)) {
                        if (orgNameLine[i].contains("Holly") && !orgNameLine[i].contains("Academy") && statusLine[i].contains("Closed Tomorrow")) {
                            txtHolly.setText("Holly: CLOSED");
                            txtHolly.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtHolly.setTextColor(Color.WHITE);
                            tier2++;
                            Holly = true;
                        } else {
                            txtHolly.setText("Holly: OPEN");
                        }
                    }
                    if (!(Lapeer)) {
                        if (orgNameLine[i].contains("Lapeer") && !orgNameLine[i].contains("Chatfield") && !orgNameLine[i].contains("Transit") && !orgNameLine[i].contains("CMH") && !orgNameLine[i].contains("Tech") && !orgNameLine[i].contains("Offices") && !orgNameLine[i].contains("Library") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Paul") && statusLine[i].contains("Closed Tomorrow")) {
                            txtLapeer.setText("Lapeer: CLOSED");
                            txtLapeer.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtLapeer.setTextColor(Color.WHITE);
                            tier2++;
                            Lapeer = true;
                        } else {
                            txtLapeer.setText("Lapeer: OPEN");
                        }
                    }
                    if (!(Owosso)) {
                        if (orgNameLine[i].contains("Owosso") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Baker") && !orgNameLine[i].contains("Paul") && statusLine[i].contains("Closed Tomorrow")) {
                            txtOwosso.setText("Owosso: CLOSED");
                            txtOwosso.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtOwosso.setTextColor(Color.WHITE);
                            tier2++;
                            Owosso = true;
                        } else {
                            txtOwosso.setText("Owosso: OPEN");
                        }
                    }
                    if (!(Beecher)) {
                        if (orgNameLine[i].contains("Beecher") && statusLine[i].contains("Closed Tomorrow")) {
                            txtBeecher.setText("Beecher: CLOSED");
                            txtBeecher.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtBeecher.setTextColor(Color.WHITE);
                            tier2++;
                            Beecher = true;
                        } else {
                            txtBeecher.setText("Beecher: OPEN");
                        }
                    }
                    if (!(Clio)) {
                        if (orgNameLine[i].contains("Clio") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("City") && !orgNameLine[i].contains("Cornerstone") && statusLine[i].contains("Closed Tomorrow")) {
                            txtClio.setText("Clio: CLOSED");
                            txtClio.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtClio.setTextColor(Color.WHITE);
                            tier3++;
                            Clio = true;
                        } else {
                            txtClio.setText("Clio: OPEN");
                        }
                    }
                    if (!(Davison)) {
                        if (orgNameLine[i].contains("Davison") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Faith") && !orgNameLine[i].contains("Montessori") && statusLine[i].contains("Closed Tomorrow")) {
                            txtDavison.setText("Davison: CLOSED");
                            txtDavison.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtDavison.setTextColor(Color.WHITE);
                            tier3++;
                            Davison = true;
                        } else {
                            txtDavison.setText("Davison: OPEN");
                        }
                    }
                    if (!(Fenton)) {
                        if (orgNameLine[i].contains("Fenton") && !orgNameLine[i].contains("Lake") && !orgNameLine[i].contains("City") && !orgNameLine[i].contains("Montessori") && statusLine[i].contains("Closed Tomorrow")) {
                            txtFenton.setText("Fenton: CLOSED");
                            txtFenton.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtFenton.setTextColor(Color.WHITE);
                            tier3++;
                            Fenton = true;
                        } else {
                            txtFenton.setText("Fenton: OPEN");
                        }
                    }
                    if (!(Flushing)) {
                        if (orgNameLine[i].contains("Flushing") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Robert") && statusLine[i].contains("Closed Tomorrow")) {
                            txtFlushing.setText("Flushing: CLOSED");
                            txtFlushing.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtFlushing.setTextColor(Color.WHITE);
                            tier3++;
                            Flushing = true;
                        } else {
                            txtFlushing.setText("Flushing: OPEN");
                        }
                    }
                    if (!(Genesee)) {
                        if (orgNameLine[i].contains("Genesee") && !orgNameLine[i].contains("Freedom") && !orgNameLine[i].contains("Christian") && !orgNameLine[i].contains("Mobile") && !orgNameLine[i].contains("Programs") && !orgNameLine[i].contains("Hlth") && !orgNameLine[i].contains("Sys") && !orgNameLine[i].contains("Stem") && !orgNameLine[i].contains("I.S.D.") && statusLine[i].contains("Closed Tomorrow")) {
                            txtGenesee.setText("Genesee: CLOSED");
                            txtGenesee.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtGenesee.setTextColor(Color.WHITE);
                            tier3++;
                            Genesee = true;
                        } else {
                            txtGenesee.setText("Genesee: OPEN");
                        }
                    }
                    if (!(Kearsley)) {
                        if (orgNameLine[i].contains("Kearsley") && statusLine[i].contains("Closed Tomorrow")) {
                            txtKearsley.setText("Kearsley: CLOSED");
                            txtKearsley.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtKearsley.setTextColor(Color.WHITE);
                            tier3++;
                            Kearsley = true;
                        } else {
                            txtKearsley.setText("Kearsley: OPEN");
                        }
                    }
                    if (!(LKFenton)) {
                        if (orgNameLine[i].contains("Lake Fenton") && statusLine[i].contains("Closed Tomorrow")) {
                            txtLKFenton.setText("Lake Fenton: CLOSED");
                            txtLKFenton.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtLKFenton.setTextColor(Color.WHITE);
                            tier3++;
                            LKFenton = true;
                        } else {
                            txtLKFenton.setText("Lake Fenton: OPEN");
                        }
                    }
                    if (!(Linden)) {
                        if (orgNameLine[i].contains("Linden") && !orgNameLine[i].contains("Charter") && statusLine[i].contains("Closed Tomorrow")) {
                            txtLinden.setText("Linden: CLOSED");
                            txtLinden.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtLinden.setTextColor(Color.WHITE);
                            tier3++;
                            Linden = true;
                        } else {
                            txtLinden.setText("Linden: OPEN");
                        }
                    }
                    if (!(Montrose)) {
                        if (orgNameLine[i].contains("Montrose") && !orgNameLine[i].contains("Senior") && statusLine[i].contains("Closed Tomorrow")) {
                            txtMontrose.setText("Montrose: CLOSED");
                            txtMontrose.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtMontrose.setTextColor(Color.WHITE);
                            tier3++;
                            Montrose = true;
                        } else {
                            txtMontrose.setText("Montrose: OPEN");
                        }
                    }
                    if (!(Morris)) {
                        if (orgNameLine[i].contains("Mt. Morris") && !orgNameLine[i].contains("Administration") && !orgNameLine[i].contains("Twp") && !orgNameLine[i].contains("Mary") && statusLine[i].contains("Closed Tomorrow")) {
                            txtMorris.setText("Mount Morris: CLOSED");
                            txtMorris.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtMorris.setTextColor(Color.WHITE);
                            tier3++;
                            Morris = true;
                        } else {
                            txtMorris.setText("Mount Morris: OPEN");
                        }
                    }
                    if (!(SzCreek)) {
                        if (orgNameLine[i].contains("Swartz Creek") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Montessori") && statusLine[i].contains("Closed Tomorrow")) {
                            txtSzCreek.setText("Swartz Creek: CLOSED");
                            txtSzCreek.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtSzCreek.setTextColor(Color.WHITE);
                            tier3++;
                            SzCreek = true;
                        } else {
                            txtSzCreek.setText("Swartz Creek: OPEN");
                        }
                    }
                    if (!(Atherton)) {
                        if (orgNameLine[i].contains("Atherton") && statusLine[i].contains("Closed Tomorrow")) {
                            txtAtherton.setText("Atherton: CLOSED");
                            txtAtherton.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtAtherton.setTextColor(Color.WHITE);
                            tier4++;
                            Atherton = true;
                        } else {
                            txtAtherton.setText("Atherton: OPEN");
                        }
                    }
                    if (!(Bendle)) {
                        if (orgNameLine[i].contains("Bendle") && statusLine[i].contains("Closed Tomorrow")) {
                            txtBendle.setText("Bendle: CLOSED");
                            txtBendle.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtBendle.setTextColor(Color.WHITE);
                            tier4++;
                            Bendle = true;
                        } else {
                            txtBendle.setText("Bendle: OPEN");
                        }
                    }
                    if (!(Bentley)) {
                        if (orgNameLine[i].contains("Bentley") && statusLine[i].contains("Closed Tomorrow")) {
                            txtBentley.setText("Bentley: CLOSED");
                            txtBentley.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtBentley.setTextColor(Color.WHITE);
                            tier4++;
                            Bentley = true;
                        } else {
                            txtBentley.setText("Bentley: OPEN");
                        }
                    }
                    if (!(Flint)) {
                        if (orgNameLine[i].contains("Flint Community Schools") && statusLine[i].contains("Closed Tomorrow")) {
                            txtFlint.setText("Flint: CLOSED");
                            txtFlint.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtFlint.setTextColor(Color.WHITE);
                            tier4++;
                            Flint = true;
                        } else {
                            txtFlint.setText("Flint: OPEN");
                        }
                    }
                    if (!(Goodrich)) {
                        if (orgNameLine[i].contains("Goodrich") && statusLine[i].contains("Closed Tomorrow")) {
                            txtGoodrich.setText("Goodrich: CLOSED");
                            txtGoodrich.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtGoodrich.setTextColor(Color.WHITE);
                            tier4++;
                            Goodrich = true;
                        } else {
                            txtGoodrich.setText("Goodrich: OPEN");
                        }
                    }
                    if (!(Carman)) {
                        if (orgNameLine[i].contains("Carman-Ainsworth") && !orgNameLine[i].contains("Senior") && statusLine[i].contains("Closed Tomorrow")) {
                            txtCarman.setText("Carman-Ainsworth: CLOSED");
                            txtCarman.setBackgroundColor(Color.rgb(255, 165, 0));
                            txtCarman.setTextColor(Color.WHITE);
                            tier5++;
                            Carman = true;
                        } else {
                            txtCarman.setText("Carman-Ainsworth: OPEN");
                        }
                    }
                }

            }
        });
    }


    private void checkGBClosed() {
        int gbnum = 0; //For debugging purposes only
                System.out.println("Checking if GB is closed.");
                for (int i = 1; i < orgNameLine.length; i++) {
                    if (!GB) {
                        System.out.println("GB is false.");
                        if (orgNameLine[i].contains("Grand Blanc") && !orgNameLine[i].contains("Academy") && !orgNameLine[i].contains("Freedom") && !orgNameLine[i].contains("Offices") && !orgNameLine[i].contains("City") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Holy") && statusLine[i].contains("Closed Today") && dayrun == 0) {
                            runOnUiThread(new Runnable() {
                                  public void run() {
                                      txtInfo.setText(txtInfo.getText() + "\nGrand Blanc is Closed Today! \nEnjoy your Snow Day!");
                                      txtGB.setText("Grand Blanc: CLOSED");
                                      txtGB.setBackgroundColor(Color.RED);
                                      percent = 100;
                                      txtPercent.setText(percent + "%");
                                  }
                            });
                            GB = true;
                            System.out.println("GB Found (today)!");
                            break;
                        } else if (orgNameLine[i].contains("Grand Blanc") && !orgNameLine[i].contains("Academy") && !orgNameLine[i].contains("Freedom") && !orgNameLine[i].contains("Offices") && !orgNameLine[i].contains("City") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Holy") && statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    txtInfo.setText(txtInfo.getText() + "\nGrand Blanc is Closed Tomorrow! \nEnjoy your Snow Day!");
                                    txtGB.setText("Grand Blanc: CLOSED");
                                    txtGB.setBackgroundColor(Color.RED);
                                    percent = 100;
                                    txtPercent.setText(percent + "%");
                                }
                            });
                            GB = true;
                            System.out.println("GB Found (tomorrow)!");
                            break;
                        } else {
                            System.out.println("Didn't find GB yet (" + (gbnum + 1) + ")");
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    txtGB.setText("Grand Blanc: OPEN");
                                    txtGB.setBackgroundColor(Color.rgb(55, 60, 65));
                                }
                            });
                            GB = false;
                            gbnum++;
                        }

                    }
                }
                System.out.println("Loop exited.");
            }

    private class WeatherScraper extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... nothing) {
            System.out.println("Weather scraper started.");
            /**NATIONAL WEATHER SERVICE SCRAPER**/
            //Change the percentage based on current storm/wind/temperature warnings.

            Document weatherdoc = null;

            //Live html
            try {
                weatherdoc = Jsoup.connect("http://forecast.weather.gov/afm/PointClick.php?lat=42.92580&lon=-83.61870").get();
            } catch (IOException ex) {
                txtInfo.setText(txtInfo.getText() + "\nCould not retrieve weather information. \nAre you connected to the internet?");
            }

            System.out.println("Searching for elements in class 'warn'");
            Elements weatherWarn = weatherdoc.getElementsByClass("warn");
            System.out.println("Saving elements to searchable string weathertext");
            weathertext = weatherWarn.toString();

            if (weathertext.equals("")) {
                System.out.println("weathertext is empty.");
                try {
                    System.out.println("Searching for element 'hazards_content'.");
                    System.out.println("This element should always be present even if no hazards are present.");
                    Element weatherNull = weatherdoc.getElementById("hazards_content");
                    weathercheck = weatherNull.toString();
                    if (weathercheck.contains("No Hazards in Effect")) {
                        System.out.println("Webpage parsed correctly: no hazards present.");
                        txtWeather.setText(R.string.NoWeather);
                    }
                } catch (NullPointerException e) {
                    System.out.println("Something has changed in the webpage and can't be parsed.");
                    txtWeather.setText(R.string.WeatherError + R.string.ErrorContact);
                }
            } else {
                System.out.println("Hazards found.");
                //Use the data
                getWeather();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            NWSActive = false;
            System.out.println("WeatherScraper finished.");
        }
    }

    private void getWeather() {
        runOnUiThread(new Runnable() {
            public void run() {
                System.out.println("Running getWeather()");
                //Only the highest weatherpercent is stored (not cumulative)

                if (weathertext.contains("Significant Weather Advisory")) {
                    System.out.println("Significant Weather Advisory - 15% weatherpercent");
                    if (!nullWeather) {
                        txtWeather.setText(txtWeather.getText() + "\nA Significant Weather Advisory is in effect.");
                    } else {
                        txtWeather.setText("A Significant Weather Advisory is in effect.");
                        nullWeather = false;
                    }
                    weatherpercent = 15;
                }
                if (weathertext.contains("Winter Weather Advisory")) {
                    System.out.println("Winter Weather Advisory - 30% weatherpercent");
                    if (!nullWeather) {
                        txtWeather.setText(txtWeather.getText() + "\nA Winter Weather Advisory is in effect.");
                    } else {
                        txtWeather.setText("A Winter Weather Advisory is in effect.");
                        nullWeather = false;
                    }
                    weatherpercent = 30;
                }
                if (weathertext.contains("Winter Storm Watch")) {
                    System.out.println("Winter Storm Watch - 40% weatherpercent");
                    if (!nullWeather) {
                        txtWeather.setText(txtWeather.getText() + "\nA Winter Storm Watch is in effect.");
                    } else {
                        txtWeather.setText("A Winter Storm Watch is in effect.");
                        nullWeather = false;
                    }
                    weatherpercent = 40;
                }
                if (weathertext.contains("Lake-Effect Snow Advisory") || weathertext.contains("Lake-Effect Snow Watch")) {
                    System.out.println("Lake Effect Snow Advisory / Watch - 40% weatherpercent");
                    if (!nullWeather) {
                        txtWeather.setText(txtWeather.getText() + "\nA Lake-Effect Snow Advisory / Watch is in effect.");
                    } else {
                        txtWeather.setText("A Lake-Effect Snow Advisory / Watch is in effect.");
                        nullWeather = false;
                    }
                    weatherpercent = 40;
                }
                if (weathertext.contains("Freezing Rain Advisory") || weathertext.contains("Freezing Drizzle Advisory") || weathertext.contains("Freezing Fog Advisory")) {
                    System.out.println("Freezing Rain - 40% weatherpercent");
                    if (!nullWeather) {
                        txtWeather.setText(txtWeather.getText() + "\nA Freezing Rain / Drizzle / Fog Advisory is in effect.");
                    } else {
                        txtWeather.setText("A Freezing Rain / Drizzle / Fog Advisory is in effect.");
                        nullWeather = false;
                    }
                    weatherpercent = 40;
                }
                if (weathertext.contains("Wind Chill Advisory")) {
                    System.out.println("Wind Chill Advisory - 40% weatherpercent");
                    if (!nullWeather) {
                        txtWeather.setText(txtWeather.getText() + "\nA Wind Chill Advisory is in effect.");
                    } else {
                        txtWeather.setText("A Wind Chill Advisory is in effect.");
                        nullWeather = false;
                    }
                    weatherpercent = 40;
                }

                if (weathertext.contains("Wind Chill Watch")) {
                    System.out.println("Wind Chill Watch - 40% weatherpercent");
                    if (!nullWeather) {
                        txtWeather.setText(txtWeather.getText() + "\nA Wind Chill Watch is in effect.");
                    } else {
                        txtWeather.setText("A Wind Chill Watch is in effect.");
                        nullWeather = false;
                    }
                    weatherpercent = 40;
                }
                if (weathertext.contains("Blizzard Watch")) {
                    System.out.println("Blizzard Watch - 40% weatherpercent");
                    if (!nullWeather) {
                        txtWeather.setText(txtWeather.getText() + "\nA Blizzard Watch is in effect.");
                    } else {
                        txtWeather.setText("A Blizzard Watch is in effect.");
                    }
                    nullWeather = false;
                    weatherpercent = 40;
                }
                if (weathertext.contains("Winter Storm Warning")) {
                    System.out.println("Winter Storm Warning - 60% weatherpercent");
                    if (!nullWeather) {
                        txtWeather.setText(txtWeather.getText() + "\nA Winter Storm Warning is in effect.");
                    } else {
                        txtWeather.setText("A Winter Storm Warning is in effect.");
                        nullWeather = false;
                    }
                    weatherpercent = 60;
                }
                if (weathertext.contains("Lake-Effect Snow Warning")) {
                    System.out.println("Lake Effect Snow Warning - 70% weatherpercent");
                    if (!nullWeather) {
                        txtWeather.setText(txtWeather.getText() + "\nA Lake-Effect Snow Warning is in effect.");
                    } else {
                        txtWeather.setText("A Lake-Effect Snow Warning is in effect.");
                        nullWeather = false;
                    }
                    weatherpercent = 70;
                }
                if (weathertext.contains("Ice Storm Warning")) {
                    System.out.println("Ice Storm Warning - 70% weatherpercent");
                    if (!nullWeather) {
                        txtWeather.setText(txtWeather.getText() + "\nAn Ice Storm Warning is in effect.");
                    } else {
                        txtWeather.setText("An Ice Storm Warning is in effect.");
                        nullWeather = false;
                    }
                    weatherpercent = 70;
                }
                if (weathertext.contains("Wind Chill Warning")) {
                    System.out.println("Wind Chill Warning - 75% weatherpercent");
                    if (!nullWeather) {
                        txtWeather.setText(txtWeather.getText() + "\nA Wind Chill Warning is in effect.");
                    } else {
                        txtWeather.setText("A Wind Chill Warning is in effect.");
                        nullWeather = false;
                    }
                    weatherpercent = 75;
                }
                if (weathertext.contains("Blizzard Warning")) {
                    System.out.println("Blizzard Warning - 75% weatherpercent");
                    if (!nullWeather) {
                        txtWeather.setText(txtWeather.getText() + "\nA Blizzard Warning is in effect.");
                    } else {
                        txtWeather.setText("A Blizzard Warning is in effect.");
                        nullWeather = false;
                    }
                    weatherpercent = 75;
                }
            }
        });
    }

    private class PercentCalculate extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... nothing) {
            System.out.println("Starting PercentCalculate");

            //Give the scrapers time to act before displaying the percent

            //Sleep for 1000 ms - if the while loop is run *too* soon a scraper might not have
            //a chance to start before being considered 'done'

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (WJRTActive || NWSActive) {
                try {
                    System.out.println("Waiting for scrapers to finish...");
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            System.out.println("Both scrapers have finished.");


            if (tier5 == 1) {
                System.out.println("Carman-Ainsworth was closed. We'll close. 90% schoolpercent");
                schoolpercent += 90;
            } else if (tier4 > 2) {
                System.out.println("3+ schools near GB were closed. 80% schoolpercent");
                schoolpercent += 80;
            } else if (tier3 > 2) {
                System.out.println("3+ schools in Genesee County were closed. 60% schoolpercent");
                schoolpercent += 60;
            } else if (tier2 > 2) {
                System.out.println("3+ schools in nearby counties were closed. 40% schoolpercent");
                schoolpercent += 40;
            } else if (tier1  > 2) {
                System.out.println("3+ academies were closed. 20% schoolpercent");
                schoolpercent += 20;
            }

            //Calculate the total percent.

            if (weatherpercent > schoolpercent) {
                percent = weatherpercent;
            }else if (schoolpercent > weatherpercent) {
                percent = schoolpercent;
            }

            System.out.println("schoolpercent: " + schoolpercent);
            System.out.println("weatherpercent: " + weatherpercent);
            System.out.println("Final percent is " + percent);

            //Reduce the percent chance by three for every snow day entered.
            System.out.println("Percent will be reduced by 3% for every snow day that has already occurred.");
            percent -= (days * 3);
            //No negative percents.
            if (percent < 0) {
                System.out.println("Percent became negative. Setting to 0%.");
                percent = 0;
            }

            //Don't allow a chance above 90%.
            if (percent > 90) {
                System.out.println("Percent (somehow) was above 90%. Capping at 90%.");
                percent = 90;
            }

            //Negate the above results for special cases
            if (GB) {
                System.out.println("WJRTScraper reports Grand Blanc is closed. Overriding percentage, setting to 100%");
                percent = 100;
            }

            percentscroll=0;

            System.out.println("Enjoy this cool little animation.");
            runOnUiThread(new Runnable() {
                  public void run() {
                      
                      //Show all hidden views
                      showViews();
                      txtPercent.setText("0%");
                      txtPercent.setVisibility(View.VISIBLE);
                      txtInfo.setVisibility(View.VISIBLE);
                      progCalculate.setVisibility(View.GONE);
                  }
            });

            //Animate txtPercent

            try

            {
                for (int i = 0; i < percent; i++) {
                    Thread.sleep(10);
                    if (percentscroll >= 0 && percentscroll <= 20) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                txtPercent.setTextColor(Color.RED);
                            }
                        });
                    } else if (percentscroll > 20 && percentscroll <= 60) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                txtPercent.setTextColor(Color.rgb(255, 165, 0));
                            }
                        });
                    } else if (percentscroll > 60 && percentscroll <= 80) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                txtPercent.setTextColor(Color.GREEN);
                            }
                        });
                    } else if (percentscroll > 80) {
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
            }

            catch(
            InterruptedException ex
            )

            {
                ex.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void Result) {
            System.out.println("Program Completed. We made it!");  
        }
    }
    
    private void showViews() {
        txtGBAcademy.setVisibility(View.VISIBLE);
        
        txtGISD.setVisibility(View.VISIBLE);
        
        txtHolyFamily.setVisibility(View.VISIBLE);
        
        txtWPAcademy.setVisibility(View.VISIBLE);
       
        txtBeecher.setVisibility(View.VISIBLE);
       
        txtClio.setVisibility(View.VISIBLE);
       
        txtDavison.setVisibility(View.VISIBLE);
        
        txtFenton.setVisibility(View.VISIBLE);
        
        txtFlushing.setVisibility(View.VISIBLE);
        
        txtGenesee.setVisibility(View.VISIBLE);
        
        txtKearsley.setVisibility(View.VISIBLE);
        
        txtLKFenton.setVisibility(View.VISIBLE);
        
        txtLinden.setVisibility(View.VISIBLE);
        
        txtMontrose.setVisibility(View.VISIBLE);
       
        txtMorris.setVisibility(View.VISIBLE);
        
        txtSzCreek.setVisibility(View.VISIBLE);
        
        txtAtherton.setVisibility(View.VISIBLE);
       
        txtBendle.setVisibility(View.VISIBLE);

        txtBentley.setVisibility(View.VISIBLE);
        
        txtFlint.setVisibility(View.VISIBLE);
        
        txtGoodrich.setVisibility(View.VISIBLE);
        
        txtDurand.setVisibility(View.VISIBLE);
       
        txtHolly.setVisibility(View.VISIBLE);
       
        txtLapeer.setVisibility(View.VISIBLE);
        
        txtOwosso.setVisibility(View.VISIBLE);
        
        txtCarman.setVisibility(View.VISIBLE);

        txtGB.setVisibility(View.VISIBLE);

        txtTier1.setVisibility(View.VISIBLE);
        txtTier2.setVisibility(View.VISIBLE);
        txtTier3.setVisibility(View.VISIBLE);
        txtTier4.setVisibility(View.VISIBLE);

        txtWeather.setVisibility(View.VISIBLE);
        btnRadar.setVisibility(View.VISIBLE);
    }
}







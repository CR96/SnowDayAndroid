package com.GBSnowDay.SnowDay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SnowDay extends Activity {

    //Declare all views
    ProgressBar progCalculate;
    RadioButton optToday;
    RadioButton optTomorrow;
    TextView txtInfo;
    Spinner lstDays;
    Button btnCalculate;

    TextView txtGB;
    TextView txtCarman;
    TextView txtAtherton;
    TextView txtBendle;
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

    TextView txtPercent;
    TextView txtWeather;

    //Declare variables
    public String orgName;
    public String status;
    public String hazardName;
    public String schooltext;
    public String weathertext;
    public String weathercheck;
    public String today;
    public String tomorrow;
    public Date date;
    public Format formatter;

    public String[] orgNameLine;
    public String[] statusLine;

    public int days;
    public int schoolpercent = 0;
    public int weatherpercent = 0;
    public int percent;
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

    

    //Figure out what tomorrow is
    //Saturday = 0, Sunday = 1
    Calendar calendar = Calendar.getInstance();
    int weekday = calendar.get(Calendar.DAY_OF_WEEK);
    int month = calendar.get(Calendar.MONTH);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snow_day);

        //Declare views
        progCalculate = (ProgressBar) findViewById(R.id.progCalculate);
        optToday = (RadioButton) findViewById(R.id.optToday);
        optTomorrow = (RadioButton) findViewById(R.id.optTomorrow);
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        lstDays = (Spinner) findViewById(R.id.lstDays);
        btnCalculate = (Button) findViewById(R.id.btnCalculate);

        txtGB = (TextView) findViewById(R.id.txtGB);
        txtCarman = (TextView) findViewById(R.id.txtCarman);
        txtAtherton = (TextView) findViewById(R.id.txtAtherton);
        txtBendle = (TextView) findViewById(R.id.txtBendle);
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

        txtPercent = (TextView) findViewById(R.id.txtPercent);
        txtWeather = (TextView) findViewById(R.id.txtWeather);

        //Make sure the user doesn't try to run the program on the weekend or during school hours
        //checkWeekend();
        //checkTime();

        //Listen for button click
        btnCalculate.setEnabled(true);
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                progCalculate.setVisibility(View.VISIBLE);
                new Calculate();
                //Switch to SnowDayResult activity
                Intent i = new Intent(getApplicationContext(), SnowDayResult.class);
                startActivity(i);
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public class Calculate extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... nothing) {
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

            //Date setup
            if (optToday.isChecked()) {
                dayrun = 0;
            } else if (optTomorrow.isChecked()) {
                dayrun = 1;
            }

            date = calendar.getTime();
            formatter = new SimpleDateFormat("MMM dd yyyy");
            today = formatter.format(date);
            calendar.add(Calendar.DATE, 1);
            date = calendar.getTime();
            formatter = new SimpleDateFormat("MMM dd yyyy");
            tomorrow = formatter.format(date);

            //Set calculation to today or tomorrow
            if (dayrun == 0) {
                txtInfo.setText(txtInfo.getText() + "\n" + getString(R.string.DayRun) + " " + getString(R.string.today));
            } else if (dayrun == 1) {
                txtInfo.setText(txtInfo.getText() + "\n" + getString(R.string.DayRun) + " " + getString(R.string.tomorrow));

            }

            /**WJRT SCHOOL CLOSINGS SCRAPER**/
            WJRTScraper();

            //The first test: School Closings!
            //Decide whether to check for today's closings or tomorrow's closings.

            if(dayrun==0) {
                checkClosingsToday();
            }else if(dayrun==1) {
                checkClosingsTomorrow();
            }

            //Sanity check - make sure GB isn't actually closed before predicting
            checkGBClosed();

            //Have the user input past snow days
            days = lstDays.getSelectedItemPosition() - 1;

            //Next test: Weather!

            /**NATIONAL WEATHER SERVICE SCRAPER**/
            WeatherScraper();

            //Do the math
            PercentCalculate();

            if (dayrun == 1) {
                calendar.add(Calendar.DATE, -1);
            }
            //optToday.setEnabled(false);
            //optTomorrow.setEnabled(false);
            //lstDays.setEnabled(false);

            return null;
        }
    }

    private void Reset() {

        //Reset variables
        today = "";
        tomorrow = "";
        schoolpercent = 0;
        weatherpercent = 0;
        percent = 0;
        tier1 = 0;
        tier2 = 0;
        tier3 = 0;
        tier4 = 0;
        tier5 = 0;

        schoolNull = false;

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
        txtInfo.setText("");
        txtWeather.setText("");
        nullWeather = true;
        txtGBAcademy.setText("");
        txtGBAcademy.setBackgroundColor(Color.WHITE);
        txtGISD.setText("");
        txtGISD.setBackgroundColor(Color.WHITE);
        txtHolyFamily.setText("");
        txtHolyFamily.setBackgroundColor(Color.WHITE);
        txtWPAcademy.setText("");
        txtWPAcademy.setBackgroundColor(Color.WHITE);
        txtDurand.setText("");
        txtDurand.setBackgroundColor(Color.WHITE);
        txtBeecher.setText("");
        txtBeecher.setBackgroundColor(Color.WHITE);
        txtClio.setText("");
        txtClio.setBackgroundColor(Color.WHITE);
        txtDavison.setText("");
        txtDavison.setBackgroundColor(Color.WHITE);
        txtFenton.setText("");
        txtFenton.setBackgroundColor(Color.WHITE);
        txtFlushing.setText("");
        txtFlushing.setBackgroundColor(Color.WHITE);
        txtGenesee.setText("");
        txtGenesee.setBackgroundColor(Color.WHITE);
        txtKearsley.setText("");
        txtKearsley.setBackgroundColor(Color.WHITE);
        txtLKFenton.setText("");
        txtLKFenton.setBackgroundColor(Color.WHITE);
        txtLinden.setText("");
        txtLinden.setBackgroundColor(Color.WHITE);
        txtMontrose.setText("");
        txtMontrose.setBackgroundColor(Color.WHITE);
        txtMorris.setText("");
        txtMorris.setBackgroundColor(Color.WHITE);
        txtSzCreek.setText("");
        txtSzCreek.setBackgroundColor(Color.WHITE);
        txtAtherton.setText("");
        txtAtherton.setBackgroundColor(Color.WHITE);
        txtDurand.setText("");
        txtDurand.setBackgroundColor(Color.WHITE);
        txtHolly.setText("");
        txtHolly.setBackgroundColor(Color.WHITE);
        txtLapeer.setText("");
        txtLapeer.setBackgroundColor(Color.WHITE);
        txtOwosso.setText("");
        txtOwosso.setBackgroundColor(Color.WHITE);
        txtBendle.setText("");
        txtBendle.setBackgroundColor(Color.WHITE);
        txtFlint.setText("");
        txtFlint.setBackgroundColor(Color.WHITE);
        txtGoodrich.setText("");
        txtGoodrich.setBackgroundColor(Color.WHITE);
        txtCarman.setText("");
        txtCarman.setBackgroundColor(Color.WHITE);
        txtGB.setText("");
        txtGB.setBackgroundColor(Color.WHITE);
    }

    private void checkWeekend() {

        //Friday is 6
        //Saturday is 7
        //Sunday is 1

        if (weekday == 6) {
            txtInfo.setText(this.getString(R.string.SaturdayTomorrow));
            optTomorrow.setEnabled(false);
            optToday.setChecked(true);
        } else if (weekday == 7) {
            txtInfo.setText(this.getString(R.string.SaturdayToday));
            optToday.setEnabled(false);
            optTomorrow.setEnabled(false);
            lstDays.setEnabled(false);
        } else if (weekday == 1) {
            txtInfo.setText(this.getString(R.string.SundayToday));
            optToday.setEnabled(false);
            optTomorrow.setChecked(true);
        }
    }

    private void checkTime() {

        if (calendar.get(Calendar.HOUR_OF_DAY) >= 7 && calendar.get(Calendar.HOUR_OF_DAY) < 14 && weekday != 7 && weekday != 1) {
            optToday.setEnabled(false);
            //txtGB.setText("Grand Blanc: OPEN");
            txtInfo.setText(txtInfo.getText() + this.getString(R.string.SchoolOpen));
            dayrun = 1;
        } else if (calendar.get(Calendar.HOUR_OF_DAY) >= 14 && weekday != 7 && weekday != 1) {
            optToday.setEnabled(false);
            //txtGB.setText("Grand Blanc: Dismissed");
            //txtGB.(Color.YELLOW);
            txtInfo.setText(txtInfo.getText() + this.getString(R.string.GBDismissed));
            dayrun = 1;
        }
    }

    private void WJRTScraper() {
        //Scrape School Closings from WJRT with Jsoup.
        //Run scraper in an Async task.

        /**WJRT SCHOOL CLOSINGS SCRAPER**/
        //Scrape School Closings from WJRT with Jsoup.
        //The following is a rigged archive from January 5th - every school referenced by this program was closed the following day.

            /*Document schools = null;
            try {
                File input = new File("mnt/sdcard/Closings.htm");
                schools = Jsoup.parse(input, "UTF-8", "");
            }catch (IOException e) {
                TextView txtInfo = (TextView) findViewById(R.id.txtInfo);
                txtInfo.setText(txtInfo.getText() + getString(R.string.NoConnection));
                e.printStackTrace();
            }*/

        //This is a second rigged archive from December 23rd - Swartz Creek and Kearsley were closed on the day for reference.

            /*Document schools = null;
            try {
                File input = new File("mnt/sdcard/ClosingsToday.htm");
                schools = Jsoup.parse(input, "UTF-8", "");
            }catch (IOException e) {
                TextView txtInfo = (TextView) findViewById(R.id.txtInfo);
                txtInfo.setText(txtInfo.getText() + getString(R.string.NoConnection));
                e.printStackTrace();
            }*/

        //This third document tests for false triggers, e.g. "Owosso" shouldn't show as "closed" if only "Owosso Senior Center" is closed.
        //This document will not trigger any closings if the code is working properly.

           /* Document schools = null;
            try {
                File input = new File("mnt/sdcard/Trials.htm");
                schools = Jsoup.parse(input, "UTF-8", "");
            }catch (IOException e) {
                TextView txtInfo = (TextView) findViewById(R.id.txtInfo);
                txtInfo.setText(txtInfo.getText() + getString(R.string.NoConnection));
                e.printStackTrace();
            }*/

        //Fourth html archive - every school except GB is closed (shouldn't trigger 100%)

            Document schools = null;
            try {
                File input = new File("mnt/sdcard/GBNotClosed.htm");
                schools = Jsoup.parse(input, "UTF-8", "");
            } catch (IOException e) {
                TextView txtInfo = (TextView) findViewById(R.id.txtInfo);
                txtInfo.setText(getString(R.string.NoConnection));
                e.printStackTrace();
            }

        //This is a blank example (no active records) - check how the program runs when nullpointerexception is thrown

        /*Document schools = null;
        try {
            File input = new File("mnt/sdcard/Blank.htm");
            schools = Jsoup.parse(input, "UTF-8", "");
        }catch (IOException e) {
            TextView txtInfo = (TextView) findViewById(R.id.txtInfo);
            txtInfo.setText(txtInfo.getText() + getString(R.string.NoConnection));
            e.printStackTrace();
        }*/

        //This is the current listings page.

            /*Document schools = null;
            try {
                schools = Jsoup.connect("http://ftpcontent2.worldnow.com/wjrt/school/closings.htm").get();
            }catch (IOException e) {
                System.out.println(R.string.NoConnection);
            }*/

        for (Element row : schools.select("td[bgcolor]")) {
            orgName = orgName + "\n" + (row.select("font.orgname").first().text());
            status = status + "\n" + (row.select("font.status").first().text());
        }

        if (orgName == null || status == null) {
            schooltext = schools.text();
            //This shows in place of the table (as plain text) if no schools or institutions are closed.
            if (schooltext.contains("no active records")) {
                txtInfo.setText(txtInfo.getText() + "\n" + R.string.NoClosings);
            } else {
                txtInfo.setText(txtInfo.getText() + "\n" + R.string.WJRTError + "\n" + R.string.ErrorContact);
            }

        }

        orgName = "DummyLine1\nDummyLine2\nDummyLine3";
        status = "DummyLine1\nDummyLine2\nDummyLine3";

        orgNameLine = orgName.split("\n");
        statusLine = status.split("\n");

    }

    private void checkGBClosed() {

        for (int i = 1; i < orgNameLine.length; i++) {
            if (GB == false) {
                if (orgNameLine[i].contains("Grand Blanc") && !orgNameLine[i].contains("Academy") && !orgNameLine[i].contains("Freedom") && !orgNameLine[i].contains("Offices") && !orgNameLine[i].contains("City") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Holy") && statusLine[i].contains("Closed Today") && dayrun == 0) {
                    txtInfo.setText(txtInfo.getText() + "\nGrand Blanc is Closed Today! \nEnjoy your Snow Day!");
                    txtGB.setText("Grand Blanc: CLOSED");
                    txtGB.setBackgroundColor(Color.RED);
                    percent = 100;
                    txtPercent.setText(percent + "%");
                    GB = true;
                    //Wait for the enter key before exiting
                    //txtPrint.setText("Press Enter to exit.");
                    //System.in.read();
                    //System.exit(0);
                }else if (orgNameLine[i].contains("Grand Blanc")&& !orgNameLine[i].contains("Academy") && !orgNameLine[i].contains("Freedom") && !orgNameLine[i].contains("Offices") && !orgNameLine[i].contains("City") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Holy") && statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                    txtInfo.setText(txtInfo.getText() + "\nGrand Blanc is Closed Tomorrow! \nEnjoy your Snow Day!");
                    txtGB.setText("Grand Blanc: CLOSED");
                    txtGB.setBackgroundColor(Color.RED);
                    percent = 100;
                    txtPercent.setText(percent + "%");
                    GB = true;
                    //Wait for the enter key before exiting
                    //txtPrint.setText("Press Enter to exit.");
                    //System.exit(0);
                }else{
                    txtGB.setText("Grand Blanc: OPEN");
                    txtGB.setBackgroundColor(Color.WHITE);
                    GB = false;
                }
            }
        }
    }

    private void checkClosingsToday() {

        for (int i = 1; i < orgNameLine.length; i++) {
            if (!(GBAcademy)) {
                if (orgNameLine[i].contains("Grand Blanc Academy") && statusLine[i].contains("Closed Today")) {
                    txtGBAcademy.setText("Grand Blanc Academy: CLOSED");
                    txtGBAcademy.setBackgroundColor(Color.YELLOW);
                    tier1++;
                    GBAcademy = true;
                } else {
                    txtGBAcademy.setText("Grand Blanc Academy: OPEN");
                }
            }
            if (!(GISD)) {
                if (orgNameLine[i].contains("Genesee I.S.D.") && statusLine[i].contains("Closed Today")) {
                    txtGISD.setText("Genesee I.S.D.: CLOSED");
                    txtGISD.setBackgroundColor(Color.YELLOW);
                    tier1++;
                    GISD = true;
                } else {
                    txtGISD.setText("Genesee I.S.D.: OPEN");
                }
            }
            if (!(HolyFamily)) {
                if (orgNameLine[i].contains("Holy Family") && statusLine[i].contains("Closed Today")) {
                    txtHolyFamily.setText("Holy Family: CLOSED");
                    txtHolyFamily.setBackgroundColor(Color.YELLOW);
                    tier1++;
                    HolyFamily = true;
                } else {
                    txtHolyFamily.setText("Holy Family: OPEN");
                }
            }
            if (!(WPAcademy)) {
                if (orgNameLine[i].contains("Woodland Park Academy") && statusLine[i].contains("Closed Today")) {
                    txtWPAcademy.setText("Woodland Park Academy: CLOSED");
                    txtWPAcademy.setBackgroundColor(Color.YELLOW);
                    tier1++;
                    WPAcademy = true;
                } else {
                    txtWPAcademy.setText("Woodland Park Academy: OPEN");
                }
            }
            if (!(Durand)) {
                if (orgNameLine[i].contains("Durand") && !orgNameLine[i].contains("Senior") && statusLine[i].contains("Closed Today")) {
                    txtDurand.setText("Durand: CLOSED");
                    txtDurand.setBackgroundColor(Color.YELLOW);
                    tier2++;
                    Durand = true;
                } else {
                    txtDurand.setText("Durand: OPEN");
                }
            }
            if (!(Holly)) {
                if (orgNameLine[i].contains("Holly") && !orgNameLine[i].contains("Academy") && statusLine[i].contains("Closed Today")) {
                    txtHolly.setText("Holly: CLOSED");
                    txtHolly.setBackgroundColor(Color.YELLOW);
                    tier2++;
                    Holly = true;
                } else {
                    txtHolly.setText("Holly: OPEN");
                }
            }
            if (!(Lapeer)) {
                if (orgNameLine[i].contains("Lapeer") && !orgNameLine[i].contains("Chatfield") && !orgNameLine[i].contains("Transit") && !orgNameLine[i].contains("CMH") && !orgNameLine[i].contains("Tech") && !orgNameLine[i].contains("Offices") && !orgNameLine[i].contains("Library") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Paul") && statusLine[i].contains("Closed Today")) {
                    txtLapeer.setText("Lapeer: CLOSED");
                    txtLapeer.setBackgroundColor(Color.YELLOW);
                    tier2++;
                    Lapeer = true;
                } else {
                    txtLapeer.setText("Lapeer: OPEN");
                }
            }
            if (!(Owosso)) {
                if (orgNameLine[i].contains("Owosso") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Baker") && !orgNameLine[i].contains("Paul") && statusLine[i].contains("Closed Today")) {
                    txtOwosso.setText("Owosso: CLOSED");
                    txtOwosso.setBackgroundColor(Color.YELLOW);
                    tier2++;
                    Owosso = true;
                } else {
                    txtOwosso.setText("Owosso: OPEN");
                }
            }
            if (!(Beecher)) {
                if (orgNameLine[i].contains("Beecher") && statusLine[i].contains("Closed Today")) {
                    txtBeecher.setText("Beecher: CLOSED");
                    txtBeecher.setBackgroundColor(Color.YELLOW);
                    tier2++;
                    Beecher = true;
                } else {
                    txtBeecher.setText("Beecher: OPEN");
                }
            }
            if (!(Clio)) {
                if (orgNameLine[i].contains("Clio") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("City") && !orgNameLine[i].contains("Cornerstone") && statusLine[i].contains("Closed Today")) {
                    txtClio.setText("Clio: CLOSED");
                    txtClio.setBackgroundColor(Color.YELLOW);
                    tier3++;
                    Clio = true;
                } else {
                    txtClio.setText("Clio: OPEN");
                }
            }
            if (!(Davison)) {
                if (orgNameLine[i].contains("Davison") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Faith") && !orgNameLine[i].contains("Montessori") && statusLine[i].contains("Closed Today")) {
                    txtDavison.setText("Davison: CLOSED");
                    txtDavison.setBackgroundColor(Color.YELLOW);
                    tier3++;
                    Davison = true;
                } else {
                    txtDavison.setText("Davison: OPEN");
                }
            }
            if (!(Fenton)) {
                if (orgNameLine[i].contains("Fenton") && !orgNameLine[i].contains("Lake") && !orgNameLine[i].contains("City") && !orgNameLine[i].contains("Montessori") && statusLine[i].contains("Closed Today")) {
                    txtFenton.setText("Fenton: CLOSED");
                    txtFenton.setBackgroundColor(Color.YELLOW);
                    tier3++;
                    Fenton = true;
                } else {
                    txtFenton.setText("Fenton: OPEN");
                }
            }
            if (!(Flushing)) {
                if (orgNameLine[i].contains("Flushing") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Robert") && statusLine[i].contains("Closed Today")) {
                    txtFlushing.setText("Flushing: CLOSED");
                    txtFlushing.setBackgroundColor(Color.YELLOW);
                    tier3++;
                    Flushing = true;
                } else {
                    txtFlushing.setText("Flushing: OPEN");
                }
            }
            if (!(Genesee)) {
                if (orgNameLine[i].contains("Genesee") && !orgNameLine[i].contains("Freedom") && !orgNameLine[i].contains("Christian") && !orgNameLine[i].contains("Mobile") && !orgNameLine[i].contains("Programs") && !orgNameLine[i].contains("Hlth") && !orgNameLine[i].contains("Sys") && !orgNameLine[i].contains("Stem") && !orgNameLine[i].contains("I.S.D.") && statusLine[i].contains("Closed Today")) {
                    txtGenesee.setText("Genesee: CLOSED");
                    txtGenesee.setBackgroundColor(Color.YELLOW);
                    tier3++;
                    Genesee = true;
                } else {
                    txtGenesee.setText("Genesee: OPEN");
                }
            }
            if (!(Kearsley)) {
                if (orgNameLine[i].contains("Kearsley") && statusLine[i].contains("Closed Today")) {
                    txtKearsley.setText("Kearsley: CLOSED");
                    txtKearsley.setBackgroundColor(Color.YELLOW);
                    tier3++;
                    Kearsley = true;
                } else {
                    txtKearsley.setText("Kearsley: OPEN");
                }
            }
            if (!(LKFenton)) {
                if (orgNameLine[i].contains("Lake Fenton") && statusLine[i].contains("Closed Today")) {
                    txtLKFenton.setText("Lake Fenton: CLOSED");
                    txtLKFenton.setBackgroundColor(Color.YELLOW);
                    tier3++;
                    LKFenton = true;
                } else {
                    txtLKFenton.setText("Lake Fenton: OPEN");
                }
            }
            if (!(Linden)) {
                if (orgNameLine[i].contains("Linden") && !orgNameLine[i].contains("Charter") && statusLine[i].contains("Closed Today")) {
                    txtLinden.setText("Linden: CLOSED");
                    txtLinden.setBackgroundColor(Color.YELLOW);
                    tier3++;
                    Linden = true;
                } else {
                    txtLinden.setText("Linden: OPEN");
                }
            }
            if (!(Montrose)) {
                if (orgNameLine[i].contains("Montrose") && !orgNameLine[i].contains("Senior") && statusLine[i].contains("Closed Today")) {
                    txtMontrose.setText("Montrose: CLOSED");
                    txtMontrose.setBackgroundColor(Color.YELLOW);
                    tier3++;
                    Montrose = true;
                } else {
                    txtMontrose.setText("Montrose: OPEN");
                }
            }
            if (!(Morris)) {
                if (orgNameLine[i].contains("Mt. Morris") && !orgNameLine[i].contains("Administration") && !orgNameLine[i].contains("Twp") && !orgNameLine[i].contains("Mary") && statusLine[i].contains("Closed Today")) {
                    txtMorris.setText("Mount Morris: CLOSED");
                    txtMorris.setBackgroundColor(Color.YELLOW);
                    tier3++;
                    Morris = true;
                } else {
                    txtMorris.setText("Mount Morris: OPEN");
                }
            }
            if (!(SzCreek)) {
                if (orgNameLine[i].contains("Swartz Creek") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Montessori") && statusLine[i].contains("Closed Today")) {
                    txtSzCreek.setText("Swartz Creek: CLOSED");
                    txtSzCreek.setBackgroundColor(Color.YELLOW);
                    tier3++;
                    SzCreek = true;
                } else {
                    txtSzCreek.setText("Swartz Creek: OPEN");
                }
            }
            if (!(Atherton)) {
                if (orgNameLine[i].contains("Atherton") && statusLine[i].contains("Closed Today")) {
                    txtAtherton.setText("Atherton: CLOSED");
                    txtAtherton.setBackgroundColor(Color.YELLOW);
                    tier4++;
                    Atherton = true;
                } else {
                    txtAtherton.setText("Atherton: OPEN");
                }
            }
            if (!(Bendle)) {
                if (orgNameLine[i].contains("Bendle") && statusLine[i].contains("Closed Today")) {
                    txtBendle.setText("Bendle: CLOSED");
                    txtBendle.setBackgroundColor(Color.YELLOW);
                    tier4++;
                    Bendle = true;
                } else {
                    txtBendle.setText("Bendle: OPEN");
                }
            }
            if (!(Flint)) {
                if (orgNameLine[i].contains("Flint Community Schools") && statusLine[i].contains("Closed Today")) {
                    txtFlint.setText("Flint: CLOSED");
                    txtFlint.setBackgroundColor(Color.YELLOW);
                    tier4++;
                    Flint = true;
                } else {
                    txtFlint.setText("Flint: OPEN");
                }
            }
            if (!(Goodrich)) {
                if (orgNameLine[i].contains("Goodrich") && statusLine[i].contains("Closed Today")) {
                    txtGoodrich.setText("Goodrich: CLOSED");
                    txtGoodrich.setBackgroundColor(Color.YELLOW);
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
                    tier5++;
                    Carman = true;
                } else {
                    txtCarman.setText("Carman-Ainsworth: OPEN");
                }
            }
        }

    }

    private void checkClosingsTomorrow() {

        for (int i = 1; i < orgNameLine.length; i++) {
            if (!(GBAcademy)) {
                if (orgNameLine[i].contains("Grand Blanc Academy") && statusLine[i].contains("Closed Tomorrow")) {
                    txtGBAcademy.setText("Grand Blanc Academy: CLOSED");
                    txtGBAcademy.setBackgroundColor(Color.YELLOW);
                    tier1++;
                    GBAcademy = true;
                } else {
                    txtGBAcademy.setText("Grand Blanc Academy: OPEN");
                }
            }
            if (!(GISD)) {
                if (orgNameLine[i].contains("Genesee I.S.D.") && statusLine[i].contains("Closed Tomorrow")) {
                    txtGISD.setText("Genesee I.S.D.: CLOSED");
                    txtGISD.setBackgroundColor(Color.YELLOW);
                    tier1++;
                    GISD = true;
                } else {
                    txtGISD.setText("Genesee I.S.D.: OPEN");
                }
            }
            if (!(HolyFamily)) {
                if (orgNameLine[i].contains("Holy Family") && statusLine[i].contains("Closed Tomorrow")) {
                    txtHolyFamily.setText("Holy Family: CLOSED");
                    txtHolyFamily.setBackgroundColor(Color.YELLOW);
                    tier1++;
                    HolyFamily = true;
                } else {
                    txtHolyFamily.setText("Holy Family: OPEN");
                }
            }
            if (!(WPAcademy)) {
                if (orgNameLine[i].contains("Woodland Park Academy") && statusLine[i].contains("Closed Tomorrow")) {
                    txtWPAcademy.setText("Woodland Park Academy: CLOSED");
                    txtWPAcademy.setBackgroundColor(Color.YELLOW);
                    tier1++;
                    WPAcademy = true;
                } else {
                    txtWPAcademy.setText("Woodland Park Academy: OPEN");
                }
            }
            if (!(Durand)) {
                if (orgNameLine[i].contains("Durand") && !orgNameLine[i].contains("Senior") && statusLine[i].contains("Closed Tomorrow")) {
                    txtDurand.setText("Durand: CLOSED");
                    txtDurand.setBackgroundColor(Color.YELLOW);
                    tier2++;
                    Durand = true;
                } else {
                    txtDurand.setText("Durand: OPEN");
                }
            }
            if (!(Holly)) {
                if (orgNameLine[i].contains("Holly") && !orgNameLine[i].contains("Academy") && statusLine[i].contains("Closed Tomorrow")) {
                    txtHolly.setText("Holly: CLOSED");
                    txtHolly.setBackgroundColor(Color.YELLOW);
                    tier2++;
                    Holly = true;
                } else {
                    txtHolly.setText("Holly: OPEN");
                }
            }
            if (!(Lapeer)) {
                if (orgNameLine[i].contains("Lapeer") && !orgNameLine[i].contains("Chatfield") && !orgNameLine[i].contains("Transit") && !orgNameLine[i].contains("CMH") && !orgNameLine[i].contains("Tech") && !orgNameLine[i].contains("Offices") && !orgNameLine[i].contains("Library") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Paul") && statusLine[i].contains("Closed Tomorrow")) {
                    txtLapeer.setText("Lapeer: CLOSED");
                    txtLapeer.setBackgroundColor(Color.YELLOW);
                    tier2++;
                    Lapeer = true;
                } else {
                    txtLapeer.setText("Lapeer: OPEN");
                }
            }
            if (!(Owosso)) {
                if (orgNameLine[i].contains("Owosso") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Baker") && !orgNameLine[i].contains("Paul") && statusLine[i].contains("Closed Tomorrow")) {
                    txtOwosso.setText("Owosso: CLOSED");
                    txtOwosso.setBackgroundColor(Color.YELLOW);
                    tier2++;
                    Owosso = true;
                } else {
                    txtOwosso.setText("Owosso: OPEN");
                }
            }
            if (!(Beecher)) {
                if (orgNameLine[i].contains("Beecher") && statusLine[i].contains("Closed Tomorrow")) {
                    txtBeecher.setText("Beecher: CLOSED");
                    txtBeecher.setBackgroundColor(Color.YELLOW);
                    tier2++;
                    Beecher = true;
                } else {
                    txtBeecher.setText("Beecher: OPEN");
                }
            }
            if (!(Clio)) {
                if (orgNameLine[i].contains("Clio") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("City") && !orgNameLine[i].contains("Cornerstone") && statusLine[i].contains("Closed Tomorrow")) {
                    txtClio.setText("Clio: CLOSED");
                    txtClio.setBackgroundColor(Color.YELLOW);
                    tier3++;
                    Clio = true;
                } else {
                    txtClio.setText("Clio: OPEN");
                }
            }
            if (!(Davison)) {
                if (orgNameLine[i].contains("Davison") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Faith") && !orgNameLine[i].contains("Montessori") && statusLine[i].contains("Closed Tomorrow")) {
                    txtDavison.setText("Davison: CLOSED");
                    txtDavison.setBackgroundColor(Color.YELLOW);
                    tier3++;
                    Davison = true;
                } else {
                    txtDavison.setText("Davison: OPEN");
                }
            }
            if (!(Fenton)) {
                if (orgNameLine[i].contains("Fenton") && !orgNameLine[i].contains("Lake") && !orgNameLine[i].contains("City") && !orgNameLine[i].contains("Montessori") && statusLine[i].contains("Closed Tomorrow")) {
                    txtFenton.setText("Fenton: CLOSED");
                    txtFenton.setBackgroundColor(Color.YELLOW);
                    tier3++;
                    Fenton = true;
                } else {
                    txtFenton.setText("Fenton: OPEN");
                }
            }
            if (!(Flushing)) {
                if (orgNameLine[i].contains("Flushing") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Robert") && statusLine[i].contains("Closed Tomorrow")) {
                    txtFlushing.setText("Flushing: CLOSED");
                    txtFlushing.setBackgroundColor(Color.YELLOW);
                    tier3++;
                    Flushing = true;
                } else {
                    txtFlushing.setText("Flushing: OPEN");
                }
            }
            if (!(Genesee)) {
                if (orgNameLine[i].contains("Genesee") && !orgNameLine[i].contains("Freedom") && !orgNameLine[i].contains("Christian") && !orgNameLine[i].contains("Mobile") && !orgNameLine[i].contains("Programs") && !orgNameLine[i].contains("Hlth") && !orgNameLine[i].contains("Sys") && !orgNameLine[i].contains("Stem") && !orgNameLine[i].contains("I.S.D.") && statusLine[i].contains("Closed Tomorrow")) {
                    txtGenesee.setText("Genesee: CLOSED");
                    txtGenesee.setBackgroundColor(Color.YELLOW);
                    tier3++;
                    Genesee = true;
                } else {
                    txtGenesee.setText("Genesee: OPEN");
                }
            }
            if (!(Kearsley)) {
                if (orgNameLine[i].contains("Kearsley") && statusLine[i].contains("Closed Tomorrow")) {
                    txtKearsley.setText("Kearsley: CLOSED");
                    txtKearsley.setBackgroundColor(Color.YELLOW);
                    tier3++;
                    Kearsley = true;
                } else {
                    txtKearsley.setText("Kearsley: OPEN");
                }
            }
            if (!(LKFenton)) {
                if (orgNameLine[i].contains("Lake Fenton") && statusLine[i].contains("Closed Tomorrow")) {
                    txtLKFenton.setText("Lake Fenton: CLOSED");
                    txtLKFenton.setBackgroundColor(Color.YELLOW);
                    tier3++;
                    LKFenton = true;
                } else {
                    txtLKFenton.setText("Lake Fenton: OPEN");
                }
            }
            if (!(Linden)) {
                if (orgNameLine[i].contains("Linden") && !orgNameLine[i].contains("Charter") && statusLine[i].contains("Closed Tomorrow")) {
                    txtLinden.setText("Linden: CLOSED");
                    txtLinden.setBackgroundColor(Color.YELLOW);
                    tier3++;
                    Linden = true;
                } else {
                    txtLinden.setText("Linden: OPEN");
                }
            }
            if (!(Montrose)) {
                if (orgNameLine[i].contains("Montrose") && !orgNameLine[i].contains("Senior") && statusLine[i].contains("Closed Tomorrow")) {
                    txtMontrose.setText("Montrose: CLOSED");
                    txtMontrose.setBackgroundColor(Color.YELLOW);
                    tier3++;
                    Montrose = true;
                } else {
                    txtMontrose.setText("Montrose: OPEN");
                }
            }
            if (!(Morris)) {
                if (orgNameLine[i].contains("Mt. Morris") && !orgNameLine[i].contains("Administration") && !orgNameLine[i].contains("Twp") && !orgNameLine[i].contains("Mary") && statusLine[i].contains("Closed Tomorrow")) {
                    txtMorris.setText("Mount Morris: CLOSED");
                    txtMorris.setBackgroundColor(Color.YELLOW);
                    tier3++;
                    Morris = true;
                } else {
                    txtMorris.setText("Mount Morris: OPEN");
                }
            }
            if (!(SzCreek)) {
                if (orgNameLine[i].contains("Swartz Creek") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Montessori") && statusLine[i].contains("Closed Tomorrow")) {
                    txtSzCreek.setText("Swartz Creek: CLOSED");
                    txtSzCreek.setBackgroundColor(Color.YELLOW);
                    tier3++;
                    SzCreek = true;
                } else {
                    txtSzCreek.setText("Swartz Creek: OPEN");
                }
            }
            if (!(Atherton)) {
                if (orgNameLine[i].contains("Atherton") && statusLine[i].contains("Closed Tomorrow")) {
                    txtAtherton.setText("Atherton: CLOSED");
                    txtAtherton.setBackgroundColor(Color.YELLOW);
                    tier4++;
                    Atherton = true;
                } else {
                    txtAtherton.setText("Atherton: OPEN");
                }
            }
            if (!(Bendle)) {
                if (orgNameLine[i].contains("Bendle") && statusLine[i].contains("Closed Tomorrow")) {
                    txtBendle.setText("Bendle: CLOSED");
                    txtBendle.setBackgroundColor(Color.YELLOW);
                    tier4++;
                    Bendle = true;
                } else {
                    txtBendle.setText("Bendle: OPEN");
                }
            }
            if (!(Flint)) {
                if (orgNameLine[i].contains("Flint Community Schools") && statusLine[i].contains("Closed Tomorrow")) {
                    txtFlint.setText("Flint: CLOSED");
                    txtFlint.setBackgroundColor(Color.YELLOW);
                    tier4++;
                    Flint = true;
                } else {
                    txtFlint.setText("Flint: OPEN");
                }
            }
            if (!(Goodrich)) {
                if (orgNameLine[i].contains("Goodrich") && statusLine[i].contains("Closed Tomorrow")) {
                    txtGoodrich.setText("Goodrich: CLOSED");
                    txtGoodrich.setBackgroundColor(Color.YELLOW);
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
                    tier5++;
                    Carman = true;
                } else {
                    txtCarman.setText("Carman-Ainsworth: OPEN");
                }
            }
        }
    }

    private void WeatherScraper() {

        /**NATIONAL WEATHER SERVICE SCRAPER**/
        //txtWeather.setText(txtWeather.getText() + "Retrieving Weather from NWS Detroit/Pontiac...");
        //Change the percentage based on current storm/wind/temperature warnings.

        //Live html
        //Document weatherdoc = Jsoup.connect("http://forecast.weather.gov/afm/PointClick.php?lat=42.92580&lon=-83.61870").get();

        //Document with multiple preset conditions
        File weatherinput = new File("./Weather.htm");
        Document weatherdoc = null;
        try {
            weatherdoc = Jsoup.parse(weatherinput, "UTF-8", "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Document with no warnings
        //File weatherinput = new File("./WeatherTest.htm");
        //Document weatherdoc = Jsoup.parse(input2, "UTF-8", "");

        //NullPointerException test
        //File weatherinput = new File("./Blank.htm");
        //Document weatherdoc = Jsoup.parse(input2, "UTF-8", "");

        //String weatherWarn = null;
        Elements weatherWarn = weatherdoc.getElementsByClass("warn");
        weathertext = weatherWarn.toString();

        if (weathertext.equals("")) {
            try {
                Element weatherNull = weatherdoc.getElementById("hazards_content");
                weathercheck = weatherNull.toString();
                if (weathercheck.contains("No Hazards in Effect")) {
                    txtWeather.setText("No applicable weather warnings.");
                }
            }catch (NullPointerException e) {
                System.out.println("Unable to obtain weather. \nIf this error persists please contact the developer.");
                System.exit(0);
            }
        }else{
            //Use the data
            getWeather();
        }
    }

    private void getWeather() {
        if (weathertext.contains("Hazardous Weather Outlook")) {
            if (!nullWeather) {
                txtWeather.setText(txtWeather.getText() + "\nA Hazardous Weather Outlook is in effect.");

            }else{
                txtWeather.setText("A Hazardous Weather Outlook is in effect.");
                nullWeather = false;
            }
            weatherpercent = 0;
        }
        if (weathertext.contains("Significant Weather Advisory")) {
            if (!nullWeather) {
                txtWeather.setText(txtWeather.getText() + "\nA Significant Weather Advisory is in effect.");
            }else{
                txtWeather.setText("A Significant Weather Advisory is in effect.");
                nullWeather = false;
            }
            weatherpercent = 15;
        }
        if (weathertext.contains("Winter Weather Advisory")) {
            if (!nullWeather) {
                txtWeather.setText(txtWeather.getText() + "\nA Winter Weather Advisory is in effect.");
            }else{
                txtWeather.setText("A Winter Weather Advisory is in effect.");
                nullWeather = false;
            }
            weatherpercent = 30;
        }
        if (weathertext.contains("Winter Storm Watch")) {
            if (!nullWeather) {
                txtWeather.setText(txtWeather.getText() + "\nA Winter Storm Watch is in effect.");
            }else{
                txtWeather.setText("A Winter Storm Watch is in effect.");
                nullWeather = false;
            }
            weatherpercent = 40;
        }
        if (weathertext.contains("Lake-Effect Snow Advisory") || weathertext.contains("Lake-Effect Snow Watch")) {
            if (!nullWeather) {
                txtWeather.setText(txtWeather.getText() + "\nA Lake-Effect Snow Advisory / Watch is in effect.");
            }else{
                txtWeather.setText("A Lake-Effect Snow Advisory / Watch is in effect.");
                nullWeather = false;
            }
            weatherpercent = 40;
        }
        if (weathertext.contains("Freezing Rain Advisory") || weathertext.contains("Freezing Drizzle Advisory") || weathertext.contains("Freezing Fog Advisory")) {
            if (!nullWeather) {
                txtWeather.setText(txtWeather.getText() + "\nA Freezing Rain / Drizzle / Fog Advisory is in effect.");
            }else{
                txtWeather.setText("A Freezing Rain / Drizzle / Fog Advisory is in effect.");
                nullWeather = false;
            }
            weatherpercent = 40;
        }
        if (weathertext.contains("Wind Chill Advisory")) {
            if (!nullWeather) {
                txtWeather.setText(txtWeather.getText() + "\nA Wind Chill Advisory is in effect.");
            }else{
                txtWeather.setText("A Wind Chill Advisory is in effect.");
                nullWeather = false;
            }
            weatherpercent = 40;
        }

        if (weathertext.contains("Wind Chill Watch")) {
            if (!nullWeather) {
                txtWeather.setText(txtWeather.getText() + "\nA Wind Chill Watch is in effect.");
            }else{
                txtWeather.setText("A Wind Chill Watch is in effect.");
                nullWeather = false;
            }
            weatherpercent = 40;
        }
        if (weathertext.contains("Blizzard Watch")) {
            if (!nullWeather) {
                txtWeather.setText(txtWeather.getText() + "\nA Blizzard Watch is in effect.");
            }else{
                txtWeather.setText("A Blizzard Watch is in effect.");
            }
            nullWeather = false;
            weatherpercent = 40;
        }
        if (weathertext.contains("Winter Storm Warning")) {
            if (!nullWeather) {
                txtWeather.setText(txtWeather.getText() + "\nA Winter Storm Warning is in effect.");
            }else{
                txtWeather.setText("A Winter Storm Warning is in effect.");
                nullWeather = false;
            }
            weatherpercent = 60;
        }
        if (weathertext.contains("Lake-Effect Snow Warning")) {
            if (!nullWeather) {
                txtWeather.setText(txtWeather.getText() + "\nA Lake-Effect Snow Warning is in effect.");
            }else{
                txtWeather.setText("A Lake-Effect Snow Warning is in effect.");
                nullWeather = false;
            }
            weatherpercent = 70;
        }
        if (weathertext.contains("Ice Storm Warning")) {
            if (!nullWeather) {
                txtWeather.setText(txtWeather.getText() + "\nAn Ice Storm Warning is in effect.");
            }else{
                txtWeather.setText("An Ice Storm Warning is in effect.");
                nullWeather = false;
            }
            weatherpercent = 70;
        }
        if (weathertext.contains("Wind Chill Warning")) {
            if (!nullWeather) {
                txtWeather.setText(txtWeather.getText() + "\nA Wind Chill Warning is in effect.");
            }else{
                txtWeather.setText("A Wind Chill Warning is in effect.");
                nullWeather = false;
            }
            weatherpercent = 75;
        }
        if (weathertext.contains("Blizzard Warning")) {
            if (!nullWeather) {
                txtWeather.setText(txtWeather.getText() + "\nA Blizzard Warning is in effect.");
            }else{
                txtWeather.setText("A Blizzard Warning is in effect.");
                nullWeather = false;
            }
            weatherpercent = 75;
        }
    }

    private void PercentCalculate() {
        if (tier5==1) {
            schoolpercent+=90;
        }else if (tier4!=0) {
            schoolpercent+=80;
        }else if (tier3!=0) {
            schoolpercent+=60;
        }else if (tier2!=0) {
            schoolpercent+=40;
        }else if (tier1!=0) {
            schoolpercent+=20;
        }

        //Calculate the total percent.
        int percentarray[] = {schoolpercent, weatherpercent};
        //percent = percentarray[0];
        for (int i = 0; i <percentarray.length;i++) {
            if (percentarray[i]>percent){
                percent = percentarray[i];
            }
        }

        //Reduce the percent chance by three for every snow day entered.
        percent-=(days*3);
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
            percent = 100;
        }

        if (percent >= 0 && percent <= 20) {
            txtPercent.setTextColor(Color.RED);
        }else if (percent > 20 && percent <=60) {
            txtPercent.setTextColor(Color.rgb(255, 165, 0));
        }else if (percent > 60 && percent <=80) {
            txtPercent.setTextColor(Color.GREEN);
        }else if (percent >80) {
            txtPercent.setTextColor(Color.BLUE);
        }
    }
}



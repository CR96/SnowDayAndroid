package com.GBSnowDay.SnowDay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;


public class SnowDayResult extends Activity {

    //Declare all views
    ListView lstClosings;
    TextView txtTier1;
    TextView txtTier2;
    TextView txtTier3;
    TextView txtTier4;

    ListView lstGB;
    ListView lstWJRT;
    ListView lstNWS;
    
    TextView txtInfo;
    TextView txtPercent;

    ListView lstWeather;
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

    public List<String> GBInfo = new ArrayList<String>();
    public List<String> closings = new ArrayList<String>();
    public List<String> wjrtInfo = new ArrayList<String>();
    public List<String> nwsArray = new ArrayList<String>();
    public List<String> nwsInfo = new ArrayList<String>();
    
    public int GBCount = 1;
    public int weatherCount = 0;
    public int wjrtCount = 0;
    public int nwsCount = 0;

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
    public boolean WJRTFail;
    public boolean NWSFail;

    //Custom adapter
    private CustomAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        //Add the GBInfo value so it can be set out of sequence
        GBInfo.add(0, "");

        txtInfo = (TextView) findViewById(R.id.txtInfo);

        lstWeather = (ListView) findViewById(R.id.lstWeather);
        webRadar = (WebView) findViewById(R.id.webRadar);
        btnRadar = (Button) findViewById(R.id.btnRadar);
        progCalculate = (ProgressBar) findViewById(R.id.progCalculate);

        Calculate();
    }

    //Adapter class
    private class CustomAdapter extends BaseAdapter {
        private static final int TYPE_ITEM = 0;
        private static final int TYPE_SEPARATOR =1;
        private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

        private ArrayList<String> mData = new ArrayList<String>();
        private LayoutInflater mInflater;

        private TreeSet<Integer> mSeparatorsSet = new TreeSet<Integer>();

        public CustomAdapter() {
            mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            System.out.println(position);
            ViewHolder holder = null;
            int type = getItemViewType(position);
            if (convertView == null) {
                holder = new ViewHolder();
                switch (type) {
                    case TYPE_ITEM:
                        if (Carman && position == 1 || Atherton && position == 2) {
                            //TODO: If the school is closed, make it orange
                            convertView = mInflater.inflate(R.layout.itemclosed, null);
                            holder.textView = (TextView)convertView.findViewById(R.id.text);
                        }else{
                            convertView = mInflater.inflate(R.layout.itemlist, null);
                            holder.textView = (TextView)convertView.findViewById(R.id.text);
                        }
                        break;
                    case TYPE_SEPARATOR:
                        convertView = mInflater.inflate(R.layout.itemseparator, null);
                        holder.textView = (TextView)convertView.findViewById(R.id.textSeparator);
                        break;
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.textView.setText(mData.get(position));
            return convertView;
        }
    }

    public static class ViewHolder {
        public TextView textView;
    }


    @SuppressLint("NewApi")
    public void radarToggle(View view) {
        if (webRadar.getVisibility() == View.GONE) {
            webRadar.setEnabled(true);
            webRadar.setVisibility(View.VISIBLE);
            webRadar.loadUrl("http://radar.weather.gov/Conus/Loop/centgrtlakes_loop.gif");
            webRadar.getSettings().setLoadWithOverviewMode(true);
            webRadar.getSettings().setUseWideViewPort(true);
            btnRadar.setText(getString(R.string.radarhide));
        } else {
            webRadar.setVisibility(View.GONE);
            webRadar.setEnabled(false);
            btnRadar.setText(getString(R.string.radarshow));
        }
    }

    private void Calculate() {
        //Read dayrun and days from SnowDay class
        Intent result = getIntent();
        dayrun = result.getIntExtra("dayrun", dayrun);
        days = result.getIntExtra("days", days);


        /**WJRT SCHOOL CLOSINGS SCRAPER**/
        new WJRTScraper().execute();

        //Next Test: Weather!

        /**NATIONAL WEATHER SERVICE SCRAPER**/
       new WeatherScraper().execute();

        //Final Percent Calculator
       new PercentCalculate().execute();


    }

    private class WJRTScraper extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... nothing) {
            WJRTActive = true;
            Document schools = null;
            //Scrape School Closings from WJRT with Jsoup.

            /**WJRT SCHOOL CLOSINGS SCRAPER**/
            //Scrape School Closings from WJRT with Jsoup.
            //Run scraper in an Async task.

            //TODO: Remove these tests
            //The following is a rigged archive from January 5th - every school referenced by this program was closed the following day.
//            System.out.println("Reading from Closings.htm on emulated SD card");
            try {
                File input = new File("mnt/sdcard/Closings.htm");
                schools = Jsoup.parse(input, "UTF-8", "");
                System.out.println("Read successful");
            }catch (IOException e) {
                e.printStackTrace();
                System.out.println("Couldn't read the file.");
            }

            //This is a second rigged archive from December 23rd - Swartz Creek and Kearsley were closed on the day for reference.

//            try {
//                File input = new File("mnt/sdcard/ClosingsToday.htm");
//                schools = Jsoup.parse(input, "UTF-8", "");
//            }catch (IOException e) {
//                TextView txtInfo = (TextView) findViewById(R.id.txtInfo);
//                txtInfo.setText(txtInfo.getText() + getString(R.string.NoConnection));
//                e.printStackTrace();
//            }

            //This third document tests for false triggers, e.g. "Owosso" shouldn't show as "closed" if only "Owosso Senior Center" is closed.
            //This document will not trigger any closings if the code is working properly.

            /*try {
                File input = new File("mnt/sdcard/Trials.htm");
                schools = Jsoup.parse(input, "UTF-8", "");
            }catch (IOException e) {
                TextView txtInfo = (TextView) findViewById(R.id.txtInfo);
                txtInfo.setText(txtInfo.getText() + getString(R.string.NoConnection));
                e.printStackTrace();
            }*/

            //Fourth html archive - every school except GB, Durand, Owosso, and Holy Family is closed (shouldn't trigger 100%)

           /* try {
                File input = new File("mnt/sdcard/GBNotClosed.htm");
                schools = Jsoup.parse(input, "UTF-8", "");
            } catch (IOException e) {
                txtInfo.setText(getString(R.string.NoConnection));
                e.printStackTrace();
            }*/

            //This is a blank example (no active records) - check how the program runs when nullpointerexception is thrown

        /*try {
            File input = new File("mnt/sdcard/Blank.htm");
            schools = Jsoup.parse(input, "UTF-8", "");
        }catch (IOException e) {
            TextView txtInfo = (TextView) findViewById(R.id.txtInfo);
            txtInfo.setText(txtInfo.getText() + getString(R.string.NoConnection));
            e.printStackTrace();
        }*/


            //This is the current listings page.

            try {
                //schools = Jsoup.connect("http://ftpcontent2.worldnow.com/wjrt/school/closings.htm").get();
                //Attempt to parse input file
                for (Element row : schools.select("td[bgcolor]")) {
                    //Reading closings
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
                        System.out.println("368 False");
                    } else {
                        //Webpage layout was not recognized.
                        wjrtInfo.add(wjrtCount, getString(R.string.WJRTParseError));
                        wjrtInfo.add(wjrtCount + 1, getString(R.string.ErrorContact));
                        wjrtCount = wjrtCount + 2;
                        WJRTFail = true;
                        System.out.println("375 False");

                    }

                    //Set dummy content so the scraper doesn't fail with a NullPointerException
                    orgName = "DummyLine1\nDummyLine2\nDummyLine3";
                    status = "DummyLine1\nDummyLine2\nDummyLine3";

                }

//            } catch (IOException e) {
//                wjrtInfo.add(wjrtCount, getString(R.string.WJRTConnectionError) + " " + getString(R.string.NoConnection));
//                wjrtCount++;
//                WJRTFail = true;
//                System.out.println("388 True");

            } catch (NullPointerException e) {
                //Webpage layout was not recognized.
                wjrtInfo.add(wjrtCount, getString(R.string.WJRTParseError));
                wjrtInfo.add(wjrtCount + 1, getString(R.string.ErrorContact));
                wjrtCount = wjrtCount + 2;
                WJRTFail = true;
            }

            //Only run if WJRTFail is false to avoid NullPointerExceptions
            if (!WJRTFail) {
                //Splitting orgName and status strings by line break.
                //Saving to orgNameLine and statusLine.
                //This will create string arrays that can be parsed by for loops.
                orgNameLine = orgName.split("\n");
                statusLine = status.split("\n");

                //The first test: School Closings!
                //Decide whether to check for today's closings or tomorrow's closings.

                //Sanity check - make sure GB isn't actually closed before predicting
                //Check to see if Grand Blanc is already closed.
                checkGBClosed();

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
            WJRTActive = false;
        }

    }

    private void checkClosingsToday() {
        for (int i = 1; i < orgNameLine.length; i++) {
            if (!(Carman)) {
                if (orgNameLine[i].contains("Carman-Ainsworth") && !orgNameLine[i].contains("Senior") && statusLine[i].contains("Closed Today")) {
                    closings.set(1, "Carman-Ainsworth: CLOSED");
                    tier5++;
                    Carman = true;
                } else {
                    closings.set(1, "Carman-Ainsworth: OPEN");
                }
            }
            if (!(Atherton)) {
                if (orgNameLine[i].contains("Atherton") && statusLine[i].contains("Closed Today")) {
                    closings.set(2, "Atherton: CLOSED");
                    tier4++;
                    Atherton = true;
                } else {
                    closings.set(2, "Atherton: OPEN");
                }
            }
            if (!(Bendle)) {
                if (orgNameLine[i].contains("Bendle") && statusLine[i].contains("Closed Today")) {
                    closings.set(3, "Bendle: CLOSED");
                    tier4++;
                    Bendle = true;
                } else {
                    closings.set(3, "Bendle: OPEN");
                }
            }
            if (!(Bentley)) {
                if (orgNameLine[i].contains("Bentley") && statusLine[i].contains("Closed Today")) {
                    closings.set(4, "Bentley: CLOSED");
                    tier4++;
                    Bentley = true;
                } else {
                    closings.set(4, "Bentley: OPEN");
                }
            }
            if (!(Flint)) {
                if (orgNameLine[i].contains("Flint Community Schools") && statusLine[i].contains("Closed Today")) {
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
                if (orgNameLine[i].contains("Clio") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("City") && !orgNameLine[i].contains("Cornerstone") && statusLine[i].contains("Closed Today")) {
                    closings.set(8, "Clio: CLOSED");
                    tier3++;
                    Clio = true;
                } else {
                    closings.set(8, "Clio: OPEN");
                }
            }
            if (!(Davison)) {
                if (orgNameLine[i].contains("Davison") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Faith") && !orgNameLine[i].contains("Montessori") && statusLine[i].contains("Closed Today")) {
                    closings.set(9, "Davison: CLOSED");
                    tier3++;
                    Davison = true;
                } else {
                    closings.set(9, "Davison: OPEN");
                }
            }
            if (!(Fenton)) {
                if (orgNameLine[i].contains("Fenton") && !orgNameLine[i].contains("Lake") && !orgNameLine[i].contains("City") && !orgNameLine[i].contains("Montessori") && statusLine[i].contains("Closed Today")) {
                    closings.set(10, "Fenton: CLOSED");
                    tier3++;
                    Fenton = true;
                } else {
                    closings.set(10, "Fenton: OPEN");
                }
            }
            if (!(Flushing)) {
                if (orgNameLine[i].contains("Flushing") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Robert") && statusLine[i].contains("Closed Today")) {
                    closings.set(11, "Flushing: CLOSED");
                    tier3++;
                    Flushing = true;
                } else {
                    closings.set(11, "Flushing: OPEN");
                }
            }
            if (!(Genesee)) {
                if (orgNameLine[i].contains("Genesee") && !orgNameLine[i].contains("Freedom") && !orgNameLine[i].contains("Christian") && !orgNameLine[i].contains("Mobile") && !orgNameLine[i].contains("Programs") && !orgNameLine[i].contains("Hlth") && !orgNameLine[i].contains("Sys") && !orgNameLine[i].contains("Stem") && !orgNameLine[i].contains("I.S.D.") && statusLine[i].contains("Closed Today")) {
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
                if (orgNameLine[i].contains("Linden") && !orgNameLine[i].contains("Charter") && statusLine[i].contains("Closed Today")) {
                    closings.set(15, "Linden: CLOSED");
                    tier3++;
                    Linden = true;
                } else {
                    closings.set(15, "Linden: OPEN");
                }
            }
            if (!(Montrose)) {
                if (orgNameLine[i].contains("Montrose") && !orgNameLine[i].contains("Senior") && statusLine[i].contains("Closed Today")) {
                    closings.set(16, "Montrose: CLOSED");
                    tier3++;
                    Montrose = true;
                } else {
                    closings.set(16, "Montrose: OPEN");
                }
            }
            if (!(Morris)) {
                if (orgNameLine[i].contains("Mt. Morris") && !orgNameLine[i].contains("Administration") && !orgNameLine[i].contains("Twp") && !orgNameLine[i].contains("Mary") && statusLine[i].contains("Closed Today")) {
                    closings.set(17, "Mount Morris: CLOSED");
                    tier3++;
                    Morris = true;
                } else {
                    closings.set(17, "Mount Morris: OPEN");
                }
            }
            if (!(SzCreek)) {
                if (orgNameLine[i].contains("Swartz Creek") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Montessori") && statusLine[i].contains("Closed Today")) {
                    closings.set(18, "Swartz Creek: CLOSED");
                    tier3++;
                    SzCreek = true;
                } else {
                    closings.set(18, "Swartz Creek: OPEN");
                }
            }
            if (!(Durand)) {
                if (orgNameLine[i].contains("Durand") && !orgNameLine[i].contains("Senior") && statusLine[i].contains("Closed Today")) {
                    closings.set(19, "Durand: CLOSED");
                    tier2++;
                    Durand = true;
                } else {
                    closings.set(19, "Durand: OPEN");
                }
            }
            if (!(Holly)) {
                if (orgNameLine[i].contains("Holly") && !orgNameLine[i].contains("Academy") && statusLine[i].contains("Closed Today")) {
                    closings.set(20, "Holly: CLOSED");
                    tier2++;
                    Holly = true;
                } else {
                    closings.set(20, "Holly: OPEN");
                }
            }
            if (!(Lapeer)) {
                if (orgNameLine[i].contains("Lapeer") && !orgNameLine[i].contains("Chatfield") && !orgNameLine[i].contains("Transit") && !orgNameLine[i].contains("CMH") && !orgNameLine[i].contains("Tech") && !orgNameLine[i].contains("Offices") && !orgNameLine[i].contains("Library") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Paul") && statusLine[i].contains("Closed Today")) {
                    closings.set(21, "Lapeer: CLOSED");
                    tier2++;
                    Lapeer = true;
                } else {
                    closings.set(21, "Lapeer: OPEN");
                }
            }
            if (!(Owosso)) {
                if (orgNameLine[i].contains("Owosso") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Baker") && !orgNameLine[i].contains("Paul") && statusLine[i].contains("Closed Today")) {
                    closings.set(22, "Owosso: CLOSED");
                    tier2++;
                    Owosso = true;
                } else {
                    closings.set(22, "Owosso: OPEN");
                }
            }
            if (!(GBAcademy)) {
                if (orgNameLine[i].contains("Grand Blanc Academy") && statusLine[i].contains("Closed Today")) {
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
                if (orgNameLine[i].contains("Woodland Park Academy") && statusLine[i].contains("Closed Today")) {
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
            if (!(Carman)) {
                if (orgNameLine[i].contains("Carman-Ainsworth") && !orgNameLine[i].contains("Senior") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(1, "Carman-Ainsworth: CLOSED");
                    tier5++;
                    Carman = true;
                } else {
                    closings.set(1, "Carman-Ainsworth: OPEN");
                }
            }
            if (!(Atherton)) {
                if (orgNameLine[i].contains("Atherton") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(2, "Atherton: CLOSED");
                    tier4++;
                    Atherton = true;
                } else {
                    closings.set(2, "Atherton: OPEN");
                }
            }
            if (!(Bendle)) {
                if (orgNameLine[i].contains("Bendle") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(3, "Bendle: CLOSED");
                    tier4++;
                    Bendle = true;
                } else {
                    closings.set(3, "Bendle: OPEN");
                }
            }
            if (!(Bentley)) {
                if (orgNameLine[i].contains("Bentley") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(4, "Bentley: CLOSED");
                    tier4++;
                    Bentley = true;
                } else {
                    closings.set(4, "Bentley: OPEN");
                }
            }
            if (!(Flint)) {
                if (orgNameLine[i].contains("Flint Community Schools") && statusLine[i].contains("Closed Tomorrow")) {
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
                if (orgNameLine[i].contains("Clio") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("City") && !orgNameLine[i].contains("Cornerstone") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(8, "Clio: CLOSED");
                    tier3++;
                    Clio = true;
                } else {
                    closings.set(8, "Clio: OPEN");
                }
            }
            if (!(Davison)) {
                if (orgNameLine[i].contains("Davison") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Faith") && !orgNameLine[i].contains("Montessori") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(9, "Davison: CLOSED");
                    tier3++;
                    Davison = true;
                } else {
                    closings.set(9, "Davison: OPEN");
                }
            }
            if (!(Fenton)) {
                if (orgNameLine[i].contains("Fenton") && !orgNameLine[i].contains("Lake") && !orgNameLine[i].contains("City") && !orgNameLine[i].contains("Montessori") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(10, "Fenton: CLOSED");
                    tier3++;
                    Fenton = true;
                } else {
                    closings.set(10, "Fenton: OPEN");
                }
            }
            if (!(Flushing)) {
                if (orgNameLine[i].contains("Flushing") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Robert") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(11, "Flushing: CLOSED");
                    tier3++;
                    Flushing = true;
                } else {
                    closings.set(11, "Flushing: OPEN");
                }
            }
            if (!(Genesee)) {
                if (orgNameLine[i].contains("Genesee") && !orgNameLine[i].contains("Freedom") && !orgNameLine[i].contains("Christian") && !orgNameLine[i].contains("Mobile") && !orgNameLine[i].contains("Programs") && !orgNameLine[i].contains("Hlth") && !orgNameLine[i].contains("Sys") && !orgNameLine[i].contains("Stem") && !orgNameLine[i].contains("I.S.D.") && statusLine[i].contains("Closed Tomorrow")) {
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
                if (orgNameLine[i].contains("Linden") && !orgNameLine[i].contains("Charter") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(15, "Linden: CLOSED");
                    tier3++;
                    Linden = true;
                } else {
                    closings.set(15, "Linden: OPEN");
                }
            }
            if (!(Montrose)) {
                if (orgNameLine[i].contains("Montrose") && !orgNameLine[i].contains("Senior") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(16, "Montrose: CLOSED");
                    tier3++;
                    Montrose = true;
                } else {
                    closings.set(16, "Montrose: OPEN");
                }
            }
            if (!(Morris)) {
                if (orgNameLine[i].contains("Mt. Morris") && !orgNameLine[i].contains("Administration") && !orgNameLine[i].contains("Twp") && !orgNameLine[i].contains("Mary") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(17, "Mount Morris: CLOSED");
                    tier3++;
                    Morris = true;
                } else {
                    closings.set(17, "Mount Morris: OPEN");
                }
            }
            if (!(SzCreek)) {
                if (orgNameLine[i].contains("Swartz Creek") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Montessori") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(18, "Swartz Creek: CLOSED");
                    tier3++;
                    SzCreek = true;
                } else {
                    closings.set(18, "Swartz Creek: OPEN");
                }
            }
            if (!(Durand)) {
                if (orgNameLine[i].contains("Durand") && !orgNameLine[i].contains("Senior") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(19, "Durand: CLOSED");
                    tier2++;
                    Durand = true;
                } else {
                    closings.set(19, "Durand: OPEN");
                }
            }
            if (!(Holly)) {
                if (orgNameLine[i].contains("Holly") && !orgNameLine[i].contains("Academy") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(20, "Holly: CLOSED");
                    tier2++;
                    Holly = true;
                } else {
                    closings.set(20, "Holly: OPEN");
                }
            }
            if (!(Lapeer)) {
                if (orgNameLine[i].contains("Lapeer") && !orgNameLine[i].contains("Chatfield") && !orgNameLine[i].contains("Transit") && !orgNameLine[i].contains("CMH") && !orgNameLine[i].contains("Tech") && !orgNameLine[i].contains("Offices") && !orgNameLine[i].contains("Library") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Paul") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(21, "Lapeer: CLOSED");
                    tier2++;
                    Lapeer = true;
                } else {
                    closings.set(21, "Lapeer: OPEN");
                }
            }
            if (!(Owosso)) {
                if (orgNameLine[i].contains("Owosso") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Baker") && !orgNameLine[i].contains("Paul") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(22, "Owosso: CLOSED");
                    tier2++;
                    Owosso = true;
                } else {
                    closings.set(22, "Owosso: OPEN");
                }
            }
            if (!(GBAcademy)) {
                if (orgNameLine[i].contains("Grand Blanc Academy") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(23, "Grand Blanc Academy: CLOSED");
                    tier1++;
                    GBAcademy = true;
                } else {
                    closings.set(23, "Grand Blanc Academy: OPEN");
                }
            }
            if (!(GISD)) {
                if (orgNameLine[i].contains("Genesee I.S.D.") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(24, "Genesee I.S.D.: CLOSED");
                    tier1++;
                    GISD = true;
                } else {
                    closings.set(24, "Genesee I.S.D.: OPEN");
                }
            }
            if (!(HolyFamily)) {
                if (orgNameLine[i].contains("Holy Family") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(25, "Holy Family: CLOSED");
                    tier1++;
                    HolyFamily = true;
                } else {
                    closings.set(25, "Holy Family: OPEN");
                }
            }
            if (!(WPAcademy)) {
                if (orgNameLine[i].contains("Woodland Park Academy") && statusLine[i].contains("Closed Tomorrow")) {
                    closings.set(26, "Woodland Park Academy: CLOSED");
                    tier1++;
                    WPAcademy = true;
                } else {
                    closings.set(26, "Woodland Park Academy: OPEN");
                }
            } 
        }
    }

    
    private void checkGBClosed() {
                //Checking if GB is closed.
                for (int i = 1; i < orgNameLine.length; i++) {
                    if (!GB) {
                        if (orgNameLine[i].contains("Grand Blanc") && !orgNameLine[i].contains("Academy") && !orgNameLine[i].contains("Freedom") && !orgNameLine[i].contains("Offices") && !orgNameLine[i].contains("City") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Holy") && statusLine[i].contains("Closed Today") && dayrun == 0) {
                            GBInfo.set(0, getString(R.string.GB) + " " + getString(R.string.Closed));
                            GBInfo.add(GBCount, getString(R.string.SnowDay));
                            GBCount++;
                            lstGB.setBackgroundColor(Color.RED);
                            percent = 100;
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    txtPercent.setText(percent + "%");
                                }
                            });
                            GB = true;
                            //GB Found (today)
                            break;
                        } else if (orgNameLine[i].contains("Grand Blanc") && !orgNameLine[i].contains("Academy") && !orgNameLine[i].contains("Freedom") && !orgNameLine[i].contains("Offices") && !orgNameLine[i].contains("City") && !orgNameLine[i].contains("Senior") && !orgNameLine[i].contains("Holy") && statusLine[i].contains("Closed Tomorrow") && dayrun == 1) {
                            GBInfo.set(0, getString(R.string.GB) + " " + getString(R.string.Closed));
                            GBInfo.add(GBCount, getString(R.string.SnowDay));
                            GBCount++;
                            lstGB.setBackgroundColor(Color.RED);
                            percent = 100;
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    txtPercent.setText(percent + "%");
                                }
                            });
                            GB = true;
                            //GB Found (tomorrow)
                            break;
                        }

                    }
                }
        if (!GB) {
            //Didn't find GB
            GBInfo.set(0, getString(R.string.GB) + " " + getString(R.string.Open));

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
                                weathercheck = weatherNull.toString();
                                if (weathercheck.contains("No Hazards in Effect")) {
                                    //Webpage parsed correctly: no hazards present.
                                    nwsInfo.add(0, getString(R.string.NoWeather));
                                    nwsCount++;
                                    NWSFail = false;
                                }
                            } else {
                                //Hazards found. Use the data
                                getWeather();
                            }
                        } catch (IOException e) {
                            nwsInfo.add(nwsCount, getString(R.string.WeatherError));
                            nwsInfo.add(nwsCount + 1, getString(R.string.NoConnection));
                            nwsCount = nwsCount + 2;
                            NWSFail = true;
                        } catch (NullPointerException e) {
                            //Something has changed in the webpage and it can't be parsed.
                            nwsInfo.add(nwsCount, getString(R.string.WeatherError));
                            nwsInfo.add(nwsCount + 1, getString(R.string.ErrorContact));
                            nwsCount = nwsCount + 2;
                            NWSFail = true;
                        }


                        return null;
                    }

                    protected void onPostExecute(Void result) {
                        NWSActive = false;
                    }
                }

            private void getWeather() {
                        //Only the highest weatherpercent is stored (not cumulative)

                        if (weathertext.contains("Significant Weather Advisory")) {
                            //Significant Weather Advisory - 15% weatherpercent
                            nwsArray.add(weatherCount,"A Significant Weather Advisory is in effect.");
                            weatherCount++;
                            weatherpercent = 15;
                        }
                        if (weathertext.contains("Winter Weather Advisory")) {
                            //Winter Weather Advisory - 30% weatherpercent
                            nwsArray.add(weatherCount, "A Winter Weather Advisory is in effect.");
                            weatherCount++;
                            weatherpercent = 30;
                        }
                        if (weathertext.contains("Winter Storm Watch")) {
                            //Winter Storm Watch - 40% weatherpercent
                            nwsArray.add(weatherCount, "A Winter Storm Watch is in effect.");
                            weatherCount++;
                            weatherpercent = 40;
                        }
                        if (weathertext.contains("Lake-Effect Snow Advisory") || weathertext.contains("Lake-Effect Snow Watch")) {
                            //Lake Effect Snow Advisory / Watch - 40% weatherpercent
                            nwsArray.add(weatherCount, "A Lake-Effect Snow Advisory / Watch is in effect.");
                            weatherCount++;
                            weatherpercent = 40;
                        }
                        if (weathertext.contains("Freezing Rain Advisory") || weathertext.contains("Freezing Drizzle Advisory") || weathertext.contains("Freezing Fog Advisory")) {
                            //Freezing Rain - 40% weatherpercent
                            nwsArray.add(weatherCount, "A Freezing Rain / Drizzle / Fog Advisory is in effect.");
                            weatherCount++;
                            weatherpercent = 40;
                        }
                        if (weathertext.contains("Wind Chill Advisory")) {
                            //Wind Chill Advisory - 40% weatherpercent
                            nwsArray.add(weatherCount, "A Wind Chill Advisory is in effect.");
                            weatherCount++;
                            weatherpercent = 40;
                        }

                        if (weathertext.contains("Wind Chill Watch")) {
                            //Wind Chill Watch - 40% weatherpercent
                            nwsArray.add(weatherCount, "A Wind Chill Watch is in effect.");
                            weatherCount++;
                            weatherpercent = 40;
                        }
                        if (weathertext.contains("Blizzard Watch")) {
                            //Blizzard Watch - 40% weatherpercent
                            nwsArray.add(weatherCount, "A Blizzard Watch is in effect.");
                            weatherCount++;
                            weatherpercent = 40;
                        }
                        if (weathertext.contains("Winter Storm Warning")) {
                            //Winter Storm Warning - 60% weatherpercent
                            nwsArray.add(weatherCount, "A Winter Storm Warning is in effect.");
                            weatherCount++;
                            weatherpercent = 60;
                        }
                        if (weathertext.contains("Lake-Effect Snow Warning")) {
                            //Lake Effect Snow Warning - 70% weatherpercent
                            nwsArray.add(weatherCount, "A Lake-Effect Snow Warning is in effect.");
                            weatherCount++;
                            weatherpercent = 70;
                        }
                        if (weathertext.contains("Ice Storm Warning")) {
                            //Ice Storm Warning - 70% weatherpercent
                            nwsArray.add(weatherCount, "An Ice Storm Warning is in effect.");
                            weatherCount++;
                            weatherpercent = 70;
                        }
                        if (weathertext.contains("Wind Chill Warning")) {
                            //Wind Chill Warning - 75% weatherpercent
                            nwsArray.add(weatherCount, "A Wind Chill Warning is in effect.");
                            weatherCount++;
                            weatherpercent = 75;
                        }
                        if (weathertext.contains("Blizzard Warning")) {
                            //Blizzard Warning - 75% weatherpercent
                            nwsArray.add(weatherCount, "A Blizzard Warning is in effect.");
                            weatherCount++;
                            weatherpercent = 75;
                        }

                //TODO: Consider snowfall amount and wind chill
            }

            private class PercentCalculate extends AsyncTask<Void, Void, Void> {
                protected Void doInBackground(Void... nothing) {

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
                            //Wait for scrapers to finish before continuing
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }


                    if (tier5 == 1) {
                        //"Carman-Ainsworth was closed. 90% schoolpercent.
                        schoolpercent += 90;
                    } else if (tier4 > 2) {
                        //3+ schools near GB were closed. 80% schoolpercent.
                        schoolpercent += 80;
                    } else if (tier3 > 2) {
                        //3+ schools in Genesee County were closed. 60% schoolpercent.
                        schoolpercent += 60;
                    } else if (tier2 > 2) {
                        //3+ schools in nearby counties were closed. 40% schoolpercent.
                        schoolpercent += 40;
                    } else if (tier1 > 2) {
                        //3+ academies were closed. 20% schoolpercent.
                        schoolpercent += 20;
                    }

                    //Calculate the total percent.

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
                    }

                    percentscroll = 0;

                    runOnUiThread(new Runnable() {
                        public void run() {
                            progCalculate.setVisibility(View.GONE);
                            txtPercent.setText("0%");
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
                    } catch (
                            InterruptedException ex
                            )

                    {
                        ex.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtInfo.setVisibility(View.VISIBLE);
                            btnRadar.setVisibility(View.VISIBLE);

                            //Set the content of the information ListView

                            ArrayAdapter<String> gbadapter = new ArrayAdapter<String>(getApplicationContext(),
                                    android.R.layout.simple_list_item_1, GBInfo);
                            lstGB.setAdapter(gbadapter);

                            //Set up the ListView adapter that displays school closings

                            //TODO: Show warning text if either scraper cannot display a result.

                           if (!WJRTFail) {
                               System.out.println("WJRT has not failed.");
                               mAdapter = new CustomAdapter();
                               mAdapter.addSeparatorItem(getString(R.string.tier4));
                               for (int i = 1; i < 27; i++) {
                                   mAdapter.addItem(closings.get(i));
                                   if (i == 7) {
                                       mAdapter.addSeparatorItem(getString(R.string.tier3));
                                   } else if (i == 18) {
                                       mAdapter.addSeparatorItem(getString(R.string.tier2));
                                   } else if (i == 22) {
                                       mAdapter.addSeparatorItem(getString(R.string.tier1));
                                   }
                               }

//                               //Remove blank items
//                               for (int i = 0; i < closings.size(); i++) {
//                                   if (closings.get(i).equals("")) {
//                                      closings.remove(i);
//                                   }
//                               }

                               lstClosings.setAdapter(mAdapter);

                               runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       lstClosings.setVisibility(View.VISIBLE);
                                   }
                               });
                           }else{
                               System.out.println("WJRT has failed.");
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
                                System.out.println("NWS has not failed.");
                                mAdapter = new CustomAdapter();
                                mAdapter.addSeparatorItem(getString(R.string.NWS));
                                for (int i = 1; i < weatherCount - 1; i++) {
                                    mAdapter.addItem(nwsArray.get(i));
                                }
                                lstWeather.setAdapter(mAdapter);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        lstWeather.setVisibility(View.VISIBLE);
                                    }
                                });
                            }else{
                                System.out.println("NWS has failed.");
                                ArrayAdapter<String> NWSadapter = new ArrayAdapter<String>(getApplicationContext(),
                                        android.R.layout.simple_list_item_1, nwsInfo);
                                lstWJRT.setAdapter(NWSadapter);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        lstNWS.setVisibility(View.VISIBLE);

                                    }
                                });

                            }
                        }
                    });
                }
            }
        }







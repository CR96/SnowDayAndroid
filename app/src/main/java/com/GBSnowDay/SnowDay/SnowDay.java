package com.GBSnowDay.SnowDay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.Format;
import java.util.Calendar;
import java.util.Date;

public class SnowDay extends Activity {

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

        //Make sure the user doesn't try to run the program on the weekend or during school hours
        checkWeekend();
        checkTime();
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



    public void calculate(View view) {
        Intent i = new Intent(getApplicationContext(), SnowDayResult.class);
        startActivity(i);

    }

    private void checkWeekend() {

        TextView txtInfo = (TextView) findViewById(R.id.txtInfo);
        RadioButton optToday = (RadioButton) findViewById(R.id.optToday);
        RadioButton optTomorrow = (RadioButton) findViewById(R.id.optTomorrow);
        Spinner lstDays = (Spinner) findViewById(R.id.lstDays);

        //Friday is 6
        //Saturday is 7
        //Sunday is 1

        if (weekday == 6) {
            txtInfo.setText(this.getString(R.string.SaturdayTomorrow));
            optTomorrow.setEnabled(false);
            optToday.setChecked(true);
        }else if (weekday == 7) {
            txtInfo.setText(this.getString(R.string.SaturdayToday));
            optToday.setEnabled(false);
            optTomorrow.setEnabled(false);
            lstDays.setEnabled(false);
        }else if (weekday == 1) {
            txtInfo.setText(this.getString(R.string.SundayToday));
            optToday.setEnabled(false);
            optTomorrow.setChecked(true);
        }
    }

    private void checkTime() {

        RadioButton optToday = (RadioButton) findViewById(R.id.optToday);
        TextView txtInfo = (TextView) findViewById(R.id.txtInfo);

        if (calendar.get(Calendar.HOUR_OF_DAY) >= 7 && calendar.get(Calendar.HOUR_OF_DAY)<14 && weekday!=7 && weekday!=1) {
            optToday.setEnabled(false);
            //txtGB.setText("Grand Blanc: OPEN");
            txtInfo.setText(txtInfo.getText() + this.getString(R.string.SchoolOpen));
            dayrun = 1;
        }else if (calendar.get(Calendar.HOUR_OF_DAY) >=14 && weekday!=7 && weekday!=1) {
            optToday.setEnabled(false);
            //txtGB.setText("Grand Blanc: Dismissed");
            //txtGB.setBackground(Color.YELLOW);
            txtInfo.setText(txtInfo.getText() + this.getString(R.string.GBDismissed));
            dayrun = 1;
        }
    }


}

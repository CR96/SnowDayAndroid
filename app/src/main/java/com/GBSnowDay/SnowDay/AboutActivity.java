package com.GBSnowDay.SnowDay;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        ImageButton btnTwitter = (ImageButton) findViewById(R.id.btnTwitter);
        ImageButton btnMail = (ImageButton) findViewById(R.id.btnEmail);
        ImageButton btnWeb = (ImageButton) findViewById(R.id.btnWeb);
        ImageButton btnGit = (ImageButton) findViewById(R.id.btnGit);

        btnTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tw = new Intent(AboutActivity.this, TwitterActivity.class);
                startActivity(tw);
            }
        });

        btnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailing = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", getString(R.string.email), null));
                startActivity(emailing);

            }
        });

        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.website)));
                startActivity(browserIntent);
            }
        });

        btnGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.git)));
                startActivity(browserIntent);
            }
        });

        PackageInfo versionInfo = getPackageInfo();
        TextView txtVersion = (TextView) findViewById(R.id.txtVersion);
        txtVersion.setText(" v" + versionInfo.versionName);
    }

    private PackageInfo getPackageInfo() {
        PackageInfo pi = null;
        try {
            pi = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.license) {
            //Show the license dialog
            new EULADialog(this).show();
            return true;
        }else if (id == android.R.id.home) {
            //Return to the previous activity
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

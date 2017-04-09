/*
 * Copyright 2014-2017 Corey Rowe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.GBSnowDay.SnowDay.activity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.GBSnowDay.SnowDay.R;
import com.GBSnowDay.SnowDay.dialog.LicenseDialog;
import com.GBSnowDay.SnowDay.util.CustomTabsUtility;

public class AboutActivity extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        ImageButton btnTwitter = (ImageButton) findViewById(R.id.btnTwitter);
        ImageButton btnMail = (ImageButton) findViewById(R.id.btnEmail);
        ImageButton btnWeb = (ImageButton) findViewById(R.id.btnWeb);
        ImageButton btnGit = (ImageButton) findViewById(R.id.btnGit);

        TextView txtVersion = (TextView) findViewById(R.id.txtVersion);

        if (savedInstanceState == null) {

            Animation twitter = AnimationUtils.loadAnimation(this, R.anim.slide_in_overshoot);
            twitter.setStartOffset(500);
            btnTwitter.startAnimation(twitter);

            Animation mail = AnimationUtils.loadAnimation(this, R.anim.slide_in_overshoot);
            mail.setStartOffset(600);
            btnMail.startAnimation(mail);

            Animation web = AnimationUtils.loadAnimation(this, R.anim.slide_in_overshoot);
            web.setStartOffset(700);
            btnWeb.startAnimation(web);

            Animation git = AnimationUtils.loadAnimation(this, R.anim.slide_in_overshoot);
            git.setStartOffset(800);
            btnGit.startAnimation(git);

            Animation version = AnimationUtils.loadAnimation(this, R.anim.slide_in);
            version.setStartOffset(1300);
            txtVersion.startAnimation(version);
        }


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
                try {
                    // Try to use a custom tab
                    CustomTabsIntent customTabsIntent = CustomTabsUtility.prepareIntent(
                            AboutActivity.this, true);
                    Uri uri = Uri.parse(getString(R.string.website));
                    customTabsIntent.launchUrl(AboutActivity.this, uri);
                } catch (ActivityNotFoundException e) {
                    CustomTabsUtility.launchInBrowser(AboutActivity.this, getString(R.string.website));
                }
            }
        });

        btnGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Try to use a custom tab
                    CustomTabsIntent customTabsIntent = CustomTabsUtility.prepareIntent(
                            AboutActivity.this, true);
                    Uri uri = Uri.parse(getString(R.string.git));
                    customTabsIntent.launchUrl(AboutActivity.this, uri);
                } catch (ActivityNotFoundException e) {
                    CustomTabsUtility.launchInBrowser(AboutActivity.this, getString(R.string.git));
                }
            }
        });

        PackageInfo versionInfo = getPackageInfo();
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
            new LicenseDialog(this).show();
            return true;
        }else if (id == android.R.id.home) {
            //Return to the previous activity
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

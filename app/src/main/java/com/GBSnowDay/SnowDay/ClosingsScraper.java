package com.GBSnowDay.SnowDay;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import com.crashlytics.android.Crashlytics;

import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

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

class ClosingsScraper extends AsyncTask<Void, Void, ClosingsData> {

    private ClosingsData closingsData;

    private ArrayList<String> orgNames = new ArrayList<>();
    private ArrayList<String> orgStatuses = new ArrayList<>();

    private int dayrun;
    private String weekdaytoday;
    private String weekdaytomorrow;

    private AsyncResponse delegate = null;
    private Resources res;

    interface AsyncResponse {
        void processFinish(ClosingsData closingsData);
    }

    ClosingsScraper(Context context, int i, AsyncResponse delegate) {
        res = context.getResources();
        dayrun = i;
        this.delegate = delegate;
    }

    @Override
    protected ClosingsData doInBackground(Void...params) {
        closingsData = new ClosingsData();

        Document schools = null;
        try {
            schools = Jsoup.connect(
                    res.getString(R.string.ClosingsURL))
                    .timeout(10000)
                    .get();

            Element table = schools.select("table").last();
            Elements rows = table.select("tr");

            for (int i = 1; i < rows.size(); i++) { //Skip header row
                Element row = rows.get(i);
                orgNames.add(row.select("td").get(0).text());
                orgStatuses.add(row.select("td").get(1).text());
            }
        }catch (IOException e) {
            //Connectivity issues
            closingsData.error = res.getString(R.string.WJRTConnectionError);
            Crashlytics.logException(e);
            cancel(true);
        }catch (NullPointerException | IndexOutOfBoundsException e) {
            /* This shows in place of the table (as plain text)
            if no schools or institutions are closed. */
            if (schools != null && !schools.text().contains("no closings or delays")) {
                //Webpage layout was not recognized.
                closingsData.error = res.getString(R.string.WJRTParseError);
                Crashlytics.logException(e);
                cancel(true);
            }
        }finally{
            parseClosings();
        }
        return closingsData;
    }

    private void parseClosings() {

        DateTime today = new DateTime();

        //Get the day of the week as a string.
        weekdaytoday = today.dayOfWeek().getAsText();

        //Get tomorrow's weekday as a string.
        DateTime tomorrow = new DateTime();

        weekdaytomorrow = tomorrow.plusDays(1).dayOfWeek().getAsText();

        //Sanity check - make sure Grand Blanc isn't already closed before predicting
        closingsData.GB = isClosed(
                res.getStringArray(R.array.checks_gb),
                res.getString(R.string.GB),
                true,
                -1);

        if (closingsData.GB) {
            closingsData.GBText.add(res.getString(R.string.SnowDay));
            closingsData.GBSubtext.add(null);
        } else {
            if (dayrun == 0) {
                if (today.getHourOfDay() >= 7 && today.getHourOfDay() < 16) {
                    //Time is between 7AM and 4PM. School is already in session.
                    closingsData.GBText.add(res.getString(R.string.SchoolOpen));
                    closingsData.GBSubtext.add(null);
                    closingsData.GBOpen = true;
                } else if (today.getHourOfDay() >= 16) {
                    //Time is after 4PM. School is already out.
                    closingsData.GBText.add(res.getString(R.string.Dismissed));
                    closingsData.GBSubtext.add(null);
                    closingsData.GBOpen = true;
                }
            }
        }

        //Check school closings
        String[] tier1schools = res.getStringArray(R.array.name_t1);
        String[] tier2schools = res.getStringArray(R.array.name_t2);
        String[] tier3schools = res.getStringArray(R.array.name_t3);
        String[] tier4schools = res.getStringArray(R.array.name_t4);

        //Tier 4
        closingsData.Atherton = isClosed(
                res.getStringArray(R.array.checks_atherton),
                tier4schools[0],
                false,
                4);
        closingsData.Bendle = isClosed(
                res.getStringArray(R.array.checks_bendle),
                tier4schools[1],
                false,
                4);
        closingsData.Bentley = isClosed(
                res.getStringArray(R.array.checks_bentley),
                tier4schools[2],
                false,
                4);
        closingsData.Carman = isClosed(
                res.getStringArray(R.array.checks_carman),
                tier4schools[3],
                false,
                4);
        closingsData.Flint = isClosed(
                res.getStringArray(R.array.checks_flint),
                tier4schools[4],
                false,
                4);
        closingsData.Goodrich = isClosed(
                res.getStringArray(R.array.checks_goodrich),
                tier4schools[5],
                false,
                4);

        //Tier 3
        closingsData.Beecher = isClosed(
                res.getStringArray(R.array.checks_beecher),
                tier3schools[0],
                false,
                3);
        closingsData.Clio = isClosed(
                res.getStringArray(R.array.checks_clio),
                tier3schools[1],
                false,
                3);
        closingsData.Davison = isClosed(
                res.getStringArray(R.array.checks_davison),
                tier3schools[2],
                false,
                3);
        closingsData.Fenton = isClosed(
                res.getStringArray(R.array.checks_fenton),
                tier3schools[3],
                false,
                3);
        closingsData.Flushing = isClosed(
                res.getStringArray(R.array.checks_flushing),
                tier3schools[4],
                false,
                3);
        closingsData.Genesee = isClosed(
                res.getStringArray(R.array.checks_genesee),
                tier3schools[5],
                false,
                3);
        closingsData.Kearsley = isClosed(
                res.getStringArray(R.array.checks_kearsley),
                tier3schools[6],
                false,
                3);
        closingsData.LKFenton = isClosed(
                res.getStringArray(R.array.checks_lkfenton),
                tier3schools[7],
                false,
                3);
        closingsData.Linden = isClosed(
                res.getStringArray(R.array.checks_linden),
                tier3schools[8],
                false,
                3);
        closingsData. Montrose = isClosed(
                res.getStringArray(R.array.checks_montrose),
                tier3schools[9],
                false,
                3);
        closingsData.Morris = isClosed(
                res.getStringArray(R.array.checks_morris),
                tier3schools[10],
                false,
                3);
        closingsData.SzCreek = isClosed(
                res.getStringArray(R.array.checks_szcreek),
                tier3schools[11],
                false,
                3);

        //Tier 2
        closingsData.Durand = isClosed(
                res.getStringArray(R.array.checks_durand),
                tier2schools[0],
                false,
                2);
        closingsData.Holly = isClosed(
                res.getStringArray(R.array.checks_holly),
                tier2schools[1],
                false,
                2);
        closingsData.Lapeer = isClosed(
                res.getStringArray(R.array.checks_lapeer),
                tier2schools[2],
                false,
                2);
        closingsData.Owosso = isClosed(
                res.getStringArray(R.array.checks_owosso),
                tier2schools[3],
                false,
                2);

        //Tier 1
        closingsData.GBAcademy = isClosed(
                res.getStringArray(R.array.checks_gbacademy),
                tier1schools[0],
                false,
                1);
        closingsData.GISD = isClosed(
                res.getStringArray(R.array.checks_gisd),
                tier1schools[1],
                false,
                1);
        closingsData.HolyFamily = isClosed(
                res.getStringArray(R.array.checks_holyfamily),
                tier1schools[2],
                false,
                1);
        closingsData.WPAcademy = isClosed(
                res.getStringArray(R.array.checks_wpacademy),
                tier1schools[3],
                false,
                1);

        //Set the schoolpercent
        if (closingsData.tier1 > 2) {
            //3+ academies are closed. 20% schoolpercent.
            closingsData.schoolpercent = 20;
        }
        if (closingsData.tier2 > 2) {
            //3+ schools in nearby counties are closed. 40% schoolpercent.
            closingsData.schoolpercent = 40;
        }
        if (closingsData.tier3 > 2) {
            //3+ schools in Genesee County are closed. 60% schoolpercent.
            closingsData.schoolpercent = 60;
        }
        if (closingsData.tier4 > 2) {
            //3+ schools near GB are closed. 80% schoolpercent.
            closingsData.schoolpercent = 80;
            if (closingsData.Carman) {
                //Carman is closed along with 2+ close schools. 90% schoolpercent.
                closingsData.schoolpercent = 90;
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
                        closingsData.GBMessage = true;
                        closingsData.GBText.add(schoolName);
                        closingsData.GBSubtext.add(orgStatuses.get(i));
                    } else {
                        closingsData.displayedOrgNames.add(schoolName);
                        closingsData.displayedOrgStatuses.add(orgStatuses.get(i));
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
                                    closingsData.tier1++;
                                case 2:
                                    closingsData.tier2++;
                                case 3:
                                    closingsData.tier3++;
                                case 4:
                                    closingsData.tier4++;
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
            closingsData.GBText.add(schoolName);
            closingsData.GBSubtext.add(res.getString(R.string.Open));
        }else if (!schoolFound){
            closingsData.displayedOrgNames.add(schoolName);
            closingsData.displayedOrgStatuses.add(res.getString(R.string.Open));
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

    @Override
    protected void onPostExecute(ClosingsData closingsData) {
        delegate.processFinish(closingsData);
    }

    @Override
    protected void onCancelled(ClosingsData closingsData) {
        delegate.processFinish(closingsData);
    }
}

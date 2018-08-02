/*
 * Copyright 2014-2018 Corey Rowe
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

package com.GBSnowDay.SnowDay.network;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import com.GBSnowDay.SnowDay.R;
import com.GBSnowDay.SnowDay.model.WeatherModel;
import com.crashlytics.android.Crashlytics;

import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherScraper extends AsyncTask<Void, Void, List<WeatherModel>> {

    /**
     * This is an array of {@link WeatherModel} objects.
     * WeatherModel is a custom object containing the information needed to display a single warning.
     */
    private List<WeatherModel> weatherModels;

    private final int dayrun;
    private boolean weatherWarningPresent;
    private int weatherPercent;

    private String error;

    private AsyncResponse delegate = null;

    // The parsable format of warning expiration times as present in the RSS feed
    private final SimpleDateFormat sdfInput = new SimpleDateFormat
            ("yyyy-MM-dd'T'HH:mm", Locale.US);

    // The readable format of warning expiration times as seen by the user
    private final SimpleDateFormat sdfOutput = new SimpleDateFormat
            ("MMMM dd 'at' h:mm a", Locale.US);

    private final Resources res;

    // This interface serves as a delegate that passes the array of WeatherModel objects
    // to the UI thread after the AsyncTask finishes running.
    // It contains two overloaded processFinish() methods: one that returns if the scraper
    // succeeds, and one that only returns an error message if it fails.
    public interface AsyncResponse {
        void processFinish(List<WeatherModel> weatherModel,
                           int weatherPercent,
                           boolean isWeatherWarningPresent);

        void processFinish(String error);
    }

    /**
     * Constructs a new WeatherScraper.
     * @param context The Context from the calling Activity used to access resources
     * @param dayrun Whether the calculation was run for "today" or "tomorrow" (inputted by user)
     * @param delegate The interface implementation used to pass the array of weather warnings
     */
    public WeatherScraper(Context context, int dayrun, AsyncResponse delegate) {
        res = context.getResources();
        this.dayrun = dayrun;
        this.delegate = delegate;
    }

    /**
     * Retrieve and parse the RSS feed asynchronously.
     * @param params void (unused as only a single fixed URL is accessed)
     * @return the array of {@link WeatherModel} objects
     */
    @Override
    protected List<WeatherModel> doInBackground(Void...params) {
        weatherModels = new ArrayList<>();

        try {
            Document weather = Jsoup.connect(
                    res.getString(R.string.WeatherURL))
                    .timeout(10000)
                    .get();

            // Various elements present in the RSS feed
            Elements title = weather.select("title");
            Elements summary = weather.select("summary");
            Elements expiretime = weather.select("cap|expires");
            Elements link = weather.select("link");

            if (title != null) {
                for (int i = 0; i < title.size(); i++) {
                    // The warning titles in the RSS feed contain the text "issued by NWS at ...".
                    // This removes that portion of the warning title if present for a cleaner
                    // appearance in the displayed list. The expiration time is shown separately.
                    // Example: "Winter Storm Warning" vs. "Winter Storm Warning issued by NWS at ..."
                    int stringend = title.get(i).text().indexOf("issued");
                    if (stringend != -1) {
                        weatherModels.add(new WeatherModel(
                                title.get(i).text().substring(0, stringend)));
                    } else {
                        weatherModels.add(new WeatherModel(
                                title.get(i).text()));
                    }
                }

                if (!title.get(1).text().contains("no active")) {
                    //Weather warnings are present.
                    weatherWarningPresent = true;
                }
            }

            // The first Title element in the feed is the feed title, not a warning title.
            // The text of this element is displayed as a header above any warnings present.
            // The WeatherAdapter class compensates for this when constructing the CardViews
            // seen in the Weather fragment. Only subsequent array elements contain warning
            // information, hence the use of "i + 1" in the following statements.

            if (expiretime != null) {
                Date time;
                String readableTime;
                for (int i = 0; i < expiretime.size(); i++) {
                    String expireTime = expiretime.get(i).text();
                    time = sdfInput.parse(expireTime);
                    readableTime = sdfOutput.format(time);

                    weatherModels.get(i + 1)
                            .setWarningExpireTime(expireTime);
                    weatherModels.get(i + 1)
                            .setWarningReadableTime("Expires " + readableTime);
                }
            }

            if (summary != null) {
                for (int i = 0; i < summary.size(); i++) {
                    weatherModels.get(i + 1)
                        .setWarningSummary(summary.get(i).text() + "...");
                }
            }

            if (link != null) {
                link.remove(0); //Intentionally remove the root-level link tag
                for (int i = 0; i < link.size(); i++) {
                    weatherModels.get(i + 1)
                        .setWarningLink(link.get(i).attr("href"));
                }
            }

            // These method calls check for the presence of specific warnings.
            // If one is present, weatherPercent is set to the corresponding value.
            // More severe warnings result in a higher weatherPercent value.

            //Significant Weather Advisory
            checkWeatherWarning(res.getString(R.string.SigWeather), 15);

            //Winter Weather Advisory
            checkWeatherWarning(res.getString(R.string.WinterAdvisory), 30);

            //Lake Effect Snow Advisory
            checkWeatherWarning(res.getString(R.string.LakeSnowAdvisory), 40);

            //Freezing Rain
            checkWeatherWarning(res.getString(R.string.Rain), 40);

            //Freezing Drizzle
            checkWeatherWarning(res.getString(R.string.Drizzle), 40);

            //Freezing Fog
            checkWeatherWarning(res.getString(R.string.Fog), 40);

            //Wind Chill Advisory
            checkWeatherWarning(res.getString(R.string.WindChillAdvisory), 40);

            //Ice Storm Warning
            checkWeatherWarning(res.getString(R.string.IceStorm), 70);

            //Wind Chill Watch
            checkWeatherWarning(res.getString(R.string.WindChillWatch), 70);

            //Wind Chill Warning
            checkWeatherWarning(res.getString(R.string.WindChillWarn), 70);

            //Winter Storm Watch
            checkWeatherWarning(res.getString(R.string.WinterWatch), 80);

            //Winter Storm Warning
            checkWeatherWarning(res.getString(R.string.WinterWarn), 80);

            //Lake Effect Snow Watch
            checkWeatherWarning(res.getString(R.string.LakeSnowWatch), 80);

            //Lake Effect Snow Warning
            checkWeatherWarning(res.getString(R.string.LakeSnowWarn), 80);

            //Blizzard Watch
            checkWeatherWarning(res.getString(R.string.BlizzardWatch), 90);

            //Blizzard Warning
            checkWeatherWarning(res.getString(R.string.BlizzardWarn), 90);

        }catch (IOException e) {
            //Connectivity issues
            error = res.getString(R.string.WeatherConnectionError);
            Crashlytics.logException(e);
            cancel(true);
        }catch (NullPointerException | IndexOutOfBoundsException | ParseException e) {
            //RSS layout was not recognized.
            error = res.getString(R.string.WeatherParseError);
            Crashlytics.logException(e);
            cancel(true);
        }

        return weatherModels;
    }

    /**Check for the presence of weather warnings.
     * Only the highest weather percent is stored (not cumulative).
     * Calculation is affected based on when the warning expires and the day the user selected.
     * @param warn The string identifying the warning to search for
     * @param weight The value weatherPercent is set to if the warning is found
     * @throws ParseException if the RSS layout is not recognized
     */
    private void checkWeatherWarning(String warn, int weight) throws ParseException {
        DateTime warningDate = null;
        DateTime today = new DateTime();
        DateTime tomorrow = today.plusDays(1).withHourOfDay(0); //Midnight tomorrow
        for (int i = 0; i < weatherModels.size(); i++) {
            if (weatherModels.get(i).getWarningTitle().contains(warn)) {
                warningDate = new DateTime(sdfInput.parse(
                        weatherModels.get(i).getWarningExpireTime()));
            }

            if (warningDate != null) {
                if (dayrun == 0) { // If the weather warning is present, it has an effect today.
                    weatherPercent = weight;
                } else if ((warningDate.isEqual(tomorrow) || warningDate.isAfter(tomorrow))) {
                    //If the weather warning expires at or after midnight tomorrow, it has an effect.
                    weatherPercent = weight;
                }
            }
        }
    }

    //Scraper succeeded: pass the list of objects to the calling Activity.
    @Override
    protected void onPostExecute(List<WeatherModel> weatherModels) {
        delegate.processFinish(weatherModels, weatherPercent, weatherWarningPresent);
    }

    //Scraper failed: pass the error message to the calling Activity.
    @Override
    protected void onCancelled() {
        delegate.processFinish(error);
    }
}
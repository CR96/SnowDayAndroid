package com.GBSnowDay.SnowDay.network;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import com.GBSnowDay.SnowDay.R;
import com.GBSnowDay.SnowDay.model.WeatherData;
import com.crashlytics.android.Crashlytics;

import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

public class WeatherScraper extends AsyncTask<Void, Void, WeatherData> {

    private WeatherData weatherData;

    private int dayrun;

    private SimpleDateFormat sdfInput = new SimpleDateFormat
            ("yyyy-MM-dd'T'HH:mm", Locale.US);
    private SimpleDateFormat sdfOutput = new SimpleDateFormat
            ("MMMM dd 'at' h:mm a", Locale.US);

    private AsyncResponse delegate = null;
    private Resources res;

    public interface AsyncResponse {
        void processFinish(WeatherData weatherData);
    }

    public WeatherScraper(Context context, int i, AsyncResponse delegate) {
        res = context.getResources();
        dayrun = i;
        this.delegate = delegate;
    }

    @Override
    protected WeatherData doInBackground(Void...params) {
        weatherData = new WeatherData();

        try {
            Document weather = Jsoup.connect(
                    res.getString(R.string.WeatherURL))
                    .timeout(10000)
                    .get();

            Elements title = weather.select("title");
            Elements summary = weather.select("summary");
            Elements expiretime = weather.select("cap|expires");
            Elements link = weather.select("link");

            if (title != null) {
                for (int i = 0; i < title.size(); i++) {
                    int stringend = title.get(i).text().indexOf("issued");
                    if (stringend != -1) {
                        weatherData.warningTitles.add(title.get(i).text().substring(0, stringend));
                    } else {
                        weatherData.warningTitles.add(title.get(i).text());
                    }
                }

                if (!weatherData.warningTitles.get(1).contains("no active")) {
                    //Weather warnings are present.
                    weatherData.weatherWarningsPresent = true;
                }
            }
            if (expiretime != null) {
                Date date;
                String readableDate;
                for (int i = 0; i < expiretime.size(); i++) {
                    weatherData.warningExpireTimes.add(expiretime.get(i).text());
                    date = sdfInput.parse(weatherData.warningExpireTimes.get(i));
                    readableDate = sdfOutput.format(date);
                    weatherData.warningReadableTimes.add("Expires " + readableDate);
                }
            }

            if (summary != null) {
                for (int i = 0; i < summary.size(); i++) {
                    weatherData.warningSummaries.add(summary.get(i).text() + "...");
                }
            }

            if (link != null) {
                for (int i = 0; i < link.size(); i++) {
                    weatherData.warningLinks.add(link.get(i).attr("href"));
                }
            }
        }catch (IOException e) {
            //Connectivity issues
            weatherData.error = res.getString(R.string.WeatherConnectionError);
            Crashlytics.logException(e);
            cancel(true);
        }catch (NullPointerException | IndexOutOfBoundsException | ParseException e) {
            //RSS layout was not recognized.
            weatherData.error = res.getString(R.string.WeatherParseError);
            Crashlytics.logException(e);
            cancel(true);
        }finally{
            parseWeather();
        }
        return weatherData;
    }

    private void parseWeather() {
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
    }

    /**Check for the presence of weather warnings.
     * Only the highest weather percent is stored (not cumulative).
     * Calculation is affected based on when warning expires.
     * @param warn The string identifying the warning to search for
     * @param weight The value weatherpercent is set to if the warning is found
     */
    private void checkWeatherWarning(String warn, int weight) {
        DateTime warningDate = null;
        DateTime today = new DateTime();
        DateTime tomorrow;
        for (int i = 0; i < weatherData.warningTitles.size(); i++) {
            if (weatherData.warningTitles.get(i).contains(warn)) {
                try {
                    warningDate = new DateTime(sdfInput.parse(
                            weatherData.warningExpireTimes.get(i - 1)));
                } catch (ParseException e) {
                    //RSS layout was not recognized.
                    weatherData.error = res.getString(R.string.WeatherParseError);
                    Crashlytics.logException(e);
                    cancel(true);
                }
                tomorrow = today.plusDays(1);
                if (warningDate != null) {
                    if ((warningDate.isEqual(today) || warningDate.isAfter(today))
                            && (dayrun == 0)) {
                        weatherData.weatherpercent = weight;
                    } else if ((warningDate.isEqual(tomorrow) || warningDate.isAfter(tomorrow))
                            && (dayrun == 1)) {
                        weatherData.weatherpercent = weight;
                    }
                }
            }
        }
    }

    @Override
    protected void onPostExecute(WeatherData weatherData) {
        delegate.processFinish(weatherData);
    }

    @Override
    protected void onCancelled(WeatherData weatherData) {
        delegate.processFinish(weatherData);
    }
}

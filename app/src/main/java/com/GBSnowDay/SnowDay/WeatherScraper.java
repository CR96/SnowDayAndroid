package com.GBSnowDay.SnowDay;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import com.crashlytics.android.Crashlytics;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

class WeatherScraper extends AsyncTask<Void, Void, WeatherScraper.WeatherData> {

    class WeatherData {
        ArrayList<String> warningTitles = new ArrayList<>();
        ArrayList<String> warningExpireTimes = new ArrayList<>();
        ArrayList<String> warningReadableTimes = new ArrayList<>();
        ArrayList<String> warningSummaries = new ArrayList<>();
        ArrayList<String> warningLinks = new ArrayList<>();
        boolean weatherWarningsPresent;
        String error;
    }

    interface AsyncResponse {
        void processFinish(WeatherData weatherData);
    }

    private AsyncResponse delegate = null;
    private Resources res;

    WeatherScraper(Context context, AsyncResponse delegate) {
        res = context.getResources();
        this.delegate = delegate;
    }

    @Override
    protected WeatherData doInBackground(Void...params) {
        WeatherData weatherData = new WeatherData();

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
                SimpleDateFormat sdfInput = new SimpleDateFormat
                        ("yyyy-MM-dd'T'HH:mm", Locale.US);
                SimpleDateFormat sdfOutput = new SimpleDateFormat
                        ("MMMM dd 'at' h:mm a", Locale.US);
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
            //Webpage layout was not recognized.
            weatherData.error = res.getString(R.string.WeatherParseError);
            Crashlytics.logException(e);
            cancel(true);
        }
        return weatherData;
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

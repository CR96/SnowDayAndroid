package com.GBSnowDay.SnowDay;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import com.crashlytics.android.Crashlytics;

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

class ClosingsScraper extends AsyncTask<Void, Void, ClosingsScraper.ClosingsData> {

    class ClosingsData {
        ArrayList<String> orgNames = new ArrayList<>();
        ArrayList<String> orgStatuses = new ArrayList<>();
        String error;
    }

    interface AsyncResponse {
        void processFinish(ClosingsData closingsData);
    }

    private AsyncResponse delegate = null;
    private Resources res;

    ClosingsScraper(Context context, AsyncResponse delegate) {
        res = context.getResources();
        this.delegate = delegate;
    }

    @Override
    protected ClosingsData doInBackground(Void...params) {
        ClosingsData closingsData = new ClosingsData();

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
                closingsData.orgNames.add(row.select("td").get(0).text());
                closingsData.orgStatuses.add(row.select("td").get(1).text());
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
        }
        return closingsData;
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

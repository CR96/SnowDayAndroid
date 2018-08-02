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

package com.GBSnowDay.SnowDay.model;

import android.content.Context;
import android.content.res.Resources;

import com.GBSnowDay.SnowDay.R;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class EventModel {
    private final ArrayList<String> infoList = new ArrayList<>();
    private boolean todayValid = true;
    private boolean tomorrowValid = true;
    private boolean eventPresent;
    private boolean bobcats;

    //Figure out what tomorrow is
    //Saturday = 6, Sunday = 7
    private final DateTime dt = new DateTime();
    private final int weekday = dt.getDayOfWeek();

    private final Resources res;

    /**
     * Make sure the user doesn't try to run the program on the weekend or on specific dates.
     * @param context Application context used to access string resources
     */
    public EventModel(Context context) {
        res = context.getResources();

        checkDate();

        //Only run checkWeekend() if today or tomorrow is still valid
        if (todayValid || tomorrowValid) {
            checkWeekend();
        }
    }

    /** @return whether today is valid **/
    public boolean getTodayValid() {
        return todayValid;
    }

    /** @return whether tomorrow is valid **/
    public boolean getTomorrowValid() {
        return tomorrowValid;
    }

    /** @return whether an event is present (affects list entry color) **/
    public boolean getEventPresent() {
        return eventPresent;
    }

    /** @return whether the program is run on the day of commencement (affects list entry color) **/
    public boolean getBobcats() {
        return bobcats;
    }

    /** @return the list to be populated in the initial activity's RecyclerView **/
    public ArrayList<String> getInfoList() {
        return infoList;
    }

    private void checkDate() {

        //Set the current month, day, and year
        SimpleDateFormat currentDate = new SimpleDateFormat("MMMM dd yyyy", Locale.US);
        String textdate = currentDate.format(dt.toDate());

        infoList.add(0, "Current Date: " + textdate);

        /*Check for days school is not in session (such as Winter Break, development days, etc.)
        Uses a mixture of SimpleDateFormat for simple string comparison and JodaTime for more
        complicated arguments*/

        if (dt.getMonthOfYear() == 6 && dt.getDayOfMonth() > 14) {
            //Summer break (June)
            infoList.add(res.getString(R.string.Summer));
            eventPresent = true;
            todayValid = false;
            tomorrowValid = false;
        } else if (dt.getMonthOfYear() > 6 && dt.getMonthOfYear() <= 8) {
            //Summer break (July and August)
            infoList.add(res.getString(R.string.Summer));
            eventPresent = true;
            todayValid = false;
            tomorrowValid = false;
        } else if (dt.getMonthOfYear() == 9 && dt.getDayOfMonth() < 3) {
            //Summer break (September)
            infoList.add(res.getString(R.string.Summer));
            eventPresent = true;
            todayValid = false;
            tomorrowValid = false;
        } else if (textdate.equals("September 03 2018")) {
            infoList.add(res.getString(R.string.YearStart));
            eventPresent = true;
            todayValid = false;
        }
    }

    private void checkWeekend() {
        //Friday is 5
        //Saturday is 6
        //Sunday is 7

        if (weekday == 5) {
            infoList.add(res.getString(R.string.SaturdayTomorrow));
            tomorrowValid = false;
            eventPresent = true;
        } else if (weekday == 6) {
            infoList.add(res.getString(R.string.SaturdayToday));
            todayValid = false;
            tomorrowValid = false;
            eventPresent = true;
        } else if (weekday == 7) {
            infoList.add(res.getString(R.string.SundayToday));
            todayValid = false;
            eventPresent = true;
        }
    }
}

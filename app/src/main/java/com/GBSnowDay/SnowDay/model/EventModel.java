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
        } else if (dt.getMonthOfYear() == 9 && dt.getDayOfMonth() < 5) {
            //Summer break (September)
            infoList.add(res.getString(R.string.Summer));
            eventPresent = true;
            todayValid = false;
            tomorrowValid = false;
        } else if (textdate.equals("September 05 2016")) {
            infoList.add(res.getString(R.string.YearStart));
            eventPresent = true;
            todayValid = false;
        } else if (textdate.equals("October 07 2016")) {
            infoList.add(res.getString(R.string.HC));
            eventPresent = true;
        } else if (textdate.equals("October 11 2016") || textdate.equals("December 06 2016")
                || textdate.equals("January 31 2017") || textdate.equals("May 02 2017")) {
            infoList.add(res.getString(R.string.LSTomorrow));
            eventPresent = true;
        } else if (textdate.equals("October 12 2016") || textdate.equals("December 07 2016")
                || textdate.equals("February 01 2017") || textdate.equals("May 03 2017")) {
            infoList.add(res.getString(R.string.LSToday));
            eventPresent = true;
        } else if (textdate.equals("November 24 2016")) {
            infoList.add(res.getString(R.string.Thanksgiving));
            eventPresent = true;
            todayValid = false;
            tomorrowValid = false;
        } else if (textdate.equals("November 24 2016") || textdate.equals("November 25 2016")) {
            infoList.add(res.getString(R.string.ThanksgivingRecess));
            eventPresent = true;
            todayValid = false;
        } else if (textdate.equals("December 21 2016")) {
            infoList.add(res.getString(R.string.WinterBreakTomorrow));
            eventPresent = true;
            tomorrowValid = false;
        } else if (textdate.equals("December 22 2016") || textdate.equals("December 23 2016")
                || textdate.equals("December 24 2016") || textdate.equals("December 25 2016")
                || textdate.equals("December 26 2016") || textdate.equals("December 27 2016")
                || textdate.equals("December 28 2016") || textdate.equals("December 29 2016")
                || textdate.equals("December 30 2016") || textdate.equals("December 31 2016")
                || textdate.equals("January 01 2017") || textdate.equals("January 02 2017")) {
            //Winter Break
            //noinspection IfCanBeSwitch
            if (textdate.equals("December 25 2016")) {
                infoList.add(res.getString(R.string.Christmas));
                eventPresent = true;
                todayValid = false;
                tomorrowValid = false;
            } else if (textdate.equals("January 01 2017")) {
                infoList.add(res.getString(R.string.NewYear));
                eventPresent = true;
                todayValid = false;
                tomorrowValid = false;
            } else if (textdate.equals("January 02 2017")) {
                eventPresent = true;
                todayValid = false;
            } else {
                eventPresent = true;
                todayValid = false;
                tomorrowValid = false;
            }
            infoList.add(res.getString(R.string.WinterBreak));
        } else if (textdate.equals("January 15 2017")) {
            infoList.add(res.getString(R.string.MLKTomorrow) + res.getString(R.string.NoSessionTomorrow));
            eventPresent = true;
            todayValid = false;
            tomorrowValid = false;
        } else if (textdate.equals("January 16 2017")) {
            //MLK Day
            infoList.add(res.getString(R.string.MLK) + res.getString(R.string.NoSessionToday));
            eventPresent = true;
            todayValid = false;
        } else if (textdate.equals("January 22 2017")) {
            infoList.add(res.getString(R.string.RecordsTomorrow) + res.getString(R.string.NoSessionTomorrow));
            eventPresent = true;
            tomorrowValid = false;
        } else if (textdate.equals("January 23 2017")) {
            infoList.add(res.getString(R.string.Records) + res.getString(R.string.NoSessionToday));
            eventPresent = true;
            todayValid = false;
            //Lincoln's birthday is on a Saturday in 2017.
        /*}else if (textdate.equals("February 11 2017")) {
            infoList.add(res.getString(R.string.LincolnTomorrow) + res.getString(R.string.NoSessionTomorrow));
            eventPresent = true;
            tomorrowValid = false;
        } else if (textdate.equals("February 12 2017")) {
            infoList.add(res.getString(R.string.Lincoln) + res.getString(R.string.NoSessionToday));
            eventPresent = true;
            todayValid = false;*/
        } else if (textdate.equals("February 16 2017")) {
            //This is the Thursday leading into "President's Weekend"
            infoList.add(res.getString(R.string.TomorrowGeneric));
            eventPresent = true;
            tomorrowValid = false;
        } else if (textdate.equals("February 17 2017")) {
            //Friday of "President's Weekend"
            infoList.add(res.getString(R.string.TodayGeneric));
            eventPresent = true;
            todayValid = false;
        } else if (textdate.equals("February 19 2017")) {
            infoList.add(res.getString(R.string.PresidentTomorrow) + res.getString(R.string.NoSessionTomorrow));
            eventPresent = true;
            todayValid = false;
            tomorrowValid = false;
        } else if (textdate.equals("February 20 2017")) {
            infoList.add(res.getString(R.string.President) + res.getString(R.string.NoSessionToday));
            eventPresent = true;
            todayValid = false;
        } else if (textdate.equals("November 15 2016") || textdate.equals("March 07 2017")) {
            infoList.add(res.getString(R.string.HalfDayConferenceMSTomorrow));
            eventPresent = true;
        } else if (textdate.equals("November 16 2016") || textdate.equals("November 17 2016")
                || textdate.equals("March 08 2017") || textdate.equals("March 09 2017")) {
            infoList.add(res.getString(R.string.HalfDayConferenceMSTodayTomorrow));
            eventPresent = true;
        } else if (textdate.equals("November 18 2016") || textdate.equals("March 10 2017")) {
            infoList.add(res.getString(R.string.HalfDayConferenceMSToday));
            eventPresent = true;
        } else if (textdate.equals("October 20 2016")) {
            infoList.add(res.getString(R.string.HalfDayHSTomorrow));
            eventPresent = true;
        } else if (textdate.equals("October 21 2016")) {
            infoList.add(res.getString(R.string.HalfDayHSToday));
            eventPresent = true;
        } else if (textdate.equals("October 06 2016") || textdate.equals("November 22 2016")
                || textdate.equals("March 30 2017")) {
            infoList.add(res.getString(R.string.HalfDayTomorrow));
            eventPresent = true;
        } else if (textdate.equals("October 07 2016") || textdate.equals("November 23 2016")
                || textdate.equals("March 31 2017")) {
            if (textdate.equals("November 23 2017")) {
                infoList.add(res.getString(R.string.ThanksgivingRecessTomorrow));
                eventPresent = true;
                tomorrowValid = false;
            }

            infoList.add(res.getString(R.string.HalfDay));
            eventPresent = true;
        } else if (textdate.equals("April 13 2017")) {
            infoList.add(res.getString(R.string.GoodFridayTomorrow) + res.getString(R.string.NoSessionTomorrow));
            eventPresent = true;
            tomorrowValid = false;
        } else if (textdate.equals("April 14 2017")) {
            infoList.add(res.getString(R.string.GoodFriday) + res.getString(R.string.NoSessionToday));
            eventPresent = true;
            todayValid = false;
        } else if (textdate.equals("April 16 2017")) {
            infoList.add(res.getString(R.string.Easter));
            eventPresent = true;
            todayValid = false;
        } else if (textdate.equals("March 31 2017")) {
            infoList.add(res.getString(R.string.HalfDay));
            infoList.add(res.getString(R.string.SpringBreakTomorrow));
            eventPresent = true;
            tomorrowValid = false;
        } else if (textdate.equals("April 01 2017") || textdate.equals("April 02 2017")
                || textdate.equals("April 03 2017") || textdate.equals("April 04 2017")
                || textdate.equals("April 05 2017") || textdate.equals("April 06 2017")
                || textdate.equals("April 07 2017")) {
            //Spring Break

            infoList.add(res.getString(R.string.SpringBreak));
            eventPresent = true;
            todayValid = false;
            tomorrowValid = false;
        } else if (textdate.equals("November 07 2016")) {
            infoList.add(res.getString(R.string.PDDTomorrow) + res.getString(R.string.NoSessionTomorrow));
            infoList.add("Don't forget to vote tomorrow!");
            eventPresent = true;
            tomorrowValid = false;
        } else if (textdate.equals("November 08 2016")) {
            infoList.add(res.getString(R.string.PDD) + res.getString(R.string.NoSessionToday));
            infoList.add("Don't forget to vote today!");
            eventPresent = true;
            todayValid = false;
        } else if (textdate.equals("May 28 2017")) {
            infoList.add(res.getString(R.string.MemorialDayTomorrow) + res.getString(R.string.NoSessionTomorrow));
            eventPresent = true;
            tomorrowValid = false;
        } else if (textdate.equals("May 29 2017")) {
            infoList.add(res.getString(R.string.MemorialDay) + res.getString(R.string.NoSessionToday));
            eventPresent = true;
            todayValid = false;
        } else if (textdate.equals("June 01 2017")) {
            infoList.add(res.getString(R.string.Senior));
            eventPresent = true;
            bobcats = true;
        } else if (textdate.equals("June 14 2017")) {
            infoList.add(res.getString(R.string.YearEnd));
            eventPresent = true;
            tomorrowValid = false;
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

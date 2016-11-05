package com.GBSnowDay.SnowDay.model;

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

public class WeatherModel {
    private ArrayList<String> warningTitles = new ArrayList<>();
    private ArrayList<String> warningExpireTimes = new ArrayList<>();
    private ArrayList<String> warningReadableTimes = new ArrayList<>();
    private ArrayList<String> warningSummaries = new ArrayList<>();
    private ArrayList<String> warningLinks = new ArrayList<>();

    private int weatherPercent;

    private boolean weatherWarningPresent;

    private String error;

    public ArrayList<String> getWarningTitles() {
        return warningTitles;
    }

    public void addWarningTitle(String s) {
        this.warningTitles.add(s);
    }

    public ArrayList<String> getWarningExpireTimes() {
        return warningExpireTimes;
    }

    public void addWarningExpireTime(String s) {
        this.warningExpireTimes.add(s);
    }

    public ArrayList<String> getWarningReadableTimes() {
        return warningReadableTimes;
    }

    public void addWarningReadableTime(String s) {
        this.warningReadableTimes.add(s);
    }

    public ArrayList<String> getWarningSummaries() {
        return warningSummaries;
    }

    public void addWarningSummary(String s) {
        this.warningSummaries.add(s);
    }

    public ArrayList<String> getWarningLinks() {
        return warningLinks;
    }

    public void addWarningLink(String s) {
        this.warningLinks.add(s);
    }

    public int getWeatherPercent() {
        return weatherPercent;
    }

    public void setWeatherPercent(int weatherPercent) {
        this.weatherPercent = weatherPercent;
    }

    public boolean isWeatherWarningPresent() {
        return weatherWarningPresent;
    }

    public void setWeatherWarningPresent(boolean weatherWarningPresent) {
        this.weatherWarningPresent = weatherWarningPresent;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
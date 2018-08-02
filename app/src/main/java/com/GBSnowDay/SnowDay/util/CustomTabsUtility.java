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

package com.GBSnowDay.SnowDay.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.GBSnowDay.SnowDay.R;
import com.crashlytics.android.Crashlytics;

public class CustomTabsUtility {

    /**
     * Returns a {@link CustomTabsIntent}.
     * @param context passed from the calling activity
     * @param setShowTitle optionally show the webpage title
     * @return a {@link CustomTabsIntent} designed to blend in with the application.
     */
    public static CustomTabsIntent prepareIntent(Context context, boolean setShowTitle) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder()
                .setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                .setShowTitle(setShowTitle);

        return builder.build();
    }

    /**
     * Browser fallback if Chrome Custom Tabs are unavailable
     * @param context passed from the calling activity
     * @param url The web resource to load
     */
    public static void launchInBrowser(Context context, String url) {
        try {
            Intent browserIntent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(context.getString(R.string.RadarURL))
            );
            context.startActivity(browserIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(
                    context,
                    context.getString(R.string.NoBrowser),
                    Toast.LENGTH_SHORT
            ).show();
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }
}

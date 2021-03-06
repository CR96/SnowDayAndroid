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

package com.GBSnowDay.SnowDay.dialog;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.GBSnowDay.SnowDay.R;

public class WeatherDialog {

    private final Context context;

    private final String title;
    private final String expires;
    private final String summary;
    private final String link;

    public WeatherDialog(Context c,
                         String t,
                         String e,
                         String s,
                         String l) {
        context = c;
        title = t;
        expires = e;
        summary = s;
        link = l;
    }

    public void show() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        @SuppressLint("InflateParams")
        View content = LayoutInflater.from(context).inflate(R.layout.dialog_weather, null);

        TextView txtTitle = content.findViewById(R.id.txtTitle);
        TextView txtExpires = content.findViewById(R.id.txtExpires);
        TextView txtSummary = content.findViewById(R.id.txtSummary);

        txtTitle.setText(title);
        txtExpires.setText(expires);
        txtSummary.setText(summary);

        builder.setPositiveButton(R.string.action_info, (dialog, which) -> {
            Uri uri = Uri.parse(link);
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }catch (ActivityNotFoundException e) {
                Toast.makeText(context, "Can't open link", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.Close, (d, i) -> d.dismiss());
        builder.setView(content);
        builder.show();


    }
}

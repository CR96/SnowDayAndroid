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
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.GBSnowDay.SnowDay.R;

public class LicenseDialog {

    private final Context context;

    public LicenseDialog(Context context) {
        this.context = context;
    }

    private PackageInfo getPackageInfo() {
        PackageInfo pi = null;
        try {
            pi = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi;
    }

    public void show() {

        PackageInfo versionInfo = getPackageInfo();

        String title = context.getString(R.string.app_name) + " v" + versionInfo.versionName;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        @SuppressLint("InflateParams")
        View content = LayoutInflater.from(context).inflate(R.layout.dialog_license, null);

        TextView txtTitle = content.findViewById(R.id.txtTitle);
        TextView txtNewTitle = content.findViewById(R.id.txtNewTitle);
        TextView txtNewText = content.findViewById(R.id.txtNewText);
        TextView txtLicenseTitle = content.findViewById(R.id.txtLicenseTitle);
        TextView txtLicenseText = content.findViewById(R.id.txtLicenseText);

        txtTitle.setText(title);
        txtNewTitle.setText(context.getString(R.string.UpdateTitle));
        txtNewText.setText(context.getString(R.string.Updates));
        txtLicenseTitle.setText(R.string.LicenseTitle);
        txtLicenseText.setText(context.getString(R.string.License));

        builder.setPositiveButton(R.string.Close, (d, i) -> d.dismiss());
        builder.setView(content);
        builder.show();


    }
}

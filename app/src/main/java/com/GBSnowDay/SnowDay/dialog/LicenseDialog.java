package com.GBSnowDay.SnowDay.dialog;

import android.annotation.SuppressLint;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.GBSnowDay.SnowDay.R;

public class LicenseDialog {

    private Context context;

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

        TextView txtTitle = (TextView) content.findViewById(R.id.txtTitle);
        TextView txtNewTitle = (TextView) content.findViewById(R.id.txtNewTitle);
        TextView txtNewText = (TextView) content.findViewById(R.id.txtNewText);
        TextView txtLicenseTitle = (TextView) content.findViewById(R.id.txtLicenseTitle);
        TextView txtLicenseText = (TextView) content.findViewById(R.id.txtLicenseText);

        txtTitle.setText(title);
        txtNewTitle.setText(context.getString(R.string.UpdateTitle));
        txtNewText.setText(context.getString(R.string.Updates));
        txtLicenseTitle.setText(R.string.LicenseTitle);
        txtLicenseText.setText(context.getString(R.string.License));

        builder.setPositiveButton(R.string.Close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int i) {
                d.dismiss();
            }
        });
        builder.setView(content);
        builder.show();


    }
}

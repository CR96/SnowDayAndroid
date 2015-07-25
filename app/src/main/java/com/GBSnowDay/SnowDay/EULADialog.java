package com.GBSnowDay.SnowDay;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
public class EULADialog {

    Context context;

    public EULADialog(Context context) {
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

        // Show the Eula
        String title = context.getString(R.string.app_name) + " v" + versionInfo.versionName;

        //Includes the updates as well so users know what changed.
        String message = context.getString(R.string.Updates) + "\n\n" + context.getString(R.string.EULA);
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(R.drawable.ic_launcher)
                .setNeutralButton(R.string.Close, new Dialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        builder.create().show();
        }
    }

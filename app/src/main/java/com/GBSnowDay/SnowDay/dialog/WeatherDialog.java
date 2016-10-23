package com.GBSnowDay.SnowDay.dialog;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.GBSnowDay.SnowDay.R;

public class WeatherDialog {

    private Context context;

    private String title;
    private String expires;
    private String summary;
    private String link;

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

        TextView txtTitle = (TextView) content.findViewById(R.id.txtTitle);
        TextView txtExpires = (TextView) content.findViewById(R.id.txtExpires);
        TextView txtSummary = (TextView) content.findViewById(R.id.txtSummary);

        txtTitle.setText(title);
        txtExpires.setText(expires);
        txtSummary.setText(summary);

        builder.setPositiveButton(R.string.action_info, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri = Uri.parse(link);
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "Can't open link", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.Close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int i) {
                d.dismiss();
            }
        });
        builder.setView(content);
        builder.show();


    }
}

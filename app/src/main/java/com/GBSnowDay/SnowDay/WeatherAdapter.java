package com.GBSnowDay.SnowDay;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

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

class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> warningTitles = new ArrayList<>();
    private ArrayList<String> warningReadableTimes = new ArrayList<>();
    private ArrayList<String> warningSummaries = new ArrayList<>();
    private ArrayList<String> warningLinks = new ArrayList<>();
    private boolean weatherWarningsPresent;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        ViewHolder(CardView v) {
            super(v);
            mCardView = v;
        }
    }

    WeatherAdapter(ArrayList<String> data1,
                   ArrayList<String> data2,
                   ArrayList<String> data3,
                   ArrayList<String> data4,
                   boolean b1) {
        warningTitles = data1;
        warningReadableTimes = data2;
        warningSummaries = data3;
        warningLinks = data4;
        weatherWarningsPresent = b1;
    }

    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        mContext = parent.getContext();

        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.item_weather, parent, false);  //Just for now
        return new ViewHolder((CardView) v);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        LinearLayout layout = (LinearLayout) holder.mCardView.getChildAt(0);
        TextView text = (TextView) layout.getChildAt(0);
        TextView subtext = (TextView) layout.getChildAt(1);
        text.setText(warningTitles.get(position));

        //Color the weather warning based on severity.
        holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));

        if (position == 0) {
            text.setTextSize(14);
            holder.mCardView.setContentPadding(16, 16, 0, 0);
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBackground));
            holder.mCardView.setCardElevation(0);
            subtext.setVisibility(View.GONE);
        }else if (warningReadableTimes.size() < 1) {
            subtext.setVisibility(View.GONE);
        }else{
            subtext.setText(warningReadableTimes.get(position - 1));
        }

        if (warningTitles.get(position).contains(mContext.getString(R.string.SigWeather))
                || warningTitles.get(position).contains(mContext.getString(R.string.WinterAdvisory))
                || warningTitles.get(position).contains(mContext.getString(R.string.LakeSnowAdvisory))
                || warningTitles.get(position).contains(mContext.getString(R.string.Rain))
                || warningTitles.get(position).contains(mContext.getString(R.string.Drizzle))
                || warningTitles.get(position).contains(mContext.getString(R.string.Fog))
                || warningTitles.get(position).contains(mContext.getString(R.string.WindChillAdvisory))) {
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        } else if (warningTitles.get(position).contains(mContext.getString(R.string.WinterWatch))
                || warningTitles.get(position).contains(mContext.getString(R.string.LakeSnowWatch))
                || warningTitles.get(position).contains(mContext.getString(R.string.WindChillWatch))
                || warningTitles.get(position).contains(mContext.getString(R.string.BlizzardWatch))) {
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.orange));
        } else if (warningTitles.get(position).contains(mContext.getString(R.string.WinterWarn))
                || warningTitles.get(position).contains(mContext.getString(R.string.LakeSnowWarn))
                || warningTitles.get(position).contains(mContext.getString(R.string.IceStorm))
                || warningTitles.get(position).contains(mContext.getString(R.string.WindChillWarn))
                || warningTitles.get(position).contains(mContext.getString(R.string.BlizzardWarn))) {
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
        }

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Don't show a message for the list header
                if (position > 0 && weatherWarningsPresent) {
                    try {
                        new WeatherDialog(mContext,
                                warningTitles.get(position),
                                warningReadableTimes.get(position - 1),
                                warningSummaries.get(position - 1),
                                warningLinks.get(position))
                                .show();
                    } catch (NullPointerException | IndexOutOfBoundsException e) {
                        Toast.makeText(mContext, mContext.getString(R.string.WarningParseError), Toast.LENGTH_SHORT).show();
                        Crashlytics.logException(e);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return warningTitles.size();
    }
}

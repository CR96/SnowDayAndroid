package com.GBSnowDay.SnowDay.adapter;

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

import com.GBSnowDay.SnowDay.R;
import com.GBSnowDay.SnowDay.model.WeatherData;
import com.GBSnowDay.SnowDay.dialog.WeatherDialog;
import com.crashlytics.android.Crashlytics;

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

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private Context mContext;
    private WeatherData mData;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        ViewHolder(CardView v) {
            super(v);
            mCardView = v;
        }
    }

    public WeatherAdapter(WeatherData weatherData) {
        mData = weatherData;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        LinearLayout layout = (LinearLayout) holder.mCardView.getChildAt(0);
        TextView text = (TextView) layout.getChildAt(0);
        TextView subtext = (TextView) layout.getChildAt(1);
        text.setText(mData.warningTitles.get(position));

        //Color the weather warning based on severity.
        holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));

        if (position == 0) {
            text.setTextSize(14);
            holder.mCardView.setContentPadding(16, 16, 0, 0);
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBackground));
            holder.mCardView.setCardElevation(0);
            subtext.setVisibility(View.GONE);
        }else{
            if (mData.warningReadableTimes.size() < 1) {
                subtext.setVisibility(View.GONE);
            }else{
                subtext.setText(mData.warningReadableTimes.get(position - 1));
            }

            if (mData.warningTitles.get(position).contains("Warning")) {
                holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
            }else if (mData.warningTitles.get(position).contains("Watch")) {
                holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.orange));
            }else if (mData.warningTitles.get(position).contains("Advisory")) {
                holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
            }
        }

        final int i = holder.getAdapterPosition();

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Don't show a message for the list header

                if (i > 0 && mData.weatherWarningsPresent) {
                    try {
                        new WeatherDialog(mContext,
                                mData.warningTitles.get(i),
                                mData.warningReadableTimes.get(i - 1),
                                mData.warningSummaries.get(i - 1),
                                mData.warningLinks.get(i))
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
        return mData.warningTitles.size();
    }
}

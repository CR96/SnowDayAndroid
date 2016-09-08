package com.GBSnowDay.SnowDay;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

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
    private List<String> mData;
    private List<String> weatherSummary;
    private List<String> weatherLink;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        ViewHolder(CardView v) {
            super(v);
            mCardView = v;
        }
    }

    WeatherAdapter(List<String> data1,
                   List<String> data2,
                   List<String> data3) {
        mData = data1;
        weatherSummary = data2;
        weatherLink = data3;
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
        text.setText(mData.get(position));

        //Color the weather warning based on severity.
        if (position == 0) {
            holder.mCardView.setContentPadding(8, 8, 8, 8);
            text.setTextSize(14);
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBackground));
            holder.mCardView.setCardElevation(0);
        }
        if (mData.get(position).contains(mContext.getString(R.string.SigWeather))
                || mData.get(position).contains(mContext.getString(R.string.WinterAdvisory))
                || mData.get(position).contains(mContext.getString(R.string.LakeSnowAdvisory))
                || mData.get(position).contains(mContext.getString(R.string.Rain))
                || mData.get(position).contains(mContext.getString(R.string.Drizzle))
                || mData.get(position).contains(mContext.getString(R.string.Fog))
                || mData.get(position).contains(mContext.getString(R.string.WindChillAdvisory))) {
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        } else if (mData.get(position).contains(mContext.getString(R.string.WinterWatch))
                || mData.get(position).contains(mContext.getString(R.string.LakeSnowWatch))
                || mData.get(position).contains(mContext.getString(R.string.WindChillWatch))
                || mData.get(position).contains(mContext.getString(R.string.BlizzardWatch))) {
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.orange));
        } else if (mData.get(position).contains(mContext.getString(R.string.WinterWarn))
                || mData.get(position).contains(mContext.getString(R.string.LakeSnowWarn))
                || mData.get(position).contains(mContext.getString(R.string.IceStorm))
                || mData.get(position).contains(mContext.getString(R.string.WindChillWarn))
                || mData.get(position).contains(mContext.getString(R.string.BlizzardWarn))) {
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
        }

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Don't show a message for the list header
                if (position > 0 && ResultActivity.WeatherWarningsPresent) {
                    try {
                        new WeatherDialog(mContext, mData.get(position), weatherSummary.get(position - 1), weatherLink.get(position)).show();
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
        return mData.size();
    }
}

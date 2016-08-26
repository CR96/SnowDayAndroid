package com.GBSnowDay.SnowDay;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mData;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public ViewHolder(CardView v) {
            super(v);
            mCardView = v;
        }
    }

    public CustomAdapter(List<String> data) {
        mData = data;
    }

    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        mContext = parent.getContext();

        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.item, parent, false);
        return new ViewHolder((CardView) v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TextView textView = (TextView) holder.mCardView.getChildAt(0);
        textView.setText(mData.get(position));

        if (MainActivity.bobcats && position == 1) {
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.bobcats));
        }else if (MainActivity.eventPresent && position == 1) {
            //If there is a reminder / event, make it blue
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        }else{
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
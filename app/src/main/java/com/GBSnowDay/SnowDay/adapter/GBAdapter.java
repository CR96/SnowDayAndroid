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

import com.GBSnowDay.SnowDay.R;

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

public class GBAdapter extends RecyclerView.Adapter<GBAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mData1;
    private List<String> mData2;

    private boolean gb;
    private boolean gbmessage;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        ViewHolder(CardView v) {
            super(v);
            mCardView = v;
        }
    }

    public GBAdapter(List<String> data1, List<String> data2, boolean b1, boolean b2) {
        mData1 = data1;
        mData2 = data2;
        gb = b1;
        gbmessage = b2;
    }

    @Override
    public GBAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        mContext = parent.getContext();

        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.item_gb, parent, false);
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

        text.setText(mData1.get(position));

        if (mData2.get(position) == null) {
            subtext.setVisibility(View.GONE);
        }else {
            subtext.setText(mData2.get(position));
        }

        if (gb && position == 0) {
            //If GB is closed, make card background red.
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
        }else if (gb && position == 1) {
            //Make "Enjoy your snow day!" blue.
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        }else if (gbmessage && position == 0) {
            //If GB has a message, make card background orange.
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.orange));
        }else{
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
            holder.mCardView.setCardElevation(0);
        }
    }

    @Override
    public int getItemCount() {
        return mData1.size();
    }
}

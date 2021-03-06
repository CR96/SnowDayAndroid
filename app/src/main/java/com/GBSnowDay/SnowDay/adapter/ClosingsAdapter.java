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

package com.GBSnowDay.SnowDay.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.GBSnowDay.SnowDay.R;
import com.GBSnowDay.SnowDay.model.ClosingModel;

import java.util.List;

public class ClosingsAdapter extends RecyclerView.Adapter<ClosingsAdapter.ViewHolder> {
    private Context mContext;
    private final List<ClosingModel> mData;

    class ViewHolder extends RecyclerView.ViewHolder {
        final CardView mCardView;
        ViewHolder(CardView v) {
            super(v);
            mCardView = v;
        }
    }

    public ClosingsAdapter(List<ClosingModel> closingModels) {
        mData = closingModels;
    }

    @NonNull
    @Override
    public ClosingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                         int viewType) {
        mContext = parent.getContext();

        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.item_closings, parent, false);
        return new ViewHolder((CardView) v);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        LinearLayout layout = (LinearLayout) holder.mCardView.getChildAt(0);
        TextView txtOrg = (TextView) layout.getChildAt(0);
        TextView txtStatus = (TextView) layout.getChildAt(1);

        txtOrg.setText(mData.get(position).getOrgName());
        txtStatus.setText(mData.get(position).getOrgStatus());
        if (mData.get(position).isSectionHeader()) {
            //Make section headers blue.
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBackground));
            holder.mCardView.setContentPadding(32, 32, 0, 0);
            holder.mCardView.setCardElevation(0);
            txtOrg.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
            txtOrg.setTextSize(20);
            txtStatus.setVisibility(View.GONE);
        }else if (mData.get(position).isClosed()) {
            //If the school is closed, make it red.
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
            holder.mCardView.setCardElevation(16);
        }else if (mData.get(position).isMessagePresent()) {
            //If the school has a message, make it orange.
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.orange));
            holder.mCardView.setCardElevation(16);
        }else{
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
            holder.mCardView.setCardElevation(0);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}

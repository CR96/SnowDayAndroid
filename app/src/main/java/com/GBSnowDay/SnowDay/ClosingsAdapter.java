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

class ClosingsAdapter extends RecyclerView.Adapter<ClosingsAdapter.ViewHolder> {
    private Context mContext;
    private ClosingsData mData;

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        ViewHolder(CardView v) {
            super(v);
            mCardView = v;
        }
    }

    ClosingsAdapter(ClosingsData closingsData) {
        mData = closingsData;
    }

    @Override
    public ClosingsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
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
    public void onBindViewHolder(ViewHolder holder, int position) {

        LinearLayout layout = (LinearLayout) holder.mCardView.getChildAt(0);
        TextView txtOrg = (TextView) layout.getChildAt(0);
        TextView txtStatus = (TextView) layout.getChildAt(1);

        txtOrg.setText(mData.displayedOrgNames.get(position));
        txtStatus.setText(mData.displayedOrgStatuses.get(position));
        if (position == 0 || position == 7 || position == 20 || position == 25) {
            //Make section headers blue.
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBackground));
            holder.mCardView.setCardElevation(0);
            holder.mCardView.setContentPadding(16, 32, 0, 0);
            txtStatus.setVisibility(View.GONE);
        }else if (mData.Atherton && position == 1 || mData.Bendle && position == 2
                || mData.Bentley && position == 3 || mData.Carman && position == 4
                || mData.Flint && position == 5 || mData.Goodrich && position == 6
                || mData.Beecher && position == 8 || mData.Clio && position == 9
                || mData.Davison && position == 10 || mData.Fenton && position == 11
                || mData.Flushing && position == 12 || mData.Genesee && position == 13
                || mData.Kearsley && position == 14 || mData.LKFenton && position == 15
                || mData.Linden && position == 16 || mData.Montrose && position == 17
                || mData.Morris && position == 18 || mData.SzCreek && position == 19
                || mData.Durand && position == 21 || mData.Holly && position == 22
                || mData.Lapeer && position == 23 || mData.Owosso && position == 24
                || mData.GBAcademy && position == 26 || mData.GISD && position == 27
                || mData.HolyFamily && position == 28 || mData.WPAcademy && position == 29) {
            //If the school is closed, make it orange.
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.orange));
            holder.mCardView.setCardElevation(16);
        }else{
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            holder.mCardView.setCardElevation(0);
        }
    }

    @Override
    public int getItemCount() {
        return mData.displayedOrgNames.size();
    }
}

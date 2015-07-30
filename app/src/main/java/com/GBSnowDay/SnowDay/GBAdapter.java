package com.GBSnowDay.SnowDay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GBAdapter extends BaseAdapter {

    /*Copyright 2014 Corey Rowe

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.*/

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

    private ArrayList<String> mData = new ArrayList<>();
    private LayoutInflater mInflater;

    public GBAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final String item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int type = getItemViewType(position);
        holder = new ViewHolder();
            /*No 'if (convertView == null)' statement to prevent view recycling
            (views must remain fixed)*/
        switch (type) {
            case TYPE_ITEM:
                if (ResultActivity.GB && position == 0) {
                    //If GB is closed, make it red.
                    convertView = mInflater.inflate(R.layout.item_red_center, parent, false);
                    holder.textView = (TextView) convertView.findViewById(R.id.text);
                    break;
                } else if (ResultActivity.GB && position == 1) {
                    convertView = mInflater.inflate(R.layout.item_blue_center, parent, false);
                    holder.textView = (TextView) convertView.findViewById(R.id.text);
                    break;
                } else {
                    convertView = mInflater.inflate(R.layout.item_center, parent, false);
                    holder.textView = (TextView) convertView.findViewById(R.id.text);
                    break;
                }
        }
        convertView.setTag(holder);
        holder.textView.setText(mData.get(position));
        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
    }
}

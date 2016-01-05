package com.GBSnowDay.SnowDay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    /*Copyright 2014-2015 Corey Rowe

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

    private ArrayList<String> mData = new ArrayList<>();
    private LayoutInflater mInflater;

    public CustomAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final String item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
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
        switch (type) {
            case TYPE_ITEM:
                if (MainActivity.bobcats && position == 1) {

                    convertView = mInflater.inflate(R.layout.item_bobcats, parent, false);
                    holder.textView = (TextView) convertView.findViewById(R.id.text);
                } else if (MainActivity.eventPresent && position == 1) {
                    //If there is a reminder / event, make it blue
                    convertView = mInflater.inflate(R.layout.item_reminder, parent, false);
                    holder.textView = (TextView) convertView.findViewById(R.id.text);
                } else {
                    convertView = mInflater.inflate(R.layout.item_center, parent, false);
                    holder.textView = (TextView) convertView.findViewById(R.id.text);
                }
                break;
        }
        convertView.setTag(holder);
        holder.textView.setText(mData.get(position));
        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
    }
}
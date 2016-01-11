package com.GBSnowDay.SnowDay;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class WeatherAdapter extends BaseAdapter {

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
    
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

    private ArrayList<String> mData = new ArrayList<>();
    private LayoutInflater mInflater;

    private TreeSet<Integer> mSeparatorsSet = new TreeSet<>();
    
    Context context;
    List<String> weather;

    public WeatherAdapter(Context context, List<String> weather) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.weather = weather;
    }

    public void addItem(final String item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSeparatorItem(final String item) {
        mData.add(item);
        //Save separator position
        mSeparatorsSet.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
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
        Resources res = context.getResources();
        ViewHolder holder;
        int type = getItemViewType(position);
        holder = new ViewHolder();
            /*No 'if (convertView == null)' statement to prevent view recycling
            (views must remain fixed)*/
        switch (type) {
            case TYPE_ITEM:
                //Color the weather warning based on severity.
                if (weather.get(position).contains(res.getString(R.string.SigWeather))
                        || weather.get(position).contains(res.getString(R.string.WinterAdvisory))
                        || weather.get(position).contains(res.getString(R.string.LakeSnowAdvisory))
                        || weather.get(position).contains(res.getString(R.string.Rain))
                        || weather.get(position).contains(res.getString(R.string.Drizzle))
                        || weather.get(position).contains(res.getString(R.string.Fog))
                        || weather.get(position).contains(res.getString(R.string.WindChillAdvisory))) {
                    convertView = mInflater.inflate(R.layout.item_blue, parent, false);
                    holder.textView = (TextView) convertView.findViewById(R.id.text);
                } else if (weather.get(position).contains(res.getString(R.string.WinterWatch))
                        || weather.get(position).contains(res.getString(R.string.LakeSnowWatch))
                        || weather.get(position).contains(res.getString(R.string.WindChillWatch))
                        || weather.get(position).contains(res.getString(R.string.BlizzardWatch))) {
                    convertView = mInflater.inflate(R.layout.item_orange, parent, false);
                    holder.textView = (TextView) convertView.findViewById(R.id.text);
                } else if (weather.get(position).contains(res.getString(R.string.WinterWarn))
                        || weather.get(position).contains(res.getString(R.string.LakeSnowWarn))
                        || weather.get(position).contains(res.getString(R.string.IceStorm))
                        || weather.get(position).contains(res.getString(R.string.WindChillWarn))
                        || weather.get(position).contains(res.getString(R.string.BlizzardWarn))) {
                    convertView = mInflater.inflate(R.layout.item_red, parent, false);
                    holder.textView = (TextView) convertView.findViewById(R.id.text);
                } else {
                    convertView = mInflater.inflate(R.layout.item, parent, false);
                    holder.textView = (TextView) convertView.findViewById(R.id.text);
                }
                break;
            case TYPE_SEPARATOR:
                convertView = mInflater.inflate(R.layout.separator, parent, false);
                holder.textView = (TextView) convertView.findViewById(R.id.textSeparator);
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

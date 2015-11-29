package com.GBSnowDay.SnowDay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TreeSet;

public class ClosingsAdapter extends BaseAdapter {

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
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

    private ArrayList<String> mData = new ArrayList<>();
    private LayoutInflater mInflater;

    private TreeSet<Integer> mSeparatorsSet = new TreeSet<>();

    public ClosingsAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        ViewHolder holder;
        int type = getItemViewType(position);
        holder = new ViewHolder();
            /*No 'if (convertView == null)' statement to prevent view recycling
            (views must remain fixed)*/
        switch (type) {
            case TYPE_ITEM:
                if (ResultActivity.Atherton && position == 2 || ResultActivity.Bendle && position == 3
                        || ResultActivity.Bentley && position == 4 || ResultActivity.Carman && position == 5
                        || ResultActivity.Flint && position == 6 || ResultActivity.Goodrich && position == 7
                        || ResultActivity.Beecher && position == 9 || ResultActivity.Clio && position == 10
                        || ResultActivity.Davison && position == 11 || ResultActivity.Fenton && position == 12
                        || ResultActivity.Flushing && position == 13 || ResultActivity.Genesee && position == 14
                        || ResultActivity.Kearsley && position == 15 || ResultActivity.LKFenton && position == 16
                        || ResultActivity.Linden && position == 17 || ResultActivity.Montrose && position == 18
                        || ResultActivity.Morris && position == 19 || ResultActivity.SzCreek && position == 20
                        || ResultActivity.Durand && position == 22 || ResultActivity.Holly && position == 23
                        || ResultActivity.Lapeer && position == 24 || ResultActivity.Owosso && position == 25
                        || ResultActivity.GBAcademy && position == 27 || ResultActivity.GISD && position == 28
                        || ResultActivity.HolyFamily && position == 29 || ResultActivity.WPAcademy && position == 30) {
                    //If the school is closed, make it orange.
                    convertView = mInflater.inflate(R.layout.item_orange, parent, false);
                    holder.textView = (TextView)convertView.findViewById(R.id.text);
                    break;
                }else{
                    convertView = mInflater.inflate(R.layout.item, parent, false);
                    holder.textView = (TextView)convertView.findViewById(R.id.text);
                    break;
                }
            case TYPE_SEPARATOR:
                //Set the text separators ("Districts near Grand Blanc", etc.)
                if (position == 0) {
                    convertView = mInflater.inflate(R.layout.separator_red, parent, false);
                    holder.textView = (TextView)convertView.findViewById(R.id.textSeparator);
                    break;
                }else{
                    convertView = mInflater.inflate(R.layout.separator, parent, false);
                    holder.textView = (TextView) convertView.findViewById(R.id.textSeparator);
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

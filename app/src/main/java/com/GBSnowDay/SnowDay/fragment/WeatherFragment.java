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

package com.GBSnowDay.SnowDay.fragment;


import android.content.ActivityNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.GBSnowDay.SnowDay.R;
import com.GBSnowDay.SnowDay.util.CustomTabsUtility;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    public Button btnRadar;

    public RecyclerView lstWeather;
    public TextView txtWeatherInfo;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnRadar = view.findViewById(R.id.btnRadar);

        txtWeatherInfo = view.findViewById(R.id.txtWeatherInfo);
        lstWeather = view.findViewById(R.id.lstWeather);

        btnRadar.setOnClickListener(v -> {
            btnRadar.setEnabled(false);
            try {
                // Try to use a custom tab
                CustomTabsIntent customTabsIntent = CustomTabsUtility.prepareIntent(
                        getActivity(), false);
                Uri uri = Uri.parse(getString(R.string.RadarURL));
                customTabsIntent.launchUrl(Objects.requireNonNull(getActivity()), uri);
            } catch (ActivityNotFoundException e) {
                CustomTabsUtility.launchInBrowser(getActivity(), getString(R.string.RadarURL));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        btnRadar.setEnabled(true);
    }
}

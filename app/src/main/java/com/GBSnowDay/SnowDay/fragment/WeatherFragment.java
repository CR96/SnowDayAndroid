/*
 * Copyright 2014-2017 Corey Rowe
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


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.GBSnowDay.SnowDay.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    private WebView webRadar;
    private Button btnRadar;

    public RecyclerView lstWeather;
    public TextView txtWeatherInfo;

    public WeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        webRadar = (WebView) view.findViewById(R.id.webRadar);
        btnRadar = (Button) view.findViewById(R.id.btnRadar);

        txtWeatherInfo = (TextView) view.findViewById(R.id.txtWeatherInfo);
        lstWeather = (RecyclerView) view.findViewById(R.id.lstWeather);

        webRadar.loadUrl("http://radar.weather.gov/Conus/Loop/centgrtlakes_loop.gif");
        webRadar.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        webRadar.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        btnRadar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show / hide and configure the WebView-based radar
                if (webRadar.getVisibility() == View.GONE) {
                    webRadar.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in));
                    webRadar.setVisibility(View.VISIBLE);
                    btnRadar.setText(getString(R.string.radarhide));
                } else {
                    webRadar.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out));
                    webRadar.setVisibility(View.GONE);
                    btnRadar.setText(getString(R.string.radarshow));
                }
            }
        });
    }
}

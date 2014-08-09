package com.GBSnowDay.SnowDay;

import android.content.Context;
import android.webkit.WebView;

public class WeatherGif extends WebView {

    public WeatherGif(Context context, String path) {
        super(context);
        loadUrl(path);
    }

}

package com.GBSnowDay.SnowDay;

import android.content.Context;
import android.webkit.WebView;

public class weather_gif extends WebView {

    public weather_gif(Context context, String path) {
        super(context);
        loadUrl(path);
    }

}

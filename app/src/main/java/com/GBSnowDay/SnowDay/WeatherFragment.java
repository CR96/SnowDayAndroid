package com.GBSnowDay.SnowDay;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    WebView webRadar;
    Button btnRadar;

    public static ListView lstNWS;
    public static ListView lstWeather;

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

        lstNWS = (ListView) view.findViewById(R.id.lstNWS);
        lstWeather = (ListView) view.findViewById(R.id.lstWeather);

        btnRadar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show / hide and configure the WebView-based radar
                if (webRadar.getVisibility() == View.GONE) {
                    webRadar.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in));
                    webRadar.setVisibility(View.VISIBLE);
                    webRadar.loadUrl("http://radar.weather.gov/Conus/Loop/centgrtlakes_loop.gif");
                    webRadar.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
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
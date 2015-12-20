package com.GBSnowDay.SnowDay;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PercentFragment extends Fragment {

    public static TextView txtPercent;
    public static ListView lstGB;

    static ImageView progCalculate;

    public PercentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_percent, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtPercent = (TextView) view.findViewById(R.id.txtPercent);
        lstGB = (ListView) view.findViewById(R.id.lstGB);

        progCalculate = (ImageView) view.findViewById(R.id.progCalculate);

        Animation fade = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);
        PercentFragment.progCalculate.startAnimation(fade);

        Animation rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        PercentFragment.progCalculate.startAnimation(rotate);

        ImageView sf1 = (ImageView) view.findViewById(R.id.snowflake1);
        ImageView sf2 = (ImageView) view.findViewById(R.id.snowflake2);
        ImageView sf3 = (ImageView) view.findViewById(R.id.snowflake3);
        ImageView sf4 = (ImageView) view.findViewById(R.id.snowflake4);
        ImageView sf5 = (ImageView) view.findViewById(R.id.snowflake5);
        ImageView sf6 = (ImageView) view.findViewById(R.id.snowflake6);
        ImageView sf7 = (ImageView) view.findViewById(R.id.snowflake7);
        ImageView sf8 = (ImageView) view.findViewById(R.id.snowflake8);
        ImageView sf9 = (ImageView) view.findViewById(R.id.snowflake9);

        /*Snowflake Positions - snowflakes "fall" towards the bottom right corner of the screen in a staggered pattern
         *
         * (5)          (6)         (7)         (8)         (9)
         * 400/fast   1600/slow   800/fast   1400/slow    600/fast
         *
         * (4)
         * 1200/slow
         *
         * (3)
         * 1000/fast
         *
         * (2)
         * 1800/slow
         *
         * (1)
         * 200/fast
         */

        Animation snow1 = AnimationUtils.loadAnimation(getActivity(), R.anim.snowflake_slow);
        snow1.setStartOffset(200);
        Animation snow2 = AnimationUtils.loadAnimation(getActivity(), R.anim.snowflake_fast);
        snow2.setStartOffset(1800);
        Animation snow3 = AnimationUtils.loadAnimation(getActivity(), R.anim.snowflake_slow);
        snow3.setStartOffset(1000);
        Animation snow4 = AnimationUtils.loadAnimation(getActivity(), R.anim.snowflake_fast);
        snow4.setStartOffset(1200);
        Animation snow5 = AnimationUtils.loadAnimation(getActivity(), R.anim.snowflake_slow);
        snow5.setStartOffset(400);
        Animation snow6 = AnimationUtils.loadAnimation(getActivity(), R.anim.snowflake_fast);
        snow6.setStartOffset(1600);
        Animation snow7 = AnimationUtils.loadAnimation(getActivity(), R.anim.snowflake_slow);
        snow7.setStartOffset(800);
        Animation snow8 = AnimationUtils.loadAnimation(getActivity(), R.anim.snowflake_fast);
        snow8.setStartOffset(1400);
        Animation snow9 = AnimationUtils.loadAnimation(getActivity(), R.anim.snowflake_slow);
        snow9.setStartOffset(600);


        sf1.startAnimation(snow1);
        sf2.startAnimation(snow2);
        sf3.startAnimation(snow3);
        sf4.startAnimation(snow4);
        sf5.startAnimation(snow5);
        sf6.startAnimation(snow6);
        sf7.startAnimation(snow7);
        sf8.startAnimation(snow8);
        sf9.startAnimation(snow9);

    }



}

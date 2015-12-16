package com.GBSnowDay.SnowDay;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class PercentFragment extends Fragment {

    public static TextView txtPercent;
    public static ListView lstGB;

    static ProgressBar progCalculate;

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

        progCalculate  = (ProgressBar) view.findViewById(R.id.progCalculate);
    }
}

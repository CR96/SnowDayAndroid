package com.GBSnowDay.SnowDay;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClosingsFragment extends Fragment {

    public RecyclerView lstClosings;

    public TextView txtClosingsInfo;

    public ClosingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_closings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Declare views
        lstClosings = (RecyclerView) view.findViewById(R.id.lstClosings);

        txtClosingsInfo = (TextView) view.findViewById(R.id.txtClosingsInfo);
    }

}

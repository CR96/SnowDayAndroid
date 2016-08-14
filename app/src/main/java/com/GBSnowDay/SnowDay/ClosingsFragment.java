package com.GBSnowDay.SnowDay;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClosingsFragment extends Fragment {

    public ListView lstClosings;

    public ListView lstWJRT;

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
        lstClosings = (ListView) view.findViewById(R.id.lstClosings);

        lstWJRT = (ListView) view.findViewById(R.id.lstWJRT);
    }

}

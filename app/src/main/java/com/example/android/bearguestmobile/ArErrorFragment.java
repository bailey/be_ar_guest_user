package com.example.android.bearguestmobile;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/*
 * Displays placeholder "AR Not Compatible" error to user
 */
public class ArErrorFragment extends Fragment {


    public ArErrorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Hide the back arrow and set title
        ToolbarViewModel toolbarViewModel = ViewModelProviders.of((MainActivity)getActivity()).get(ToolbarViewModel.class);
        toolbarViewModel.setShowBackArrow(false);
        toolbarViewModel.setToolbarTitle("AR Scanner");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ar_error, container, false);
    }

}

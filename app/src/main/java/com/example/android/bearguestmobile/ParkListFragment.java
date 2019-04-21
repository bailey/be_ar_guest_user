package com.example.android.bearguestmobile;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ParkListFragment extends Fragment {

    private View parkListFragmentView;

    ParkListAdapter adapter;

    public ParkListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parkListFragmentView = inflater.inflate(R.layout.fragment_park_list, container, false);

        // data to populate the RecyclerView with
        ArrayList<String> parks = new ArrayList<>();
        parks.add("Magic Kingdom");
        parks.add("Epcot");
        parks.add("Hollywood Studios");
        parks.add("Animal Kingdom");

        // set up the RecyclerView
        RecyclerView recyclerView = ((RecyclerView)parkListFragmentView).findViewById(R.id.rvParkList);
        Context context = parkListFragmentView.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ParkListAdapter(context, parks);
        recyclerView.setAdapter(adapter);

        // Hide the back arrow and set title
        ToolbarViewModel toolbarViewModel = ViewModelProviders.of((MainActivity)getActivity()).get(ToolbarViewModel.class);
        toolbarViewModel.setShowBackArrow(false);
        toolbarViewModel.setToolbarTitle("Parks");

        return parkListFragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}

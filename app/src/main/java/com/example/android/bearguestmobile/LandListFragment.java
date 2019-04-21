package com.example.android.bearguestmobile;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LandListFragment extends Fragment {

    private View landListView;
    private LandListAdapter adapter;

    public LandListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        landListView = inflater.inflate(R.layout.fragment_land_list, container, false);

        // set up the RecyclerView
        RecyclerView recyclerView = ((RecyclerView)landListView).findViewById(R.id.rvLandList);
        Context context = landListView.getContext();

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new LandListAdapter(context, new ArrayList<Land>());
        recyclerView.setAdapter(adapter);

        // Set screen title and show up arrow
        ToolbarViewModel toolbarViewModel = ViewModelProviders.of((MainActivity)getActivity()).get(ToolbarViewModel.class);
        toolbarViewModel.setToolbarTitle("Lands");
        toolbarViewModel.setShowBackArrow(true);

        return landListView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DashboardViewModel dashboardViewModel = ViewModelProviders.of((MainActivity)getActivity()).get(DashboardViewModel.class);

        // Get a list of observable land objects from shared ViewModel
        dashboardViewModel.getLandListByParkID().observe(this, new Observer<List<Land>>() {
            @Override
            public void onChanged(@Nullable List<Land> lands) {
                // Update the UI
                if(lands!=null){
                    adapter.setRestaurantList(lands);
                }
                else {
                    Toast.makeText(getActivity(), "Error: observable object List<Land> is null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

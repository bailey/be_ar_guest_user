package com.example.android.bearguestmobile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * Trip List Display Fragment
 * Creates the recycler view and adapter to display list of user's trips
 * Loaded into frame on TripsViewFragment
*/
public class TripListFragment extends Fragment {

    private View tripListView;
    private TripListAdapter adapter;

    public TripListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tripListView = inflater.inflate(R.layout.fragment_trip_list, container, false);

        // Hide the back arrow and set title IF this fragment has been loaded from bottom tab
        // If as part of AddItemToTrip, then do nothing with the backstack and toolbar title
        TripViewModel tripViewModel = ViewModelProviders.of((MainActivity)getActivity()).get(TripViewModel.class);
        if(!tripViewModel.getIsAddItemToTrip()) {
            ToolbarViewModel toolbarViewModel = ViewModelProviders.of((MainActivity) getActivity()).get(ToolbarViewModel.class);
            toolbarViewModel.setShowBackArrow(false);
            toolbarViewModel.setToolbarTitle("Trips");
        }

        // Set text of subtitle TextView
        ViewModelProviders.of((MainActivity)getActivity()).get(TripViewModel.class).setTripPageHeader("Upcoming Trips");

        // set up the RecyclerView and TripListAdapter
        RecyclerView recyclerView = ((RecyclerView)tripListView).findViewById(R.id.rvTripList);
        Context context = tripListView.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        FragmentManager childFragmentManager = this.getChildFragmentManager();
        //adapter = new TripListAdapter(context, new ArrayList<>(), childFragmentManager);
        adapter = new TripListAdapter(context, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        return tripListView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Declare View Models
        TripViewModel tripViewModel = ViewModelProviders.of((MainActivity)getActivity()).get(TripViewModel.class);

        Uid user = new Uid();
        user.setUserID(FirebaseAuth.getInstance().getUid());
        tripViewModel.getTripsByUser(user).observe(getViewLifecycleOwner(), new Observer<List<Trip>>() {
            @Override
            public void onChanged(@Nullable List<Trip> tripList) {
                // update UI
                adapter.setTripList(tripList);
            }
        });
    }
}

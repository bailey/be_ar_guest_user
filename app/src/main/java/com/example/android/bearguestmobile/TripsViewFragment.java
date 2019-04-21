package com.example.android.bearguestmobile;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/*
 * Main Tab View: Trips
 * Displays a list of all user trips and their date range.
 * Loads TripListFragment into frame
 * Launches detailed view of each trip when clicked
 */
public class TripsViewFragment extends Fragment {

    private View tripListView;
    private TripListAdapter adapter;

    public TripsViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        tripListView = inflater.inflate(R.layout.fragment_upcoming_trips, container, false);

        TripViewModel tripViewModel = ViewModelProviders.of((MainActivity)getActivity()).get(TripViewModel.class);
        if(!tripViewModel.getIsAddItemToTrip()) {
            ToolbarViewModel toolbarViewModel = ViewModelProviders.of((MainActivity) getActivity()).get(ToolbarViewModel.class);
            toolbarViewModel.setShowBackArrow(false);
            toolbarViewModel.setToolbarTitle("Trips");
        }

        // Hide the FAB if this was launched from AddItemToTrip
        if(tripViewModel.getIsAddItemToTrip()) {
            FloatingActionButton button_addNewTrip = (FloatingActionButton) tripListView.findViewById(R.id.fabNewTrip);
            button_addNewTrip.hide();
        }

        // set up the RecyclerView and TripListAdapter to display the list of trips
        RecyclerView recyclerView = (RecyclerView)(tripListView.findViewById(R.id.rv_new_trip_list));
        Context context = tripListView.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new TripListAdapter(context, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Capture clicks to newTrip FAB
        addButtonListeners();

        return tripListView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TripViewModel tripViewModel = ViewModelProviders.of((MainActivity)getActivity()).get(TripViewModel.class);

        // Observe the text for the subtitle TextView
        tripViewModel.getTripPageHeader().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String header) {
                // Update the UI
                if(header!=null){
                    ((TextView) tripListView.findViewById(R.id.upcoming_trips)).setText(header);
                }
                else {
                    Toast.makeText(getActivity(), "Error: observable object String header is null", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Observe the list of trips
        Uid user = new Uid();
        user.setUserID(FirebaseAuth.getInstance().getUid());
        tripViewModel.getTripsByUser(user).observe(getViewLifecycleOwner(), new Observer<List<Trip>>() {
            @Override
            public void onChanged(@Nullable List<Trip> tripList) {
                // update UI
                adapter.setTripList(tripList);
                Log.v("trip", "setting trip list adapter in tripsViewFragment observe, size: " + tripList.size());
            }
        });
    }

    private void addButtonListeners() {
        // Handle clicks to FAB
        final FloatingActionButton button_addNewTrip = (FloatingActionButton) tripListView.findViewById(R.id.fabNewTrip);
        button_addNewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == button_addNewTrip){
                    // Create full screen fragment to slide up and over entire screen
                    TripCreateFragment tripCreateFragment = new TripCreateFragment();
                    FragmentTransaction transaction = ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction();
                    transaction
                            .setCustomAnimations(R.anim.slide_up, R.anim.slide_down, R.anim.slide_up, R.anim.slide_down)
                            .replace(android.R.id.content, tripCreateFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
    }

}

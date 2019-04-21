package com.example.android.bearguestmobile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class RestaurantListFragment extends Fragment {

    private View restaurantListFragmentView;
    private RestaurantListAdapter adapter;

    // Required empty public constructor
    public RestaurantListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        restaurantListFragmentView = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        // set up the RecyclerView list of restaurants
        RecyclerView recyclerView = ((RecyclerView)restaurantListFragmentView).findViewById(R.id.rvRestaurantList);
        Context context = restaurantListFragmentView.getContext();

        // Set list adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new RestaurantListAdapter(context, new ArrayList<Restaurant>());
        recyclerView.setAdapter(adapter);

        // Set screen title and show up arrow
        ToolbarViewModel toolbarViewModel = ViewModelProviders.of((MainActivity)getActivity()).get(ToolbarViewModel.class);
        toolbarViewModel.setToolbarTitle("Restaurants");
        toolbarViewModel.setShowBackArrow(true);

        return restaurantListFragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DashboardViewModel dashboardViewModel = ViewModelProviders.of((MainActivity)getActivity()).get(DashboardViewModel.class);
        // Get a list of observable restaurant objects, by selected ParkID, from shared ViewModel
        dashboardViewModel.getRestaurantListByLandID().observe(this, new Observer<List<Restaurant>>() {
            @Override
            public void onChanged(@Nullable List<Restaurant> restaurants) {
                // Update the UI
                if(restaurants!=null){
                    adapter.setRestaurantList(restaurants);
                }
                else {
                    Toast.makeText(getActivity(), "Error: observable object List<Restaurant> is null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

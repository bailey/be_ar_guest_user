package com.example.android.bearguestmobile;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.Date;
import java.util.List;

/*
 * Add Item to Trip
 * Launched on "Add to Trip" button click
 * Slides up over screen, loads trip view fragments with "add" buttons visible
 */
public class AddItemToTripFragment extends Fragment {

    private View addItemToTripView;

    public AddItemToTripFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addItemToTripView = inflater.inflate(R.layout.fragment_add_item_to_trip, container, false);

        addButtonListeners();

        // Load list into day_list frame
        Context context = addItemToTripView.getContext();
        TripsViewFragment tripsViewFragment = new TripsViewFragment();
        FragmentTransaction transaction = ((MainActivity)context).getSupportFragmentManager().beginTransaction();
        transaction
                .replace(R.id.frame_add_to_trip, tripsViewFragment)
                .commit();

        return addItemToTripView;
    }

    private void addButtonListeners() {

        final ImageView close_button = (ImageView) addItemToTripView.findViewById(R.id.icon_close);
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == close_button){
                    // Reset the viewmodel
                    ViewModelProviders.of((MainActivity)getActivity()).get(TripViewModel.class).setIsAddItemToTrip(false);

                    // Close without making any changes, show previous fragment that had been loaded
                    //((MainActivity)getActivity()).getSupportFragmentManager().popBackStackImmediate();
                    closeAddItemToTripWindow();
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Declare View Model
        TripViewModel tripViewModel = ViewModelProviders.of((MainActivity)getActivity()).get(TripViewModel.class);

        tripViewModel.getMealToAdd().observe(this, new Observer<Meal>() {
            @Override
            public void onChanged(@Nullable Meal meal) {
                // If meal has returned valid data, send to database and update UI
                if(meal!=null && meal.getMealName()!=null  && meal.getDay()!=null
                              && meal.getRestaurantID()!=0 && meal.getTripID()!=null) {
                    // Send to database
                    TripRepository.getInstance().addMealToTrip(meal);

                    // Close window
                    closeAddItemToTripWindow();

                    // Notify user that meal was successfully added
                    // TODO: make custom Toast message
                    Toast toast = Toast.makeText((MainActivity)getActivity(), "Successfully added to trip!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();

                }
            }
        });
    }

    public void closeAddItemToTripWindow() {
        FragmentManager fragmentManager = ((MainActivity)getActivity()).getSupportFragmentManager();
        fragmentManager.popBackStack("TAG_ADD_ITEM_TO_TRIP", FragmentManager.POP_BACK_STACK_INCLUSIVE);

        // Reset view model object for mealToAdd
        TripViewModel tripViewModel = ViewModelProviders.of((MainActivity)getActivity()).get(TripViewModel.class);
        tripViewModel.setMealToAdd(null);
    }
}

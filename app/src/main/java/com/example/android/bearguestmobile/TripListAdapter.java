package com.example.android.bearguestmobile;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/*
 * Trip List Adapter for TripListFragment
 * Displays a list of all user trips and their date range.
 */
public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.ViewHolder> {

    private List<Trip> mData;
    private LayoutInflater mInflater;
    private Context context;
    private TripViewModel tripViewModel;

    // data is passed into the constructor
    TripListAdapter(Context context, List<Trip> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        this.tripViewModel = ViewModelProviders.of((MainActivity)context).get(TripViewModel.class);

    }

    // inflates the row layout from xml when needed
    @Override
    public TripListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fragment_trip_list_row, parent, false);
        return new ViewHolder(view);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tripName;
        TextView startDate;
        TextView endDate;
        TextView oneDate;

        ViewHolder(View tripView) {
            super(tripView);

            // Save references to each row's text field
            tripName = tripView.findViewById(R.id.trip_name);
            startDate = tripView.findViewById(R.id.text_start_date);
            endDate = tripView.findViewById(R.id.text_end_date);
            oneDate = tripView.findViewById(R.id.text_one_day_trip);

            tripView.setOnClickListener(this);
        }

        // Handle clicks to list of trips to show trip's detailed view
        @Override
        public void onClick(View view) {
            // Open that trip, set the selected MenuItem
            Trip trip = getItem(getAdapterPosition());
            tripViewModel.setSelectedTrip(trip);

            TripDetailsViewFragment tripDetailsViewFragment = new TripDetailsViewFragment();
            FragmentTransaction transaction = ((MainActivity)context).getSupportFragmentManager().beginTransaction();

            // Determine which frame to load this into, and only allow backwards navigation if this is loaded
            // in the Trips main view, not the AddItemToTrip fragment
            if(!tripViewModel.getIsAddItemToTrip()){
                transaction.addToBackStack(null);
                transaction.replace(R.id.fragment_container, tripDetailsViewFragment, "TRIP_DETAILS_FRAGMENT");
            }
            // Load into diff fragment container if from add item to trip
            else {
                transaction.replace(R.id.frame_add_to_trip, tripDetailsViewFragment, "TRIP_DETAILS_FRAGMENT");
            }

            transaction.commit();
        }
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(TripListAdapter.ViewHolder holder, int position) {
        Trip trip = mData.get(position);
        holder.tripName.setText(trip.getTripName());
        Date startDateFromJson = trip.getStartDate();
        Date endDateFromJson = trip.getEndDate();

        SimpleDateFormat formatterOutput = new SimpleDateFormat("EEE. M/d/yy");

        String sStartDate = formatterOutput.format(startDateFromJson);
        String sEndDate = formatterOutput.format(endDateFromJson);

        // If we have a one day trip, don't show both the start and end date
        if(sStartDate.equals(sEndDate)) {
            Log.v("date", "Trip " + trip.getTripName() + " dates are equal: sStart=" + sStartDate + " and sEnd=" + sEndDate);
            holder.oneDate.setVisibility(View.VISIBLE);
            holder.oneDate.setText(sStartDate);
            holder.startDate.setVisibility(View.GONE);
            holder.endDate.setVisibility(View.GONE);
        } else {
            holder.startDate.setText(sStartDate + " -");
            holder.endDate.setText(sEndDate);
            Log.v("date", "Trip " + trip.getTripName() + " dates are not equal: sStart=" + sStartDate + " and sEnd=" + sEndDate);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    Trip getItem(int id) {
        return mData.get(id);
    }

    // Update the data (list of trips) when changed
    public void setTripList(List<Trip> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }
}

package com.example.android.bearguestmobile;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ParkListAdapter extends RecyclerView.Adapter<ParkListAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private Context context;
    private ViewModel dashboardViewModel;

    // data is passed into the constructor
    ParkListAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fragment_park_list_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String park = mData.get(position);
        holder.myTextView.setText(park);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.park_name);
            itemView.setOnClickListener(this);
        }

        // Handle clicks to the list of parks
        @Override
        public void onClick(View view) {
            // Display only lands for park that has been clicked by passing (adapter position + 1)
            // Set selected ParkID
            DashboardViewModel dashboardViewModel = ViewModelProviders.of((MainActivity)context).get(DashboardViewModel.class);
            ParkID parkID = new ParkID(this.getAdapterPosition() + 1);
            dashboardViewModel.setParkIDObservable(parkID);

            LandListFragment landListFragment = new LandListFragment();
            FragmentTransaction transaction = ((MainActivity)context).getSupportFragmentManager().beginTransaction();
            transaction
                    .replace(R.id.fragment_container, landListFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }
}

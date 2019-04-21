package com.example.android.bearguestmobile;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class LandListAdapter extends RecyclerView.Adapter<LandListAdapter.ViewHolder> {

    private List<Land> mLandList;
    private LayoutInflater mInflater;
    private Context context;

    // data is passed into the constructor
    LandListAdapter(Context context, List<Land> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mLandList = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fragment_land_list_row, parent, false);
        return new ViewHolder(view);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mLandList.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.land_name);
            itemView.setOnClickListener(this);
        }

        // Handle clicks to list of lands to show restaurants at that land
        @Override
        public void onClick(View view) {
            RestaurantListFragment restaurantListFragment = new RestaurantListFragment();

            // Obtain shared view model and set the landID value to the restaurant just clicked
            DashboardViewModel dashboardViewModel = ViewModelProviders.of((MainActivity)context).get(DashboardViewModel.class);
            dashboardViewModel.setSelectedLand(getItem(getAdapterPosition()));

            FragmentTransaction transaction = ((MainActivity)context).getSupportFragmentManager().beginTransaction();
            transaction
                    .replace(R.id.fragment_container, restaurantListFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(LandListAdapter.ViewHolder holder, int position) {
        Land land = mLandList.get(position);
        holder.myTextView.setText(land.getLandName());
    }

    // convenience method for getting data at click position
    Land getItem(int id) {
        return mLandList.get(id);
    }

    public void setRestaurantList(List<Land> data) {
        this.mLandList = data;
        this.notifyDataSetChanged();
    }
}

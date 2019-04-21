package com.example.android.bearguestmobile;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {

    private List<Restaurant> mData;
    private LayoutInflater mInflater;
    private Context context;

    // data is passed into the constructor
    RestaurantListAdapter(Context context, List<Restaurant> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fragment_restaurant_list_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(RestaurantListAdapter.ViewHolder holder, int position) {
        Restaurant restaurant = mData.get(position);
        holder.myTextView.setText(restaurant.getRestaurantName());
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
            myTextView = itemView.findViewById(R.id.restaurant_name);
            itemView.setOnClickListener(this);
        }

        // Handle clicks to list of restaurants to show items at that restaurant
        @Override
        public void onClick(View view) {
            ItemListFragment itemListFragment = new ItemListFragment();

            // Obtain shared view model and set the restaurantID value to the restaurant just clicked
            DashboardViewModel dashboardViewModel = ViewModelProviders.of((MainActivity)context).get(DashboardViewModel.class);

            int iRestaurantID = getItem(getAdapterPosition()).getRestaurantID();
            RestaurantID restaurantID = new RestaurantID(iRestaurantID);
            dashboardViewModel.setSelectedRestaurantID(restaurantID);

            FragmentTransaction transaction = ((MainActivity)context).getSupportFragmentManager().beginTransaction();
            transaction
                    .replace(R.id.fragment_container, itemListFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    // convenience method for getting data at click position
    Restaurant getItem(int id) {
        return mData.get(id);
    }

    public void setRestaurantList(List<Restaurant> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }
}

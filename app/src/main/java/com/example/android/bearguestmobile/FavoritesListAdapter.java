package com.example.android.bearguestmobile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesListAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private Context context;
    private List<MenuItem> mFavorites;

    public FavoritesListAdapter(Context context, List<MenuItem> favorites) {
        this.context = context;
        this.mFavorites = favorites;
        this.mInflater = LayoutInflater.from(context);
    }

    // inflates the row layout from xml when needed
    @Override
    public FavoritesListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fragment_favorites_list_row, parent, false);
        return new ViewHolder(view);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textFavoriteLabel;
        TextView textRestName;

        ViewHolder(View favListView) {
            super(favListView);
            textFavoriteLabel = favListView.findViewById(R.id.text_favorite_label);
            textRestName = favListView.findViewById(R.id.text_restaurant_name);
            favListView.setOnClickListener(this);
        }

        // Handle clicks to list of favorites to go to item view?
        @Override
        public void onClick(View view) {

        }
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(FavoritesListAdapter.ViewHolder holder, int position) {
        String itemName = mFavorites.get(position).getItemName();
        String restaurantName = mFavorites.get(position).getRestaurantName();

        holder.textFavoriteLabel.setText(itemName);
        holder.textRestName.setText(restaurantName);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mFavorites.size();
    }

    public void setmFavorites(List<MenuItem> mFavorites) {
        this.mFavorites = mFavorites;
        this.notifyDataSetChanged();
    }
}

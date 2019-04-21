package com.example.android.bearguestmobile;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    private List<MenuItem> mData;
    private LayoutInflater mInflater;
    private Context context;

    // data is passed into the constructor
    ItemListAdapter(Context context, List<MenuItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fragment_item_list_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ItemListAdapter.ViewHolder holder, int position) {
        MenuItem menuItem = mData.get(position);
        holder.myTextView.setText(menuItem.getItemName());
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
            myTextView = itemView.findViewById(R.id.item_name);
            itemView.setOnClickListener(this);
        }

        // Handle clicks to list of items to show items detailed item view
        @Override
        public void onClick(View view) {
            ItemViewFragment itemViewFragment = new ItemViewFragment();

            // Set the selected MenuItem
            MenuItem selectedMenuItem = getItem(getAdapterPosition());
            ViewModelProviders.of((MainActivity)context).get(DashboardViewModel.class).setSelectedMenuItem(selectedMenuItem);

            FragmentTransaction transaction = ((MainActivity)context).getSupportFragmentManager().beginTransaction();
            transaction
                    .replace(R.id.fragment_container, itemViewFragment, "ITEM_VIEW_FRAGMENT")
                    .addToBackStack(null)
                    .commit();
        }
    }

    // convenience method for getting data at click position
    MenuItem getItem(int id) {
        return mData.get(id);
    }

    public void setMenuItemList(List<MenuItem> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }
}

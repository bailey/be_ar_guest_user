package com.example.android.bearguestmobile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/*
 * Item Comment List helper fragment
 * Initializes recycler view and observes comment list data
 */
public class ItemCommentsFragment extends Fragment {

    private View itemCommentsView;
    private ItemCommentsAdapter adapter;

    public ItemCommentsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itemCommentsView = inflater.inflate(R.layout.fragment_item_comments, container, false);

        // set up the RecyclerView
        RecyclerView recyclerView = ((RecyclerView)itemCommentsView).findViewById(R.id.rvItemComments);
        Context context = itemCommentsView.getContext();

        // Set the list adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ItemCommentsAdapter(context, new ArrayList<Review>());
        recyclerView.setAdapter(adapter);

        return itemCommentsView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Declare View Models
        ItemCommentsViewModel itemCommentsViewModel = ViewModelProviders.of((MainActivity) getActivity()).get(ItemCommentsViewModel.class);
        DashboardViewModel dashboardViewModel = ViewModelProviders.of((MainActivity)getActivity()).get(DashboardViewModel.class);

        // Get the menu item currently selected, create ItemID object for that item
        LiveData<MenuItem> menuItem = dashboardViewModel.getSelectedMenuItem();
        ItemID itemID = new ItemID(menuItem.getValue().getItemID());

        // Observe a list of all comments for that menu item
        itemCommentsViewModel.getAllReviewsByItem(itemID).observe(getViewLifecycleOwner(), new Observer<List<Review>>() {
            @Override
            public void onChanged(@Nullable List<Review> reviewList) {
                // Update the UI
                adapter.setReviewList(reviewList);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}

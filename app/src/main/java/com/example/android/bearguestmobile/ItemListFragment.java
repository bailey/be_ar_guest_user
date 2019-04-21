package com.example.android.bearguestmobile;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ItemListFragment extends Fragment {
    private View itemFragmentView;
    private ItemListAdapter adapter;

    public ItemListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        itemFragmentView = inflater.inflate(R.layout.fragment_item_list, container, false);

        // set up the RecyclerView list of items
        RecyclerView recyclerView = (RecyclerView) itemFragmentView.findViewById(R.id.rvItemList);
        Context context = itemFragmentView.getContext();

        // Set list adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ItemListAdapter(context, new ArrayList<MenuItem>());
        recyclerView.setAdapter(adapter);

        // Set screen title and up arrow
        ToolbarViewModel toolbarViewModel = ViewModelProviders.of((MainActivity) getActivity()).get(ToolbarViewModel.class);
        toolbarViewModel.setToolbarTitle("Menu Items");
        toolbarViewModel.setShowBackArrow(true);

        return itemFragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DashboardViewModel dashboardViewModel = ViewModelProviders.of((MainActivity) getActivity()).get(DashboardViewModel.class);

        // Observe an item list that changes as selectedRestaurantID changes
        dashboardViewModel.getMenuItemListByRestaurantIDObservable().observe(this, new Observer<List<MenuItem>>() {
            @Override
            public void onChanged(@Nullable List<MenuItem> menuItemList) {
                // Update the UI
                adapter.setMenuItemList(menuItemList);
            }
        });
    }
}

package com.example.android.bearguestmobile;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private View dashboardFragmentView;
    private FirebaseAuth mAuth;
    private Toolbar topToolbar;
    private Context context;
    private FavoritesListAdapter adapter;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        dashboardFragmentView = inflater.inflate(R.layout.dashboard_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        context = (MainActivity)getActivity();

        // Set screen title
        ViewModelProviders.of((MainActivity)getActivity()).get(ToolbarViewModel.class).setToolbarTitle("Be AR Guest");

        // Set up recycler view and adapter to show horizontal list of favorited items
        adapter = new FavoritesListAdapter(context, new ArrayList<>());
        RecyclerView recyclerView = dashboardFragmentView.findViewById(R.id.rv_horiz_fav_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        return dashboardFragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set user's name in Welcome text
        String uid = mAuth.getCurrentUser().getUid();
        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        userViewModel.getUserByUid(uid).observe(this, new Observer<Profile>() {
            @Override
            public void onChanged(@Nullable Profile profile) {
                ((TextView) dashboardFragmentView.findViewById(R.id.dashboard_welcome_user))
                        .setText("Welcome, " + profile.getfName() + "!");
            }
        });

        // Set list of favorited items
        ItemCommentsViewModel itemCommentsViewModel = ViewModelProviders.of(this).get(ItemCommentsViewModel.class);
        Uid uUid = new Uid();
        uUid.setUserID(uid);

        itemCommentsViewModel.getUserFavoritedItems(uUid).observe(this, new Observer<List<MenuItem>>() {
            @Override
            public void onChanged(@Nullable List<MenuItem> menuItemList) {
                adapter.setmFavorites(menuItemList);

                if(menuItemList.isEmpty()) {
                    ((TextView) dashboardFragmentView.findViewById(R.id.text_no_faves)).setVisibility(View.VISIBLE);
                }
                else {
                    ((TextView) dashboardFragmentView.findViewById(R.id.text_no_faves)).setVisibility(View.GONE);
                }
            }
        });
    }
}

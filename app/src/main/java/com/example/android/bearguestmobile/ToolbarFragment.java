package com.example.android.bearguestmobile;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class ToolbarFragment extends Fragment {

    private Toolbar topToolbar;
    private View toolbarFragmentView;

    public static ToolbarFragment newInstance() {
        return new ToolbarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        toolbarFragmentView = inflater.inflate(R.layout.fragment_toolbar, container, false);

        // Set custom top toolbar with search icon
        topToolbar = (Toolbar) toolbarFragmentView.findViewById(R.id.top_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(topToolbar);

        // Check if assigned ActionBar is null
        if(((AppCompatActivity)getActivity()).getSupportActionBar() == null) {
            Toast.makeText(getActivity(),"getSupportActionBar is null",Toast.LENGTH_SHORT).show();
            return toolbarFragmentView;
        }

        // Handle clicks to up arrow
        topToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity)getActivity()).getSupportFragmentManager().popBackStackImmediate();
            }
        });

        return toolbarFragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ToolbarViewModel toolbarViewModel = ViewModelProviders.of((MainActivity)getActivity()).get(ToolbarViewModel.class);

        // Observe and update the toolbar title as necessary
        toolbarViewModel.getToolbarTitle().observe(this, new Observer<String>() {
            @Override
            public void onChanged (@Nullable String screenTitle) {
                // Change title in action bar
                ((MainActivity)getActivity()).getSupportActionBar().setTitle(screenTitle);
            }
        });

        // Observe and update whether the up arrow is shown or hidden
        toolbarViewModel.getShowBackArrow().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean showBackArrow) {
                // Determine if arrow should be shown
                ActionBar toolbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
                if(toolbar != null) {
                    if(showBackArrow) {
                        toolbar.setDisplayHomeAsUpEnabled(true);
                        toolbar.setDisplayShowHomeEnabled(true);

                    }
                    else {
                        toolbar.setDisplayHomeAsUpEnabled(false);
                        toolbar.setDisplayShowHomeEnabled(false);
                    }
                }
                else {
                    Log.v("Toolbar Frag", "getSupportActionBar is null");
                }
            }
        });
    }
}

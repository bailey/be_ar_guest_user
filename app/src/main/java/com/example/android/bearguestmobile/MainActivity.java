package com.example.android.bearguestmobile;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.ar.core.ArCoreApk;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    public ToolbarFragment topToolbar;
    private FragmentTransaction transaction;
    public String MAIN_TAB_FRAGMENT = "main_tab_fragment";

    public DashboardViewModel dashboardViewModel;
    public ToolbarViewModel toolbarViewModel;
    public UserViewModel userViewModel;
    public TripViewModel tripViewModel;
    public ItemCommentsViewModel itemCommentsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load Toolbar Fragment
        transaction = getSupportFragmentManager().beginTransaction();
        topToolbar = new ToolbarFragment();
        transaction.replace(R.id.top_toolbar_container, topToolbar);
        transaction.commit();

        if(savedInstanceState==null) {
            // Load Dashboard Fragment
            transaction = getSupportFragmentManager().beginTransaction();
            DashboardFragment dashboardFragment = new DashboardFragment();
            transaction.replace(R.id.fragment_container, dashboardFragment, MAIN_TAB_FRAGMENT);
            transaction.commit();
            showUpButton(false);
        }

        // Create DashboardViewModel for Restaurant and Park list fragments to share
        // Create Toolbar ViewModel to control back vs hamburger icon
        // Create User ViewModel to manage logged in user
        // Create Trip ViewModel to manage list of trips
        // Create ItemComments ViewModel to query for each item's reviews
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        toolbarViewModel = ViewModelProviders.of(this).get(ToolbarViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        tripViewModel = ViewModelProviders.of(this).get(TripViewModel.class);
        itemCommentsViewModel = ViewModelProviders.of(this).get(ItemCommentsViewModel.class);

        // Handle clicks to bottom navigation bar
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_nav_bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /** Inflates the top toolbar with search icon **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_top_toolbar, menu);
        return true;
    }

    // Handle clicks to the overflow toolbar menu (3-dots icon)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        transaction = getSupportFragmentManager().beginTransaction();
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_signout:
                // Sign Out
                clearBackStack();
                startActivity(new Intent(getApplicationContext(), FirebaseUIActivity.class));
                FirebaseAuth.getInstance().signOut();
                return true;

            case R.id.action_settings:
                SettingsFragment settingsFragment = new SettingsFragment();
                transaction
                        .setCustomAnimations(R.anim.slide_up, R.anim.slide_down, R.anim.slide_up, R.anim.slide_down)
                        .add(android.R.id.content, settingsFragment, null)
                        .addToBackStack(null)
                        .commit();
                return true;
            // Not implemented
//            case R.id.action_search:
//                Toast.makeText((MainActivity) this, "Clicked search", Toast.LENGTH_SHORT).show();
//                return true;
        }
        return false;
    }

    /** Handle clicks to bottom nav bar **/
    private BottomNavigationView.OnNavigationItemSelectedListener
            mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener(){
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            transaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.action_dashboard:
                    clearBackStack();
                    DashboardFragment dashboardFragment = new DashboardFragment();
                    transaction.replace(R.id.fragment_container, dashboardFragment, MAIN_TAB_FRAGMENT);
                    transaction.commit();
                    showUpButton(false);
                    return true;
                case R.id.action_menus:
                    clearBackStack();
                    ParkListFragment parkListFragment = new ParkListFragment();
                    transaction.replace(R.id.fragment_container, parkListFragment, MAIN_TAB_FRAGMENT);
                    transaction.commit();
                    showUpButton(false);
                    return true;
                case R.id.action_AR:
                    // Launch ARScannerFragment if AR Core is available on this device
                    if (maybeOpenAR() ) {
                        clearBackStack();
                        ARScannerFragment arScannerFragment = new ARScannerFragment();
                        transaction.replace(R.id.fragment_container, arScannerFragment, MAIN_TAB_FRAGMENT);
                        transaction.commit();
                        return true;
                    }
                    // Otherwise, show default error fragment
                    else {
                        Log.v("button not supported", "found that ARCore is not supported");
                        clearBackStack();
                        ArErrorFragment arErrorFragment = new ArErrorFragment();
                        transaction.replace(R.id.fragment_container, arErrorFragment, MAIN_TAB_FRAGMENT);
                        transaction.commit();
                        return true;
                    }
                case R.id.action_trip_planner:
                    // Update View modelM: fragment is loaded by bottom tab, not AddItemToTrip
                    tripViewModel.setIsAddItemToTrip(false);
                    tripViewModel.setSelectedTrip(null);

                    clearBackStack();
                    TripsViewFragment tripsViewFragment = new TripsViewFragment();
                    transaction.replace(R.id.fragment_container, tripsViewFragment, "TRIPS_VIEW_FRAGMENT");
                    transaction.commit();
                    showUpButton(false);
                    return true;
            }

            return false;
        }
    };

    private void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void showUpButton(boolean show) {
        if(getSupportActionBar()==null)
            return;

        if (show) {
            // Show the back arrow
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        else {
            // Hide the back arrow
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        }
    }

    private Boolean maybeOpenAR(){
        ArCoreApk.Availability availability = ArCoreApk.getInstance().checkAvailability(this);
        /*while (availability.isTransient()) {
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                Log.v("openAR", "catch sleep", e);
            }
        }*/
        if (availability.isUnknown())
        {
            Log.v("unknown", "availability is unknown");
            return false;
        }
        if (availability.isSupported())
        {
            Log.v("supported", "availability is supported");
            return true;
        }
        Log.v("not supported", "availability is NOT supported");
        return false;
    }
}

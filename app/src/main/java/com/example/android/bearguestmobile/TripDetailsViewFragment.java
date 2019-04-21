package com.example.android.bearguestmobile;

import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/*
 * TripDetailsViewFragment
 * Displayed after selecting a specific trip from TripListFragment
 * Loads TripDaysListFragment into frame underneath page header with trip name and dates
 */
public class TripDetailsViewFragment extends Fragment {

    View tripDetailsView;
    FragmentManager fragmentManager;
    TripViewModel tripViewModel;
    private TripDaysListAdapter adapter;

    public TripDetailsViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        tripDetailsView = inflater.inflate(R.layout.fragment_trip_details_view, container, false);
        this.tripViewModel = ViewModelProviders.of((MainActivity)getActivity()).get(TripViewModel.class);

        // Set text of subtitle text view
        //tripViewModel.setTripPageHeader("Trip Details");

        // Set screen title and show up arrow IF loaded from bottom tab, not AddItemToTrip
        if(!tripViewModel.getIsAddItemToTrip()) {
            LiveData<Trip> selectedTrip = ViewModelProviders.of((MainActivity)getActivity()).get(TripViewModel.class).getSelectedTrip();
            ToolbarViewModel toolbarViewModel = ViewModelProviders.of((MainActivity)getActivity()).get(ToolbarViewModel.class);
            toolbarViewModel.setToolbarTitle(selectedTrip.getValue().getTripName());
            toolbarViewModel.setShowBackArrow(true);
        }
        // Hide edit trip functionality if loaded from AddItemToTrip
        else {
            ImageView editIcon = (ImageView) tripDetailsView.findViewById(R.id.icon_edit_trip);
            editIcon.setVisibility(View.GONE);
        }

        addButtonListeners();

        // Set up days list recyclerview
        // from TripDaysListFragment
        RecyclerView recyclerView = (RecyclerView)tripDetailsView.findViewById(R.id.rv_new_days_list);
        Context context = tripDetailsView.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new TripDaysListAdapter(context, new ArrayList<>(), new ArrayList<>());
        recyclerView.setAdapter(adapter);

//        Context context = tripDetailsView.getContext();
//        TripDaysListFragment tripDaysListFragment = new TripDaysListFragment();
//        FragmentTransaction transaction = ((MainActivity)context).getSupportFragmentManager().beginTransaction();
//        transaction
//                .replace(R.id.fragment_container, tripDaysListFragment, "TRIP_DAYS_LIST_FRAGMENT")
//                .commit();


        /*
        // Load list into day_list frame
        Context context = tripDetailsView.getContext();
        TripDaysListFragment tripDaysListFragment = new TripDaysListFragment();
        fragmentManager = this.getChildFragmentManager();
        //FragmentTransaction transaction = ((MainActivity)context).getSupportFragmentManager().beginTransaction();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction
                .replace(R.id.day_list_frame, tripDaysListFragment, "TRIP_DAYS_LIST_FRAGMENT")
                //.replace(R.id.fragment_container, tripDaysListFragment)
                .commit();
        */

        return tripDetailsView;
    }

    public void addButtonListeners() {
        // Close settings screen on exit button click
        final ImageView editIcon = (ImageView) tripDetailsView.findViewById(R.id.icon_edit_trip);
        editIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(v==editIcon) {
                    // Prompt user to edit the trip
                    editTripDialog();
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Declare View Models
        TripViewModel tripViewModel = ViewModelProviders.of((MainActivity)getActivity()).get(TripViewModel.class);
        ItemCommentsViewModel itemCommentsViewModel = ViewModelProviders.of((MainActivity)getActivity()).get(ItemCommentsViewModel.class);

        tripViewModel.getSelectedTrip().observe(getViewLifecycleOwner(), new Observer<Trip>() {
            @Override
            public void onChanged(@Nullable Trip trip) {
                // update UI
                if(trip==null)
                    return;

                Log.v("trip", "tripdetailsview on changed for get selected trip, not null. trip = " + trip.getTripName());

                Date startDateFromJson = trip.getStartDate();
                Date endDateFromJson = trip.getEndDate();
                SimpleDateFormat formatterOutput = new SimpleDateFormat("EEE. M/d/yy");
                String sStartDate = formatterOutput.format(startDateFromJson);
                String sEndDate = formatterOutput.format(endDateFromJson);

                // Set Trip Name
                ((TextView) tripDetailsView.findViewById(R.id.selected_trip)).setText(trip.getTripName());

                // Either show start and end date, or just one date
                if(sStartDate.equals(sEndDate)) {
                    ((TextView) tripDetailsView.findViewById(R.id.text_start_date)).setVisibility(View.INVISIBLE);
                    ((TextView) tripDetailsView.findViewById(R.id.text_end_date)).setVisibility(View.INVISIBLE);

                    ((TextView) tripDetailsView.findViewById(R.id.text_one_date)).setVisibility(View.VISIBLE);
                    ((TextView) tripDetailsView.findViewById(R.id.text_one_date)).setText(sStartDate);
                }
                else {
                    ((TextView) tripDetailsView.findViewById(R.id.text_start_date)).setText(sStartDate);
                    ((TextView) tripDetailsView.findViewById(R.id.text_end_date)).setText(sEndDate);
                }

                // Update Toolbar
                // Set screen title and show up arrow IF loaded from bottom tab, not AddItemToTrip
                if(!tripViewModel.getIsAddItemToTrip()) {
                    ToolbarViewModel toolbarViewModel = ViewModelProviders.of((MainActivity)getActivity()).get(ToolbarViewModel.class);
                    toolbarViewModel.setToolbarTitle(trip.getTripName());
                    toolbarViewModel.setShowBackArrow(true);
                }
            }
        });

        // Observe the trips this user has, reparse if they change
        Uid uid = new Uid();
        uid.setUserID(FirebaseAuth.getInstance().getUid());

        tripViewModel.getTripsByUser(uid).observe(getViewLifecycleOwner(), new Observer<List<Trip>>() {
            @Override
            public void onChanged(@Nullable List<Trip> tripList) {
                // update UI
                Log.v("trip", "tripdetailsviewfrag, onChanged for getTripsByUser");

                // Locate selected trip
                for(Trip t: tripList) {
                    if (t.getTripID().equals(tripViewModel.getSelectedTrip().getValue().getTripID())) {
                        // Update view model
                        tripViewModel.setSelectedTrip(t);

                        // Parse new dates and meals for that trip
                        Date sDate = t.getStartDate();
                        Date eDate = t.getEndDate();
                        Log.v("trip", "tripdetailsviewfrag onChanged for getTripsByUser, start date = " + t.getStartDate().toString()
                                + ", end date = " + t.getEndDate().toString());
                        Log.v("trip", "tripdetailsviewfrag onChanged for getTripsByUser, selected tripName = " + t.getTripName());

                        if (sDate != null && eDate != null) {
                            List<Date> tripDayList = getDates(sDate, eDate);
                            adapter.setDayList(tripDayList);
                            Log.v("trip", "setting day list for adapter");
                        }

                        adapter.setMealList(t.mealsByDay);
                        Log.v("trip", "setting meal list for adapter");

                    }
                }
            }
        });

        // Send all the favorited items to the daysListAdapter
        itemCommentsViewModel.getUserFavoritedItems(uid).observe(this, new Observer<List<MenuItem>>() {
            @Override
            public void onChanged(@Nullable List<MenuItem> menuItemList) {
                // update ui
                adapter.setFavoriteList(menuItemList);
            }
        });


    }

    // Helper method to generate list of dates within trip range
    private static List<Date> getDates(Date dateString1, Date dateString2)
    {
        ArrayList<Date> dates = new ArrayList<Date>();

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(dateString1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(dateString2);

        while(!cal1.after(cal2))
        {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }

    private void editTripDialog() {
        AlertDialog.Builder alertDialogBuilder
                = new AlertDialog.Builder((MainActivity)getActivity(), R.style.AppCompatAlertDialogStyle);

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.fragment_edit_trip_alert, (ViewGroup) getView(), false);

        String titleText = "Edit Trip";
        String dismissText = "Cancel";
        String confirmText= "Save";
        String deleteText = "Delete Trip";

        //final ToolbarFragment toolbarFragment = ((MainActivity)getActivity()).topToolbar;

        final TripViewModel tripViewModel = ViewModelProviders.of((MainActivity)getActivity()).get(TripViewModel.class);
        final EditText newTripName = (EditText) viewInflated.findViewById(R.id.input_new_trip_name);
        final DatePicker datePickerStart = ((DatePicker) viewInflated.findViewById(R.id.picker_start_date));
        final DatePicker datePickerEnd = ((DatePicker) viewInflated.findViewById(R.id.picker_end_date));
        final SimpleDateFormat formatterInput = new SimpleDateFormat("M/d/yyyy");

        // Save state of current trip values, to know what has changed
        final Trip selectedTrip = ViewModelProviders.of((MainActivity)getActivity()).get(TripViewModel.class).getSelectedTrip().getValue();

        // Prepopulate trip name field with current value
        newTripName.setText(selectedTrip.getTripName());

        // TODO prepopulate date as well?

        alertDialogBuilder
                .setView(viewInflated)
                .setCancelable(true)
                .setNegativeButton(dismissText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                // Delete this trip, then go back to Trips View list
                .setNeutralButton(deleteText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        // Create TripID obj for this trip, then make deleteTrip call
                        Trip tripToDelete = new Trip();
                        tripToDelete.setTripID(selectedTrip.getTripID());
                        tripViewModel.deleteTrip(tripToDelete);

                        // Show whole list of trips, minus that just deleted
                        ((MainActivity)getActivity()).getSupportFragmentManager().popBackStackImmediate();
                    }
                })
                .setPositiveButton(confirmText,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // Obtain values set for trip name, start & end date
                        // Format input dates as MM/dd/yyyy
                        String sTripName = newTripName.getText().toString().trim();
                        String sDateStart = (datePickerStart.getMonth()+1) + "/" +
                                datePickerStart.getDayOfMonth() + "/" +
                                datePickerStart.getYear();

                        String sDateEnd   = (datePickerEnd.getMonth()+1) + "/" +
                                datePickerEnd.getDayOfMonth() + "/" +
                                datePickerEnd.getYear();

                        // Check if TripName has been changed
                        if(!sTripName.isEmpty() && !sTripName.equals(selectedTrip.getTripName())) {
                            // update trip name
                            Log.v("updateTrip", "new trip name: " + sTripName);

                            // Create Update obj to send to database
                            Update updateTripName = new Update();
                            updateTripName.setTripID(selectedTrip.getTripID());
                            updateTripName.setFieldName("tripName");
                            updateTripName.setsNewContent(sTripName);

                            tripViewModel.updateTripName(updateTripName);
                        }

                        // Check if the dates have been changed
                        // First, parse the input dates so they can be compared
                        try {
                            Date dParsedStart = formatterInput.parse(sDateStart);
                            Date dParsedEnd   = formatterInput.parse(sDateEnd);

                            // Make sure selected dates are in proper order
                            if(dParsedEnd.before(dParsedStart)) {
                                // If the dates don't make sense, launch the error dialog again with warning Toast
                                Log.v("updateTrip", "dates not in valid order");
                                editTripDialog();
                                Toast t = Toast.makeText(getContext(), "Oops: End date cannot occur before start date.",
                                    Toast.LENGTH_SHORT);
                                t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                t.show();

                                return;
                            }

                            // Check if input dates differ from current values
                            if(!dParsedStart.equals(selectedTrip.getStartDate())) {
                                // Send to database
                                Log.v("updateTrip", "dParsedStart=" + dParsedStart.toString() + " is diff from current start date=" +
                                        selectedTrip.getStartDate().toString());

                                Trip updateStartDate = new Trip();
                                updateStartDate.setTripID(selectedTrip.getTripID());
                                updateStartDate.setNewContent(dParsedStart);
                                updateStartDate.setFieldName("startDate");

                                tripViewModel.updateTripDate(updateStartDate);
                            }

                            if(!dParsedEnd.equals(selectedTrip.getEndDate())) {
                                // Send to database
                                Log.v("updateTrip", "dParsedEnd=" + dParsedEnd.toString() + " is diff from current end date=" +
                                        selectedTrip.getEndDate().toString());

                                Trip updateEndDate = new Trip();
                                updateEndDate.setTripID(selectedTrip.getTripID());
                                updateEndDate.setNewContent(dParsedEnd);
                                updateEndDate.setFieldName("endDate");

                                tripViewModel.updateTripDate(updateEndDate);
                            }

                        }
                        catch(ParseException e) {
                            Log.v("updateTrip", "parse exception", e);
                        }

                        dialog.dismiss();

                        // Refresh page
                        Fragment frg = null;

                        frg = ((MainActivity)getActivity()).getSupportFragmentManager().findFragmentByTag("TRIP_DETAILS_FRAGMENT");
                        final FragmentTransaction ft = ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction();
                        if(frg!=null) {
                            ft.detach(frg);
                            ft.attach(frg);
                            ft.commit();
                            Log.v("Trip", "Reloaded trips details fragment");
                        }
                        else {
                            Log.v("Trip", "Failed to reload trips details fragment");
                        }
                    }
                });

        // create and show dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

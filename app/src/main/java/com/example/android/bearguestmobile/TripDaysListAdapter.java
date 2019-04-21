package com.example.android.bearguestmobile;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TripDaysListAdapter extends RecyclerView.Adapter<TripDaysListAdapter.ViewHolder> {

    private List<Date> mDataDays;
    private List<Meal> mDataMeals;
    private List<MenuItem> mFavoritedItems;
    private LayoutInflater mInflater;
    private Context context;
    private Meal mealToAdd;
    private String mInputMealName = "default";
    private TripViewModel tripViewModel;

    // data is passed into the constructor
    TripDaysListAdapter(Context context, List<Date> dataDays, List<Meal> dataMeals) {
        this.mInflater = LayoutInflater.from(context);
        this.mDataDays = dataDays;
        this.mDataMeals = dataMeals;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public TripDaysListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fragment_trip_days_list_row, parent, false);
        tripViewModel = ViewModelProviders.of((MainActivity)context).get(TripViewModel.class);


        return new ViewHolder(view);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView dayHeader;
        TextView noMealsMessage;
        ImageView addIcon;
        RecyclerView recyclerView;

        ViewHolder(View tripDaysView) {
            super(tripDaysView);
            dayHeader = tripDaysView.findViewById(R.id.text_day_header);
            noMealsMessage = tripDaysView.findViewById(R.id.text_no_meals);
            addIcon = tripDaysView.findViewById(R.id.icon_add_to_trip);
            recyclerView = tripDaysView.findViewById(R.id.rv_horiz_meals_list);
            addIcon.setOnClickListener(this);
            tripDaysView.setOnClickListener(this);
        }

        // Handle clicks to list of trips to show trip's detailed view
        @Override
        public void onClick(View view) {
            // If user clicks add button next to a certain day, POST to database
            // Used in AddItemToTrip only
            if(view.getId()==addIcon.getId()) {

                // const {tripID, restaurantID, day, mealName} = req.body;
                TripViewModel tripViewModel = ViewModelProviders.of((MainActivity)context).get(TripViewModel.class);
                DashboardViewModel dashboardViewModel = ViewModelProviders.of((MainActivity)context).get(DashboardViewModel.class);

                mealToAdd = new Meal();
                mealToAdd.setTripID(tripViewModel.getSelectedTrip().getValue().getTripID());
                mealToAdd.setRestaurantID(dashboardViewModel.getSelectedRestaurantID().getValue().getRestaurantID());
                mealToAdd.setDay(mDataDays.get(getAdapterPosition()));

                // Obtain meal name from user
                // Launches alert dialog to get meal name input, and on 'submit' sends info to trip view model
                // which is observed by AddItemsToTripFragment
                setMealName("Enter Meal Name", "Which meal is this?", "Submit", "");
            }
        }
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(TripDaysListAdapter.ViewHolder holder, int position) {
        Date dayDate = mDataDays.get(position);
        SimpleDateFormat formatterOutput = new SimpleDateFormat("EEEE, M/d/yy");
        String sDate = formatterOutput.format(dayDate);

        holder.dayHeader.setText("Day " + (position+1) + " - " + sDate);

        // Only show add button on "add to trip" rendering
        if(holder.addIcon!=null && ViewModelProviders.of((MainActivity)context).get(TripViewModel.class).getIsAddItemToTrip()) {
            holder.addIcon.setVisibility(View.VISIBLE);
        }

        // Get horizontal scroll of meals in each vertical view
        ArrayList<Meal> parsedMeals = new ArrayList<>();

        // Parse out meals for this current day
        for(Meal m : mDataMeals) {
            // Add all meals for this current day to the array
            if(m.getDay().equals(dayDate) && m.getTripID().equals(tripViewModel.getSelectedTrip().getValue().getTripID())) {
                parsedMeals.add(m);
            }
        }

        if(!parsedMeals.isEmpty()) {
            Log.v("trip", "parsedMeals size=" + parsedMeals.size() + "and contains " + parsedMeals.get(0).getMealName());
        }
        else {
            Log.v("trip", "parsedMeals is empty");
            // Set a message
            holder.noMealsMessage.setVisibility(View.VISIBLE);
        }

        //TripMealListAdapter adapter = new TripMealListAdapter(context, mDataMeals);
        TripMealListAdapter adapter = new TripMealListAdapter(context, parsedMeals, mFavoritedItems);
        holder.recyclerView.setAdapter(adapter);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mDataDays.size();
    }

    // convenience method for getting data at click position
    Date getItem(int id) {
        return mDataDays.get(id);
    }

    // Update the data (list of trips) when changed
    public void setDayList(List<Date> data) {
        this.mDataDays = data;
        this.notifyDataSetChanged();
    }

    // Update the data (list of meals) when changed
    public void setMealList(List<Meal> data) {
        this.mDataMeals = data;
        this.notifyDataSetChanged();
    }

    public void setFavoriteList(List<MenuItem> data) {
        this.mFavoritedItems = data;
        this.notifyDataSetChanged();
    }

    // Display custom alert dialog with edit text input field when user is adding item to trip
    // Reusing the custom 'Forgot Password' alert with different hint string...
    public void setMealName(String dialogTitle, String dialogText, String dismissText, String errorText) {

        AlertDialog.Builder alertDialogBuilder
                = new AlertDialog.Builder((MainActivity)context, R.style.AppCompatAlertDialogStyle);

        View viewInflated = LayoutInflater.from(context).inflate(R.layout.fragment_forgot_pwd_alert, null);
        final EditText inputMealName = (EditText) viewInflated.findViewById(R.id.input_email);
        final TextView response = (TextView) viewInflated.findViewById(R.id.text_response);

        inputMealName.setHint("Breakfast, Snack, Dinner...");

        // Set dialog view and title
        alertDialogBuilder.setView(viewInflated);
        alertDialogBuilder.setTitle(dialogTitle);

        // If there is an error message, display it
        if(errorText!=null) {
            response.setText(errorText);
        }

        // Set dialog message and button text
        alertDialogBuilder
                .setMessage(dialogText)
                .setCancelable(true)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(dismissText,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // Capture the string user entered for meal name
                        mInputMealName = inputMealName.getText().toString().trim();

                        // Check for null input on email
                        if(mInputMealName.isEmpty()) {
                            setMealName(dialogTitle, dialogText, dismissText, "Whoops, each meal requires a name. Please try again.");
                            return;
                        }
                        // Else, we have a valid trip name. Update mealToAdd value for mealName
                        // Check to make sure it's not a duplicate name for that trip and day? See what e is returned
                        else {
                            mealToAdd.setMealName(mInputMealName);

                            // Send trip to view model so that AddItemToTripFragment can close the window
                            // and post to database
                            ViewModelProviders.of((MainActivity)context).get(TripViewModel.class).setMealToAdd(mealToAdd);
                        }

                        dialog.dismiss();
                    }
                });

        // create and show dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

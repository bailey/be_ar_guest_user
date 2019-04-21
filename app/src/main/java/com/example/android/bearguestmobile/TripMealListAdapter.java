package com.example.android.bearguestmobile;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import java.util.List;

public class TripMealListAdapter extends RecyclerView.Adapter<TripMealListAdapter.ViewHolder> {

    private List<Meal> mData;
    private List<MenuItem> mFavoritedItems;
    private LayoutInflater mInflater;
    private Context context;
    private final TripViewModel tripViewModel;

    // data is passed into the constructor
    TripMealListAdapter(Context context, List<Meal> data, List<MenuItem> favoritedItems) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        this.mFavoritedItems = favoritedItems;
        this.tripViewModel = ViewModelProviders.of((MainActivity)context).get(TripViewModel.class);

    }

    // inflates the row layout from xml when needed
    @Override
    public TripMealListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fragment_trip_meal_list_row, parent, false);
        return new ViewHolder(view);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textMealName;
        TextView textRestaurantName;
        TextView textFavoritedItems;

        ViewHolder(View tripMealsView) {
            super(tripMealsView);
            textMealName = tripMealsView.findViewById(R.id.text_meal_type);
            textRestaurantName = tripMealsView.findViewById(R.id.text_restaurant_name);
            textFavoritedItems = tripMealsView.findViewById(R.id.text_favorites);
            tripMealsView.setOnClickListener(this);
        }

        // Handle clicks to list of meals to show meal's detailed view?
        @Override
        public void onClick(View view) {
            // Launch alert dialog to let user edit that meal if they want, but not if this
            // was launched from AddItemToTrip
            if(!tripViewModel.getIsAddItemToTrip()) {
                showDialog("Edit Meal", "Rename or remove this meal.", "Save", "", getAdapterPosition());
            }
        }
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(TripMealListAdapter.ViewHolder holder, int position) {
        String mealName = mData.get(position).getMealName();

        holder.textMealName.setText(mealName);
        holder.textRestaurantName.setText(mData.get(position).getRestaurantName());

        if(mFavoritedItems!=null) {
            // Determine if there are any favorited items at this restaurant
            String favoritedItems = "Favorites: ";
            for(MenuItem m: mFavoritedItems){
                if(m.getRestaurantID()==mData.get(position).getRestaurantID()) {
                    favoritedItems = favoritedItems + m.getItemName() + ", ";
                }
            }

            // Remove comma from end of string
            favoritedItems = favoritedItems.replaceAll(", $", "");

            // If the string has been updated, there are favorites from this restaurant
            if(!favoritedItems.equals("Favorites: ")) {
                holder.textFavoritedItems.setText(favoritedItems);
            }
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    Meal getItem(int id) {
        return mData.get(id);
    }

    private void showDialog(String dialogTitle, String dialogText, String dismissText, String errorText, int position) {
        AlertDialog.Builder alertDialogBuilder
                = new AlertDialog.Builder((MainActivity)context, R.style.AppCompatAlertDialogStyle);

        View viewInflated = LayoutInflater.from(context).inflate(R.layout.fragment_forgot_pwd_alert, null);

        final EditText inputNewMealName = (EditText) viewInflated.findViewById(R.id.input_email);
        inputNewMealName.setHint("Breakfast, Snack, Dessert...");
        final TextView response = (TextView) viewInflated.findViewById(R.id.text_response);

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
                        String sNewMealName = inputNewMealName.getText().toString().trim();
                        Log.v("Trip", "new meal name = " + sNewMealName);

                        // If user input is blank, show message again with error
                        if(sNewMealName.isEmpty()) {
                            showDialog(dialogTitle, dialogText, dismissText, "No new meal name specified.", position);
                            dialog.dismiss();
                            return;
                        }

                        // Otherwise, we are changing the Meal Name
                        Meal update = new Meal();
                        update.setTripID(mData.get(position).getTripID());
                        update.setMealName(mData.get(position).getMealName());
                        update.setFieldName("mealName");
                        update.setNewContent(sNewMealName);
                        update.setDay(mData.get(position).getDay());
                        update.setRestaurantID(mData.get(position).getRestaurantID());

                        tripViewModel.updateMeal(update);

                        // Automatically reload meal list after rename
                        Fragment frg = null;

                        frg = ((MainActivity)context).getSupportFragmentManager().findFragmentByTag("TRIP_DETAILS_FRAGMENT");
                        final FragmentTransaction ft = ((MainActivity)context).getSupportFragmentManager().beginTransaction();
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
                })
                .setNeutralButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete this meal
                        Log.v("Trip", "Delete meal");
                        dialog.dismiss();

                        // Send Update to database
                        Meal meal = new Meal();
                        meal.setTripID(mData.get(position).getTripID());
                        meal.setRestaurantID(mData.get(position).getRestaurantID());
                        meal.setDay(mData.get(position).getDay());
                        meal.setMealName(mData.get(position).getMealName());

                        tripViewModel.deleteMeal(meal);

                        // Automatically reload meal list after delete
                        Fragment frg = null;

                        frg = ((MainActivity)context).getSupportFragmentManager().findFragmentByTag("TRIP_DETAILS_FRAGMENT");
                        final FragmentTransaction ft = ((MainActivity)context).getSupportFragmentManager().beginTransaction();
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

package com.example.android.bearguestmobile;


import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/*
 * Add new Trip
 * Called from main Trip tab on FAB click
 * Slides up full screen fragment so user can set trip dates and title
 */
public class TripCreateFragment extends Fragment {

    private View tripCreateView;

    public TripCreateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        tripCreateView = inflater.inflate(R.layout.fragment_trip_create, container, false);

        addButtonListeners();
        hideKeyboardOnUnfocus();

        return tripCreateView;
    }

    private void addButtonListeners() {
        final Button button_close = (Button) tripCreateView.findViewById(R.id.button_close);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == button_close){
                    // Must manually close keyboard
                    hideKeyboardFrom((MainActivity)getActivity(), tripCreateView);

                    // Close without saving, show previous fragment that had been loaded
                    ((MainActivity)getActivity()).getSupportFragmentManager().popBackStackImmediate();
                }
            }
        });

        final Button button_create = (Button) tripCreateView.findViewById(R.id.button_create);
        button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == button_create){
                    // Capture input from user
                    TextInputLayout textInputLayout = ((TextInputLayout) tripCreateView.findViewById(R.id.text_new_trip_input));
                    String newTripName = textInputLayout.getEditText().getText().toString().trim();
                    DatePicker datePickerStart = ((DatePicker) tripCreateView.findViewById(R.id.picker_start_date));
                    DatePicker datePickerEnd = ((DatePicker) tripCreateView.findViewById(R.id.picker_end_date));

                    // Must manually close keyboard
                    hideKeyboardFrom((MainActivity)getActivity(), tripCreateView);

                    // Check for valid trip name input
                    if(newTripName.isEmpty()) {
                        textInputLayout.setError("Required Field");
                        return;
                    }

                    // Format input dates as MM/dd/yyyy
                    String sDateStart = (datePickerStart.getMonth()+1) + "/" +
                                         datePickerStart.getDayOfMonth() + "/" +
                                         datePickerStart.getYear();

                    String sDateEnd   = (datePickerEnd.getMonth()+1) + "/" +
                                         datePickerEnd.getDayOfMonth() + "/" +
                                         datePickerEnd.getYear();

                    // Provide SimpleDateFormats, specifying input and output formats where output
                    // matches the JSON request format (?)
                    SimpleDateFormat formatterInput = new SimpleDateFormat("M/d/yyyy");
                    SimpleDateFormat formatterOutput = new SimpleDateFormat("yyyy-MM-dd");

                    // Captured time zone is in the user's local time zone, but save it as UTC
                    formatterInput.setTimeZone(Calendar.getInstance().getTimeZone());
                    formatterOutput.setTimeZone(TimeZone.getTimeZone("UTC"));

                    try {
                        Date dParsedStart = formatterInput.parse(sDateStart);
                        Date dParsedEnd   = formatterInput.parse(sDateEnd);

                        // Make sure selected dates are in proper order
                        if(dParsedEnd.before(dParsedStart)) {
                            showErrorDialog("Trip Date Error",
                                            "The trip end date occurs before the trip start date. That doesn't make sense...",
                                            "Whoops");
                            return;
                        }

                        /*
                        Toast.makeText((MainActivity)getActivity(), newTripName + " start date=" +
                                formatterOutput.format(dParsedStart) +
                                ", to String= " + dParsedStart.toString(), Toast.LENGTH_SHORT).show();
                        */

                        // Use the saved title and dates to POST to database
                        Trip newTrip = new Trip();
                        newTrip.setUserID(FirebaseAuth.getInstance().getUid());
                        newTrip.setStartDate(dParsedStart);
                        newTrip.setEndDate(dParsedEnd);
                        newTrip.setTripName(newTripName);
                        ViewModelProviders.of((MainActivity)getActivity()).get(TripViewModel.class).addTrip(newTrip);
                    }
                    catch(ParseException e) {
                        Log.v("tripcreate", "parse exception", e);
                    }

                    // Close window
                    ((MainActivity)getActivity()).getSupportFragmentManager().popBackStackImmediate();


                    // Refresh page
                    Fragment frg = null;

                    frg = ((MainActivity)getActivity()).getSupportFragmentManager().findFragmentByTag("TRIPS_VIEW_FRAGMENT");
                    final FragmentTransaction ft = ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction();
                    if(frg!=null) {
                        ft.detach(frg);
                        ft.attach(frg);
                        ft.commit();
                        Log.v("Trip Create", "Reloaded trips list fragment");
                    }
                    else{
                        Log.v("Trip Create", "Failed to reload trips list fragment");
                    }
                }
            }
        });
    }

    public static void hideKeyboardFrom(Context context, View view) {
        if(context==null)
            return;
        // Must use Input Method Manager to close the keyboard
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void hideKeyboardOnUnfocus() {
        // Close keyboard when the user clicks on anything other than the EditText
        ((TextInputLayout) tripCreateView
                .findViewById(R.id.text_new_trip_input))
                .getEditText()
                .setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            hideKeyboardFrom((MainActivity)getActivity(), tripCreateView);
                        }
                    }
                });
    }

    private void showErrorDialog(String errorTitle, String errorText, String dismissText) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder((MainActivity)getActivity());

        // Set dialog title
        alertDialogBuilder.setTitle(errorTitle);

        // Set dialog message and button text
        alertDialogBuilder
                .setMessage(errorText)
                .setCancelable(false)
                .setPositiveButton(dismissText,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.dismiss();
                    }
                });

        // create and show dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

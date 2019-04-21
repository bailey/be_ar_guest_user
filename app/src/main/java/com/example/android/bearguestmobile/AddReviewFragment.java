package com.example.android.bearguestmobile;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class AddReviewFragment extends Fragment {

    View addReviewFragmentView;


    public AddReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addReviewFragmentView = inflater.inflate(R.layout.fragment_add_review, container, false);
        
        addButtonListeners();

        return addReviewFragmentView;
    }
    
    private void addButtonListeners() {
        final Button button_close = (Button) addReviewFragmentView.findViewById(R.id.button_close);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == button_close){
                    // Close without saving, show previous fragment that had been loaded
                    // Must manually close keyboard
                    hideKeyboardFrom((MainActivity)getActivity(), addReviewFragmentView);
                    ((MainActivity)getActivity()).getSupportFragmentManager().popBackStackImmediate();
                }
            }
        });

        final Button button_post = (Button) addReviewFragmentView.findViewById(R.id.button_post);
        button_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == button_post){
                    // Capture star rating and review text
                    float rating = ((RatingBar) addReviewFragmentView.findViewById(R.id.ratingBar_review)).getRating();
                    String review = ((EditText) addReviewFragmentView.findViewById(R.id.editText_leave_comment)).getText().toString().trim();

                    // Comment and rating must have a value
                    if(review.equals("") || rating==0) {
                        Toast.makeText((MainActivity)getActivity(), "Reviews must include a rating and comment.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // Must manually close keyboard
                        hideKeyboardFrom((MainActivity)getActivity(), addReviewFragmentView);

                        // Send to database...
                        // const {userID, itemID, comment, rating, isFavorite, flag} = req.body;
                        AddReview newReview = new AddReview();
                        DashboardViewModel dashboardViewModel = ViewModelProviders.of((MainActivity)getActivity()).get(DashboardViewModel.class);

                        ItemCommentsViewModel itemCommentsViewModel = ViewModelProviders.of((MainActivity)getActivity()).get(ItemCommentsViewModel.class);

                        newReview.setUserID(FirebaseAuth.getInstance().getUid());
                        newReview.setItemID(dashboardViewModel.getSelectedMenuItem().getValue().getItemID());
                        newReview.setComment(review);
                        newReview.setRating(rating);

                        itemCommentsViewModel.addNewComment(newReview, new ItemID(newReview.getItemID()));

                        // Return to previous fragments
                        ((MainActivity)getActivity()).getSupportFragmentManager().popBackStackImmediate();
                    }
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Declare View Models
        final ItemCommentsViewModel itemCommentsViewModel = ViewModelProviders.of((MainActivity) getActivity()).get(ItemCommentsViewModel.class);
        final DashboardViewModel dashboardViewModel = ViewModelProviders.of((MainActivity) getActivity()).get(DashboardViewModel.class);

        // If this user is editing an existing review, show their old review content
        ItemID itemID = new ItemID(dashboardViewModel.getSelectedMenuItem().getValue().getItemID());
        itemCommentsViewModel.getAllReviewsByItem(itemID).observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(@Nullable List<Review> reviewList) {
                if(!reviewList.isEmpty()) {
                    for(Review r : reviewList) {
                        // If this is the user's review, and it's not just a favorite/flag indicator
                        // then prepopulate the comment and rating
                        if(r.getUserID().equals(FirebaseAuth.getInstance().getUid())
                           && r.getComment()!=null && r.getRating()!=0) {
                            EditText entry = addReviewFragmentView.findViewById(R.id.editText_leave_comment);
                            RatingBar rating = ((RatingBar) addReviewFragmentView.findViewById(R.id.ratingBar_review));
                            entry.setText(r.getComment());
                            rating.setRating(r.getRating());
                        }
                    }
                }
            }
        });
    }

    protected void showSoftwareKeyboard(boolean showKeyboard){
        final Activity activity = (MainActivity)getActivity();
        final InputMethodManager inputManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), showKeyboard ? InputMethodManager.SHOW_FORCED : InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        if(context==null)
            return;
        // Must use Input Method Manager to
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}

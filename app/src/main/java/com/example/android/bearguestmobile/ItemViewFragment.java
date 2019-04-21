package com.example.android.bearguestmobile;


import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.media.Rating;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

/*
 * Detailed view of item
 * Loads the ItemCommentsFragment to show list of comments for that Item
 * Provides buttons for adding review, adding to trip, and favoriting
 */
public class ItemViewFragment extends Fragment {
    private View itemFragmentView;
    private Context context;
    private ItemCommentsViewModel itemCommentsViewModel;

    public ItemViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itemFragmentView = inflater.inflate(R.layout.fragment_item_view, container, false);

        // Set screen title and up arrow
        ToolbarViewModel toolbarViewModel = ViewModelProviders.of((MainActivity) getActivity()).get(ToolbarViewModel.class);
        toolbarViewModel.setToolbarTitle("Item View");
        toolbarViewModel.setShowBackArrow(true);

        // Initialize number of reviews to zero
        final TextView numRatings = (TextView)itemFragmentView.findViewById(R.id.item_num_ratings);
        numRatings.setText("0 reviews");

        // Initialize hasCommentForItem to false
        this.itemCommentsViewModel = ViewModelProviders.of((MainActivity) getActivity()).get(ItemCommentsViewModel.class);
        itemCommentsViewModel.setHasCommentForItem(false);


        // Handle buttons and rating bar
        addButtonListeners();
        setOverallRating();

        // Load item comments fragment
        context = itemFragmentView.getContext();
        ItemCommentsFragment itemCommentsFragment = new ItemCommentsFragment();
        FragmentTransaction transaction = ((MainActivity)context).getSupportFragmentManager().beginTransaction();
        transaction
                .replace(R.id.frame_comments_fragment, itemCommentsFragment, "ITEM_COMMENTS_FRAGMENT")
                .commit();

        return itemFragmentView;
    }

    private void addButtonListeners() {
        // Handle clicks to bookmark, +Trip, and +Review button
        final Button button_addToTrip = (Button) itemFragmentView.findViewById(R.id.button_add_to_trip);
        button_addToTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == button_addToTrip){
                    // Notify fragments to display the "Add to Trip" icon button
                    TripViewModel tripViewModel = ViewModelProviders.of((MainActivity)context).get(TripViewModel.class);
                    tripViewModel.setIsAddItemToTrip(true);

                    // AddItemToTripFragment slides up from the bottom
                    AddItemToTripFragment addItemToTripFragment = new AddItemToTripFragment();
                    FragmentTransaction transaction = ((MainActivity)context).getSupportFragmentManager().beginTransaction();
                    transaction
                            .setCustomAnimations(R.anim.slide_up, 0, 0, R.anim.slide_down)
                            .replace(android.R.id.content, addItemToTripFragment)
                            .addToBackStack("TAG_ADD_ITEM_TO_TRIP")
                            .commit();
                }
            }
        });

        final Button button_addReview = (Button) itemFragmentView.findViewById(R.id.button_add_review);
        button_addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == button_addReview){
                    // Create full screen fragment to slide up and over entire screen
                    AddReviewFragment addReviewFragment = new AddReviewFragment();
                    FragmentTransaction transaction = ((MainActivity)context).getSupportFragmentManager().beginTransaction();
                    transaction
                            .setCustomAnimations(R.anim.slide_up, 0, 0, R.anim.slide_down)
                            .replace(R.id.fragment_container, addReviewFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        final ImageButton button_addFavorite = (ImageButton) itemFragmentView.findViewById(R.id.button_favorite);
        //final Image button_addFavorite = (Image) itemFragmentView.findViewById(R.id.button_favorite);
        button_addFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == button_addFavorite){
                    // Todo: Add to user's favorite
                    ((ImageView)v).setImageResource(R.drawable.item_heart_filled);
                    DashboardViewModel dashboardViewModel = ViewModelProviders.of((MainActivity)getActivity()).get(DashboardViewModel.class);
                    ItemCommentsViewModel itemCommentsViewModel = ViewModelProviders.of((MainActivity) getActivity()).get(ItemCommentsViewModel.class);

                    // On click, set the favorite to the opposite of its current value
                    int oppOfCurrent = (itemCommentsViewModel.getIsSelectedItemFavorite()) == 0 ? 1 : 0;
                    Favorite newFav = new Favorite(FirebaseAuth.getInstance().getUid(),
                                                    dashboardViewModel.getSelectedMenuItem().getValue().getItemID(),
                                                    oppOfCurrent);

                    itemCommentsViewModel.updateFavoriteStatus(newFav);

                    // Update the icon color
                    if(oppOfCurrent==1) {
                        ((ImageView)v).setImageResource(R.drawable.item_heart_filled);
                        itemCommentsViewModel.setIsSelectedItemFavorite(oppOfCurrent);
                    }
                    else {
                        ((ImageView)v).setImageResource(R.drawable.item_view_heart_outline);
                        itemCommentsViewModel.setIsSelectedItemFavorite(oppOfCurrent);
                    }
                }
            }
        });
    }

    private void setOverallRating() {
        final RatingBar ratingBar = (RatingBar) itemFragmentView.findViewById(R.id.ratingBar);
        final TextView numRatings = (TextView)itemFragmentView.findViewById(R.id.item_num_ratings);

        // Declare View Models
        ItemCommentsViewModel itemCommentsViewModel = ViewModelProviders.of((MainActivity) getActivity()).get(ItemCommentsViewModel.class);
        DashboardViewModel dashboardViewModel = ViewModelProviders.of((MainActivity)getActivity()).get(DashboardViewModel.class);

        // Get the menu item currently selected, create ItemID object for that item
        LiveData<MenuItem> menuItem = dashboardViewModel.getSelectedMenuItem();
        ItemID itemID = new ItemID(menuItem.getValue().getItemID());

        // Observe a list of all comments for that menu item
        itemCommentsViewModel.getAllReviewsByItem(itemID).observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(@Nullable List<Review> reviewList) {
                // Update the UI
                float totalRating = 0;
                int i = 0;
                for (i=0; i<reviewList.size(); i++) {
                    totalRating += reviewList.get((int)i).getRating();
                }
                float averageRating = totalRating / i;

                ratingBar.setRating(averageRating);
                numRatings.setText(i + " reviews");
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Declare View Models
        final ItemCommentsViewModel itemCommentsViewModel = ViewModelProviders.of((MainActivity) getActivity()).get(ItemCommentsViewModel.class);
        final DashboardViewModel dashboardViewModel = ViewModelProviders.of((MainActivity)getActivity()).get(DashboardViewModel.class);

        // Set item and item description
        dashboardViewModel.getSelectedMenuItem().observe(this, new Observer<MenuItem>() {
            @Override
            public void onChanged(@Nullable MenuItem selectedItem) {
                ((TextView) itemFragmentView.findViewById(R.id.item_view_title)).setText(selectedItem.getItemName());
                ((TextView) itemFragmentView.findViewById(R.id.item_view_description)).setText(selectedItem.getItemDescription());
            }
        });

        // Set if the icon has a favorite or not
        Uid uid = new Uid();
        uid.setUserID(FirebaseAuth.getInstance().getUid());
        itemCommentsViewModel.getUserFavoritedItems(uid).observe(this, new Observer<List<MenuItem>>() {
            @Override
            public void onChanged(@Nullable List<MenuItem> menuItemList) {
                // update ui
                ImageButton button_addFavorite = (ImageButton) itemFragmentView.findViewById(R.id.button_favorite);
                button_addFavorite.setImageResource(R.drawable.item_view_heart_outline);
                itemCommentsViewModel.setIsSelectedItemFavorite(0);

                for(MenuItem m: menuItemList) {
                    if(m.getItemID()== dashboardViewModel.getSelectedMenuItem().getValue().getItemID()) {
                        // This item is in the user's favorites, change the favorite icon
                        button_addFavorite.setImageResource(R.drawable.item_heart_filled);
                        itemCommentsViewModel.setIsSelectedItemFavorite(1);
                        return;
                    }
                }
            }
        });

        itemCommentsViewModel.getHasCommentForItem().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean hasComment) {
                Button button_add_review = (Button) itemFragmentView.findViewById(R.id.button_add_review);

                // Change text for "Add/Edit Review" button; show "edit" if user already has comment
                if(hasComment) {
                    button_add_review.setText("Edit Review");
                }
                else {
                    button_add_review.setText("Add Review");
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("onResume", "here");
    }

}

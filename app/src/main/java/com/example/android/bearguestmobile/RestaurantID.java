package com.example.android.bearguestmobile;

import com.google.gson.annotations.SerializedName;

public class RestaurantID {
    @SerializedName("restaurantID")
    private int restaurantID;

    public RestaurantID(int inputID) {
        this.restaurantID = inputID;
    }

    public int getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(int restaurantID) {
        this.restaurantID = restaurantID;
    }
}

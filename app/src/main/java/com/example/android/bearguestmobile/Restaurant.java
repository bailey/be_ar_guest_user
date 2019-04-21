package com.example.android.bearguestmobile;

import com.google.gson.annotations.SerializedName;

public class Restaurant {

    @SerializedName("restaurantID")
    int restaurantID;
    @SerializedName("landID")
    int landID;
    @SerializedName("restaurantTypeID")
    int restaurantTypeID;
    @SerializedName("restaurantName")
    String restaurantName;
    @SerializedName("restaurantStatus")
    String restaurantStatus;

    public Restaurant(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public int getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(int restaurantID) {
        this.restaurantID = restaurantID;
    }

    public int getLandID() {
        return landID;
    }

    public void setLandID(int landID) {
        this.landID = landID;
    }

    public int getRestaurantTypeID() {
        return restaurantTypeID;
    }

    public void setRestaurantTypeID(int restaurantTypeID) {
        this.restaurantTypeID = restaurantTypeID;
    }

    public String isRestaurantStatus() {
        return restaurantStatus;
    }

    public void setRestaurantStatus(String restaurantStatus) {
        this.restaurantStatus = restaurantStatus;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}

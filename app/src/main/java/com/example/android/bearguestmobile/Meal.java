package com.example.android.bearguestmobile;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Meal {

    @SerializedName("tripID")
    private String tripID;
    @SerializedName("restaurantID")
    private int restaurantID;
    @SerializedName("day")
    private Date day;
    @SerializedName("mealName")
    private String mealName;
    @SerializedName("restaurantTypeName")
    private String restaurantTypeName;
    @SerializedName("restaurantName")
    private String restaurantName;
    @SerializedName("fieldName")
    private String fieldName;
    @SerializedName("newContent")
    private String newContent;

    public String getTripID() {
        return tripID;
    }

    public void setTripID(String tripID) {
        this.tripID = tripID;
    }

    public int getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(int restaurantID) {
        this.restaurantID = restaurantID;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getRestaurantTypeName() {
        return restaurantTypeName;
    }

    public void setRestaurantTypeName(String restaurantTypeName) {
        this.restaurantTypeName = restaurantTypeName;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getNewContent() {
        return newContent;
    }

    public void setNewContent(String newContent) {
        this.newContent = newContent;
    }
}

package com.example.android.bearguestmobile;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;


public class Trip {

    @SerializedName("tripID")
    String tripID;
    @SerializedName("userID")
    String userID;
    @SerializedName("startDate")
    Date startDate;
    @SerializedName("endDate")
    Date endDate;
    @SerializedName("tripName")
    String tripName;
    @SerializedName("mealsByDay")
    ArrayList<Meal> mealsByDay;
    @SerializedName("newContent")
    Date newContent;
    @SerializedName("fieldName")
    String fieldName;

    // Empty public constructor
    public Trip() {}

    public String getTripID() {
        return tripID;
    }

    public void setTripID(String tripID) {
        this.tripID = tripID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public ArrayList<Meal> getMealsByDay() {
        return mealsByDay;
    }

    public void setMealsByDay(ArrayList<Meal> mealsByDay) {
        this.mealsByDay = mealsByDay;
    }

    public Date getNewContent() {
        return newContent;
    }

    public void setNewContent(Date newContent) {
        this.newContent = newContent;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}

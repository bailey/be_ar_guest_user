package com.example.android.bearguestmobile;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Update {

    @SerializedName("tripID")
    String tripID;
    @SerializedName("mealName")
    String currentMealName;
    @SerializedName("fieldName")
    String fieldName;
    @SerializedName("newContent")
    String newContent;
//    @SerializedName("newContentD")
//    Date dNewContent;

    public Update() {}

    public String getTripID() {
        return tripID;
    }

    public void setTripID(String tripID) {
        this.tripID = tripID;
    }

    public String getCurrentMealName() {
        return currentMealName;
    }

    public void setCurrentMealName(String currentMealName) {
        this.currentMealName = currentMealName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getsNewContent() {
        return this.newContent;
    }

    public void setsNewContent(String newContent) {
        this.newContent = newContent;
    }

//    public Date getdNewContent() {
//        return dNewContent;
//    }
//
//    public void setdNewContent(Date dNewContent) {
//        this.dNewContent = dNewContent;
//    }
}

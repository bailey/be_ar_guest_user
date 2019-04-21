package com.example.android.bearguestmobile;

import com.google.gson.annotations.SerializedName;

public class AddReview {


    @SerializedName("userID")
    String userID;
    @SerializedName("itemID")
    int itemID;
    @SerializedName("comment")
    String comment;
    @SerializedName("rating")
    float rating;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}

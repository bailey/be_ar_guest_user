package com.example.android.bearguestmobile;

import com.google.gson.annotations.SerializedName;

public class Favorite {


    @SerializedName("userID")
    private String userID;
    @SerializedName("itemID")
    private int itemID;
    @SerializedName("favorite")
    private int favorite;

    public Favorite(String userID, int itemID, int favorite) {
        this.userID=userID;
        this.itemID=itemID;
        this.favorite=favorite;
    }

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

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }
}

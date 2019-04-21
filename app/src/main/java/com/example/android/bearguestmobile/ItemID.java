package com.example.android.bearguestmobile;

import com.google.gson.annotations.SerializedName;

public class ItemID {
    @SerializedName("itemID")
    private int itemID;

    public ItemID(int itemID) { this.itemID = itemID; }

    public int getitemID() {
        return this.itemID;
    }

    public void setitemID(int itemID) {
        this.itemID = itemID;
    }
}

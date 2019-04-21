package com.example.android.bearguestmobile;

import com.google.gson.annotations.SerializedName;

public class ParkID {

    @SerializedName("parkID")
    private int parkID;

    public ParkID(int parkID) { this.parkID = parkID; }

    public int getParkID() {
        return this.parkID;
    }

    public void setParkID(int parkID) {
        this.parkID = parkID;
    }
}

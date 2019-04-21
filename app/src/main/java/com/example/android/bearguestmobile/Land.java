package com.example.android.bearguestmobile;

import com.google.gson.annotations.SerializedName;

public class Land {

    @SerializedName("landID")
    int landID;
    @SerializedName("landName")
    String landName;
    @SerializedName("parkID")
    int parkID;

    public Land(int landID, String landName, int parkID) {
        this.landID=landID;
        this.landName=landName;
        this.parkID=parkID;
    }

    public int getLandID() {
        return landID;
    }

    public void setLandID(int landID) {
        this.landID = landID;
    }

    public String getLandName() {
        return landName;
    }

    public void setLandName(String landName) {
        this.landName = landName;
    }

    public int getParkID() {
        return parkID;
    }

    public void setParkID(int parkID) {
        this.parkID = parkID;
    }
}

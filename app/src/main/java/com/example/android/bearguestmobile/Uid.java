package com.example.android.bearguestmobile;

import android.widget.Toast;

import com.google.gson.annotations.SerializedName;

public class Uid {

    @SerializedName("uid")
    private String uid;
    @SerializedName("userID")
    private String userID;

    public Uid(String uid) { this.uid = uid; }

    public Uid() {}

    public String getuid() {
        return this.uid;
    }

    public void setuid(String uid) {
        this.uid = uid;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}

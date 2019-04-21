package com.example.android.bearguestmobile;

import com.google.gson.annotations.SerializedName;

public class Profile {

    public Profile(String userID, String fName, String lName, String email, String dateCreated,
                   String role, String imageURL, String userStatus) {
        this.userID = userID;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.dateCreated = dateCreated;
        this.role = role;
        this.imageURL = imageURL;
        this.userStatus = userStatus;
    }

    @SerializedName("userID")
    String userID;
    @SerializedName("fName")
    String fName;
    @SerializedName("lName")
    String lName;
    @SerializedName("email")
    String email;
    @SerializedName("dateCreated")
    String dateCreated;
    @SerializedName("role")
    String role;
    @SerializedName("imageURL")
    String imageURL;
    @SerializedName("userStatus")
    String userStatus;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
}

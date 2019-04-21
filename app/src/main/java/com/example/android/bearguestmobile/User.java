package com.example.android.bearguestmobile;

import com.google.gson.annotations.SerializedName;

public class User {

    // firstName, lastName, userEmail, uid, userImageURL
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("userEmail")
    private String userEmail;
    @SerializedName("uid")
    private String uid;
    @SerializedName("userImageURL")
    private String userImageURL;

    public User() {}

    public User(String firstName, String lastName, String userEmail, String uid, String userImageURL){
        this.firstName=firstName;
        this.lastName=lastName;
        this.userEmail=userEmail;
        this.uid=uid;
        this.userImageURL=userImageURL;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserImageURL() {
        return userImageURL;
    }

    public void setUserImageURL(String userImageURL) {
        this.userImageURL = userImageURL;
    }
}

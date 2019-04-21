package com.example.android.bearguestmobile;

import com.google.gson.annotations.SerializedName;

public class Review {

    /*
    {
 *       "userID": string,
 *       "itemID": number,
 *       "comment": string,
 *       "rating": number,
 *       "isFavorite": number, // 0 or 1
 *       "flag": number, // 0 or 1
 *       "fName": string,
 *       "lName": string,
 *       "email": string,
 *       "LocalDateCreated": LocalDate, // LocalDate the profile was created
 *       "role": string, // admin or user
 *       "imageURL": string,
 *       "userStatus": string // active or inactive
 *       },
    */

    @SerializedName("userID")
    String userID;
    @SerializedName("itemID")
    int itemID;
    @SerializedName("comment")
    String comment;
    @SerializedName("rating")
    float rating;
    @SerializedName("isFavorite")
    int isFavorite;
    @SerializedName("flag")
    int flag;
    @SerializedName("fName")
    String fName;
    @SerializedName("lName")
    String lName;
    @SerializedName("email")
    String email;
    @SerializedName("role")
    String role;
    @SerializedName("imageURL")
    String imageURL;
    @SerializedName("userStatus")
    String userStatus;
    @SerializedName("dateOfComment")
    String dateOfComment;

    public Review() {}

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

    public int getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
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

    public String getDateOfComment() {
        return this.dateOfComment;
    }

    public void setDateOfComment(String dateOfComment) {
        this.dateOfComment = dateOfComment;
    }
}

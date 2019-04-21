package com.example.android.bearguestmobile;

import com.google.gson.annotations.SerializedName;

public class MenuItem {
    /**
     *
     * `itemID` INT NOT NULL AUTO_INCREMENT,
     *   `restaurantID` INT NOT NULL,
     *   `itemName` VARCHAR(100) NOT NULL,
     *   `itemDescription` TEXT,
     *   `secret` BOOLEAN NOT NULL,
     *   `vegan` BOOLEAN NOT NULL,
     *   `substitution` TEXT,
     *   `itemStatus` VARCHAR(50) NOT NULL,
     */

    @SerializedName("itemID")
    int itemID;
    @SerializedName("restaurantID")
    int restaurantID;
    @SerializedName("itemName")
    String itemName;
    @SerializedName("itemDescription")
    String itemDescription;
    // Change secret and vegan to int's instead of boolean
    @SerializedName("secret")
    int secret;
    @SerializedName("vegan")
    int vegan;
    @SerializedName("substitution")
    String substitution;
    @SerializedName("itemStatus")
    String itemStatus;
    @SerializedName("pageNum")
    int pageNum;
    @SerializedName("x")
    float x;
    @SerializedName("z")
    float z;
    @SerializedName("restaurantName")
    String restaurantName;
    @SerializedName("parkID")
    int parkID;


    public float getX() { return x; }

    public void setX(float x) { this.x = x; }

    public float getZ() { return z; }

    public void setZ(float z) { this.z = z; }

    public int getPageNum() { return pageNum; }

    public void setPageNum(int pageNum) { this.pageNum = pageNum;  }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(int restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public int getSecret() {
        return secret;
    }

    public void setSecret(int secret) {
        this.secret = secret;
    }

    public int getVegan() {
        return vegan;
    }

    public void setVegan(int vegan) {
        this.vegan = vegan;
    }

    public String getSubstitution() {
        return substitution;
    }

    public void setSubstitution(String substitution) {
        this.substitution = substitution;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public int getParkID() {
        return parkID;
    }

    public void setParkID(int parkID) {
        this.parkID = parkID;
    }
}

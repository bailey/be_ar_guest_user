package com.example.android.bearguestmobile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.ClipData;
import android.util.Log;

import java.util.List;

public class DashboardViewModel extends ViewModel {

    private LiveData<List<Restaurant>> restaurantListObservable;
    private LiveData<List<Restaurant>> restaurantListByParkIDObservable;
    private LiveData<List<Restaurant>> restaurantListByLandIDObservable;
    private LiveData<List<MenuItem>> menuItemListObservable;
    private LiveData<List<Land>> landListObservable;
    private LiveData<List<MenuItem>> menuItemListForAR;
    private LiveData<BlobClass> blobClassObservable;

    private MutableLiveData<ParkID> muteSelectedParkIDObservable = new MutableLiveData<>();
    private MutableLiveData<Land> muteSelectedLandObservable = new MutableLiveData<>();
    private MutableLiveData<RestaurantID> muteSelectedRestaurantIDObservable = new MutableLiveData<>();
    private MutableLiveData<MenuItem> muteSelectedMenuItemObservable = new MutableLiveData<>();


    public DashboardViewModel() {
        restaurantListObservable = DashboardRepository.getInstance().getRestaurantList();
    }

    // Called from ParkListFragment, sets ParkID after click
    public void setParkIDObservable(ParkID inputSelectedParkIDObservable) {
        this.muteSelectedParkIDObservable.setValue(inputSelectedParkIDObservable);
    }

    public LiveData<ParkID> getParkIDObservable() {
        return muteSelectedParkIDObservable;
    }

    public LiveData<List<Land>> getLandList() {
        this.landListObservable = DashboardRepository.getInstance().getAllLands();
        return this.landListObservable;
    }

    public LiveData<List<Land>> getLandListByParkID(){
        this.landListObservable = DashboardRepository.getInstance().getLandListByParkID(this.muteSelectedParkIDObservable);
        return this.landListObservable;
    }

    // Return all restaurants for a specific land.
    // Right now uses ParkID to query, should be updated?
    public LiveData<Land> getSelectedLand() {
        return this.muteSelectedLandObservable;
    }

    public void setSelectedLand(Land selectedLand) {
        Log.v("land", "set selected land to landid="+selectedLand.getLandID());
        this.muteSelectedLandObservable.setValue(selectedLand);
    }

    public LiveData<List<Restaurant>> getRestaurantListByLandID() {
        this.restaurantListByLandIDObservable = DashboardRepository.getInstance().getRestaurantListByLandID(this.muteSelectedLandObservable);
        Log.v("land", "get rest's by land when land ID = " + this.muteSelectedLandObservable.getValue().getLandID());
        return this.restaurantListByLandIDObservable;
    }

    public LiveData<RestaurantID> getSelectedRestaurantID() { return this.muteSelectedRestaurantIDObservable; }

    public void setSelectedRestaurantID(RestaurantID inputRestaurantID) {
        this.muteSelectedRestaurantIDObservable.setValue(inputRestaurantID);
        this.menuItemListObservable = DashboardRepository.getInstance().getMenuItemListByRestaurantID(muteSelectedRestaurantIDObservable);
    }

    public LiveData<List<MenuItem>> getMenuItemListByRestaurantIDObservable() {
        return this.menuItemListObservable;
    }

    public LiveData<List<MenuItem>> getMenuItemListForARByRestaurantName(LiveData<Restaurant> restaurantName) {
        this.menuItemListForAR = DashboardRepository.getInstance().getMenuItemListByRestaurantName(restaurantName);
        return this.menuItemListForAR;
    }
    
    public LiveData<MenuItem> getSelectedMenuItem() {
        return this.muteSelectedMenuItemObservable;
    }

    public void setSelectedMenuItem(MenuItem selectedMenuItem) {
        this.muteSelectedMenuItemObservable.setValue(selectedMenuItem);
    }

    public LiveData<BlobClass> getImgdb() {
        this.blobClassObservable = DashboardRepository.getInstance().getImgdb();
        return this.blobClassObservable;
    }
}
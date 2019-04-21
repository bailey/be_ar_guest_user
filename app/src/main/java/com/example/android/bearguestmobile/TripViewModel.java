package com.example.android.bearguestmobile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

public class TripViewModel extends ViewModel {

    private MutableLiveData<Boolean> isAddItemToTrip = new MutableLiveData<>();
    private MutableLiveData<Trip> selectedTrip = new MutableLiveData<>();
    private MutableLiveData<String> tripPageHeader = new MutableLiveData<>();
    private MutableLiveData<Meal> mealToAdd = new MutableLiveData<>();
    
    private LiveData<List<Trip>> tripList;

    public void setIsAddItemToTrip(Boolean isAddItemToTrip) {
        this.isAddItemToTrip.setValue(isAddItemToTrip);
    }

    public Boolean getIsAddItemToTrip() {
        return this.isAddItemToTrip.getValue();
    }

    public void setSelectedTrip(Trip selectedTrip) {
        this.selectedTrip.setValue(selectedTrip);
    }

    public LiveData<Trip> getSelectedTrip() {
        return this.selectedTrip;
    }

    public void setTripPageHeader(String newHeader) {
        this.tripPageHeader.setValue(newHeader);
    }

    public LiveData<String> getTripPageHeader() { return this.tripPageHeader; }

    public void addTrip(Trip trip) {
        TripRepository.getInstance().addTrip(trip);
    }

    public LiveData<List<Trip>> getTripsByUser(Uid uid) {
        this.tripList = TripRepository.getInstance().getTripsByUser(uid);
        return this.tripList;
    }

    public void setMealToAdd(Meal meal) { this.mealToAdd.setValue(meal); }

    public LiveData<Meal> getMealToAdd() { return this.mealToAdd; }

    public void updateTripName(Update update) { TripRepository.getInstance().updateTripName(update); }

    public void updateTripDate(Trip trip) { TripRepository.getInstance().updateTripDate(trip); }

    public void deleteTrip(Trip trip) { TripRepository.getInstance().deleteTrip(trip); }

    public void updateMeal(Meal meal) { TripRepository.getInstance().updateMeal(meal); }

    public void deleteMeal(Meal meal) { TripRepository.getInstance().deleteMeal(meal); }

}

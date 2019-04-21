package com.example.android.bearguestmobile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TripRepository {

    private Webservice webservice;
    private static TripRepository tripRepository;
    private static final String BASE_URL = "https://be-ar-guest.herokuapp.com";

    // Add a review (with comment and rating) to an item
    public void addTrip(Trip trip) {

        // Make API call
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        webservice = retrofit.create(Webservice.class);
        Call<Trip> call = webservice.addTrip(trip);

        // Handle API response, update value of reviewList which notifies ViewModel
        call.enqueue(new Callback<Trip>() {
            @Override
            public void onResponse(Call<Trip> call, Response<Trip> response) {
                if (response.isSuccessful()) {
                    Log.v("trip repo", "add trip successful");
                    Log.v("trip repo", "onResponse = " + response.body().tripName);

                } else {
                    Log.v("trip repo", "add trip onResponse not successful");
                }
            }
            @Override
            public void onFailure(Call<Trip> call, Throwable t) {
                Log.w("trip repo", "addtrip onFailure", t);
            }
        });
    }

    // Return a list of trips by the given user (UID field set)
    public LiveData<List<Trip>> getTripsByUser(Uid user) {
        final MutableLiveData<List<Trip>> tripList = new MutableLiveData<>();

        // Make API call
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        webservice = retrofit.create(Webservice.class);
        Call<List<Trip>> call = webservice.getTripsByUser(user);

        // Handle API response, update value of reviewList which notifies ViewModel
        call.enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                if (response.isSuccessful()) {
                    Log.v("trip repo", "get trips successful");
                    tripList.setValue(response.body());
                } else {
                    Log.v("trip repo", "get trips onResponse not successful");
                }
            }
            @Override
            public void onFailure(Call<List<Trip>> call, Throwable t) {
                Log.w("trip repo", "get trips onFailure", t);
            }
        });
        return tripList;
    }

    // Add meal to the selected day and trip
    public void addMealToTrip(Meal meal) {
        // Make API call
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        webservice = retrofit.create(Webservice.class);
        Call<Meal> call = webservice.addMealToTrip(meal);

        // Handle API response, update value of reviewList which notifies ViewModel
        call.enqueue(new Callback<Meal>() {
            @Override
            public void onResponse(Call<Meal> call, Response<Meal> response) {
                if (response.isSuccessful()) {
                    Log.v("trip repo", "add meal successful");
                    Log.v("trip repo", "onResponse = " + response.body().getMealName());

                } else {
                    Log.v("trip repo", "add meal onResponse not successful");
                }
            }
            @Override
            public void onFailure(Call<Meal> call, Throwable t) {
                Log.w("trip repo", "add meal onFailure", t);
            }
        });
    }
    
    // Update the trip
    public void updateTripName(Update update) {
        // Make API call
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        webservice = retrofit.create(Webservice.class);
        Call<Update> call = webservice.updateTripName(update);

        // Handle API response, update value of reviewList which notifies ViewModel
        call.enqueue(new Callback<Update>() {
            @Override
            public void onResponse(Call<Update> call, Response<Update> response) {
                if (response.isSuccessful()) {
                    Log.v("trip repo", "update trip name successful");
                    Log.v("trip repo", "onResponse = " + response.body());

                } else {
                    Log.v("trip repo", "update trip name onResponse not successful");
                }
            }
            @Override
            public void onFailure(Call<Update> call, Throwable t) {
                Log.w("trip repo", "update trip name onFailure", t);
            }
        });
    }
    
    public void updateTripDate(Trip trip) {
        // Make API call
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        webservice = retrofit.create(Webservice.class);
        Call<Trip> call = webservice.updateTripDate(trip);

        // Handle API response, update value of reviewList which notifies ViewModel
        call.enqueue(new Callback<Trip>() {
            @Override
            public void onResponse(Call<Trip> call, Response<Trip> response) {
                if (response.isSuccessful()) {
                    Log.v("trip repo", "update trip date successful");
                    Log.v("trip repo", "onResponse = " + response.body());

                } else {
                    Log.v("trip repo", "update trip date onResponse not successful");
                }
            }
            @Override
            public void onFailure(Call<Trip> call, Throwable t) {
                Log.w("trip repo", "update trip date onFailure", t);
            }
        });
    }
    
    // Delete this trip
    public void deleteTrip(Trip trip) {
        // Make API call
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        webservice = retrofit.create(Webservice.class);
        Call<Trip> call = webservice.deleteTrip(trip);

        // Handle API response, Trip value of reviewList which notifies ViewModel
        call.enqueue(new Callback<Trip>() {
            @Override
            public void onResponse(Call<Trip> call, Response<Trip> response) {
                if (response.isSuccessful()) {
                    Log.v("trip repo", "Delete trip successful");
                    Log.v("trip repo", "onResponse = " + response.body());

                } else {
                    Log.v("trip repo", "Delete trip onResponse not successful");
                }
            }
            @Override
            public void onFailure(Call<Trip> call, Throwable t) {
                Log.w("trip repo", "Delete trip onFailure", t);
            }
        });
    }

    // Update the meal
    public void updateMeal(Meal meal) {
        // Make API call
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        webservice = retrofit.create(Webservice.class);
        Call<Meal> call = webservice.updateMeal(meal);

        // Handle API response, update value of reviewList which notifies ViewModel
        call.enqueue(new Callback<Meal>() {
            @Override
            public void onResponse(Call<Meal> call, Response<Meal> response) {
                if (response.isSuccessful()) {
                    Log.v("trip repo", "update meal successful");
                    Log.v("trip repo", "onResponse = " + response.body());

                } else {
                    Log.v("trip repo", "update meal onResponse not successful");
                }
            }
            @Override
            public void onFailure(Call<Meal> call, Throwable t) {
                Log.w("trip repo", "update meal onFailure", t);
            }
        });
    }

    // Delete this meal
    public void deleteMeal(Meal meal) {
        // Make API call
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        webservice = retrofit.create(Webservice.class);
        Call<Meal> call = webservice.deleteMeal(meal);

        // Handle API response, Meal value of reviewList which notifies ViewModel
        call.enqueue(new Callback<Meal>() {
            @Override
            public void onResponse(Call<Meal> call, Response<Meal> response) {
                if (response.isSuccessful()) {
                    Log.v("Trip repo", "Delete Meal successful");
                    Log.v("Trip repo", "onResponse = " + response.body());

                } else {
                    Log.v("Trip repo", "Delete Meal onResponse not successful");
                }
            }
            @Override
            public void onFailure(Call<Meal> call, Throwable t) {
                Log.w("Meal repo", "Delete Meal onFailure", t);
            }
        });
    }

    public synchronized static TripRepository getInstance() {
        //TODO No need to implement this singleton if using Dagger to handle Dependency Injection
        if (tripRepository == null) {
            tripRepository = new TripRepository();
        }
        return tripRepository;
    }
}

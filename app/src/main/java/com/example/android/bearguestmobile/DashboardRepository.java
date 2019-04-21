package com.example.android.bearguestmobile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import java.util.List;

//import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

//@Singleton
public class DashboardRepository {
    private Webservice webservice;
    private static DashboardRepository dashboardRepository;
    private static final String BASE_URL = "https://be-ar-guest.herokuapp.com";

    // Temporary test method to return all lands regardless of ParkID
    public LiveData<List<Land>> getAllLands() {
        final MutableLiveData<List<Land>> landListData = new MutableLiveData<>();

        // Make API call
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        webservice = retrofit.create(Webservice.class);
        Call<List<Land>> call = webservice.getAllLands();

        // Handle API response, update value of restaurantData which notifies ViewModel
        call.enqueue(new Callback<List<Land>>() {
            @Override
            public void onResponse(Call<List<Land>> call, Response<List<Land>> response) {
                if(response.isSuccessful()) {
                    landListData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Land>> call, Throwable t) {
                Log.v("dash repo", "getAllLands - on Failure", t);
            }
        });

        return landListData;
    }

    public LiveData<List<Land>> getLandListByParkID(LiveData<ParkID> parkID) {
        final MutableLiveData<List<Land>> landListData = new MutableLiveData<>();

        // Make API call
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        webservice = retrofit.create(Webservice.class);
        Call<List<Land>> call = webservice.getLandListByParkID(parkID.getValue());

        // Handle API response, update value of restaurantData which notifies ViewModel
        call.enqueue(new Callback<List<Land>>() {
            @Override
            public void onResponse(Call<List<Land>> call, Response<List<Land>> response) {
                if(response.isSuccessful()) {
                    landListData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Land>> call, Throwable t) {
                Log.v("dash repo", "getLandListByParkID - on Failure", t);
            }
        });

        return landListData;
    }

    // Test method: returns all restaurants in database
    public LiveData<List<Restaurant>> getRestaurantList() {
        final MutableLiveData<List<Restaurant>> restaurantData = new MutableLiveData<>();

        // Make API call
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        webservice = retrofit.create(Webservice.class);
        Call<List<Restaurant>> call = webservice.getRestaurant();

        // Handle API response, update value of restaurantData which notifies ViewModel
        call.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                if(response.isSuccessful()) {
                    restaurantData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {

            }
        });

        return restaurantData;
    }

    // Returns all restaurants for a given Park ID
//    public LiveData<List<Restaurant>> getRestaurantListByParkID(LiveData<ParkID> parkID) {
//        final MutableLiveData<List<Restaurant>> restaurantData = new MutableLiveData<>();
//
//        // Make API call
//        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
//        webservice = retrofit.create(Webservice.class);
//        Call<List<Restaurant>> call = webservice.getRestaurantByParkID(parkID.getValue());
//
//        // Handle API response, update value of restaurantData which notifies ViewModel
//        call.enqueue(new Callback<List<Restaurant>>() {
//            @Override
//            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
//                if(response.isSuccessful()) {
//                    restaurantData.setValue(response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
//
//            }
//        });
//
//        return restaurantData;
//    }

    // Returns all restaurants for a given Land ID
    public LiveData<List<Restaurant>> getRestaurantListByLandID(LiveData<Land> land) {
        final MutableLiveData<List<Restaurant>> restaurantData = new MutableLiveData<>();

        // Temp: Query by park ID
        //final ParkID parkID = new ParkID(land.getValue().getLandID());

        // Make API call
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        webservice = retrofit.create(Webservice.class);
        Call<List<Restaurant>> call = webservice.getRestaurantsByLand(land.getValue());

        // Handle API response, update value of restaurantData which notifies ViewModel
        call.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                if(response.isSuccessful()) {
                    restaurantData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                Log.v("dash repo", "on Failure - getRestaurantListByLandID", t);
            }
        });

        return restaurantData;
    }

    public LiveData<List<MenuItem>> getMenuItemListByRestaurantID(LiveData<RestaurantID> restaurantID) {
        final MutableLiveData<List<MenuItem>> menuData = new MutableLiveData<>();

        // Make API call
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        webservice = retrofit.create(Webservice.class);
        Call<List<MenuItem>> call = webservice.getMenuItemsByRestaurant(restaurantID.getValue());

        // Handle API response
        call.enqueue(new Callback<List<MenuItem>>() {
            @Override
            public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> response) {
                if(response.isSuccessful()) {
                    menuData.setValue(response.body());
                }
                else {
                    Log.v("DashRepo Err: ", "line 94, response not successful: " + response);
                }
            }

            @Override
            public void onFailure(Call<List<MenuItem>> call, Throwable t) {
                Log.v("DashRepo Err: ", "line 99, no response at all", t);
            }
        });

        return menuData;
    }

    public LiveData<List<MenuItem>> getMenuItemListByRestaurantName(LiveData<Restaurant> restaurantName) {
        final MutableLiveData<List<MenuItem>> menuData = new MutableLiveData<>();

        // Make API call
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        webservice = retrofit.create(Webservice.class);
        Call<List<MenuItem>> call = webservice.getMenuItemsByRestaurantName(restaurantName.getValue());

        // Handle API response
        call.enqueue(new Callback<List<MenuItem>>() {
            @Override
            public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> response) {
                if(response.isSuccessful()) {
                    menuData.setValue(response.body());
                }
                else {
                    Log.v("DashRepo Err: ", "line 209: getMenuItemListByRestName not successful " + response);
                }
            }

            @Override
            public void onFailure(Call<List<MenuItem>> call, Throwable t) {
                Log.v("DashRepo Err: ", "getMenuItemListByRestName - no response", t);
            }
        });

        return menuData;
    }

    // Test: just return all items
    public LiveData<List<MenuItem>> getItemList() {
        final MutableLiveData<List<MenuItem>> itemData = new MutableLiveData<>();

        // Make API call
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        webservice = retrofit.create(Webservice.class);
        Call<List<MenuItem>> call = webservice.getAllMenuItems();

        // Handle API response, update value of restaurantData which notifies ViewModel
        call.enqueue(new Callback<List<MenuItem>>() {
            @Override
            public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> response) {
                if(response.isSuccessful()) {
                    itemData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<MenuItem>> call, Throwable t) {
                Log.v("Dash Repo getItemList", "on Failure", t);
            }
        });

        return itemData;
    }

    // Test: just return all items
    public LiveData<BlobClass> getImgdb() {
        final MutableLiveData<BlobClass> blobData = new MutableLiveData<>();

        // Make API call
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        webservice = retrofit.create(Webservice.class);
        Call<BlobClass> call = webservice.getImgdb();

        // Handle API response, update value of restaurantData which notifies ViewModel
        call.enqueue(new Callback<BlobClass>() {
            @Override
            public void onResponse(Call<BlobClass> call, Response<BlobClass> response) {
                if(response.isSuccessful()) {
                    blobData.setValue(response.body());
                    Log.v("Dash Repo getImgdb", "on response successful");
                }
            }

            @Override
            public void onFailure(Call<BlobClass> call, Throwable t) {
                Log.v("Dash Repo getImgdb", "on Failure", t);
            }
        });

        return blobData;
    }

    public synchronized static DashboardRepository getInstance() {
        //TODO No need to implement this singleton in Part #2 since Dagger will handle it ...
        if (dashboardRepository == null) {
            if (dashboardRepository == null) {
                dashboardRepository = new DashboardRepository();
            }
        }
        return dashboardRepository;
    }
}

package com.example.android.bearguestmobile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserRepository {

    private Webservice webservice;
    private static UserRepository userRepository;
    private static final String BASE_URL = "https://be-ar-guest.herokuapp.com";

    public LiveData<Profile> getUser(String uid) {
        final MutableLiveData<List<Profile>> profileDataList = new MutableLiveData<>();
        final MutableLiveData<Profile> profileData = new MutableLiveData<>();
        Uid user = new Uid(uid);

        // Make API call
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        webservice = retrofit.create(Webservice.class);
        Call<List<Profile>> call = webservice.getUser(user);

        // Handle API response, update value of restaurantData which notifies ViewModel
        call.enqueue(new Callback<List<Profile>>() {
            @Override
            public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
                if(response.isSuccessful()) {
                    // Retrofit returns a JSON object aka List<Profile>, but querying by UID should
                    // only return one profile, so list.size() should always = 1
                    profileDataList.setValue(response.body());

                    if(profileDataList.getValue() != null && profileDataList.getValue().size()==1) {
                        profileData.setValue(profileDataList.getValue().get(0));
                        Log.v("userrepo", "is successful, userdata: " + profileDataList.getValue().get(0).getUserID());

                    }
                    else {
                        Log.v("userrepo", "response.body is null OR size > 1");
                    }
                }
                else {
                    Log.v("userrepo", "onResponse not successful");
                }
            }

            @Override
            public void onFailure(Call<List<Profile>> call, Throwable t) {
                Log.w("userrepo", "on Failure: ", t);
            }
        });

        return profileData;
    }

    public synchronized static UserRepository getInstance() {
        //TODO No need to implement this singleton if using Dagger to handle Dependency Injection
        if (userRepository == null) {
            userRepository = new UserRepository();
        }
        return userRepository;
    }
}

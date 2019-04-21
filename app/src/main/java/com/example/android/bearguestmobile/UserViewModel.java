package com.example.android.bearguestmobile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.List;

public class UserViewModel extends ViewModel {

    private LiveData<Profile> userLiveData;

    public UserViewModel() { }

    public LiveData<Profile> getUserByUid(String uid) {
        this.userLiveData = UserRepository.getInstance().getUser(uid);
        return this.userLiveData;
    }
}

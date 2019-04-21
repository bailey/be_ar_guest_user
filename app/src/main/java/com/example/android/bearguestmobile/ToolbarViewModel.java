package com.example.android.bearguestmobile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class ToolbarViewModel extends ViewModel {

    private MutableLiveData<Boolean> showBackArrow = new MutableLiveData<>();
    private MutableLiveData<String> muteToolbarTitle = new MutableLiveData<>();

    public ToolbarViewModel() {}

    public LiveData<Boolean> getShowBackArrow() {
        return this.showBackArrow;
    }

    public void setShowBackArrow(Boolean showBackArrow) {
        this.showBackArrow.setValue(showBackArrow);
    }

    public LiveData<String> getToolbarTitle() { return this.muteToolbarTitle; }

    public void setToolbarTitle(String currentScreenTitle) {
        this.muteToolbarTitle.setValue(currentScreenTitle);
    }
}

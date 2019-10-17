package com.example.prikkie.ui.multiplayer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MultiplayerViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MultiplayerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the multiplayer fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
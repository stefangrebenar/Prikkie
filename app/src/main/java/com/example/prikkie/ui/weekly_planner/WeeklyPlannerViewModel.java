package com.example.prikkie.ui.weekly_planner;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WeeklyPlannerViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public WeeklyPlannerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the weekly planner fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
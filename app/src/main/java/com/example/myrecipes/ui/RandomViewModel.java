package com.example.myrecipes.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RandomViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RandomViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is random fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
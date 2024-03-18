package com.lionel.operational.ui.acceptance;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AcceptanceViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AcceptanceViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
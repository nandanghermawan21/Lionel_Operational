package com.lionel.operational.ui.PaySlip;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PaySlipViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PaySlipViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
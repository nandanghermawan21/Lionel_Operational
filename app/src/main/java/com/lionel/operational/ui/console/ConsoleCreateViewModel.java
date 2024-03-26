package com.lionel.operational.ui.console;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lionel.operational.model.DestinationModel;

import java.util.ArrayList;
import java.util.List;

public class ConsoleCreateViewModel extends ViewModel {
    private MutableLiveData<DestinationModel> destinationModel;
    private MutableLiveData<String> consoleCodeErrorMessage;
    private MutableLiveData<String> destinationErrorMessage;

    public ConsoleCreateViewModel() {
        destinationModel = new MutableLiveData<>();
        consoleCodeErrorMessage = new MutableLiveData<>();
        destinationErrorMessage = new MutableLiveData<>();
    }

    public LiveData<DestinationModel> getDestinationModel() {
        return destinationModel;
    }

    public void setDestinationModel(DestinationModel destinationModel) {
          this.destinationModel.setValue(destinationModel);
    }

    public MutableLiveData<String> getConsoleCodeErrorMessage() {
        return consoleCodeErrorMessage;
    }

    public void setConsoleCodeErrorMessage(String consoleCodeErrorMessage) {
        this.consoleCodeErrorMessage.setValue(consoleCodeErrorMessage);
    }

    public MutableLiveData<String> getDestinationErrorMessage() {
        return destinationErrorMessage;
    }

    public void setDestinationErrorMessage(String destinationErrorMessage) {
           this.destinationErrorMessage.setValue(destinationErrorMessage);
    }
}
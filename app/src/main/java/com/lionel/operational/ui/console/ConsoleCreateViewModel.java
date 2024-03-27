package com.lionel.operational.ui.console;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lionel.operational.model.DestinationModel;
import com.lionel.operational.model.ShipmentModel;

import java.util.ArrayList;
import java.util.List;

public class ConsoleCreateViewModel extends ViewModel {
    private MutableLiveData<String> state;
    private MutableLiveData<DestinationModel> destinationModel;
    private MutableLiveData<List<ShipmentModel>> shipmentList;
    private MutableLiveData<String> consoleCodeErrorMessage;
    private MutableLiveData<String> destinationErrorMessage;

    public ConsoleCreateViewModel() {
        state = new MutableLiveData<>();
        destinationModel = new MutableLiveData<>();
        consoleCodeErrorMessage = new MutableLiveData<>();
        destinationErrorMessage = new MutableLiveData<>();
        setStateAsNew();
    }

    public MutableLiveData<String> getState() {
        return state;
    }

    public void setStateAsNew() {
        this.state.setValue("NEW");
    }

    public void setStateAsCreated() {
        this.state.setValue("CREATED");
    }

    public boolean isStateNew() {
        return this.state.getValue().equals("NEW");
    }

    public boolean isStateCreated() {
        return this.state.getValue().equals("CREATED");
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

    public MutableLiveData<List<ShipmentModel>> getShipmentList() {
        if (shipmentList == null) {
            shipmentList = new MutableLiveData<>();
            shipmentList.setValue(new ArrayList<>());
        }
        return shipmentList;
    }

    public void addShipment(ShipmentModel shipmentModel) {
        List<ShipmentModel> list = getShipmentList().getValue();
        list.add(shipmentModel);
        shipmentList.setValue(list);
    }

    public void removeShipment(ShipmentModel shipmentModel) {
        List<ShipmentModel> list = getShipmentList().getValue();
        list.remove(shipmentModel);
        shipmentList.setValue(list);
    }
}
package com.lionel.operational.ui.acceptance;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lionel.operational.model.ShipmentModel;

public class AcceptanceViewModel extends ViewModel {

    private MutableLiveData<String> state;
    private MutableLiveData<ShipmentModel> shipment;

    public AcceptanceViewModel() {
        this.state = new MutableLiveData<>();
        this.shipment = new MutableLiveData<>();
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

    public MutableLiveData<ShipmentModel> getShipment() {
        return shipment;
    }

    public void setShipment(ShipmentModel shipment) {
        this.shipment.setValue(shipment);
    }
}
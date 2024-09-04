package com.lionel.operational.ui.acceptance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lionel.operational.model.ShipmentItemList;
import com.lionel.operational.model.ShipmentModel;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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

    public List<ShipmentItemList> getShipmentItemList() {
        //jika shipmen null kembalikan empty list
        if (shipment.getValue() == null) {
            return Collections.emptyList();
        }else{
            //jika detail null kembalikan empty list
            if (shipment.getValue().getDetail() == null) {
                return Collections.emptyList();
            }else{
                //jika shipment list null kembalikan empty list
                if (shipment.getValue().getDetail().getShipmentItemList() == null) {
                    return Collections.emptyList();
                }else{
                    return shipment.getValue().getDetail().getShipmentItemList();
                }
            }
        }
    }

    //buat funcsi khusus untuk membaca accepted
    public boolean isShipmentAccepted(){
        if (shipment.getValue() == null) {
            return false;
        }else{
            return "1".equals(shipment.getValue().getAccepted());
        }
    }

    public void setShipment(ShipmentModel shipment) {
        this.shipment.setValue(shipment);
    }
}
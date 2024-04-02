package com.lionel.operational.ui.WayBill;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lionel.operational.model.DestinationModel;
import com.lionel.operational.model.ServiceModel;
import com.lionel.operational.model.ShipmentModel;
import com.lionel.operational.model.ShippingAgentModel;
import com.lionel.operational.model.ShippingLinerModel;
import com.lionel.operational.model.ShippingMethodModel;

import java.util.ArrayList;
import java.util.List;

public class WayBillViewModel extends ViewModel {

    private MutableLiveData<String> state;
    private MutableLiveData<ShippingMethodModel> shippingMethod;
    private MutableLiveData<ShippingAgentModel> shippingAgent;
    private MutableLiveData<DestinationModel> origin;
    private MutableLiveData<ShippingLinerModel> liner;
    private MutableLiveData<ServiceModel> service;
    private MutableLiveData<List<ShipmentModel>> shipmentList;

    public WayBillViewModel() {
        state = new MutableLiveData<>();
        shippingMethod = new MutableLiveData<>();
        shippingAgent = new MutableLiveData<>();
        origin = new MutableLiveData<>();
        liner = new MutableLiveData<>();
        service = new MutableLiveData<>();
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

    public MutableLiveData<ShippingMethodModel> getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(ShippingMethodModel shippingMethod) {
        this.shippingMethod.setValue(shippingMethod);
    }

    public MutableLiveData<ShippingAgentModel> getShippingAgent() {
        return shippingAgent;
    }

    public void setShippingAgent(ShippingAgentModel shippingAgent) {
        this.shippingAgent.setValue(shippingAgent);
    }

    public MutableLiveData<DestinationModel> getOrigin() {
        return origin;
    }

    public void setOrigin(DestinationModel origin) {
        this.origin.setValue(origin);
    }

    public MutableLiveData<ShippingLinerModel> getLiner() {
        return liner;
    }

    public void setLiner(ShippingLinerModel liner) {
        this.liner.setValue(liner);
    }

    public MutableLiveData<ServiceModel> getService() {
        return service;
    }

    public void setService(ServiceModel service) {
        this.service.setValue(service);
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

    //add more then one shipment
    public void addShipmentList(List<ShipmentModel> shipmentModelList) {
        List<ShipmentModel> list = getShipmentList().getValue();
        list.addAll(shipmentModelList);
        shipmentList.setValue(list);
    }

    public void clear() {
        //reset all data
        setStateAsNew();
        setShippingMethod(null);
        setShippingAgent(null);
        setOrigin(null);
        setLiner(null);
        getShipmentList().getValue().clear();
    }
}
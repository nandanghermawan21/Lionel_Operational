package com.lionel.operational.ui.stob;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lionel.operational.model.CityModel;
import com.lionel.operational.model.DestinationModel;
import com.lionel.operational.model.ShippingAgentModel;
import com.lionel.operational.model.ShippingMethodModel;
import com.lionel.operational.model.WayBillModel;

import java.util.ArrayList;
import java.util.List;

public class CreateStobViewModel extends ViewModel {
    private MutableLiveData<String> state;
    private MutableLiveData<ShippingAgentModel> shippingAgent;
    private MutableLiveData<ShippingMethodModel> shippingMethode;
    private MutableLiveData<CityModel> cityModel;
    private MutableLiveData<List<WayBillModel>> wayBillList;



    public CreateStobViewModel() {
        state = new MutableLiveData<>();
        shippingAgent = new MutableLiveData<>();
        shippingMethode = new MutableLiveData<>();
        cityModel = new MutableLiveData<>();
        wayBillList = new MutableLiveData<>();
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

    public MutableLiveData<ShippingAgentModel> getShippingAgent() {
        return shippingAgent;
    }

    public void setShippingAgent(ShippingAgentModel shippingAgent) {
        this.shippingAgent.setValue(shippingAgent);
    }

    public LiveData<CityModel> getCityModel() {
        return cityModel;
    }

    public void setCityModel(CityModel cityModel) {
        this.cityModel.setValue(cityModel);
    }

    public MutableLiveData<ShippingMethodModel> getShippingMethod() {
        return shippingMethode;
    }

    public void setShippingMethod(ShippingMethodModel shippingMethod) {
        this.shippingMethode.setValue(shippingMethod);
    }

    public MutableLiveData<List<WayBillModel>> getWayBillList() {
        if (wayBillList.getValue() == null) {
            wayBillList = new MutableLiveData<>();
            wayBillList.setValue(new ArrayList<>());
        }
        return wayBillList;
    }

    public void setWayBillList(List<WayBillModel> wayBillList) {
        this.wayBillList.setValue(wayBillList);
    }

    //clear data
    public void clear() {
        setStateAsNew();
        shippingAgent.setValue(null);
        shippingMethode.setValue(null);
        wayBillList.setValue(null);
    }
}
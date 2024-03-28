package com.lionel.operational.ui.WayBill;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lionel.operational.model.DestinationModel;
import com.lionel.operational.model.LinerModel;
import com.lionel.operational.model.ShippingAgentModel;
import com.lionel.operational.model.ShippingMethodModel;

public class WayBillViewModel extends ViewModel {

    private MutableLiveData<String> state;
    private MutableLiveData<ShippingMethodModel> shippingMethod;
    private MutableLiveData<ShippingAgentModel> shippingAgent;
    private MutableLiveData<DestinationModel> origin;
    private MutableLiveData<LinerModel> liner;

    public WayBillViewModel() {
        state = new MutableLiveData<>();
        shippingMethod = new MutableLiveData<>();
        shippingAgent = new MutableLiveData<>();
        origin = new MutableLiveData<>();
        liner = new MutableLiveData<>();
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

    public MutableLiveData<LinerModel> getLiner() {
        return liner;
    }

    public void setLiner(LinerModel liner) {
        this.liner.setValue(liner);
    }

}
package com.lionel.operational.ui.stob;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lionel.operational.model.ShippingAgentModel;
import com.lionel.operational.model.ShippingMethodModel;

public class CreateStobViewModel extends ViewModel {
    private MutableLiveData<String> state;
    private MutableLiveData<ShippingAgentModel> shippingAgent;
    private MutableLiveData<ShippingMethodModel> shippingMethode;

    public CreateStobViewModel() {
        state = new MutableLiveData<>();
        shippingAgent = new MutableLiveData<>();
        shippingMethode = new MutableLiveData<>();
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

    public MutableLiveData<ShippingMethodModel> getShippingMethod() {
        return shippingMethode;
    }

    public void setShippingMethod(ShippingMethodModel shippingMethod) {
        this.shippingMethode.setValue(shippingMethod);
    }
}
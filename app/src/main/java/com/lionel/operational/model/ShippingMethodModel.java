package com.lionel.operational.model;

import com.google.gson.Gson;

public class ShippingMethodModel {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //create methode to json
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}

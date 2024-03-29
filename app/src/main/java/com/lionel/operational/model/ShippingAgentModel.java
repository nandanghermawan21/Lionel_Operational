package com.lionel.operational.model;

import com.google.gson.Gson;

public class ShippingAgentModel {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}

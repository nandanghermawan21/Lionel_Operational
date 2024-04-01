package com.lionel.operational.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class ShippingLinerModel {
    private String id;
    private String name;
    @SerializedName("max_coli")
    private double maxColi;
    @SerializedName("max_gw")
    private double maxGw;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getMaxColi() {
        return maxColi;
    }

    public void setMaxColi(double maxColi) {
        this.maxColi = maxColi;
    }

    public double getMaxGw() {
        return maxGw;
    }

    public void setMaxGw(double maxGw) {
        this.maxGw = maxGw;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

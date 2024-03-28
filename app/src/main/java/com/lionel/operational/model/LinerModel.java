package com.lionel.operational.model;

import com.google.gson.annotations.SerializedName;

public class LinerModel {
    private String id;
    private String name;
    @SerializedName("max_coli")
    private String maxColi;
    @SerializedName("max_gw")
    private String maxGw;

    public LinerModel() {
    }

    public LinerModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public LinerModel(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMaxColi() {
        return maxColi;
    }

    public void setMaxColi(String maxColi) {
        this.maxColi = maxColi;
    }

    public String getMaxGw() {
        return maxGw;
    }

    public void setMaxGw(String maxGw) {
        this.maxGw = maxGw;
    }
}

package com.lionel.operational.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class ShipmentItemList {
    @SerializedName("length")
    private String length;
    @SerializedName("width")
    private String width;
    @SerializedName("height")
    private String height;
    @SerializedName("volume")
    private String volumeWeight;
    @SerializedName("volume_weight")
    private String grossWeight;
    @SerializedName("barcode")
    private String barcode;
    @SerializedName("accepted")
    private String accepted;


    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getVolumeWeight() {
        return volumeWeight;
    }

    public void setVolumeWeight(String volumeWeight) {
        this.volumeWeight = volumeWeight;
    }

    public String getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(String grossWeight) {
        this.grossWeight = grossWeight;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getAccepted() {
        return accepted;
    }

    public void setAccepted(String accepted) {
        this.accepted = accepted;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}

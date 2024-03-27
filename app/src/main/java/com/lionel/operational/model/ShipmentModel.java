package com.lionel.operational.model;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class ShipmentModel {
    private String vdate;
    private String code;
    private String barcode;
    private double length;
    private double width;
    private double height;
    private double volume;
    @SerializedName("volume_weight")
    private double volumeWeight;
    @SerializedName("gross_weight")
    private double grossWeight;
    @SerializedName("chargeable_weight")
    private double chargeableWeight;
    @SerializedName("parent_code")
    private String parentCode;
    @SerializedName("shipping_factor")
    private double shippingFactor;

    public ShipmentModel() {
    }

    public double getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public double getVolumeWeight() {
        return volumeWeight;
    }

    public void setVolumeWeight(float volumeWeight) {
        this.volumeWeight = volumeWeight;
    }

    public double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(float grossWeight) {
        this.grossWeight = grossWeight;
    }

    public double getChargeableWeight() {
        return chargeableWeight;
    }

    public void setChargeableWeight(float chargeableWeight) {
        this.chargeableWeight = chargeableWeight;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public double getShippingFactor() {
        return shippingFactor;
    }

    public void setShippingFactor(float shippingFactor) {
        this.shippingFactor = shippingFactor;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getVdate() {
        return vdate;
    }

    public void setVdate(String vdate) {
        this.vdate = vdate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}

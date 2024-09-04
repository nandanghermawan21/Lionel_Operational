package com.lionel.operational.model;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class ShipmentModel {
    private String vdate;
    private String code;
    @SerializedName("dest_branch_id")
    private String destBranchId;
    @SerializedName("service_type")
    private String serviceType;
    @SerializedName("nature_of_goods")
    private String natureOfGoods;
    @SerializedName("shipping_method")
    private String shippingMethod;
    @SerializedName("console_barcode")
    private String consoleBarcode;
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
    @SerializedName("accepted")
    private String accepted;
    private ShipmentDetailModel detail;

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

    public  String getSTTNumber() {
        return barcode == null || barcode.isEmpty() ? code : barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDestBranchId() {
        return destBranchId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getNatureOfGoods() {
        return natureOfGoods;
    }

    public void setNatureOfGoods(String natureOfGoods) {
        this.natureOfGoods = natureOfGoods;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getConsoleBarcode() {
        return consoleBarcode;
    }

    public void setConsoleBarcode(String consoleBarcode) {
        this.consoleBarcode = consoleBarcode;
    }

    public void setDestBranchId(String destBranchId) {
        this.destBranchId = destBranchId;
    }

    public String getAccepted() {
        return accepted;
    }

    public void setAccepted(String accepted) {
        this.accepted = accepted;
    }

    public ShipmentDetailModel getDetail() {
        return detail;
    }

    public void setDetail(ShipmentDetailModel detail) {
        this.detail = detail;
    }
}

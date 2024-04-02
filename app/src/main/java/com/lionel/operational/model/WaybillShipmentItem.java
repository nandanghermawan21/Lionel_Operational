package com.lionel.operational.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class WaybillShipmentItem {
    @SerializedName("code")
    private String code;

    @SerializedName("console_barcode")
    private String consoleBarcode;

    public WaybillShipmentItem(String code, String consoleBarcode) {
        this.code = code;
        this.consoleBarcode = consoleBarcode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getConsoleBarcode() {
        return consoleBarcode;
    }

    public void setConsoleBarcode(String consoleBarcode) {
        this.consoleBarcode = consoleBarcode;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}

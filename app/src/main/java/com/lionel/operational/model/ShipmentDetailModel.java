package com.lionel.operational.model;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.lionel.operational.model.ShipmentItemList;

import java.util.ArrayDeque;
import java.util.List;

public class ShipmentDetailModel {
    @SerializedName("summary")
    private String summary;
    @SerializedName("shipment_list")
    private List<ShipmentItemList> shipmentItemList;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<ShipmentItemList> getShipmentItemList() {
        return shipmentItemList;
    }

    public void setShipmentItemList(List<ShipmentItemList> shipmentItemList) {
        this.shipmentItemList = shipmentItemList;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}

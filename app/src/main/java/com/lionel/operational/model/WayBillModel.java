package com.lionel.operational.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class WayBillModel {
    private String code;
    @SerializedName("vdate")
    private String date;
    @SerializedName("shipping_agent_id")
    private String shippingAgentId;
    @SerializedName("liner_id")
    private String linerId;
    @SerializedName("branch_id")
    private String branchId;
    @SerializedName("dest_branch_id")
    private String destBranchId;
    @SerializedName("waybill_type")
    private String waybillType;
    @SerializedName("ttl_piece")
    private String ttlPieces;
    @SerializedName("ttl_chargeable_weight")
    private String ttlChargeableWeight;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShippingAgentId() {
        return shippingAgentId;
    }

    public void setShippingAgentId(String shippingAgentId) {
        this.shippingAgentId = shippingAgentId;
    }

    public String getLinerId() {
        return linerId;
    }

    public void setLinerId(String linerId) {
        this.linerId = linerId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getDestBranchId() {
        return destBranchId;
    }

    public void setDestBranchId(String destBranchId) {
        this.destBranchId = destBranchId;
    }

    public String getWaybillType() {
        return waybillType;
    }

    public void setWaybillType(String waybillType) {
        this.waybillType = waybillType;
    }

    public String getTtlPieces() {
        return ttlPieces;
    }

    public void setTtlPieces(String ttlPieces) {
        this.ttlPieces = ttlPieces;
    }

    public String getTtlChargeableWeight() {
        return ttlChargeableWeight;
    }

    public void setTtlChargeableWeight(String ttlChargeableWeight) {
        this.ttlChargeableWeight = ttlChargeableWeight;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}

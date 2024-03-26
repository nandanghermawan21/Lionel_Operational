package com.lionel.operational.model;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DestinationModel {
    @SerializedName("id")
    private String id;
    @SerializedName("branch_id")
    private String branchId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

}

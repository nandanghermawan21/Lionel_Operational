package com.lionel.operational.model;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class AccountModel {
    @SerializedName("auth")
    private int auth;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("group")
    private String group;
    @SerializedName("branch_id")
    private String branchId;

    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}

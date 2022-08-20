package com.mz.bf.addclient;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuccessModel {
    @SerializedName("success")
    @Expose
    private Integer success;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }
}

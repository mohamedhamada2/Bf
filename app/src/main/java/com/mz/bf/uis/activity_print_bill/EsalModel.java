package com.mz.bf.uis.activity_print_bill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EsalModel {
    @SerializedName("esal")
    @Expose
    private Esal esal;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;

    public Esal getEsal() {
        return esal;
    }

    public void setEsal(Esal esal) {
        this.esal = esal;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

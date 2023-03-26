package com.mz.bf.addclient;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuccessModel {
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("id")
    @Expose
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("fatora_id")
    @Expose
    private String fatora_id;

    public String getFatora_id() {
        return fatora_id;
    }

    public void setFatora_id(String fatora_id) {
        this.fatora_id = fatora_id;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }
}

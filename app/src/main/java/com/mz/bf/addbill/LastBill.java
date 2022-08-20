package com.mz.bf.addbill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastBill {
    @SerializedName("rkm_fatora")
    @Expose
    private Integer rkmFatora;

    public Integer getRkmFatora() {
        return rkmFatora;
    }

    public void setRkmFatora(Integer rkmFatora) {
        this.rkmFatora = rkmFatora;
    }
}

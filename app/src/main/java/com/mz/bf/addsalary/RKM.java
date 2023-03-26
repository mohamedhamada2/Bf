package com.mz.bf.addsalary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RKM {
    @SerializedName("rkm")
    @Expose
    private Integer rkm;

    public Integer getRkm() {
        return rkm;
    }

    public void setRkm(Integer rkm) {
        this.rkm = rkm;
    }
}

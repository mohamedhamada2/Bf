package com.mz.bf.client_safe;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SafeModel {
    @SerializedName("date_esal")
    @Expose
    private String dateEsal;
    @SerializedName("representative_id_fk")
    @Expose
    private String representativeIdFk;
    @SerializedName("num_rows")
    @Expose
    private Integer numRows;
    @SerializedName("allsandat_arbon")
    @Expose
    private String allsandatArbon;
    @SerializedName("allsandat_qabd")
    @Expose
    private Double allsandatQabd;

    public String getDateEsal() {
        return dateEsal;
    }

    public void setDateEsal(String dateEsal) {
        this.dateEsal = dateEsal;
    }

    public String getRepresentativeIdFk() {
        return representativeIdFk;
    }

    public void setRepresentativeIdFk(String representativeIdFk) {
        this.representativeIdFk = representativeIdFk;
    }

    public Integer getNumRows() {
        return numRows;
    }

    public void setNumRows(Integer numRows) {
        this.numRows = numRows;
    }

    public String getAllsandatArbon() {
        return allsandatArbon;
    }

    public void setAllsandatArbon(String allsandatArbon) {
        this.allsandatArbon = allsandatArbon;
    }

    public Double getAllsandatQabd() {
        return allsandatQabd;
    }

    public void setAllsandatQabd(Double allsandatQabd) {
        this.allsandatQabd = allsandatQabd;
    }
}

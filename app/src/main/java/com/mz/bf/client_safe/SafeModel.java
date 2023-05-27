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

    @SerializedName("all_fatora")
    @Expose
    private String all_fatora;

    @SerializedName("all_masrofat")
    @Expose
    private String all_masrofat;

    @SerializedName("allsandat_sarf")
    @Expose
    private String allsandat_sarf;

    @SerializedName("total")
    @Expose
    private String total;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getAll_fatora() {
        return all_fatora;
    }

    public void setAll_fatora(String all_fatora) {
        this.all_fatora = all_fatora;
    }

    public String getAll_masrofat() {
        return all_masrofat;
    }

    public void setAll_masrofat(String all_masrofat) {
        this.all_masrofat = all_masrofat;
    }

    public String getAllsandat_sarf() {
        return allsandat_sarf;
    }

    public void setAllsandat_sarf(String allsandat_sarf) {
        this.allsandat_sarf = allsandat_sarf;
    }

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

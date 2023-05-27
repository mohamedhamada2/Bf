package com.mz.bf.addbill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Bill {
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("rkm_fatora")
    @Expose
    private String rkmFatora;
    @SerializedName("fatora_date")
    @Expose
    private String fatoraDate;
    @SerializedName("dafter_rkm_fatora")
    @Expose
    private String dafterRkmFatora;
    @SerializedName("national_num")
    @Expose
    private String nationalNum;
    @SerializedName("type_paid")
    @Expose
    private String typePaid;
    @SerializedName("mob")
    @Expose
    private String mob;
    @SerializedName("client_name")
    @Expose
    private String clientName;
    @SerializedName("client_adress")
    @Expose
    private String clientAdress;
    @SerializedName("client_id_fk")
    @Expose
    private String clientIdFk;
    @SerializedName("main_branch_id_fk")
    @Expose
    private String mainBranchIdFk;
    @SerializedName("sub_branch_id_fk")
    @Expose
    private String subBranchIdFk;
    @SerializedName("storage_id_fk")
    @Expose
    private String storageIdFk;
    @SerializedName("fatora_cost_before_discount")
    @Expose
    private String fatoraCostBeforeDiscount;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("fatora_cost_after_discount")
    @Expose
    private String fatoraCostAfterDiscount;
    @SerializedName("paid")
    @Expose
    private String paid;
    @SerializedName("remain")
    @Expose
    private String remain;
    @SerializedName("byan")
    @Expose
    private String byan;
    @SerializedName("fatora_details")
    @Expose
    private List<FatoraDetail> fatoraDetails = null;

    public String getMandoub_discount() {
        return mandoub_discount;
    }

    public void setMandoub_discount(String mandoub_discount) {
        this.mandoub_discount = mandoub_discount;
    }

    @SerializedName("mandoub_discount")
    @Expose
    String mandoub_discount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRkmFatora() {
        return rkmFatora;
    }

    public void setRkmFatora(String rkmFatora) {
        this.rkmFatora = rkmFatora;
    }

    public String getFatoraDate() {
        return fatoraDate;
    }

    public void setFatoraDate(String fatoraDate) {
        this.fatoraDate = fatoraDate;
    }

    public String getDafterRkmFatora() {
        return dafterRkmFatora;
    }

    public void setDafterRkmFatora(String dafterRkmFatora) {
        this.dafterRkmFatora = dafterRkmFatora;
    }

    public String getNationalNum() {
        return nationalNum;
    }

    public void setNationalNum(String nationalNum) {
        this.nationalNum = nationalNum;
    }

    public String getTypePaid() {
        return typePaid;
    }

    public void setTypePaid(String typePaid) {
        this.typePaid = typePaid;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAdress() {
        return clientAdress;
    }

    public void setClientAdress(String clientAdress) {
        this.clientAdress = clientAdress;
    }

    public String getClientIdFk() {
        return clientIdFk;
    }

    public void setClientIdFk(String clientIdFk) {
        this.clientIdFk = clientIdFk;
    }

    public String getMainBranchIdFk() {
        return mainBranchIdFk;
    }

    public void setMainBranchIdFk(String mainBranchIdFk) {
        this.mainBranchIdFk = mainBranchIdFk;
    }

    public String getSubBranchIdFk() {
        return subBranchIdFk;
    }

    public void setSubBranchIdFk(String subBranchIdFk) {
        this.subBranchIdFk = subBranchIdFk;
    }

    public String getStorageIdFk() {
        return storageIdFk;
    }

    public void setStorageIdFk(String storageIdFk) {
        this.storageIdFk = storageIdFk;
    }

    public String getFatoraCostBeforeDiscount() {
        return fatoraCostBeforeDiscount;
    }

    public void setFatoraCostBeforeDiscount(String fatoraCostBeforeDiscount) {
        this.fatoraCostBeforeDiscount = fatoraCostBeforeDiscount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getFatoraCostAfterDiscount() {
        return fatoraCostAfterDiscount;
    }

    public void setFatoraCostAfterDiscount(String fatoraCostAfterDiscount) {
        this.fatoraCostAfterDiscount = fatoraCostAfterDiscount;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getRemain() {
        return remain;
    }

    public void setRemain(String remain) {
        this.remain = remain;
    }

    public String getByan() {
        return byan;
    }

    public void setByan(String byan) {
        this.byan = byan;
    }

    public List<FatoraDetail> getFatoraDetails() {
        return fatoraDetails;
    }

    public void setFatoraDetails(List<FatoraDetail> fatoraDetails) {
        this.fatoraDetails = fatoraDetails;
    }
}

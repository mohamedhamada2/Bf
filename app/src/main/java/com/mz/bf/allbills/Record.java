
package com.mz.bf.allbills;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Record {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("carorstore")
    @Expose
    private String carorstore;
    @SerializedName("representative_id_fk")
    @Expose
    private Object representativeIdFk;
    @SerializedName("car_id_fk")
    @Expose
    private Object carIdFk;
    @SerializedName("type_paid")
    @Expose
    private String typePaid;
    @SerializedName("allowed_discount")
    @Expose
    private String allowedDiscount;
    @SerializedName("rkm_fatora")
    @Expose
    private String rkmFatora;
    @SerializedName("dafter_rkm_fatora")
    @Expose
    private String dafterRkmFatora;
    @SerializedName("fatora_date")
    @Expose
    private String fatoraDate;
    @SerializedName("client_id_fk")
    @Expose
    private String clientIdFk;
    @SerializedName("mob")
    @Expose
    private String mob;
    @SerializedName("client_name")
    @Expose
    private String clientName;
    @SerializedName("national_num")
    @Expose
    private String nationalNum;
    @SerializedName("client_adress")
    @Expose
    private String clientAdress;
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
    @SerializedName("fatora_cost_after_discount")
    @Expose
    private String fatoraCostAfterDiscount;
    @SerializedName("discount")
    @Expose
    private String discount;

    public String getMandoub_discount() {
        return mandoub_discount;
    }

    public void setMandoub_discount(String mandoub_discount) {
        this.mandoub_discount = mandoub_discount;
    }

    @SerializedName("mandoub_discount")
    @Expose
    private String mandoub_discount;
    @SerializedName("paid")
    @Expose
    private String paid;
    @SerializedName("remain")
    @Expose
    private String remain;
    @SerializedName("byan")
    @Expose
    private String byan;
    @SerializedName("approved")
    @Expose
    private String approved;
    @SerializedName("month")
    @Expose
    private String month;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("publisher")
    @Expose
    private String publisher;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarorstore() {
        return carorstore;
    }

    public void setCarorstore(String carorstore) {
        this.carorstore = carorstore;
    }

    public Object getRepresentativeIdFk() {
        return representativeIdFk;
    }

    public void setRepresentativeIdFk(Object representativeIdFk) {
        this.representativeIdFk = representativeIdFk;
    }

    public Object getCarIdFk() {
        return carIdFk;
    }

    public void setCarIdFk(Object carIdFk) {
        this.carIdFk = carIdFk;
    }

    public String getTypePaid() {
        return typePaid;
    }

    public void setTypePaid(String typePaid) {
        this.typePaid = typePaid;
    }

    public String getAllowedDiscount() {
        return allowedDiscount;
    }

    public void setAllowedDiscount(String allowedDiscount) {
        this.allowedDiscount = allowedDiscount;
    }

    public String getRkmFatora() {
        return rkmFatora;
    }

    public void setRkmFatora(String rkmFatora) {
        this.rkmFatora = rkmFatora;
    }

    public String getDafterRkmFatora() {
        return dafterRkmFatora;
    }

    public void setDafterRkmFatora(String dafterRkmFatora) {
        this.dafterRkmFatora = dafterRkmFatora;
    }

    public String getFatoraDate() {
        return fatoraDate;
    }

    public void setFatoraDate(String fatoraDate) {
        this.fatoraDate = fatoraDate;
    }

    public String getClientIdFk() {
        return clientIdFk;
    }

    public void setClientIdFk(String clientIdFk) {
        this.clientIdFk = clientIdFk;
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

    public String getNationalNum() {
        return nationalNum;
    }

    public void setNationalNum(String nationalNum) {
        this.nationalNum = nationalNum;
    }

    public String getClientAdress() {
        return clientAdress;
    }

    public void setClientAdress(String clientAdress) {
        this.clientAdress = clientAdress;
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

    public String getFatoraCostAfterDiscount() {
        return fatoraCostAfterDiscount;
    }

    public void setFatoraCostAfterDiscount(String fatoraCostAfterDiscount) {
        this.fatoraCostAfterDiscount = fatoraCostAfterDiscount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
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

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

}

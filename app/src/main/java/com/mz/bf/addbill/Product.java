package com.mz.bf.addbill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("product_code")
    @Expose
    private String productCode;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("flow_line")
    @Expose
    private Object flowLine;
    @SerializedName("frame")
    @Expose
    private Object frame;
    @SerializedName("fac")
    @Expose
    private Object fac;
    @SerializedName("category_id_fk")
    @Expose
    private Object categoryIdFk;
    @SerializedName("factory_price")
    @Expose
    private String factoryPrice;
    @SerializedName("unit_id_fk")
    @Expose
    private Object unitIdFk;
    @SerializedName("one_sell_price")
    @Expose
    private String oneSellPrice;
    @SerializedName("packet_sell_price")
    @Expose
    private Object packetSellPrice;
    @SerializedName("max_one_sell_price")
    @Expose
    private Object maxOneSellPrice;
    @SerializedName("max_packet_sell_price")
    @Expose
    private Object maxPacketSellPrice;
    @SerializedName("first_rasied")
    @Expose
    private Object firstRasied;
    @SerializedName("all_amount")
    @Expose
    private Object allAmount;
    @SerializedName("main_branch_id_fk")
    @Expose
    private String mainBranchIdFk;
    @SerializedName("sub_branch_id_fk")
    @Expose
    private String subBranchIdFk;
    @SerializedName("storage_id_fk")
    @Expose
    private Object storageIdFk;
    @SerializedName("allowed_discount")
    @Expose
    private Object allowedDiscount;
    @SerializedName("img")
    @Expose
    private Object img;
    @SerializedName("date")
    @Expose
    private Object date;
    @SerializedName("publisher")
    @Expose
    private Object publisher;
    @SerializedName("deleted")
    @Expose
    private String deleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Object getFlowLine() {
        return flowLine;
    }

    public void setFlowLine(Object flowLine) {
        this.flowLine = flowLine;
    }

    public Object getFrame() {
        return frame;
    }

    public void setFrame(Object frame) {
        this.frame = frame;
    }

    public Object getFac() {
        return fac;
    }

    public void setFac(Object fac) {
        this.fac = fac;
    }

    public Object getCategoryIdFk() {
        return categoryIdFk;
    }

    public void setCategoryIdFk(Object categoryIdFk) {
        this.categoryIdFk = categoryIdFk;
    }

    public String getFactoryPrice() {
        return factoryPrice;
    }

    public void setFactoryPrice(String factoryPrice) {
        this.factoryPrice = factoryPrice;
    }

    public Object getUnitIdFk() {
        return unitIdFk;
    }

    public void setUnitIdFk(Object unitIdFk) {
        this.unitIdFk = unitIdFk;
    }

    public String getOneSellPrice() {
        return oneSellPrice;
    }

    public void setOneSellPrice(String oneSellPrice) {
        this.oneSellPrice = oneSellPrice;
    }

    public Object getPacketSellPrice() {
        return packetSellPrice;
    }

    public void setPacketSellPrice(Object packetSellPrice) {
        this.packetSellPrice = packetSellPrice;
    }

    public Object getMaxOneSellPrice() {
        return maxOneSellPrice;
    }

    public void setMaxOneSellPrice(Object maxOneSellPrice) {
        this.maxOneSellPrice = maxOneSellPrice;
    }

    public Object getMaxPacketSellPrice() {
        return maxPacketSellPrice;
    }

    public void setMaxPacketSellPrice(Object maxPacketSellPrice) {
        this.maxPacketSellPrice = maxPacketSellPrice;
    }

    public Object getFirstRasied() {
        return firstRasied;
    }

    public void setFirstRasied(Object firstRasied) {
        this.firstRasied = firstRasied;
    }

    public Object getAllAmount() {
        return allAmount;
    }

    public void setAllAmount(Object allAmount) {
        this.allAmount = allAmount;
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

    public Object getStorageIdFk() {
        return storageIdFk;
    }

    public void setStorageIdFk(Object storageIdFk) {
        this.storageIdFk = storageIdFk;
    }

    public Object getAllowedDiscount() {
        return allowedDiscount;
    }

    public void setAllowedDiscount(Object allowedDiscount) {
        this.allowedDiscount = allowedDiscount;
    }

    public Object getImg() {
        return img;
    }

    public void setImg(Object img) {
        this.img = img;
    }

    public Object getDate() {
        return date;
    }

    public void setDate(Object date) {
        this.date = date;
    }

    public Object getPublisher() {
        return publisher;
    }

    public void setPublisher(Object publisher) {
        this.publisher = publisher;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
}

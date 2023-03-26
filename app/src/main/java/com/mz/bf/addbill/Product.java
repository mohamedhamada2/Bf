package com.mz.bf.addbill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("sum")
    @Expose
    private String sum;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_code")
    @Expose
    private String productCode;
    @SerializedName("one_sell_price")
    @Expose
    private String oneSellPrice;
    @SerializedName("packet_sell_price")
    @Expose
    private String packetSellPrice;
    @SerializedName("packet_rasied")
    @Expose
    private Double packetRasied;
    @SerializedName("one_rasied")
    @Expose
    private Double oneRasied;
    @SerializedName("amount_sale")
    @Expose
    private Integer amountSale;
    @SerializedName("car_amount_hadback")
    @Expose
    private Integer carAmountHadback;
    @SerializedName("store_amount_hadback")
    @Expose
    private Integer storeAmountHadback;

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getOneSellPrice() {
        return oneSellPrice;
    }

    public void setOneSellPrice(String oneSellPrice) {
        this.oneSellPrice = oneSellPrice;
    }

    public String getPacketSellPrice() {
        return packetSellPrice;
    }

    public void setPacketSellPrice(String packetSellPrice) {
        this.packetSellPrice = packetSellPrice;
    }

    public Double getPacketRasied() {
        return packetRasied;
    }

    public void setPacketRasied(Double packetRasied) {
        this.packetRasied = packetRasied;
    }

    public Double getOneRasied() {
        return oneRasied;
    }

    public void setOneRasied(Double oneRasied) {
        this.oneRasied = oneRasied;
    }

    public Integer getAmountSale() {
        return amountSale;
    }

    public void setAmountSale(Integer amountSale) {
        this.amountSale = amountSale;
    }

    public Integer getCarAmountHadback() {
        return carAmountHadback;
    }

    public void setCarAmountHadback(Integer carAmountHadback) {
        this.carAmountHadback = carAmountHadback;
    }

    public Integer getStoreAmountHadback() {
        return storeAmountHadback;
    }

    public void setStoreAmountHadback(Integer storeAmountHadback) {
        this.storeAmountHadback = storeAmountHadback;
    }
}

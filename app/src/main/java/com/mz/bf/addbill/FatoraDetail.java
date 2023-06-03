package com.mz.bf.addbill;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "bill",primaryKeys = {"product_id_fk","product_discount","product_pouns"})
public class FatoraDetail {

    @NonNull
    @ColumnInfo(name = "product_id_fk")
    private String product_id_fk;
    @NonNull
    @ColumnInfo(name = "product_name")
    private String product_name;
    @NonNull
    @ColumnInfo(name = "type")
    private String type;
    @NonNull
    @ColumnInfo(name = "sell_price")
    private String sell_price;
    @NonNull
    @ColumnInfo(name = "amount")
    private String amount;
    @NonNull
    @ColumnInfo(name = "product_discount")
    private String product_discount;
    @NonNull
    @ColumnInfo(name = "product_pouns")
    private String product_pouns;
    @NonNull
    @ColumnInfo(name = "total")
    private String total;
    @NonNull
    @ColumnInfo(name = "notes")
    private String notes;

    @NonNull
    public String getFatora_type() {
        return fatora_type;
    }

    public void setFatora_type(@NonNull String fatora_type) {
        this.fatora_type = fatora_type;
    }

    @NonNull
    @ColumnInfo(name = "fatora_type")
    private String fatora_type;

    @NonNull
    public String getProduct_id_fk() {
        return product_id_fk;
    }

    public void setProduct_id_fk(@NonNull String product_id_fk) {
        this.product_id_fk = product_id_fk;
    }

    @NonNull
    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(@NonNull String product_name) {
        this.product_name = product_name;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    @NonNull
    public String getSell_price() {
        return sell_price;
    }

    public void setSell_price(@NonNull String sell_price) {
        this.sell_price = sell_price;
    }

    @NonNull
    public String getAmount() {
        return amount;
    }

    public void setAmount(@NonNull String amount) {
        this.amount = amount;
    }

    @NonNull
    public String getProduct_discount() {
        return product_discount;
    }

    public void setProduct_discount(@NonNull String product_discount) {
        this.product_discount = product_discount;
    }

    @NonNull
    public String getProduct_pouns() {
        return product_pouns;
    }

    public void setProduct_pouns(@NonNull String product_pouns) {
        this.product_pouns = product_pouns;
    }

    @NonNull
    public String getTotal() {
        return total;
    }

    public void setTotal(@NonNull String total) {
        this.total = total;
    }

    @NonNull
    public String getNotes() {
        return notes;
    }

    public void setNotes(@NonNull String notes) {
        this.notes = notes;
    }
}

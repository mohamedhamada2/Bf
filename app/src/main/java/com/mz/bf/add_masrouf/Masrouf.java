package com.mz.bf.add_masrouf;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Masrouf {
    @SerializedName("id_setting")
    @Expose
    private String idSetting;
    @SerializedName("title_setting")
    @Expose
    private String titleSetting;
    @SerializedName("have_branch")
    @Expose
    private String haveBranch;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("type_name")
    @Expose
    private String typeName;
    @SerializedName("form_id")
    @Expose
    private String formId;
    @SerializedName("title_order")
    @Expose
    private Object titleOrder;
    @SerializedName("title_color")
    @Expose
    private Object titleColor;
    @SerializedName("price")
    @Expose
    private Object price;
    @SerializedName("phone")
    @Expose
    private Object phone;
    @SerializedName("details")
    @Expose
    private Object details;
    @SerializedName("img")
    @Expose
    private Object img;

    public String getIdSetting() {
        return idSetting;
    }

    public void setIdSetting(String idSetting) {
        this.idSetting = idSetting;
    }

    public String getTitleSetting() {
        return titleSetting;
    }

    public void setTitleSetting(String titleSetting) {
        this.titleSetting = titleSetting;
    }

    public String getHaveBranch() {
        return haveBranch;
    }

    public void setHaveBranch(String haveBranch) {
        this.haveBranch = haveBranch;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public Object getTitleOrder() {
        return titleOrder;
    }

    public void setTitleOrder(Object titleOrder) {
        this.titleOrder = titleOrder;
    }

    public Object getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(Object titleColor) {
        this.titleColor = titleColor;
    }

    public Object getPrice() {
        return price;
    }

    public void setPrice(Object price) {
        this.price = price;
    }

    public Object getPhone() {
        return phone;
    }

    public void setPhone(Object phone) {
        this.phone = phone;
    }

    public Object getDetails() {
        return details;
    }

    public void setDetails(Object details) {
        this.details = details;
    }

    public Object getImg() {
        return img;
    }

    public void setImg(Object img) {
        this.img = img;
    }

}

package com.mz.bf.addbill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientDiscount {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("subscription")
    @Expose
    private String subscription;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("publisher")
    @Expose
    private String publisher;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}

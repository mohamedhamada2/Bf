package com.mz.bf.addbill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Client {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("main_table_id_fk")
    @Expose
    private String mainTableIdFk;
    @SerializedName("client_id_fk")
    @Expose
    private String clientIdFk;
    @SerializedName("client_name")
    @Expose
    private String clientName;
    @SerializedName("publisher")
    @Expose
    private String publisher;
    @SerializedName("date")
    @Expose
    private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMainTableIdFk() {
        return mainTableIdFk;
    }

    public void setMainTableIdFk(String mainTableIdFk) {
        this.mainTableIdFk = mainTableIdFk;
    }

    public String getClientIdFk() {
        return clientIdFk;
    }

    public void setClientIdFk(String clientIdFk) {
        this.clientIdFk = clientIdFk;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

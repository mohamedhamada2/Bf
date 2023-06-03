package com.mz.bf.clients;

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
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mob")
    @Expose
    private String mob;
    @SerializedName("long")
    @Expose
    private String _long;
    @SerializedName("lat")
    @Expose
    private String lat;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getLong() {
        return _long;
    }

    public void setLong(String _long) {
        this._long = _long;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

}

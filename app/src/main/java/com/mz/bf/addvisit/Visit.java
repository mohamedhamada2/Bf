package com.mz.bf.addvisit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Visit {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("representive_id_fk")
    @Expose
    private String representiveIdFk;
    @SerializedName("client_id_fk")
    @Expose
    private String clientIdFk;
    @SerializedName("client_name")
    @Expose
    private String clientName;
    @SerializedName("lat_map")
    @Expose
    private String latMap;
    @SerializedName("long_map")
    @Expose
    private String longMap;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("date_ar")
    @Expose
    private String dateAr;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("day_of_week")
    @Expose
    private String dayOfWeek;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRepresentiveIdFk() {
        return representiveIdFk;
    }

    public void setRepresentiveIdFk(String representiveIdFk) {
        this.representiveIdFk = representiveIdFk;
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

    public String getLatMap() {
        return latMap;
    }

    public void setLatMap(String latMap) {
        this.latMap = latMap;
    }

    public String getLongMap() {
        return longMap;
    }

    public void setLongMap(String longMap) {
        this.longMap = longMap;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateAr() {
        return dateAr;
    }

    public void setDateAr(String dateAr) {
        this.dateAr = dateAr;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}

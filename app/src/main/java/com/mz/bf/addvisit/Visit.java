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
    @SerializedName("client_name")
    @Expose
    private String clientName;
    @SerializedName("client_mobile")
    @Expose
    private String clientMobile;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("lat_map")
    @Expose
    private String latMap;
    @SerializedName("long_map")
    @Expose
    private String longMap;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("day_of_week")
    @Expose
    private Object dayOfWeek;
    @SerializedName("check_type")
    @Expose
    private String checkType;
    @SerializedName("diff_minutes")
    @Expose
    private Object diffMinutes;
    @SerializedName("image")
    @Expose
    private Object image;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("date_ar")
    @Expose
    private String dateAr;
    @SerializedName("date")
    @Expose
    private String date;

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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientMobile() {
        return clientMobile;
    }

    public void setClientMobile(String clientMobile) {
        this.clientMobile = clientMobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Object getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Object dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public Object getDiffMinutes() {
        return diffMinutes;
    }

    public void setDiffMinutes(Object diffMinutes) {
        this.diffMinutes = diffMinutes;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDateAr() {
        return dateAr;
    }

    public void setDateAr(String dateAr) {
        this.dateAr = dateAr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

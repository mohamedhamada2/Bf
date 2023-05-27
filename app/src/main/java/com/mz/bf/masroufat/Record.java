package com.mz.bf.masroufat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Record {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("representative_id_fk")
    @Expose
    private String representativeIdFk;
    @SerializedName("fe2a")
    @Expose
    private String fe2a;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("date_esal")
    @Expose
    private String dateEsal;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("publisher")
    @Expose
    private String publisher;
    @SerializedName("month")
    @Expose
    private String month;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("title_setting")
    @Expose
    private String titleSetting;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRepresentativeIdFk() {
        return representativeIdFk;
    }

    public void setRepresentativeIdFk(String representativeIdFk) {
        this.representativeIdFk = representativeIdFk;
    }

    public String getFe2a() {
        return fe2a;
    }

    public void setFe2a(String fe2a) {
        this.fe2a = fe2a;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDateEsal() {
        return dateEsal;
    }

    public void setDateEsal(String dateEsal) {
        this.dateEsal = dateEsal;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTitleSetting() {
        return titleSetting;
    }

    public void setTitleSetting(String titleSetting) {
        this.titleSetting = titleSetting;
    }
}

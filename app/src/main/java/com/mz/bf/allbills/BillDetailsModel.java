package com.mz.bf.allbills;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BillDetailsModel {
    @SerializedName("record")
    @Expose
    private Record record;
    @SerializedName("details")
    @Expose
    private List<Detail> details = null;
    @SerializedName("previous_rasied")
    @Expose
    String previous_rasied;
    @SerializedName("now_rasied")
    @Expose
    String now_rasied;

    public String getPrevious_rasied() {
        return previous_rasied;
    }

    public void setPrevious_rasied(String previous_rasied) {
        this.previous_rasied = previous_rasied;
    }

    public String getNow_rasied() {
        return now_rasied;
    }

    public void setNow_rasied(String now_rasied) {
        this.now_rasied = now_rasied;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

}

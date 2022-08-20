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

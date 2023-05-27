package com.mz.bf.masroufat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllMasroufat {
    @SerializedName("pages_count")
    @Expose
    private Integer pagesCount;
    @SerializedName("records")
    @Expose
    private List<Record> records;

    public Integer getPagesCount() {
        return pagesCount;
    }

    public void setPagesCount(Integer pagesCount) {
        this.pagesCount = pagesCount;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }
}

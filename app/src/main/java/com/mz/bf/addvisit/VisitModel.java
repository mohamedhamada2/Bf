package com.mz.bf.addvisit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VisitModel {
    @SerializedName("pages_count")
    @Expose
    private Integer pagesCount;
    @SerializedName("visits")
    @Expose
    private List<Visit> visits = null;

    public Integer getPagesCount() {
        return pagesCount;
    }

    public void setPagesCount(Integer pagesCount) {
        this.pagesCount = pagesCount;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }
}

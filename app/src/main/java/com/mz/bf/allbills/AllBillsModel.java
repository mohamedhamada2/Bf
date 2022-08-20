
package com.mz.bf.allbills;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllBillsModel {

    @SerializedName("pages_count")
    @Expose
    private Integer pagesCount;
    @SerializedName("fatora")
    @Expose
    private List<Fatora> fatora = null;

    public Integer getPagesCount() {
        return pagesCount;
    }

    public void setPagesCount(Integer pagesCount) {
        this.pagesCount = pagesCount;
    }

    public List<Fatora> getFatora() {
        return fatora;
    }

    public void setFatora(List<Fatora> fatora) {
        this.fatora = fatora;
    }

}

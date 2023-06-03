package com.mz.bf.addpayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SandModel {
    @SerializedName("pages_count")
    @Expose
    private Integer pagesCount;
    @SerializedName("clients")
    @Expose
    private List<Client> clients = null;

    public Integer getPagesCount() {
        return pagesCount;
    }

    public void setPagesCount(Integer pagesCount) {
        this.pagesCount = pagesCount;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

}

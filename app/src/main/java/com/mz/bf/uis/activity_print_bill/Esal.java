package com.mz.bf.uis.activity_print_bill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Esal {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("make_by")
    @Expose
    private String makeBy;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("main_branch_id_fk")
    @Expose
    private Object mainBranchIdFk;
    @SerializedName("sub_branch_id_fk")
    @Expose
    private Object subBranchIdFk;
    @SerializedName("rkm_esal")
    @Expose
    private String rkmEsal;
    @SerializedName("date_esal")
    @Expose
    private String dateEsal;
    @SerializedName("pay_method_id_fk")
    @Expose
    private String payMethodIdFk;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("cheque_num")
    @Expose
    private Object chequeNum;
    @SerializedName("dead_line_date")
    @Expose
    private Object deadLineDate;
    @SerializedName("bank_id_fk")
    @Expose
    private Object bankIdFk;
    @SerializedName("mofawed_person")
    @Expose
    private Object mofawedPerson;
    @SerializedName("second_mofawed_person")
    @Expose
    private Object secondMofawedPerson;
    @SerializedName("client_id_fk")
    @Expose
    private String clientIdFk;
    @SerializedName("client_name")
    @Expose
    private String clientName;
    @SerializedName("month")
    @Expose
    private String month;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("date_ar")
    @Expose
    private String dateAr;
    @SerializedName("publisher")
    @Expose
    private String publisher;
    @SerializedName("approved")
    @Expose
    private String approved;
    @SerializedName("date_tahseel")
    @Expose
    private Object dateTahseel;
    @SerializedName("person_id_fk")
    @Expose
    private Object personIdFk;
    @SerializedName("comment")
    @Expose
    private Object comment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMakeBy() {
        return makeBy;
    }

    public void setMakeBy(String makeBy) {
        this.makeBy = makeBy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getMainBranchIdFk() {
        return mainBranchIdFk;
    }

    public void setMainBranchIdFk(Object mainBranchIdFk) {
        this.mainBranchIdFk = mainBranchIdFk;
    }

    public Object getSubBranchIdFk() {
        return subBranchIdFk;
    }

    public void setSubBranchIdFk(Object subBranchIdFk) {
        this.subBranchIdFk = subBranchIdFk;
    }

    public String getRkmEsal() {
        return rkmEsal;
    }

    public void setRkmEsal(String rkmEsal) {
        this.rkmEsal = rkmEsal;
    }

    public String getDateEsal() {
        return dateEsal;
    }

    public void setDateEsal(String dateEsal) {
        this.dateEsal = dateEsal;
    }

    public String getPayMethodIdFk() {
        return payMethodIdFk;
    }

    public void setPayMethodIdFk(String payMethodIdFk) {
        this.payMethodIdFk = payMethodIdFk;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Object getChequeNum() {
        return chequeNum;
    }

    public void setChequeNum(Object chequeNum) {
        this.chequeNum = chequeNum;
    }

    public Object getDeadLineDate() {
        return deadLineDate;
    }

    public void setDeadLineDate(Object deadLineDate) {
        this.deadLineDate = deadLineDate;
    }

    public Object getBankIdFk() {
        return bankIdFk;
    }

    public void setBankIdFk(Object bankIdFk) {
        this.bankIdFk = bankIdFk;
    }

    public Object getMofawedPerson() {
        return mofawedPerson;
    }

    public void setMofawedPerson(Object mofawedPerson) {
        this.mofawedPerson = mofawedPerson;
    }

    public Object getSecondMofawedPerson() {
        return secondMofawedPerson;
    }

    public void setSecondMofawedPerson(Object secondMofawedPerson) {
        this.secondMofawedPerson = secondMofawedPerson;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public Object getDateTahseel() {
        return dateTahseel;
    }

    public void setDateTahseel(Object dateTahseel) {
        this.dateTahseel = dateTahseel;
    }

    public Object getPersonIdFk() {
        return personIdFk;
    }

    public void setPersonIdFk(Object personIdFk) {
        this.personIdFk = personIdFk;
    }

    public Object getComment() {
        return comment;
    }

    public void setComment(Object comment) {
        this.comment = comment;
    }
}

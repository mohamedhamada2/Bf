package com.mz.bf.authentication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("govern_id_fk")
    @Expose
    private String governIdFk;
    @SerializedName("national_num")
    @Expose
    private String nationalNum;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("start_job_date")
    @Expose
    private String startJobDate;
    @SerializedName("car_number")
    @Expose
    private String carNumber;
    @SerializedName("mob")
    @Expose
    private String mob;
    @SerializedName("mob2")
    @Expose
    private String mob2;
    @SerializedName("adress")
    @Expose
    private String adress;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("publisher")
    @Expose
    private String publisher;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("deleted")
    @Expose
    private String deleted;
    @SerializedName("success")
    @Expose
    private Integer success;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGovernIdFk() {
        return governIdFk;
    }

    public void setGovernIdFk(String governIdFk) {
        this.governIdFk = governIdFk;
    }

    public String getNationalNum() {
        return nationalNum;
    }

    public void setNationalNum(String nationalNum) {
        this.nationalNum = nationalNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStartJobDate() {
        return startJobDate;
    }

    public void setStartJobDate(String startJobDate) {
        this.startJobDate = startJobDate;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getMob2() {
        return mob2;
    }

    public void setMob2(String mob2) {
        this.mob2 = mob2;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }
}

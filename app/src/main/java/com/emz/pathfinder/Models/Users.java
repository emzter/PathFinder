package com.emz.pathfinder.Models;

import com.google.gson.annotations.SerializedName;

public class Users {
    @SerializedName("id")
    private int id;
    @SerializedName("guid")
    private String guid;
    @SerializedName("email")
    private String email;
    @SerializedName("user_group")
    private String group;
    @SerializedName("status")
    private String status;
    @SerializedName("validate")
    private String validate;
    @SerializedName("profile_image")
    private String proPic;
    @SerializedName("first_name")
    private String fname;
    @SerializedName("last_name")
    private String lname;
    @SerializedName("sex")
    private int sex;
    @SerializedName("birthdate")
    private String birthdate;
    @SerializedName("address")
    private String address;
    @SerializedName("subdistrict")
    private String subdistrict;
    @SerializedName("district")
    private String district;
    @SerializedName("postcode")
    private String postcode;
    @SerializedName("province")
    private String province;
    @SerializedName("telephone")
    private String telephone;

    public Users(int id, String guid, String email, String group, String status, String validate, String proPic, String fname, String lname, int sex, String birthdate, String address, String subdistrict, String district, String postcode, String province, String telephone) {
        this.id = id;
        this.guid = guid;
        this.email = email;
        this.group = group;
        this.status = status;
        this.validate = validate;
        this.proPic = proPic;
        this.fname = fname;
        this.lname = lname;
        this.sex = sex;
        this.birthdate = birthdate;
        this.address = address;
        this.subdistrict = subdistrict;
        this.district = district;
        this.postcode = postcode;
        this.province = province;
        this.telephone = telephone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getValidate() {
        return validate;
    }

    public void setValidate(String validate) {
        this.validate = validate;
    }

    public String getProPic() {
        return proPic;
    }

    public void setProPic(String proPic) {
        this.proPic = proPic;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSubdistrict() {
        return subdistrict;
    }

    public void setSubdistrict(String subdistrict) {
        this.subdistrict = subdistrict;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}

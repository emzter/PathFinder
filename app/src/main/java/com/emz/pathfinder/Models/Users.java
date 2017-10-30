package com.emz.pathfinder.Models;

public class Users {
    private int id;
    private String guid;
    private String email;
    private String user_group;
    private String status;
    private String first_name;
    private String last_name;
    private int sex;
    private String birthdate;
    private String address;
    private String subdistrict;
    private String district;
    private String postcode;
    private String province;
    private String telephone;

    public Users(int id, String guid, String email, String user_group, String status, String first_name, String last_name, int sex, String birthdate, String address, String subdistrict, String district, String postcode, String province, String telephone) {
        this.id = id;
        this.guid = guid;
        this.email = email;
        this.user_group = user_group;
        this.status = status;
        this.first_name = first_name;
        this.last_name = last_name;
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

    public String getUser_group() {
        return user_group;
    }

    public void setUser_group(String user_group) {
        this.user_group = user_group;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
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

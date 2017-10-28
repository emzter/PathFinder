package com.emz.pathfinder.Models;

public class UserModel {
    private int id;
    private String guid;
    private String name;
    private String lastname;
    private String email;
    private String group;
    private String status;
    private String birthdate;
    private int sex;
    private String address;
    private String subdistrict;
    private String district;
    private String postcode;
    private String telephone;


    public UserModel(int id) {
        this.id = id;
    }

    public UserModel(int id, String email) {
        this.id = id;
        this.email = email;
    }

    public UserModel(int id, String guid, String email, String group, String status) {
        this.id = id;
        this.guid = guid;
        this.name = name;
        this.email = email;
        this.group = group;
        this.status = status;
    }

    public UserModel(int id, String guid, String name, String lastname, String email, String group, String status, String birthdate, int sex, String address, String subdistrict, String district, String postcode, String telephone) {
        this.id = id;
        this.guid = guid;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.group = group;
        this.status = status;
        this.birthdate = birthdate;
        this.sex = sex;
        this.address = address;
        this.subdistrict = subdistrict;
        this.district = district;
        this.postcode = postcode;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}

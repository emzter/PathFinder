package com.emz.pathfinder.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Volunteer implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("email")
    private String email;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("profile_picture")
    private String proPic;
    @SerializedName("telephone")
    private String telephone;
    @SerializedName("on_order")
    private int onOrder;
    @SerializedName("category")
    private int category;
    @SerializedName("status")
    private int status;
    @SerializedName("validate")
    private int validate;
    @SerializedName("online")
    private int online;
    @SerializedName("lat")
    private double lat;
    @SerializedName("lng")
    private double lng;
    @SerializedName("rating")
    private float rating;

    public Volunteer(int id, String email, String firstName, String lastName, String proPic, String telephone, int onOrder, int category, int status, int validate, int online) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.proPic = proPic;
        this.telephone = telephone;
        this.onOrder = onOrder;
        this.category = category;
        this.status = status;
        this.validate = validate;
        this.online = online;
    }

    public Volunteer(int id, String email, String firstName, String lastName, String proPic, String telephone, int onOrder, int category, int status, int validate, int online, double lat, double lng) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.proPic = proPic;
        this.telephone = telephone;
        this.onOrder = onOrder;
        this.category = category;
        this.status = status;
        this.validate = validate;
        this.online = online;
        this.lat = lat;
        this.lng = lng;
    }

    public Volunteer(int id, String email, String firstName, String lastName, String proPic, String telephone, int onOrder, int category, int status, int validate, int online, double lat, double lng, float rating) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.proPic = proPic;
        this.telephone = telephone;
        this.onOrder = onOrder;
        this.category = category;
        this.status = status;
        this.validate = validate;
        this.online = online;
        this.lat = lat;
        this.lng = lng;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProPic() {
        return proPic;
    }

    public String getTelephone() {
        return telephone;
    }

    public int getOnOrder() {
        return onOrder;
    }

    public int getCategory() {
        return category;
    }

    public int getStatus() {
        return status;
    }

    public int getValidate() {
        return validate;
    }

    public int getOnline() {
        return online;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public float getRating() {
        return rating;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setProPic(String proPic) {
        this.proPic = proPic;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setOnOrder(int onOrder) {
        this.onOrder = onOrder;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setValidate(int validate) {
        this.validate = validate;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isOnline(){
        boolean status = false;
        if(online == 0){
            status = false;
        }else if(online == 1){
            status = true;
        }
        return status;
    }

    public String getFullName(){
        return firstName+" "+lastName;
    }
}

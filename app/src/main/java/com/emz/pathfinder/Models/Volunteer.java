package com.emz.pathfinder.Models;

import com.google.gson.annotations.SerializedName;

public class Volunteer {
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
    @SerializedName("car_type")
    private int carType;
    @SerializedName("category")
    private String category;
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

    public Volunteer(int id, String email, String firstName, String lastName, String proPic, String telephone, int carType, String category, int status, int validate, int online, double lat, double lng) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.proPic = proPic;
        this.telephone = telephone;
        this.carType = carType;
        this.category = category;
        this.status = status;
        this.validate = validate;
        this.online = online;
        this.lat = lat;
        this.lng = lng;
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

    public int getCarType() {
        return carType;
    }

    public String getCategory() {
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

    public boolean isOnline(){
        boolean status = false;
        if(online == 0){
            status = false;
        }else if(online == 1){
            status = true;
        }
        return status;
    }
}

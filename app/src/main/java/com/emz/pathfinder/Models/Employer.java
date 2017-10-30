package com.emz.pathfinder.Models;

/**
 * Created by EMZ on 30/10/2560.
 */

public class Employer {
    private int id;
    private String name;
    private String province;
    private String country;
    private String telephone;
    private String logo;
    private int category_id;

    public Employer(int id, String name, String province, String country, String telephone, String logo, int category_id) {
        this.id = id;
        this.name = name;
        this.province = province;
        this.country = country;
        this.telephone = telephone;
        this.logo = logo;
        this.category_id = category_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }
}

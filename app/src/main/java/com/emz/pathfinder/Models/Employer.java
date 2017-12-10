package com.emz.pathfinder.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by EMZ on 30/10/2560.
 */

public class Employer {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("about")
    private String about;
    @SerializedName("telephone")
    private String telephone;
    @SerializedName("logo")
    private String logo;
    @SerializedName("contact_name")
    private String contactName;
    @SerializedName("contact_email")
    private String contactEmail;
    @SerializedName("section")
    private String section;
    @SerializedName("category_id")
    private int category_id;

    public Employer(int id, String name, String about, String telephone, String logo, String contactName, String contactEmail, String section, int category_id) {
        this.id = id;
        this.name = name;
        this.about = about;
        this.telephone = telephone;
        this.logo = logo;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.section = section;
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

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
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

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }
}

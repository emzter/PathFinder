package com.emz.pathfinder.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Educations implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("institute_name")
    private String name;
    @SerializedName("background")
    private int background;
    @SerializedName("major")
    private String major;
    @SerializedName("gpa")
    private double gpa;

    public Educations(int id, String name, int background, String major, double gpa) {
        this.id = id;
        this.name = name;
        this.background = background;
        this.major = major;
        this.gpa = gpa;
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

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }
}

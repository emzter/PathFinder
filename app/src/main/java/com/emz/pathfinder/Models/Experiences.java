package com.emz.pathfinder.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Experiences implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("company")
    private String employer;
    @SerializedName("status")
    private int status;
    @SerializedName("start_at")
    private String start;
    @SerializedName("end_at")
    private String end;

    public Experiences(int id, String name, String employer, int status, String start, String end) {
        this.id = id;
        this.name = name;
        this.employer = employer;
        this.status = status;
        this.start = start;
        this.end = end;
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

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}

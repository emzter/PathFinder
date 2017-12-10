package com.emz.pathfinder.Models;

import com.google.gson.annotations.SerializedName;

public class Jobs {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("responsibilities")
    private String responsibility;
    @SerializedName("qualification")
    private String qualification;
    @SerializedName("benefit")
    private String benefit;
    @SerializedName("capacity")
    private int capacity;
    @SerializedName("cap_type")
    private int capType;
    @SerializedName("salary")
    private String salary;
    @SerializedName("salary_type")
    private int salaryType;
    @SerializedName("negotiable")
    private boolean negotiable;
    @SerializedName("location")
    private String location;
    @SerializedName("type")
    private String type;
    @SerializedName("level")
    private int level;
    @SerializedName("exp_req")
    private int exp_req;
    @SerializedName("edu_req")
    private int edu_req;
    @SerializedName("category")
    private String category;
    @SerializedName("company_id")
    private int company_id;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("favorite")
    private boolean favorite;
    @SerializedName("apply")
    private boolean apply;

    public Jobs(int id, String name, String responsibility, String qualification, String benefit, int capacity, int capType, String salary, int salaryType, boolean negotiable, String location, String type, int level, int exp_req, int edu_req, String category, int company_id, String createdAt, boolean favorite, boolean apply) {
        this.id = id;
        this.name = name;
        this.responsibility = responsibility;
        this.qualification = qualification;
        this.benefit = benefit;
        this.capacity = capacity;
        this.capType = capType;
        this.salary = salary;
        this.salaryType = salaryType;
        this.negotiable = negotiable;
        this.location = location;
        this.type = type;
        this.level = level;
        this.exp_req = exp_req;
        this.edu_req = edu_req;
        this.category = category;
        this.company_id = company_id;
        this.createdAt = createdAt;
        this.favorite = favorite;
        this.apply = apply;
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

    public String getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getBenefit() {
        return benefit;
    }

    public void setBenefit(String benefit) {
        this.benefit = benefit;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapType() {
        return capType;
    }

    public void setCapType(int capType) {
        this.capType = capType;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public int getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(int salaryType) {
        this.salaryType = salaryType;
    }

    public boolean isNegotiable() {
        return negotiable;
    }

    public void setNegotiable(boolean negotiable) {
        this.negotiable = negotiable;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp_req() {
        return exp_req;
    }

    public void setExp_req(int exp_req) {
        this.exp_req = exp_req;
    }

    public int getEdu_req() {
        return edu_req;
    }

    public void setEdu_req(int edu_req) {
        this.edu_req = edu_req;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isApply() {
        return apply;
    }

    public void setApply(boolean apply) {
        this.apply = apply;
    }
}

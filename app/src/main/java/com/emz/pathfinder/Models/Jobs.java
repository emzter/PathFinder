package com.emz.pathfinder.Models;

/**
 * Created by EMZ on 30/10/2560.
 */

public class Jobs {
    private int id;
    private String name;
    private String responsibility;
    private String qualification;
    private String benefit;
    private String salary;
    private int salary_type;
    private String location;
    private String type;
    private int level;
    private int exp_req;
    private int edu_req;
    private int category_id;
    private int company_id;

    public Jobs(int id, String name, String responsibility, String qualification, String benefit, String salary, int salary_type, String location, String type, int level, int exp_req, int edu_req, int category_id, int company_id) {
        this.id = id;
        this.name = name;
        this.responsibility = responsibility;
        this.qualification = qualification;
        this.benefit = benefit;
        this.salary = salary;
        this.salary_type = salary_type;
        this.location = location;
        this.type = type;
        this.level = level;
        this.exp_req = exp_req;
        this.edu_req = edu_req;
        this.category_id = category_id;
        this.company_id = company_id;
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

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public int getSalary_type() {
        return salary_type;
    }

    public void setSalary_type(int salary_type) {
        this.salary_type = salary_type;
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

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }
}

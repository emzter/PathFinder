package com.emz.pathfinder.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Users implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("email")
    private String email;
    @SerializedName("user_group")
    private String group;
    @SerializedName("status")
    private int status;
    @SerializedName("on_order")
    private int onOnder;
    @SerializedName("validate")
    private String validate;
    @SerializedName("profile_image")
    private String proPic;
    @SerializedName("header_image")
    private String headerPic;
    @SerializedName("first_name")
    private String fname;
    @SerializedName("last_name")
    private String lname;
    @SerializedName("sex")
    private int sex;
    @SerializedName("birthdate")
    private String birthdate;
    @SerializedName("telephone")
    private String telephone;
    @SerializedName("facebook")
    private String facebook;
    @SerializedName("twitter")
    private String twitter;
    @SerializedName("line")
    private String line;
    @SerializedName("other_link")
    private String otherlink;
    @SerializedName("friend_status")
    private int friendStatus;
    @SerializedName("friend_count")
    private int friendCount;

    public Users(int id, String email, String group, int status, int onOnder, String validate, String proPic, String headerPic, String fname, String lname, int sex, String birthdate, String telephone, String facebook, String twitter, String line, String otherlink, int friendStatus, int friendCount) {
        this.id = id;
        this.email = email;
        this.group = group;
        this.status = status;
        this.onOnder = onOnder;
        this.validate = validate;
        this.proPic = proPic;
        this.headerPic = headerPic;
        this.fname = fname;
        this.lname = lname;
        this.sex = sex;
        this.birthdate = birthdate;
        this.telephone = telephone;
        this.facebook = facebook;
        this.twitter = twitter;
        this.line = line;
        this.otherlink = otherlink;
        this.friendStatus = friendStatus;
        this.friendCount = friendCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getValidate() {
        return validate;
    }

    public void setValidate(String validate) {
        this.validate = validate;
    }

    public String getProPic() {
        return proPic;
    }

    public void setProPic(String proPic) {
        this.proPic = proPic;
    }

    public String getHeaderPic() {
        return headerPic;
    }

    public void setHeaderPic(String headerPic) {
        this.headerPic = headerPic;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getOtherlink() {
        return otherlink;
    }

    public void setOtherlink(String otherlink) {
        this.otherlink = otherlink;
    }

    public int getFriendStatus() {
        return friendStatus;
    }

    public void setFriendStatus(int friendStatus) {
        this.friendStatus = friendStatus;
    }

    public int getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(int friendCount) {
        this.friendCount = friendCount;
    }

    public int getOnOnder() {
        return onOnder;
    }

    public void setOnOnder(int onOnder) {
        this.onOnder = onOnder;
    }

    public String getFullName() { return fname+" "+lname; }
}

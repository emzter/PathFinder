package com.emz.pathfinder.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by im_ae on 23/11/2560.
 */

public class Locations {
    @SerializedName("id")
    int id;
    @SerializedName("user_id")
    int uid;
    @SerializedName("lat")
    float lat;
    @SerializedName("lng")
    float lng;
    @SerializedName("created_at")
    String created;

    public Locations(int id, int uid, float lat, float lng, String created) {
        this.id = id;
        this.uid = uid;
        this.lat = lat;
        this.lng = lng;
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}

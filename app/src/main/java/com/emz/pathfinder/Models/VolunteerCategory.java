package com.emz.pathfinder.Models;

/**
 * Created by im_ae on 13/12/2560.
 */

public class VolunteerCategory {
    private int id;
    private String name;

    public VolunteerCategory(int id, String name) {
        this.id = id;
        this.name = name;
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
}

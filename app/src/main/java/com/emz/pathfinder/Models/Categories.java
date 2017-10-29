package com.emz.pathfinder.Models;

/**
 * Created by EMZ on 30/10/2560.
 */

public class Categories {
    private int id;
    private String parent_id;
    private String name;
    private String icon;

    public Categories(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Categories(int id, String parent_id, String name, String icon) {
        this.id = id;
        this.parent_id = parent_id;
        this.name = name;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}

package com.emz.pathfinder.Models;

import com.google.gson.annotations.SerializedName;

public class Posts {
    @SerializedName("id")
    private int id;
    @SerializedName("message")
    private String message;
    @SerializedName("created_at")
    private String created;
    @SerializedName("author")
    private int author;
    @SerializedName("posted_to")
    private int recipient;

    public Posts(int id, String message, String created, int author, int recipient) {
        this.id = id;
        this.message = message;
        this.created = created;
        this.author = author;
        this.recipient = recipient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public int getRecipient() {
        return recipient;
    }

    public void setRecipient(int recipient) {
        this.recipient = recipient;
    }
}

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
    @SerializedName("like_count")
    private int likeCount;
    @SerializedName("comment_count")
    private int commentCount;
    @SerializedName("like_status")
    private boolean likeStatus;

    public Posts(int id, String message, String created, int author, int recipient, int likeCount, int commentCount, boolean likeStatus) {
        this.id = id;
        this.message = message;
        this.created = created;
        this.author = author;
        this.recipient = recipient;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.likeStatus = likeStatus;
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

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public boolean getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(boolean likeStatus) {
        this.likeStatus = likeStatus;
    }
}

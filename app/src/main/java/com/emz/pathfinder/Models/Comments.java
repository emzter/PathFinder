package com.emz.pathfinder.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Comments implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("message")
    private String message;
    @SerializedName("created_at")
    private String createAt;
    @SerializedName("author")
    private int author;
    @SerializedName("post_id")
    private int postId;
    @SerializedName("like_count")
    private int likeCount;
    @SerializedName("like_status")
    private boolean likeStatus;

    public Comments(int id, String message, String createAt, int author, int postId, int likeCount, boolean likeStatus) {
        this.id = id;
        this.message = message;
        this.createAt = createAt;
        this.author = author;
        this.postId = postId;
        this.likeCount = likeCount;
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

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public boolean isLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(boolean likeStatus) {
        this.likeStatus = likeStatus;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}

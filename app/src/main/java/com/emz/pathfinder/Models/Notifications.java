package com.emz.pathfinder.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by miste on 24/11/2560.
 */

public class Notifications {
    @SerializedName("id")
    private int id;
    @SerializedName("recipient_id")
    private int recipient;
    @SerializedName("sender_id")
    private int sender;
    @SerializedName("unread")
    private int unread;
    @SerializedName("type")
    private String type;
    @SerializedName("reference_link")
    private String ref;
    @SerializedName("created_at")
    private String created;

    public Notifications(int id, int recipient, int sender, int unread, String type, String ref, String created) {
        this.id = id;
        this.recipient = recipient;
        this.sender = sender;
        this.unread = unread;
        this.type = type;
        this.ref = ref;
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecipient() {
        return recipient;
    }

    public void setRecipient(int recipient) {
        this.recipient = recipient;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}

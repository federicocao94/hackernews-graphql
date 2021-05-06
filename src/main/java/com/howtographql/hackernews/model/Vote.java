package com.howtographql.hackernews.model;

import java.time.ZonedDateTime;

public class Vote {

    private String id;

    private ZonedDateTime createdAt;

    private String linkId;

    private String userId;

    public Vote(String id, ZonedDateTime createdAt, String linkId, String userId) {
        this.id = id;
        this.createdAt = createdAt;
        this.linkId = linkId;
        this.userId = userId;
    }

    public Vote(ZonedDateTime createdAt, String linkId, String userId) {
        this.createdAt = createdAt;
        this.linkId = linkId;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public String getLinkId() {
        return linkId;
    }

    public String getUserId() {
        return userId;
    }
}

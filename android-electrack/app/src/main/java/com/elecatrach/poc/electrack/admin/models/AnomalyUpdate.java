package com.elecatrach.poc.electrack.admin.models;

/**
 * Created by manu on 5/14/2016.
 */
public class AnomalyUpdate {
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;
}

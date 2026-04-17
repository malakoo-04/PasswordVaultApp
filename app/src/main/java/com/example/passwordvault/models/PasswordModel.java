package com.example.passwordvault.models;

import java.io.Serializable;

public class PasswordModel implements Serializable {
    private int id;
    private String serviceName;
    private String username;
    private String passwordEncrypted;
    private String website;
    private String note;
    private String createdAt;

    public PasswordModel(int id, String serviceName, String username, String passwordEncrypted,
                         String website, String note, String createdAt) {
        this.id = id;
        this.serviceName = serviceName;
        this.username = username;
        this.passwordEncrypted = passwordEncrypted;
        this.website = website;
        this.note = note;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public String getServiceName() { return serviceName; }
    public String getUsername() { return username; }
    public String getPasswordEncrypted() { return passwordEncrypted; }
    public String getWebsite() { return website; }
    public String getNote() { return note; }
    public String getCreatedAt() { return createdAt; }

    public void setId(int id) { this.id = id; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public void setUsername(String username) { this.username = username; }
    public void setPasswordEncrypted(String passwordEncrypted) { this.passwordEncrypted = passwordEncrypted; }
    public void setWebsite(String website) { this.website = website; }
    public void setNote(String note) { this.note = note; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}

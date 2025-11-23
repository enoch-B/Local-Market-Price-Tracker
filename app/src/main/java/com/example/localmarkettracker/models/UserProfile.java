package com.example.localmarkettracker.models;

public class UserProfile {
    private String uid;
    private String displayName;
    private String role;
    private String avatarUrl;

    public UserProfile() {}

    public String getUid() { return uid; }
    public void setUid(String u) { this.uid = u; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String n) { this.displayName = n; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String url) { this.avatarUrl = url; }
}

package com.example.localmarkettracker.models;

public class UserProfile {

    private String uid;
    private String email;
    private String displayName;
    private String role;
    private String avatarUrl;

    public UserProfile() {}

    public UserProfile(String uid, String email, String displayName, String role, String avatarUrl) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.role = role;
        this.avatarUrl = avatarUrl;
    }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
}

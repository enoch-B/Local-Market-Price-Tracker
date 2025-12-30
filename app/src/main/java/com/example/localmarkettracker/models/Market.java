package com.example.localmarkettracker.models;

import com.google.firebase.Timestamp;

public class Market {
    private String marketId;
    private String name;
    private String location;
    private double latitude;
    private double longitude;
    private Timestamp createdAt;

    public Market() {} // Required for Firestore

    public Market(String marketId, String name, String location) {
        this.marketId = marketId;
        this.name = name;
        this.location = location;
        this.createdAt = Timestamp.now();
    }

    // Getters and Setters
    public String getMarketId() { return marketId; }
    public void setMarketId(String marketId) { this.marketId = marketId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public Timestamp getCreatedAt() { return createdAt; }
    public String toString() { return name; } // Useful for Spinner display
}

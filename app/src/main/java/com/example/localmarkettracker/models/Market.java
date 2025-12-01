package com.example.localmarkettracker.models;

import java.util.HashMap;
import java.util.Map;

public class Market {
    private String id;
    private String name;
    private String region;
    private double lat;
    private double lng;
    private Map<String, Object> metadata;

    // Required no-arg constructor for Firestore
    public Market() {
        this.metadata = new HashMap<>();
    }

    // Convenience constructor
    public Market(String id, String name, String region, double lat, double lng) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.lat = lat;
        this.lng = lng;
        this.metadata = new HashMap<>();
    }

    // Getters & setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLng() { return lng; }
    public void setLng(double lng) { this.lng = lng; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata != null ? metadata : new HashMap<>();
    }

    // Helpful: add a metadata entry
    public void putMetadata(String key, Object value) {
        if (this.metadata == null) this.metadata = new HashMap<>();
        this.metadata.put(key, value);
    }

    @Override
    public String toString() {
        return "Market{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", region='" + region + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", metadata=" + metadata +
                '}';
    }
}

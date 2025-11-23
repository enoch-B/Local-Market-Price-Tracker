package com.example.localmarkettracker.models;


import java.util.Map;


public class Market {
private String id;
private String name;
private String region;
private double lat;
private double lng;
private Map<String,Object> metadata;


public Market() {}
public Market(String id, String name, String region, double lat, double lng){ this.id=id; this.name=name; this.region=region; this.lat=lat; this.lng=lng; }
// getters & setters omitted for brevity (include all)
}
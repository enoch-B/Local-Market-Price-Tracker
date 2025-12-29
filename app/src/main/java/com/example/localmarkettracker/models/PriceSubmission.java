package com.example.localmarkettracker.models;

import com.google.firebase.Timestamp;

public class PriceSubmission {
    private String id;
    private String vendorId;
    private String itemName;
    private String catagory;
    private double price;
    private String status; // "Pending", "Approved", "Rejected"
    private Timestamp timestamp;
    private String shopName;

    public PriceSubmission() { } // Empty constructor for Firestore

    public PriceSubmission(String vendorId, String itemName, double price, String status, Timestamp timestamp, String shopName, String catagory) {
        this.vendorId = vendorId;
        this.itemName = itemName;
        this.price = price;
        this.catagory= catagory;
        this.status = status;
        this.timestamp = timestamp;
        this.shopName = shopName;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getVendorId() { return vendorId; }
    public String getCatagory() { return catagory; }
    public void setCatagory(String catagory) { this.catagory = catagory; }
    public String getItemName() { return itemName; }
    public double getPrice() { return price; }
    public String getStatus() { return status; }
    public Timestamp getTimestamp() { return timestamp; }
    public String getShopName() { return shopName; }
}

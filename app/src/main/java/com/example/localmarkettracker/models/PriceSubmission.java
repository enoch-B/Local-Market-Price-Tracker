package com.example.localmarkettracker.models;

import com.google.firebase.Timestamp;

public class PriceSubmission {
    private String priceId; // Firestore Doc ID
    private String productId;
    private String productName; // Helper for display, even if strictly normalized
    private String vendorId;
    private String marketId;
    private String marketName; // Helper for display
    private double price;
    private String category;
    private String quality; // "A", "B", "C"
    private String stockStatus; // "in-stock", "low-stock"
    private boolean isApproved;
    private Timestamp timestamp;
    private String productImageUrl; // <-- ADD TO CONSTRUCTOR


    public PriceSubmission() { }

    public PriceSubmission(String vendorId, String marketId, String marketName, String productId, String productName, String category, double price, String quality, String stockStatus, String productImageUrl) { // <-- ADD TO CONSTRUCTOR
        this.vendorId = vendorId;
        this.marketId = marketId;
        this.marketName = marketName;
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.quality = quality;
        this.stockStatus = stockStatus;
        this.isApproved = false;
        this.timestamp = Timestamp.now();
        this.productImageUrl = productImageUrl; // <-- SET IN CONSTRUCTOR
    }

    // Getters and Setters
    public String getPriceId() { return priceId; }
    public void setPriceId(String priceId) { this.priceId = priceId; }
    public String getProductId() { return productId; }
    public String getCategory() { return category; }
    public String setVendorId(String vendorId) { return vendorId; }
    public String getProductName() { return productName; }
    public String getVendorId() { return vendorId; }
    public String setMarketId(String marketId) { return marketId; }
    public String getMarketId() { return marketId; }
    public String getMarketName() { return marketName; }
    public double getPrice() { return price; }
    public String getStatus() { return isApproved ? "Approved" : "Pending"; } // Helper for UI
    public boolean getIsApproved() { return isApproved; }
    public String getQuality() { return quality; }
    public String getStockStatus() { return stockStatus; }
    public Timestamp getTimestamp() { return timestamp; }
    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }
}

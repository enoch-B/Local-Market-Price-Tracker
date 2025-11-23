package com.example.localmarkettracker.models;

import com.google.firebase.Timestamp;

public class ItemPrice {
    // Renamed 'id' to 'itemId' and 'productName' to 'itemName' for clarity and consistency
    // Note: If you prefer 'id' and 'productName', you MUST change the adapter code instead.
    private String itemId;     // Used by ItemAdapter.java
    private String marketId;   // Used by ItemAdapter.java
    
    // CRITICAL: Added the missing 'category' field for filtering in ItemListActivity
    private String category;
    
    // Remaining fields
    private double price;
    private String currency;
    private String reporterId;
    private Timestamp timestamp;
    private boolean verified;

    public ItemPrice() {}

    // ===================================
    // Getters and Setters (Original/Main)
    // ===================================
    
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getMarketId() { return marketId; }
    public void setMarketId(String marketId) { this.marketId = marketId; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getReporterId() { return reporterId; }
    public void setReporterId(String reporterId) { this.reporterId = reporterId; }

    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }

    // ==========================================================
    // COMPILER FIXES: BRIDGE METHODS (Added in the previous step)
    // ==========================================================

    public void setProductName(String productName) {
        this.itemId = productName;
    }

    public void setMarketName(String marketName) {
        this.marketId = marketName;
    }

    public String getProductName() {
        return itemId;
    }

    public String getMarketName() {
        return marketId;
    }
}
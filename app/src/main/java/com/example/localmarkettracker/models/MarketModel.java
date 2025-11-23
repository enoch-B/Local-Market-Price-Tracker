package com.example.localmarkettracker.models;

/**
 * Model class for storing data about a physical market location,
 * used for displaying items in Nearby and Trending lists.
 */
public class MarketModel {

    private String name;
    private String description;
    private String status;
    private String category;
    private String distance;
    private int iconResId;

    private String region;   // Region / Location
    private String price;    // Optional price (for item-focused cards)
    private String imageUrl; // Optional image URL

    // ------------------------------------------------------
    // CONSTRUCTOR 1: Full details (Nearby / Trending)
    // ------------------------------------------------------
    public MarketModel(String name, String description, String status, String category,
                       String distance, int iconResId, String region) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.category = category;
        this.distance = distance;
        this.iconResId = iconResId;
        this.region = region;
    }

    // ------------------------------------------------------
    // CONSTRUCTOR 2: Partial details (Item display lists)
    // ------------------------------------------------------
    public MarketModel(String name, String price, String category, String imageUrl) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    // ------------------------------------------------------
    // EMPTY CONSTRUCTOR (Required by Firebase)
    // ------------------------------------------------------
    public MarketModel() {
    }

    // ------------------------------------------------------
    // SAFE GETTERS (Prevent crashes)
    // ------------------------------------------------------
    public String getName() {
        return name == null ? "" : name;
    }

    public String getDescription() {
        return description == null ? "" : description;
    }

    public String getStatus() {
        return status == null ? "Unknown" : status;
    }

    public String getCategory() {
        return category == null ? "N/A" : category;
    }

    public String getDistance() {
        return distance == null ? "—" : distance;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getRegion() {
        return region == null ? "Unknown" : region;
    }

    public String getPrice() {
        return price == null ? "N/A" : price;
    }

    public String getImageUrl() {
        return imageUrl == null ? "" : imageUrl;
    }

    // ------------------------------------------------------
    // SETTERS
    // ------------------------------------------------------
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setDistance(String distance) {
        this.distance = distance;
    }
    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

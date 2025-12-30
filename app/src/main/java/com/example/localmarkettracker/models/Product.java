package com.example.localmarkettracker.models;

public class Product {
    private String productId;
    private String name;
    private String category;
    private String unit; // kg, dozen, etc.
    private boolean isActive;
    private String imageUrl; // <-- ADD TO CONSTRUCTOR


    public Product() {}

    public Product(String productId, String name, String category, String unit, String imageUrl) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.isActive = true;
        this.imageUrl = imageUrl; // <-- SET IN CONSTRUCTOR
    }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getUnit() { return unit; }
    public String toString() { return name + " (" + unit + ")"; } // Useful for Spinner

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

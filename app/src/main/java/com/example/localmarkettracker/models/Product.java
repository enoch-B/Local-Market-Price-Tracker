package com.example.localmarkettracker.models;


public class Product {
private String id;
private String name;
private String category;
private String imageUrl;


public Product() {}
public Product(String id, String name, String category, String imageUrl){ this.id=id; this.name=name; this.category=category; this.imageUrl=imageUrl; }
// getters & setters
}
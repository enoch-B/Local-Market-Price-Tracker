package com.example.localmarkettracker.models;

import com.google.firebase.Timestamp;

public class Comment {
    // Fields must match the data keys used in FirebaseUtils (itemId, userId, text, timestamp)
    // Note: The previous error log in FirebaseUtils was looking for 'itemId', but your fields use 'priceEntryId'.
    // I will use 'itemId' here to resolve the error in FirebaseUtils. If you prefer 'priceEntryId',
    // you must change 'itemId' to 'priceEntryId' in the FirebaseUtils.java file as well.
    private String id;
    private String itemId; // Field used to match the expected 'getItemId()' call in FirebaseUtils
    private String userId;
    private String text;
    private Timestamp timestamp;

    // 1. Mandatory Public No-Argument Constructor for Firestore
    public Comment(){ }

    // 2. REQUIRED GETTER Methods (To fix the errors in FirebaseUtils.java)

    public String getId() {
        return id;
    }

    // Matches c.getItemId() in FirebaseUtils
    public String getItemId() {
        return itemId;
    }

    // Matches c.getUserId() in FirebaseUtils
    public String getUserId() {
        return userId;
    }

    // Matches c.getText() in FirebaseUtils
    public String getText() {
        return text;
    }

    // Matches c.getTimestamp() in FirebaseUtils
    public Timestamp getTimestamp() {
        return timestamp;
    }

    // 3. SETTER Methods (Good practice for data models)

    public void setId(String id) {
        this.id = id;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
package com.example.localmarkettracker.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.localmarkettracker.R;
import com.example.localmarkettracker.models.PriceSubmission; // Ensure this model has a 'category' field now
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddPriceActivity extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteCategory, autoCompleteProduct; // Added category
    private EditText etPrice;
    private Button btnSubmit;
    private FirebaseFirestore db;
    private String currentShopName = "Unknown Shop";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_price);

        db = FirebaseFirestore.getInstance();

        // Bind Views
        autoCompleteCategory = findViewById(R.id.autoCompleteCategory);
        autoCompleteProduct = findViewById(R.id.autoCompleteProduct);
        etPrice = findViewById(R.id.etPrice);
        btnSubmit = findViewById(R.id.btnSubmit);

        // 1. Setup Categories
        String[] categories = {
                getString(R.string.vegetables),
                getString(R.string.fruits),
                getString(R.string.meat),
                getString(R.string.dairy),
                getString(R.string.store_items),
                getString(R.string.building_materials),
                getString(R.string.grains_cereals),
                getString(R.string.spices_herbs)
        };

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categories);
        autoCompleteCategory.setAdapter(categoryAdapter);

        // 2. Setup Sample Products (Ideally fetch these based on selected category later)
        String[] products = {"Tomatoes", "Onions", "Potatoes", "Carrots", "Spinach", "Teff", "Coffee", "Cement", "Milk"};
        ArrayAdapter<String> productAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, products);
        autoCompleteProduct.setAdapter(productAdapter);

        fetchUserProfile();

        btnSubmit.setOnClickListener(v -> submitPrice());
    }

    private void fetchUserProfile() {
        String uid = FirebaseAuth.getInstance().getUid();
        if(uid != null) {
            db.collection("users").document(uid).get().addOnSuccessListener(doc -> {
                if(doc.exists() && doc.getString("shopName") != null) {
                    currentShopName = doc.getString("shopName");
                }
            });
        }
    }

    private void submitPrice() {
        String category = autoCompleteCategory.getText().toString();
        String product = autoCompleteProduct.getText().toString();
        String priceStr = etPrice.getText().toString();

        if(category.isEmpty() || product.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        String uid = FirebaseAuth.getInstance().getUid();

        // Create submission data map (or update your PriceSubmission model to include category)
        Map<String, Object> submission = new HashMap<>();
        submission.put("vendorId", uid);
        submission.put("category", category); // NEW FIELD
        submission.put("itemName", product);
        submission.put("price", price);
        submission.put("status", "Pending");
        submission.put("timestamp", new Timestamp(new Date()));
        submission.put("shopName", currentShopName);

        db.collection("price_submissions").add(submission)
                .addOnSuccessListener(doc -> {
                    Toast.makeText(this, "Price submitted for approval!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}

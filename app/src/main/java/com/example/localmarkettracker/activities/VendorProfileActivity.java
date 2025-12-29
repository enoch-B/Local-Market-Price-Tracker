package com.example.localmarkettracker.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.localmarkettracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class VendorProfileActivity extends AppCompatActivity {

    private EditText etShopName, etAddress, etPhone;
    private Button btnSave;
    private FirebaseFirestore db;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_profile);

        db = FirebaseFirestore.getInstance();

        // Ensure user is logged in
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            uid = FirebaseAuth.getInstance().getUid();
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Bind Views using IDs from activity_vendor_profile.xml
        etShopName = findViewById(R.id.etShopName);
        etAddress = findViewById(R.id.etAddress);
        etPhone = findViewById(R.id.etPhone);
        btnSave = findViewById(R.id.btnSave);

        // Load existing data
        loadCurrentData();

        // Save button listener
        btnSave.setOnClickListener(v -> saveProfile());
    }

    private void loadCurrentData() {
        if (uid == null) return;

        db.collection("users").document(uid).get().addOnSuccessListener(doc -> {
            if (doc.exists()) {
                // Populate fields if data exists
                if (doc.contains("shopName")) {
                    etShopName.setText(doc.getString("shopName"));
                }
                if (doc.contains("shopAddress")) {
                    etAddress.setText(doc.getString("shopAddress"));
                }
                if (doc.contains("phoneNumber")) {
                    etPhone.setText(doc.getString("phoneNumber"));
                }
            }
        }).addOnFailureListener(e ->
                Toast.makeText(VendorProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show()
        );
    }

    private void saveProfile() {
        String shopName = etShopName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (shopName.isEmpty()) {
            etShopName.setError("Shop name is required");
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("shopName", shopName);
        updates.put("shopAddress", address);
        updates.put("phoneNumber", phone);

        db.collection("users").document(uid).update(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Go back to dashboard
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error updating profile: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}

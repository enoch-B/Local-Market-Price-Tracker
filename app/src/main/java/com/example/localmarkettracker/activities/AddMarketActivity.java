package com.example.localmarkettracker.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.localmarkettracker.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class AddMarketActivity extends AppCompatActivity {

    private TextInputEditText etMarketName, etMarketLocation, etLatitude, etLongitude;
    private MaterialButton btnAddMarket;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_market);

        db = FirebaseFirestore.getInstance();
        etMarketName = findViewById(R.id.etMarketName);
        etMarketLocation = findViewById(R.id.etMarketLocation);
        etLatitude = findViewById(R.id.etLatitude);
        etLongitude = findViewById(R.id.etLongitude);
        btnAddMarket = findViewById(R.id.btnAddMarket);

        btnAddMarket.setOnClickListener(v -> addMarket());
    }

    private void addMarket() {
        String name = etMarketName.getText().toString().trim();
        String location = etMarketLocation.getText().toString().trim();
        String latStr = etLatitude.getText().toString().trim();
        String lngStr = etLongitude.getText().toString().trim();

        if (name.isEmpty() || location.isEmpty() || latStr.isEmpty() || lngStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double latitude = Double.parseDouble(latStr);
            double longitude = Double.parseDouble(lngStr);

            Map<String, Object> marketData = new HashMap<>();
            marketData.put("name", name);
            marketData.put("location", location);
            marketData.put("latitude", latitude);
            marketData.put("longitude", longitude);
            marketData.put("createdAt", com.google.firebase.Timestamp.now());

            db.collection("markets").add(marketData)
                    .addOnSuccessListener(docRef -> {
                        Toast.makeText(this, "Market added successfully!", Toast.LENGTH_SHORT).show();
                        finish(); // Go back to the list
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid latitude or longitude", Toast.LENGTH_SHORT).show();
        }
    }
}

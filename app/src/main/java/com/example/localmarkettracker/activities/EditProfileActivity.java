package com.example.localmarkettracker.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.localmarkettracker.R;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editName, editEmail, editLocation;
    private Button saveButton;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ensure you have created this layout file: activity_edit_profile.xml
        setContentView(R.layout.activity_edit_profile);

        // 1. Bind Views (IDs are placeholders, adjust to your XML)
        editName = findViewById(R.id.edit_profile_name);
        editEmail = findViewById(R.id.edit_profile_email);
        editLocation = findViewById(R.id.edit_profile_location);
        saveButton = findViewById(R.id.btn_save_profile);
        backButton = findViewById(R.id.backButton); // Assuming a common back button ID

        // 2. Load current profile data (Placeholder)
        loadCurrentProfileData();

        // 3. Setup Listeners
        setupListeners();
    }

    private void loadCurrentProfileData() {
        // Placeholder: In a real application, you would fetch data from SharedPreferences or a database.
        editName.setText("Luna");
        editEmail.setText("luna.tracker@example.com");
        editLocation.setText("Addis Ababa");
    }

    private void setupListeners() {
        // Handle Save Button click
        saveButton.setOnClickListener(v -> {
            saveProfileChanges();
        });

        // Handle Back Button click
        backButton.setOnClickListener(v -> {
            finish(); // Close this activity and return to ProfileActivity
        });
    }

    private void saveProfileChanges() {
        String newName = editName.getText().toString().trim();
        String newEmail = editEmail.getText().toString().trim();
        String newLocation = editLocation.getText().toString().trim();

        if (newName.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(this, "Name and Email cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- Actual Save Logic Here ---
        // 1. Validate data (e.g., email format)
        // 2. Update the data in the database/SharedPreferences
        // 3. Close the activity and pass back a result (optional)

        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
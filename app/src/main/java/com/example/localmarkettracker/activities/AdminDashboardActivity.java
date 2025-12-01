package com.example.localmarkettracker.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

// Correct R import
import com.example.localmarkettracker.R;


public class AdminDashboardActivity extends AppCompatActivity {

    private TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize views
//        tvWelcome = findViewById(R.id.tvWelcome);

        // Set a welcome message
        tvWelcome.setText("Welcome to Admin Dashboard");
    }
}


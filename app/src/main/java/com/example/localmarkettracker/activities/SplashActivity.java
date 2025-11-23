package com.example.localmarkettracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.localmarkettracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * SplashActivity - Clean, safe, crash-free version
 */
public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 1500; // 1.5 seconds
    private ImageView imgLogo;
    private TextView tvAppName;
    private ProgressBar progressSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Hide action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize views
        imgLogo = findViewById(R.id.imgLogo);
        tvAppName = findViewById(R.id.tvAppName);
        progressSplash = findViewById(R.id.progressSplash);
        progressSplash.setVisibility(View.VISIBLE);

        // Delay then continue to next screen
        new Handler(Looper.getMainLooper()).postDelayed(this::openNextScreen, SPLASH_DELAY);
    }

    private void openNextScreen() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent;

        // If user is logged in → Go to MainActivity
        if (user != null) {
            intent = new Intent(this, MainActivity.class);
        }
        // If user is NOT logged in → Go to LoginActivity
        else {
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }
}

package com.example.localmarkettracker.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.localmarkettracker.R;
import com.example.localmarkettracker.models.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.localmarkettracker.customer.CustomerActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView txtSignup, txtForgot;
    private TextView txtWelcomeMessage;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private static final String PREFS_NAME = "UserPrefs";

    private ImageView togglePassword;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Bind views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtSignup = findViewById(R.id.txtSignup);
        txtForgot = findViewById(R.id.txtForgot);
        progressBar = findViewById(R.id.progressLogin);
        txtWelcomeMessage = findViewById(R.id.txtWelcomeMessage);


        updateWelcomeMessage();

        // Toggle password visibility
//        togglePassword.setOnClickListener(v -> {
//            if (isPasswordVisible) {
//                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                isPasswordVisible = false;
//            } else {
//                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//                isPasswordVisible = true;
//            }
//            etPassword.setSelection(etPassword.getText().length());
//        });

        // Click listeners
        btnLogin.setOnClickListener(v -> loginUser());
        txtSignup.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignupActivity.class)));
        txtForgot.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));
    }

    // Display last saved username in welcome message
    private void updateWelcomeMessage() {
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedName = sharedPref.getString("USERNAME", "");
        if (!savedName.isEmpty()) {
            String message = getString(R.string.welcome_user, savedName);
            txtWelcomeMessage.setText(message);
        }
    }

    // Login user with email & password
    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    btnLogin.setEnabled(true);

                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            fetchUserProfile(firebaseUser.getUid());
                        }
                    } else {
                        Toast.makeText(LoginActivity.this,
                                "Login Failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Fetch user profile from Firestore
    private void fetchUserProfile(String uid) {
        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        UserProfile profile = document.toObject(UserProfile.class);
                        if (profile != null) {
                            saveUserToLocal(profile);
                            redirectBasedOnRole(profile);
                        } else {
                            Toast.makeText(LoginActivity.this, "User profile is empty!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "User profile not found!", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(LoginActivity.this, "Error loading profile: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }

    // Save user profile locally using SharedPreferences
    private void saveUserToLocal(UserProfile profile) {
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("UID", profile.getUid());
        editor.putString("EMAIL", profile.getEmail());
        editor.putString("USERNAME", profile.getDisplayName());
        editor.putString("ROLE", profile.getRole());
        editor.putString("AVATAR", profile.getAvatarUrl());

        editor.apply();
    }

    // Redirect users based on role
    private void redirectBasedOnRole(UserProfile profile) {
        Toast.makeText(this, "Welcome " + profile.getDisplayName(), Toast.LENGTH_SHORT).show();

        Intent intent = null;

        switch (profile.getRole().toLowerCase()) {
            case "admin":
                intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                break;
            case "vendor":
                intent = new Intent(LoginActivity.this, VendorDashboardActivity.class);
                break;
            case "customer":
                intent = new Intent(LoginActivity.this, CustomerActivity.class );
                break;
        }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
    }
}

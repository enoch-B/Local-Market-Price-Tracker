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
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView txtSignup, txtForgot;
    private TextView txtWelcomeMessage;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private static final String PREFS_NAME = "UserPrefs";

    // ✅ Added toggle eye reference
    private ImageView togglePassword;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtSignup = findViewById(R.id.txtSignup);
        txtForgot = findViewById(R.id.txtForgot);
        progressBar = findViewById(R.id.progressLogin);
        txtWelcomeMessage = findViewById(R.id.txtWelcomeMessage);

        // ✅ Bind toggle eye
        togglePassword = findViewById(R.id.togglePassword);

        updateWelcomeMessage();

        btnLogin.setOnClickListener(v -> loginUser());

        txtSignup.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });

        txtForgot.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });

        // ✅ Toggle eye functionality
        togglePassword.setOnClickListener(v -> {
            if (isPasswordVisible) {
                // Hide password
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                isPasswordVisible = false;
            } else {
                // Show password
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                isPasswordVisible = true;
            }
            // Move cursor to end
            etPassword.setSelection(etPassword.getText().length());
        });
    }

    private void updateWelcomeMessage() {
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedName = sharedPref.getString("USERNAME", "");
        if (!savedName.isEmpty()) {
            String message = getString(R.string.welcome_user, savedName);
            txtWelcomeMessage.setText(message);
        }
    }

    private void saveUserNameOnLogin(String email) {
        String display_name = email.split("@")[0];
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("USERNAME", display_name);
        editor.apply();
        updateWelcomeMessage();
    }

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
                        saveUserNameOnLogin(email);
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this,
                                "Login Failed: " + (task.getException() != null ? task.getException().getMessage() : "Error"),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}

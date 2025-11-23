package com.example.localmarkettracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.localmarkettracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * ForgotPasswordActivity
 * Allows users to request a password reset link sent to their email.
 */
public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmail;
    private Button btnReset;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.etEmail);
        btnReset = findViewById(R.id.btnReset);
        progressBar = findViewById(R.id.progressReset);
        auth = FirebaseAuth.getInstance();

        btnReset.setOnClickListener(v -> sendResetLink());
    }

    private void sendResetLink() {
        String email = etEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Please enter your email");
            etEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnReset.setEnabled(false);

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        btnReset.setEnabled(true);

                        if (task.isSuccessful()) {
                            // 1. Show confirmation toast
                            Toast.makeText(ForgotPasswordActivity.this,
                                    "Success! A password reset link has been sent to your email.",
                                    Toast.LENGTH_LONG).show();

                            // 2. Prompt the user to open their email app
                            promptUserToOpenEmail();

                            // We do NOT call finish() here, allowing the user to stay on the screen
                            // until they manually navigate away or the email app opens.

                        } else {
                            Toast.makeText(ForgotPasswordActivity.this,
                                    "Failed: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * Attempts to launch the user's default email application.
     */
    private void promptUserToOpenEmail() {
        // Intent to launch the main activity of an email application
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_EMAIL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        if (intent.resolveActivity(getPackageManager()) != null) {
            Toast.makeText(this, "Opening email app...", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        } else {
            // Fallback: This toast will show if no email app is installed/configured
            Toast.makeText(this, "Please open your email app manually to continue.", Toast.LENGTH_LONG).show();
        }
    }
}
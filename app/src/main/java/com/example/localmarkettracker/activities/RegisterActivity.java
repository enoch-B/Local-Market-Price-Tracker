package com.example.localmarkettracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.localmarkettracker.R;
import com.example.localmarkettracker.models.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * RegisterActivity - create a Firebase Auth user using email/password
 * Also writes a UserProfile document to the 'users' collection containing role and displayName.
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private Spinner spRole;
    private Button btnRegister;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        spRole = findViewById(R.id.spRole);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressRegister);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // set role spinner adapter (if not set via XML)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRole.setAdapter(adapter);

        btnRegister.setOnClickListener(v -> attemptRegister());
    }

    private void attemptRegister() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString();
        String role = spRole.getSelectedItem() != null ? spRole.getSelectedItem().toString().toLowerCase() : "buyer";

        if (name.isEmpty()) {
            etName.setError("Name required");
            etName.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            etEmail.setError("Email required");
            etEmail.requestFocus();
            return;
        }
        if (pass.length() < 6) {
            etPassword.setError("Password must be >= 6 characters");
            etPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnRegister.setEnabled(false);

        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    btnRegister.setEnabled(true);
                    if (task.isSuccessful()) {
                        // create user profile document
                        String uid = auth.getCurrentUser().getUid();
                        UserProfile up = new UserProfile();
                        up.setUid(uid);
                        up.setDisplayName(name);
                        up.setRole(role);
                        up.setAvatarUrl(null);
                        db.collection("users").document(uid).set(up)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override public void onComplete(@NonNull Task<Void> t) {
                                    if (t.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, com.example.localmarkettracker.activities.MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Profile save failed: " + (t.getException() != null ? t.getException().getMessage() : "unknown"), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration failed: " + (task.getException() != null ? task.getException().getMessage() : "unknown"), Toast.LENGTH_LONG).show();
                    }
                }
            });
    }
}

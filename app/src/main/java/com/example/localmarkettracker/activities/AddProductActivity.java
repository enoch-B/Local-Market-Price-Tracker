package com.example.localmarkettracker.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.localmarkettracker.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {

    private ImageView ivProductImage;
    private TextInputEditText etProductName;
    private AutoCompleteTextView autoCompleteCategory, autoCompleteUnit;
    private MaterialButton btnAddProduct;
    private ProgressBar progressBar;

    private FirebaseFirestore db;
    private Uri imageUri;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    ivProductImage.setImageURI(imageUri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        db = FirebaseFirestore.getInstance();

        ivProductImage = findViewById(R.id.ivProductImage);
        etProductName = findViewById(R.id.etProductName);
        autoCompleteCategory = findViewById(R.id.autoCompleteCategory);
        autoCompleteUnit = findViewById(R.id.autoCompleteUnit);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        progressBar = findViewById(R.id.progressBar);

        setupDropdowns();

        ivProductImage.setOnClickListener(v -> openImagePicker());
        btnAddProduct.setOnClickListener(v -> uploadImageToCloudinary());
    }

    private void setupDropdowns() {
        String[] categories = {"Vegetables", "Fruits", "Grains", "Dairy", "Meat"};
        autoCompleteCategory.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categories));

        String[] units = {"kg", "dozen", "liter", "piece"};
        autoCompleteUnit.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, units));
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void uploadImageToCloudinary() {
        String name = etProductName.getText().toString().trim();
        if (name.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please provide a name and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnAddProduct.setEnabled(false);

        // This sends the image to Cloudinary
        MediaManager.get().upload(imageUri).callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {
                // Upload starting
            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {
                // You can show upload progress here
            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                // Image uploaded successfully, get the URL
                String imageUrl = (String) resultData.get("secure_url");
                saveProductToFirestore(imageUrl);
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                // Upload failed
                progressBar.setVisibility(View.GONE);
                btnAddProduct.setEnabled(true);
                Toast.makeText(AddProductActivity.this, "Image upload failed: " + error.getDescription(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
                // Request rescheduled
            }
        }).dispatch();
    }

    private void saveProductToFirestore(String imageUrl) {
        String name = etProductName.getText().toString().trim();
        String category = autoCompleteCategory.getText().toString().trim();
        String unit = autoCompleteUnit.getText().toString().trim();

        Map<String, Object> product = new HashMap<>();
        product.put("name", name);
        product.put("category", category);
        product.put("unit", unit);
        product.put("imageUrl", imageUrl); // Use the real URL from Cloudinary
        product.put("isActive", true);
        product.put("createdAt", com.google.firebase.Timestamp.now());

        db.collection("products").add(product)
                .addOnSuccessListener(documentReference -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AddProductActivity.this, "Product added successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Go back to the product list
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnAddProduct.setEnabled(true);
                    Toast.makeText(AddProductActivity.this, "Firestore error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}

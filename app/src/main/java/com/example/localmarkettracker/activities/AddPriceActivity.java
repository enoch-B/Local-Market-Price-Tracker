package com.example.localmarkettracker.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.localmarkettracker.R;
import com.example.localmarkettracker.models.Market;
import com.example.localmarkettracker.models.PriceSubmission;
import com.example.localmarkettracker.models.Product;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AddPriceActivity extends AppCompatActivity {

    private AutoCompleteTextView autoMarket, autoCategory, autoProduct, autoQuality, autoStock;
    private TextInputEditText etPrice;
    private Button btnSubmit;
    private FirebaseFirestore db;

    // Lists to hold Firestore objects
    private List<Market> marketList = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();

    // Selected Data
    private Market selectedMarket;
    private Product selectedProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_price);

        db = FirebaseFirestore.getInstance();

        // Bind Views
        autoMarket = findViewById(R.id.autoCompleteMarket);
        autoCategory = findViewById(R.id.autoCompleteCategory);
        autoProduct = findViewById(R.id.autoCompleteProduct);
        autoQuality = findViewById(R.id.autoCompleteQuality);
        autoStock = findViewById(R.id.autoCompleteStock);
        etPrice = findViewById(R.id.etPrice);
        btnSubmit = findViewById(R.id.btnSubmit);

        setupStaticDropdowns();
        loadMarkets();

        btnSubmit.setOnClickListener(v -> submitPrice());
    }

    private void setupStaticDropdowns() {
        // Categories
        String[] categories = {
                "Vegetables", "Fruits", "Meat", "Dairy", "Grains"
        };
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categories);
        autoCategory.setAdapter(catAdapter);

        // When category changes, load products for that category
        autoCategory.setOnItemClickListener((parent, view, position, id) -> {
            String selectedCat = catAdapter.getItem(position);
            loadProductsByCategory(selectedCat);
            autoProduct.setText("", false); // Clear previous product selection
            selectedProduct = null;
        });

        // Set a default category and load products for it on start
        if (categories.length > 0) {
            String defaultCategory = categories[0];
            autoCategory.setText(defaultCategory, false); // Set default text without triggering listener
            loadProductsByCategory(defaultCategory); // **FIX**: Initial load of products
        }

        // Quality
        String[] qualities = {"A", "B", "C"};
        autoQuality.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, qualities));

        // Stock
        String[] stocks = {"in-stock", "low-stock", "out-of-stock"};
        autoStock.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, stocks));
    }

    private void loadMarkets() {
        db.collection("markets").get().addOnSuccessListener(queryDocumentSnapshots -> {
            marketList.clear();
            List<String> marketNames = new ArrayList<>();
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                Market market = doc.toObject(Market.class);
                market.setMarketId(doc.getId()); // Ensure ID is set
                marketList.add(market);
                marketNames.add(market.getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, marketNames);
            autoMarket.setAdapter(adapter);

            autoMarket.setOnItemClickListener((parent, view, position, id) -> selectedMarket = marketList.get(position));
        });
    }

    private void loadProductsByCategory(String category) {
        db.collection("products")
                .whereEqualTo("category", category)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    productList.clear();
                    List<String> productDisplayNames = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Product product = doc.toObject(Product.class);
                        product.setProductId(doc.getId());
                        productList.add(product);
                        // Use the toString() method from the Product model
                        productDisplayNames.add(product.toString());
                    }

                    // **FIX**: Always create a new adapter to refresh the dropdown contents
                    ArrayAdapter<String> productAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, productDisplayNames);
                    autoProduct.setAdapter(productAdapter);

                    // Re-set the listener here to capture selection from the new adapter
                    autoProduct.setOnItemClickListener((parent, view, position, id) -> {
                        if (position >= 0 && position < productList.size()) {
                            selectedProduct = productList.get(position);
                        }
                    });
                });
    }

    private void submitPrice() {
        String priceStr = etPrice.getText().toString();
        String quality = autoQuality.getText().toString();
        String stock = autoStock.getText().toString();
        String category = autoCategory.getText().toString();

        if (selectedMarket == null || selectedProduct == null || priceStr.isEmpty() || quality.isEmpty() || stock.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        String uid = FirebaseAuth.getInstance().getUid();

        // New Model Usage
        PriceSubmission submission = new PriceSubmission(
                uid,
                selectedMarket.getMarketId(),
                selectedMarket.getName(),
                selectedProduct.getProductId(),
                selectedProduct.getName(),
                category,
                price,
                quality,
                stock,
                selectedProduct.getImageUrl()
        );

        db.collection("prices").add(submission)
                .addOnSuccessListener(doc -> {
                    Toast.makeText(this, "Price Submitted!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}

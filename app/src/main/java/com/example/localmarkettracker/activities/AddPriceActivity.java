package com.example.localmarkettracker.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.localmarkettracker.R;

public class AddPriceActivity extends AppCompatActivity {

    private AutoCompleteTextView actProduct, actMarket;
    private Spinner spCurrency;
    private EditText etPrice;
    private Button btnSubmit;
    private ProgressBar progressAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_price);

        // Initialize views
        actProduct = findViewById(R.id.actProduct);
        actMarket = findViewById(R.id.actMarket);
        spCurrency = findViewById(R.id.spCurrency);
        etPrice = findViewById(R.id.etPrice);
        btnSubmit = findViewById(R.id.btnSubmit);
        progressAdd = findViewById(R.id.progressAdd);

        // Example data for AutoCompleteTextViews
        String[] products = {"Product 1", "Product 2", "Product 3"};
        String[] markets = {"Market 1", "Market 2", "Market 3"};

        // Set up adapters
        ArrayAdapter<String> productAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, products);
        actProduct.setAdapter(productAdapter);

        ArrayAdapter<String> marketAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, markets);
        actMarket.setAdapter(marketAdapter);

        // Set up the Spinner with values from arrays.xml (default_currencies)
        ArrayAdapter<CharSequence> currencyAdapter = ArrayAdapter.createFromResource(
                this, R.array.default_currencies, android.R.layout.simple_spinner_item);
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCurrency.setAdapter(currencyAdapter);

        // Button click listener
        btnSubmit.setOnClickListener(v -> submitPrice());
    }

    private void submitPrice() {
        String productName = actProduct.getText().toString().trim();
        String marketName = actMarket.getText().toString().trim();
        String currency = spCurrency.getSelectedItem().toString();
        String priceInput = etPrice.getText().toString().trim();

        // Validate input
        if (productName.isEmpty() || marketName.isEmpty() || priceInput.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceInput);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress
        progressAdd.setVisibility(View.VISIBLE);
        btnSubmit.setEnabled(false);

        // Simulate saving data with a delay
        new android.os.Handler().postDelayed(() -> {
            progressAdd.setVisibility(View.GONE);
            btnSubmit.setEnabled(true);

            Toast.makeText(AddPriceActivity.this,
                    "Price submitted: " + price + " " + currency,
                    Toast.LENGTH_SHORT).show();

            // Clear fields
            actProduct.setText("");
            actMarket.setText("");
            etPrice.setText("");
            spCurrency.setSelection(0);
        }, 2000); // 2 seconds delay
    }
}

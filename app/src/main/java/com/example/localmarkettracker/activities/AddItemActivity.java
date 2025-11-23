package com.example.localmarkettracker.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.localmarkettracker.R;
import com.example.localmarkettracker.models.ItemPrice;
import com.example.localmarkettracker.utils.FirebaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.Timestamp;
import java.util.List;

/**
 * AddItemActivity - add a new price entry (item + market + price)
 */
public class AddItemActivity extends AppCompatActivity {

    private AutoCompleteTextView actProduct, actMarket;
    private EditText etPrice;
    private Spinner spCurrency;
    private Button btnSubmit;
    private ProgressBar progress;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_add_price); // reuse layout

        actProduct = findViewById(R.id.actProduct);
        actMarket = findViewById(R.id.actMarket);
        etPrice = findViewById(R.id.etPrice);
        spCurrency = findViewById(R.id.spCurrency);
        btnSubmit = findViewById(R.id.btnSubmit);
        progress = findViewById(R.id.progressAdd);

        auth = FirebaseAuth.getInstance();


        // fill suggestions for products & markets
        FirebaseUtils.getProductNames((List<String> list) -> {
            // FIX: Explicitly referencing the inner android.R.layout constant
            ArrayAdapter<String> ad = new ArrayAdapter<>(AddItemActivity.this, android.R.layout.simple_dropdown_item_1line, list);
            actProduct.setAdapter(ad);
        });

        FirebaseUtils.getMarketNames((List<String> list) -> {
            // FIX: Explicitly referencing the inner android.R.layout constant
            ArrayAdapter<String> ad = new ArrayAdapter<>(AddItemActivity.this, android.R.layout.simple_dropdown_item_1line, list);
            actMarket.setAdapter(ad);
        });

        // ------------------------------------------------------------------

        btnSubmit.setOnClickListener(v -> submit());
    }

    private void submit() {
        // ... (rest of the submit method is unchanged and correct)

        String product = actProduct.getText().toString().trim();
        String market = actMarket.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String currency = spCurrency.getSelectedItem().toString();

        if (TextUtils.isEmpty(product) || TextUtils.isEmpty(market) || TextUtils.isEmpty(priceStr)) {
            Toast.makeText(this, "Complete fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException ex) {
            etPrice.setError("Invalid");
            return;
        }

        btnSubmit.setEnabled(false);
        progress.setVisibility(View.VISIBLE);

        ItemPrice ip = new ItemPrice();
        ip.setProductName(product);
        ip.setMarketName(market);
        ip.setPrice(price);
        ip.setCurrency(currency);
        ip.setReporterId(auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "guest");
        ip.setTimestamp(Timestamp.now());
        ip.setVerified(false);

        FirebaseUtils.addPrice(ip, (id, err) -> {
            progress.setVisibility(View.GONE);
            btnSubmit.setEnabled(true);
            if (err != null) {
                Toast.makeText(AddItemActivity.this, "Failed: " + err.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(AddItemActivity.this, "Added", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK, new Intent());
                finish();
            }
        });
    }
}
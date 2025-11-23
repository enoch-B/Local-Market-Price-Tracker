package com.example.localmarkettracker.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.localmarkettracker.R;

public class MarketDetailActivity extends AppCompatActivity {

    private ImageView imgMarketDetailIcon;
    private TextView tvMarketDetailName, tvMarketDetailStatus,
            tvMarketDetailCategory, tvMarketDetailDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_detail);

        // ---------- CONNECT VIEWS TO XML IDs ----------
        imgMarketDetailIcon = findViewById(R.id.imgMarketDetailIcon);
        tvMarketDetailName = findViewById(R.id.tvMarketDetailName);
        tvMarketDetailStatus = findViewById(R.id.tvMarketDetailStatus);
        tvMarketDetailCategory = findViewById(R.id.tvMarketDetailCategory);
        tvMarketDetailDescription = findViewById(R.id.tvMarketDetailDescription);

        // ---------- GET DATA FROM INTENT ----------
        String name = getIntent().getStringExtra("marketName");
        String status = getIntent().getStringExtra("marketStatus");
        String category = getIntent().getStringExtra("marketCategory");
        String description = getIntent().getStringExtra("marketDescription");
        int iconRes = getIntent().getIntExtra("marketIcon", R.drawable.ic_store);

        // ---------- DISPLAY DATA ----------
        if (name != null) tvMarketDetailName.setText(name);
        if (status != null) tvMarketDetailStatus.setText("Status: " + status);
        if (category != null) tvMarketDetailCategory.setText("Category: " + category);
        if (description != null) tvMarketDetailDescription.setText(description);

        imgMarketDetailIcon.setImageResource(iconRes);
    }
}

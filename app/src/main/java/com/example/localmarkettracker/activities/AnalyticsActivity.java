package com.example.localmarkettracker.activities;

import android.os.Bundle;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.localmarkettracker.R;
import com.example.localmarkettracker.utils.FirebaseUtils;
import com.example.localmarkettracker.utils.PredictionUtils;

/**
 * AnalyticsActivity - shows aggregated insights + simple prediction stub
 * Prediction uses a small heuristic now; placeholder to plug TFLite later.
 */
public class AnalyticsActivity extends AppCompatActivity {

    private Spinner spProduct;
    private Button btnRun;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_analytics);

        spProduct = findViewById(R.id.spProduct);
        btnRun = findViewById(R.id.btnPredict);
        tvResult = findViewById(R.id.tvPrediction);

        // ✅ Populate spinner with product names from Firestore
        FirebaseUtils.getProductNames(list -> {
            ArrayAdapter<String> ad = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    list
            );
            ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spProduct.setAdapter(ad);
        });

        // ✅ Run prediction when button is clicked
        btnRun.setOnClickListener(v -> {
            String product = spProduct.getSelectedItem().toString();
            double pred = PredictionUtils.predictNextPrice(product);
            tvResult.setText(String.format("Predicted price next week: %.2f", pred));
        });
    }
}

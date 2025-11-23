package com.example.localmarkettracker.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.localmarkettracker.R;

public class SettingsActivity extends AppCompatActivity {

    private Switch swDark;
    private Spinner spCurrency;
    private Spinner spLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings); // Ensure activity_settings.xml exists

        // Bind views
        swDark = findViewById(R.id.swDark);
        spCurrency = findViewById(R.id.spCurrency);
        spLanguage = findViewById(R.id.spLanguage);

        // Setup Language Spinner
        ArrayAdapter<CharSequence> la = ArrayAdapter.createFromResource(
                this,
                R.array.languages,   // ✅ Must exist in arrays.xml
                android.R.layout.simple_spinner_item
        );
        la.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLanguage.setAdapter(la);

        // Setup Currency Spinner
        ArrayAdapter<CharSequence> ca = ArrayAdapter.createFromResource(
                this,
                R.array.default_currencies,   // ✅ Must exist in arrays.xml
                android.R.layout.simple_spinner_item
        );
        ca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCurrency.setAdapter(ca);

        // Dark Mode Switch listener (stub)
        swDark.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(this, "Dark mode enabled", Toast.LENGTH_SHORT).show();
                // TODO: Apply dark theme here
            } else {
                Toast.makeText(this, "Dark mode disabled", Toast.LENGTH_SHORT).show();
                // TODO: Apply light theme here
            }
        });
    }
}

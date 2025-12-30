package com.example.localmarkettracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localmarkettracker.R;
import com.example.localmarkettracker.adapters.DealsAdapter;
import com.example.localmarkettracker.adapters.PriceAdapter;
import com.example.localmarkettracker.models.PriceSubmission;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SearchView searchView;
    private RecyclerView dealsRecyclerView, priceRecyclerView;
    private BottomNavigationView bottomNavigationView;
    // Removed the ProgressBar because it's not in the new layout

    private FirebaseFirestore db;
    private PriceAdapter priceAdapter;
    private DealsAdapter dealsAdapter;

    private List<PriceSubmission> allPrices = new ArrayList<>();
    private List<PriceSubmission> filteredPrices = new ArrayList<>();
    private List<PriceSubmission> dealList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        initializeViews();
        setupToolbar();
        setupRecyclerViews();
        setupBottomNavigation();
        setupSearch();

        loadApprovedPrices();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.searchView);
        dealsRecyclerView = findViewById(R.id.dealsRecyclerView);
        priceRecyclerView = findViewById(R.id.priceRecyclerView);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        // The ProgressBar with id 'progressBar' was removed from the layout, so we remove the findViewById call
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    private void setupRecyclerViews() {
        // Main Price List Adapter
        priceAdapter = new PriceAdapter(this, filteredPrices, new PriceAdapter.PriceItemListener() {
            @Override
            public void onFavoriteClicked(PriceSubmission price, boolean isFavorite) {
                Toast.makeText(MainActivity.this, "Favorite toggled for " + price.getProductName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemClicked(PriceSubmission price) {
                // You can navigate to a detail screen here
            }
        });
        priceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        priceRecyclerView.setAdapter(priceAdapter);

        // Best Deals List Adapter
        dealsAdapter = new DealsAdapter(this, dealList);
        dealsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        dealsRecyclerView.setAdapter(dealsAdapter);
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true; // Already here
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            // Handle other items like Favorites or Compare
            Toast.makeText(this, item.getTitle() + " Clicked", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterAndDisplayPrices(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterAndDisplayPrices(newText);
                return true;
            }
        });
    }

    private void loadApprovedPrices() {
        // Since we don't have a main progress bar, we can just let the data load in.
        // For a better UX, you could use shimmer effects on the RecyclerViews.

        db.collection("prices")
                .whereEqualTo("isApproved", true)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    allPrices.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        PriceSubmission price = doc.toObject(PriceSubmission.class);
                        price.setPriceId(doc.getId());
                        allPrices.add(price);
                    }

                    // Populate deals list (e.g., first 5 items)
                    dealList.clear();
                    dealList.addAll(allPrices.subList(0, Math.min(5, allPrices.size())));
                    dealsAdapter.notifyDataSetChanged();

                    // Initial display of all prices
                    filterAndDisplayPrices("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load prices: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void filterAndDisplayPrices(String searchText) {
        filteredPrices.clear();
        if (searchText.isEmpty()) {
            filteredPrices.addAll(allPrices);
        } else {
            String lowerCaseQuery = searchText.toLowerCase();
            for (PriceSubmission price : allPrices) {
                boolean nameMatch = price.getProductName() != null && price.getProductName().toLowerCase().contains(lowerCaseQuery);
                boolean marketMatch = price.getMarketName() != null && price.getMarketName().toLowerCase().contains(lowerCaseQuery);
                if (nameMatch || marketMatch) {
                    filteredPrices.add(price);
                }
            }
        }
        priceAdapter.notifyDataSetChanged(); // This updates the UI
    }
}

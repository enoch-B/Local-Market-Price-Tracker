package com.example.localmarkettracker.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.localmarkettracker.R;
import com.example.localmarkettracker.adapters.RecentUpdatesAdapter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    // Views
    private Toolbar toolbar;
    private TextView tvTotalMarkets, tvTotalItems, tvTodayUpdates;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerRecentUpdates;
    private ChipGroup chipGroup;
    private FloatingActionButton fabAddPrice;

    // Adapter
    private RecentUpdatesAdapter recentUpdatesAdapter;

    // Data list
    private List<PriceUpdate> priceUpdateList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize all views
        initializeViews();

        // Setup toolbar
        setupToolbar();

        // Setup statistics with sample data
        setupStatistics();

        // Setup RecyclerView
        setupRecyclerView();

        // Setup chip group filters
        setupChipFilters();



        // Setup FAB click listener
        setupFAB();

        // TODO: Load actual data from backend/API
        loadSampleData();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        tvTotalMarkets = findViewById(R.id.tv_total_markets);
        tvTotalItems = findViewById(R.id.tv_total_items);
        tvTodayUpdates = findViewById(R.id.tv_today_updates);
//        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        recyclerRecentUpdates = findViewById(R.id.recycler_recent_updates);
        chipGroup = findViewById(R.id.chip_group);
        fabAddPrice = findViewById(R.id.fab_add_price);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAdminMenu(v);
            }
        });

    }

    private void setupStatistics() {
        // TODO: Replace with actual data from API/database
        tvTotalMarkets.setText("24");
        tvTotalItems.setText("156");
        tvTodayUpdates.setText("42");
    }

    private void setupRecyclerView() {
        recentUpdatesAdapter = new RecentUpdatesAdapter(priceUpdateList, new RecentUpdatesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PriceUpdate priceUpdate) {
                // TODO: Handle item click - open edit/delete dialog
                // showUpdateDetails(priceUpdate);
            }

            @Override
            public void onApproveClick(PriceUpdate priceUpdate) {
                // TODO: Handle approve action
                // approvePriceUpdate(priceUpdate.getId());
            }

            @Override
            public void onDeleteClick(PriceUpdate priceUpdate) {
                // TODO: Handle delete action
                // deletePriceUpdate(priceUpdate.getId());
            }
        });

        recyclerRecentUpdates.setLayoutManager(new LinearLayoutManager(this));
        recyclerRecentUpdates.setAdapter(recentUpdatesAdapter);
    }

    private void setupChipFilters() {
        chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                if (!checkedIds.isEmpty()) {
                    Chip chip = findViewById(checkedIds.get(0));
                    if (chip != null) {
                        String filter = chip.getText().toString();
                        // TODO: Apply filter to data based on selection
                        applyFilter(filter);
                    }
                }
            }
        });
    }

//    private void setupSwipeRefresh() {
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // Simulate data loading
//                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        loadDashboard();
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                }, 1500);
//            }
//        });
//    }

    private void setupFAB() {
        fabAddPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Open AddPriceActivity for adding new price entries
                Intent intent = new Intent(AdminDashboardActivity.this, AddPriceActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadDashboard() {
        // TODO: Implement actual dashboard data loading from API
        // This is a placeholder for backend integration

        // For now, just reload sample data
        loadSampleData();

        // Update statistics
        setupStatistics();

        // Show toast or notification about refresh completion
        // Toast.makeText(this, "Dashboard refreshed", Toast.LENGTH_SHORT).show();
    }

    private void loadSampleData() {
        priceUpdateList.clear();

        // Add sample data
        priceUpdateList.add(new PriceUpdate("1", "Tomatoes", "Central Market", "₹45/kg", "2 hours ago", "verified"));
        priceUpdateList.add(new PriceUpdate("2", "Onions", "Local Bazaar", "₹30/kg", "3 hours ago", "pending"));
        priceUpdateList.add(new PriceUpdate("3", "Potatoes", "Super Mart", "₹25/kg", "5 hours ago", "verified"));
        priceUpdateList.add(new PriceUpdate("4", "Carrots", "Veggie Shop", "₹60/kg", "1 day ago", "verified"));
        priceUpdateList.add(new PriceUpdate("5", "Apples", "Fruit Stand", "₹120/kg", "1 day ago", "pending"));
        priceUpdateList.add(new PriceUpdate("6", "Bananas", "Central Market", "₹40/dozen", "2 days ago", "verified"));

        if (recentUpdatesAdapter != null) {
            recentUpdatesAdapter.notifyDataSetChanged();
        }
    }

    private void applyFilter(String filter) {
        // TODO: Implement actual filtering logic based on backend data
        // This is a placeholder

        List<PriceUpdate> filteredList = new ArrayList<>();

        switch (filter.toLowerCase()) {
            case "all":
                filteredList.addAll(priceUpdateList);
                break;
            case "today":
                // Filter for today's updates
                for (PriceUpdate update : priceUpdateList) {
                    if (update.getTimeAgo().contains("hour") || update.getTimeAgo().equals("today")) {
                        filteredList.add(update);
                    }
                }
                break;
            case "verified":
                // Filter for verified updates
                for (PriceUpdate update : priceUpdateList) {
                    if (update.getStatus().equals("verified")) {
                        filteredList.add(update);
                    }
                }
                break;
            case "pending":
                // Filter for pending updates
                for (PriceUpdate update : priceUpdateList) {
                    if (update.getStatus().equals("pending")) {
                        filteredList.add(update);
                    }
                }
                break;
        }

        recentUpdatesAdapter.updateList(filteredList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_drawer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_settings) {
            // TODO: Open settings activity
            return true;
        } else if (id == R.id.action_logout) {
            // TODO: Implement logout functionality
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Model class for price updates
    public static class PriceUpdate {
        private String id;
        private String itemName;
        private String marketName;
        private String price;
        private String timeAgo;
        private String status;

        public PriceUpdate(String id, String itemName, String marketName, String price, String timeAgo, String status) {
            this.id = id;
            this.itemName = itemName;
            this.marketName = marketName;
            this.price = price;
            this.timeAgo = timeAgo;
            this.status = status;
        }

        public String getId() { return id; }
        public String getItemName() { return itemName; }
        public String getMarketName() { return marketName; }
        public String getPrice() { return price; }
        public String getTimeAgo() { return timeAgo; }
        public String getStatus() { return status; }
    }


    private void showAdminMenu(View v) {
        // Create the PopupMenu
        PopupMenu popup = new PopupMenu(this, v);

        // Inflate the menu resource we created
        popup.getMenuInflater().inflate(R.menu.admin_drawer_menu, popup.getMenu());

        // Handle item clicks
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.action_manage_users) {
                    // Navigate to Manage Users Activity
                    startActivity(new Intent(AdminDashboardActivity.this, ManageUsersActivity.class));
                    return true;
                }
                else if (id == R.id.action_profile) {
                    // Navigate to Profile Activity
                    startActivity(new Intent(AdminDashboardActivity.this, ProfileActivity.class));
                    return true;
                }


    else if (id == R.id.action_logout) {
        // Handle Logout
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        return true;
    }
                return false;
}
        });

                // Show the menu
 popup.show();
    }
}
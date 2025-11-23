package com.example.localmarkettracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localmarkettracker.R;
import com.example.localmarkettracker.adapters.CategoryAdapter;
import com.example.localmarkettracker.adapters.NearbyAdapter;
import com.example.localmarkettracker.adapters.TrendingAdapter;
import com.example.localmarkettracker.models.MarketModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MaterialToolbar topAppBar;
    private TextView welcomeName;

    private RecyclerView categoriesRecycler, nearbyRecycler, trendingRecycler;
    private BottomNavigationView bottomNav;
    private FloatingActionButton fab;
    private ImageView btnMap;

    private final List<String> categories = new ArrayList<>();
    private final List<MarketModel> nearbyList = new ArrayList<>();
    private final List<MarketModel> trendingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind views
        topAppBar = findViewById(R.id.topAppBar);
        welcomeName = findViewById(R.id.welcomeName);
        categoriesRecycler = findViewById(R.id.categoriesRecycler);
        nearbyRecycler = findViewById(R.id.nearbyRecycler);
        trendingRecycler = findViewById(R.id.trendingRecycler);
        bottomNav = findViewById(R.id.bottomNav);
        fab = findViewById(R.id.fabAdd);
        btnMap = findViewById(R.id.btnMap);

        // Setup
        setupToolbar();
        prepareData();
        setupUI();
        setupListeners();
    }

    private void setupToolbar() {
        setSupportActionBar(topAppBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        topAppBar.setNavigationOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Menu icon clicked (Implement Nav Drawer)", Toast.LENGTH_SHORT).show();
        });
    }

    private void prepareData() {
        categories.add(getString(R.string.vegetables));
        categories.add(getString(R.string.fruits));
        categories.add(getString(R.string.meat));
        categories.add(getString(R.string.dairy));
        categories.add(getString(R.string.store_items));
        categories.add(getString(R.string.building_materials));
        categories.add(getString(R.string.grains_cereals));
        categories.add(getString(R.string.spices_herbs));

        nearbyList.add(new MarketModel("Central Market", "Fresh produce and grains", "Open", "Vegetables", "0.6 km", R.drawable.ic_store, "Addis Ababa"));
        nearbyList.add(new MarketModel("Fruit Plaza", "Seasonal fruits available", "Open", "Fruits", "1.2 km", R.drawable.ic_store, "Bole"));
        nearbyList.add(new MarketModel("Veg Hub", "Organic vegetables", "Closed", "Vegetables", "2.3 km", R.drawable.ic_store, "Sarbet"));

        trendingList.add(new MarketModel("Mercato", "Biggest market", "Open", "General", "3.1 km", R.drawable.ic_store, "Downtown"));
        trendingList.add(new MarketModel("Local Bazaar", "Popular for spices", "Open", "General", "0.9 km", R.drawable.ic_store, "Akaki"));
    }

    private void setupUI() {
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoriesRecycler.setAdapter(new CategoryAdapter(this, categories, name -> {
            Intent intent = new Intent(MainActivity.this, ItemListActivity.class);
            intent.putExtra("CATEGORY_NAME", name);
            startActivity(intent);
        }));

        nearbyRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        nearbyRecycler.setAdapter(new NearbyAdapter(this, nearbyList, market -> {
            Intent intent = new Intent(MainActivity.this, MarketDetailActivity.class);
            intent.putExtra("MARKET_ID", market.getName());
            startActivity(intent);
        }));

        trendingRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        trendingRecycler.setAdapter(new TrendingAdapter(this, trendingList, market -> {
            Intent intent = new Intent(MainActivity.this, MarketDetailActivity.class);
            intent.putExtra("MARKET_ID", market.getName());
            startActivity(intent);
        }));
    }

    private void setupListeners() {
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_analytics) {
                startActivity(new Intent(this, AnalyticsActivity.class));
                return true;
            }
            return false;
        });

        fab.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddPriceActivity.class));
        });

        btnMap.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        if (searchView != null) {
            searchView.setQueryHint(getString(R.string.search_hint));

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    CategoryAdapter adapter = (CategoryAdapter) categoriesRecycler.getAdapter();
                    if (adapter != null) adapter.getFilter().filter(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    CategoryAdapter adapter = (CategoryAdapter) categoriesRecycler.getAdapter();
                    if (adapter != null) adapter.getFilter().filter(newText);
                    return true;
                }
            });

            searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    welcomeName.setVisibility(View.GONE);
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    welcomeName.setVisibility(View.VISIBLE);
                    return true;
                }
            });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_map) {
            startActivity(new Intent(MainActivity.this, MapActivity.class));
            return true;
        } else if (id == R.id.action_profile) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            return true;
        } else if (id == R.id.action_logout) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


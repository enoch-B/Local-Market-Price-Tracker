package com.example.localmarkettracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localmarkettracker.R;
import com.example.localmarkettracker.adapters.ManagementAdapter;
import com.example.localmarkettracker.models.Market;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class AdminMarketsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private Toolbar toolbar;

    private FirebaseFirestore db;
    private List<Market> marketList = new ArrayList<>();
    private ManagementAdapter adapter;
    private List<ManagementAdapter.ManagementItem> displayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list);

        db = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.toolbar_list);
        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fabAdd);

        setupToolbar();
        setupRecyclerView();

        fab.setOnClickListener(v -> startActivity(new Intent(this, AddMarketActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMarkets();
    }

    private void setupToolbar() {
        toolbar.setTitle("Manage Markets");
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        ManagementAdapter.ManagementItemListener listener = new ManagementAdapter.ManagementItemListener() {
            @Override
            public void onEdit(int position) {
                showEditMarketDialog(marketList.get(position));
            }

            @Override
            public void onDelete(int position) {
                confirmDeleteMarket(marketList.get(position));
            }
        };
        adapter = new ManagementAdapter(displayList, listener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadMarkets() {
        db.collection("markets").get().addOnSuccessListener(snapshots -> {
            marketList.clear();
            displayList.clear();
            for (DocumentSnapshot doc : snapshots) {
                Market market = doc.toObject(Market.class);
                if (market != null) {
                    market.setMarketId(doc.getId());
                    marketList.add(market);
                    displayList.add(new ManagementAdapter.ManagementItem(
                            market.getName(),
                            market.getLocation(),
                            R.drawable.ic_baseline_store_24
                    ));
                }
            }
            adapter.notifyDataSetChanged();
        });
    }

    private void showEditMarketDialog(Market market) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 32, 48, 32);

        final EditText etName = new EditText(this);
        etName.setHint("Market Name");
        etName.setText(market.getName());
        layout.addView(etName);

        final EditText etLocation = new EditText(this);
        etLocation.setHint("Location");
        etLocation.setText(market.getLocation());
        layout.addView(etLocation);

        final EditText etLatitude = new EditText(this);
        etLatitude.setHint("Latitude");
        etLatitude.setText(String.valueOf(market.getLatitude()));
        layout.addView(etLatitude);

        final EditText etLongitude = new EditText(this);
        etLongitude.setHint("Longitude");
        etLongitude.setText(String.valueOf(market.getLongitude()));
        layout.addView(etLongitude);



        new AlertDialog.Builder(this)
                .setTitle("Edit Market")
                .setView(layout)
                .setPositiveButton("Save", (dialog, which) -> {
                    String newName = etName.getText().toString().trim();
                    String newLocation = etLocation.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("name", newName);
                        updates.put("location", newLocation);
                        updates.put("latitude", etLatitude.getText().toString().trim());
                        updates.put("longitude", etLongitude.getText().toString().trim());

                        db.collection("markets").document(market.getMarketId()).update(updates)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Market updated", Toast.LENGTH_SHORT).show();
                                    loadMarkets();
                                });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void confirmDeleteMarket(Market market) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Market")
                .setMessage("Are you sure you want to delete '" + market.getName() + "'? This cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.collection("markets").document(market.getMarketId()).delete()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Market deleted", Toast.LENGTH_SHORT).show();
                                loadMarkets();
                            });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}

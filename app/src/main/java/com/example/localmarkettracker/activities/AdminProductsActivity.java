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
import com.example.localmarkettracker.models.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class AdminProductsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private Toolbar toolbar;

    private FirebaseFirestore db;
    private List<Product> productList = new ArrayList<>();
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

        fab.setOnClickListener(v -> startActivity(new Intent(this, AddProductActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }

    private void setupToolbar() {
        toolbar.setTitle("Manage Products");
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        ManagementAdapter.ManagementItemListener listener = new ManagementAdapter.ManagementItemListener() {
            @Override
            public void onEdit(int position) {
                showEditProductDialog(productList.get(position));
            }

            @Override
            public void onDelete(int position) {
                confirmDeleteProduct(productList.get(position));
            }
        };
        adapter = new ManagementAdapter(displayList, listener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadProducts() {
        db.collection("products").get().addOnSuccessListener(snapshots -> {
            productList.clear();
            displayList.clear();
            for (DocumentSnapshot doc : snapshots) {
                Product product = doc.toObject(Product.class);
                if (product != null) {
                    product.setProductId(doc.getId());
                    productList.add(product);
                    displayList.add(new ManagementAdapter.ManagementItem(
                            product.getName(),
                            "Category: " + product.getCategory() + " | Unit: " + product.getUnit(),
                            R.drawable.ic_baseline_grass_24 // Use a relevant icon
                    ));
                }
            }
            adapter.notifyDataSetChanged();
        });
    }

    private void showEditProductDialog(Product product) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 32, 48, 32);

        final EditText etName = new EditText(this);
        etName.setHint("Product Name");
        etName.setText(product.getName());
        layout.addView(etName);

        final EditText etCategory = new EditText(this);
        etCategory.setHint("Category");
        etCategory.setText(product.getCategory());
        layout.addView(etCategory);

        final EditText etUnit = new EditText(this);
        etUnit.setHint("Unit (e.g., kg)");
        etUnit.setText(product.getUnit());
        layout.addView(etUnit);

        new AlertDialog.Builder(this)
                .setTitle("Edit Product")
                .setView(layout)
                .setPositiveButton("Save", (dialog, which) -> {
                    String newName = etName.getText().toString().trim();
                    String newCat = etCategory.getText().toString().trim();
                    String newUnit = etUnit.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("name", newName);
                        updates.put("category", newCat);
                        updates.put("unit", newUnit);

                        db.collection("products").document(product.getProductId()).update(updates)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Product updated", Toast.LENGTH_SHORT).show();
                                    loadProducts();
                                });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void confirmDeleteProduct(Product product) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Product")
                .setMessage("Are you sure you want to delete '" + product.getName() + "'?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.collection("products").document(product.getProductId()).delete()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show();
                                loadProducts();
                            });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}

package com.example.localmarkettracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localmarkettracker.R;
import com.example.localmarkettracker.adapters.ItemAdapter;
import com.example.localmarkettracker.models.ItemPrice;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ItemListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvCategoryTitle;
    private ItemAdapter itemAdapter;
    private List<ItemPrice> itemPriceList;
    private FirebaseFirestore db;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.itemsRecyclerView);
        tvCategoryTitle = findViewById(R.id.categoryTitleText);

        itemPriceList = new ArrayList<>();
        itemAdapter = new ItemAdapter(itemPriceList, this::onItemClick);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemAdapter);

        categoryName = getIntent().getStringExtra("CATEGORY_NAME");
        if (categoryName != null && !categoryName.isEmpty()) {
            tvCategoryTitle.setText(categoryName);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(categoryName);
            }
            fetchItemsByCategory(categoryName);
        } else {
            tvCategoryTitle.setText(getString(R.string.items_list_default_title));
            Log.w("ItemListActivity", "No category name received in intent");
        }
    }

    private void fetchItemsByCategory(@NonNull String category) {
        db.collection("itemPrices")
                .whereEqualTo("category", category)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        itemPriceList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ItemPrice item = document.toObject(ItemPrice.class);
                            itemPriceList.add(item);
                        }
                        itemAdapter.notifyDataSetChanged();
                        if (itemPriceList.isEmpty()) {
                            Toast.makeText(this, "No items found for " + category, Toast.LENGTH_LONG).show();
                            Log.i("ItemListActivity", "No items found in Firestore for category: " + category);
                        }
                    } else {
                        Toast.makeText(this, "Error fetching data: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("ItemListActivity", "Error getting documents: ", task.getException());
                    }
                });
    }

    public void onItemClick(ItemPrice item) {
        String itemId = item.getItemId();
        if (itemId != null && !itemId.isEmpty()) {
            Toast.makeText(this, "Opening details for: " + itemId, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ItemListActivity.this, ItemDetailsActivity.class);
            intent.putExtra("ITEM_ID", itemId);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Item ID is missing", Toast.LENGTH_SHORT).show();
            Log.w("ItemListActivity", "Clicked item has no ID");
        }
    }
}

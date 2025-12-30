package com.example.localmarkettracker.activities;import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localmarkettracker.R;
import com.example.localmarkettracker.models.PriceSubmission;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class VendorDashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerSubmissions;
    private SubmissionAdapter adapter;
    private List<PriceSubmission> submissionList;
    private FirebaseFirestore db;
    private TextView tvApprovedCount, tvPendingCount, tvShopName;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_dashboard);

        db = FirebaseFirestore.getInstance();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Redirect to login if user is not authenticated
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        uid = FirebaseAuth.getInstance().getUid();

        // Initialize Views
        initializeViews();

        // Setup Toolbar with Menu
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(this::handleMenu);

        // Setup RecyclerView
        recyclerSubmissions.setLayoutManager(new LinearLayoutManager(this));
        submissionList = new ArrayList<>();
        adapter = new SubmissionAdapter(submissionList);
        recyclerSubmissions.setAdapter(adapter);

        // Setup FAB
        FloatingActionButton fab = findViewById(R.id.fabAddPrice);
        fab.setOnClickListener(v -> startActivity(new Intent(this, AddPriceActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load data every time the screen is shown to get latest updates
        loadVendorProfile();
        loadSubmissions();
    }

    private void initializeViews() {
        tvApprovedCount = findViewById(R.id.tvApprovedCount);
        tvPendingCount = findViewById(R.id.tvPendingCount);
        tvShopName = findViewById(R.id.tvShopName);
        recyclerSubmissions = findViewById(R.id.recyclerSubmissions);
    }

    private boolean handleMenu(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_profile) {
            startActivity(new Intent(this, VendorProfileActivity.class));
            return true;
        } else if (itemId == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return false;
    }

    private void loadVendorProfile() {
        db.collection("users").document(uid).get().addOnSuccessListener(doc -> {
            if (doc.exists()) {
                String shop = doc.getString("shopName");
                String welcome = "Hi, " + doc.getString("displayName");

                // Assuming you have a 'tvWelcome' TextView
                TextView tvWelcome = findViewById(R.id.tvWelcome);
                tvWelcome.setText(welcome);

                tvShopName.setText(shop != null && !shop.isEmpty() ? shop : "No shop name set");
            }
        });
    }

    private void loadSubmissions() {
        db.collection("prices") // Use the 'prices' collection as per your schema
                .whereEqualTo("vendorId", uid)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Error loading data", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    submissionList.clear();
                    int approved = 0;
                    int pending = 0;

                    for (DocumentSnapshot doc : value.getDocuments()) {
                        PriceSubmission sub = doc.toObject(PriceSubmission.class);
                        if (sub != null) {
                            submissionList.add(sub);

                            // Correctly count based on the 'isApproved' boolean
                            if (sub.getIsApproved()) {
                                approved++;
                            } else if (!"Rejected".equals(sub.getStatus())) {
                                // An item is pending if it is not approved and not rejected
                                pending++;
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();
                    tvApprovedCount.setText(String.valueOf(approved));
                    tvPendingCount.setText(String.valueOf(pending));
                });
    }

    // --- Inner Adapter Class ---
    class SubmissionAdapter extends RecyclerView.Adapter<SubmissionAdapter.ViewHolder> {
        List<PriceSubmission> list;
        public SubmissionAdapter(List<PriceSubmission> list) { this.list = list; }

        @NonNull @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Using the layout we previously designed for vendor submissions
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vendor_submission, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            PriceSubmission sub = list.get(position);
            holder.tvItem.setText(sub.getProductName());
            holder.tvPrice.setText("ETB " + sub.getPrice());

            // Get status from the model
            String status = sub.getStatus();
            holder.chipStatus.setText(status);

            // Set color based on status
            int color = Color.parseColor("#FFA726"); // Default: Orange/Pending
            if ("Approved".equals(status)) color = Color.parseColor("#66BB6A"); // Green
            if ("Rejected".equals(status)) color = Color.parseColor("#EF5350"); // Red

            holder.chipStatus.setChipBackgroundColor(ColorStateList.valueOf(color));
        }

        @Override
        public int getItemCount() { return list.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvItem, tvPrice;
            Chip chipStatus;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvItem = itemView.findViewById(R.id.tvItemName);
                tvPrice = itemView.findViewById(R.id.tvPrice);
                chipStatus = itemView.findViewById(R.id.chipStatus);
            }
        }
    }
}

package com.example.localmarkettracker.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_dashboard);

        db = FirebaseFirestore.getInstance();
        String uid = FirebaseAuth.getInstance().getUid();

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.vendor_menu); // Create this menu with "Profile" and "Logout"
        toolbar.setOnMenuItemClickListener(this::handleMenu);

        // Views
        tvApprovedCount = findViewById(R.id.tvApprovedCount);
        tvPendingCount = findViewById(R.id.tvPendingCount);
        tvShopName = findViewById(R.id.tvShopName);
        FloatingActionButton fab = findViewById(R.id.fabAddPrice);

        // RecyclerView
        recyclerSubmissions = findViewById(R.id.recyclerSubmissions);
        recyclerSubmissions.setLayoutManager(new LinearLayoutManager(this));
        submissionList = new ArrayList<>();
        adapter = new SubmissionAdapter(submissionList);
        recyclerSubmissions.setAdapter(adapter);

        // Load Data
        loadVendorProfile(uid);
        loadSubmissions(uid);

        fab.setOnClickListener(v -> startActivity(new Intent(this, AddPriceActivity.class)));
    }

    private boolean handleMenu(MenuItem item) {
        if (item.getItemId() == R.id.action_profile) {
            startActivity(new Intent(this, VendorProfileActivity.class));
            return true;
        } else if (item.getItemId() == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return false;
    }

    private void loadVendorProfile(String uid) {
        db.collection("users").document(uid).get().addOnSuccessListener(doc -> {
            if(doc.exists()) {
                String shop = doc.getString("shopName");
                tvShopName.setText(shop != null ? shop : "No Shop Name Set");
            }
        });
    }

    private void loadSubmissions(String uid) {
        db.collection("price_submissions")
                .whereEqualTo("vendorId", uid)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;

                    submissionList.clear();
                    int approved = 0;
                    int pending = 0;

                    for (DocumentSnapshot doc : value.getDocuments()) {
                        PriceSubmission sub = doc.toObject(PriceSubmission.class);
                        submissionList.add(sub);

                        if ("Approved".equals(sub.getStatus())) approved++;
                        if ("Pending".equals(sub.getStatus())) pending++;
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vendor_submission, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            PriceSubmission sub = list.get(position);
            holder.tvItem.setText(sub.getItemName());
            holder.tvPrice.setText("$" + sub.getPrice());
            holder.chipStatus.setText(sub.getStatus());

            int color = Color.parseColor("#FFA726"); // Orange/Pending
            if ("Approved".equals(sub.getStatus())) color = Color.parseColor("#66BB6A"); // Green
            if ("Rejected".equals(sub.getStatus())) color = Color.parseColor("#EF5350"); // Red

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

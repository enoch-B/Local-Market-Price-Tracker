package com.example.localmarkettracker.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.localmarkettracker.R;
import com.example.localmarkettracker.models.PriceSubmission;
import com.example.localmarkettracker.models.UserProfile;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView; // Corrected: Import added
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvPendingCount, tvTotalUsers;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerRecentUpdates;
    private ChipGroup chipGroup;
    private MaterialCardView cardManageMarkets, cardManageProducts, cardManageUsers;

    private FirebaseFirestore db;
    private List<PriceSubmission> allSubmissions = new ArrayList<>();
    private List<PriceSubmission> filteredList = new ArrayList<>();
    private AdminSubmissionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        db = FirebaseFirestore.getInstance();

        initializeViews();
        setupToolbar();
        setupRecyclerView();
        setupChipFilters();
        setupManagementClicks();

        loadDataFromFirestore();
        loadUserCount();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadDataFromFirestore();
            loadUserCount();
        });
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        tvPendingCount = findViewById(R.id.tv_pending_count);
        tvTotalUsers = findViewById(R.id.tv_total_users);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        recyclerRecentUpdates = findViewById(R.id.recycler_recent_updates);
        chipGroup = findViewById(R.id.chip_group);

        cardManageMarkets = findViewById(R.id.card_manage_markets);
        cardManageProducts = findViewById(R.id.card_manage_products);
        cardManageUsers = findViewById(R.id.card_manage_users);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> showAdminMenu(v));
    }

    private void setupRecyclerView() {
        recyclerRecentUpdates.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminSubmissionAdapter(filteredList);
        recyclerRecentUpdates.setAdapter(adapter);
    }

    private void setupManagementClicks() {
        cardManageMarkets.setOnClickListener(v -> startActivity(new Intent(this, AdminMarketsActivity.class)));
        cardManageProducts.setOnClickListener(v -> startActivity(new Intent(this, AdminProductsActivity.class)));
        cardManageUsers.setOnClickListener(v -> startActivity(new Intent(this, ManageUsersActivity.class)));
    }

    private void loadUserCount() {
        db.collection("users").get().addOnSuccessListener(snapshots -> {
            tvTotalUsers.setText(String.valueOf(snapshots.size()));
        });
    }
    private void setupChipFilters() {
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {if (!checkedIds.isEmpty()) {
            filterData(checkedIds.get(0));
        }
        });
    }


    private void loadDataFromFirestore() {
        swipeRefreshLayout.setRefreshing(true);
        db.collection("prices") // Corrected: Collection is likely named "prices" from your schema
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    allSubmissions.clear();
                    int pending = 0;
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        PriceSubmission submission = doc.toObject(PriceSubmission.class);
                        if (submission != null) {
                            submission.setPriceId(doc.getId()); // Use the correct setter from the model
                            allSubmissions.add(submission);
                            if (!submission.getIsApproved() && !"Rejected".equals(submission.getStatus())) { // Corrected: Check boolean
                                pending++;
                            }
                        }
                    }
                    tvPendingCount.setText(String.valueOf(pending));
                    filterData(chipGroup.getCheckedChipId());
                    swipeRefreshLayout.setRefreshing(false);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                });
    }

    private void filterData(int checkedId) {
        filteredList.clear();
        if (checkedId == -1 || checkedId == R.id.chip_all) { // Handle no chip being checked
            filteredList.addAll(allSubmissions);
        } else if (checkedId == R.id.chip_pending) {
            for (PriceSubmission p : allSubmissions) {
                if (!p.getIsApproved() && !"Rejected".equals(p.getStatus())) filteredList.add(p);
            }
        } else if (checkedId == R.id.chip_approved) {
            for (PriceSubmission p : allSubmissions) {
                if (p.getIsApproved()) filteredList.add(p);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void updateSubmissionStatus(String priceId, boolean isApproved) {
        // Corrected: Update the boolean field in Firestore
        db.collection("prices").document(priceId)
                .update("isApproved", isApproved, "status", isApproved ? "Approved" : "Rejected")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Submission " + (isApproved ? "Approved" : "Rejected"), Toast.LENGTH_SHORT).show();
                    loadDataFromFirestore(); // Refresh data
                });
    }

    private void showAdminMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.admin_drawer_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_manage_users) {
                startActivity(new Intent(this, ManageUsersActivity.class));
                return true;
            } else if (itemId == R.id.action_logout) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            }
            return false;
        });
        popup.show();
    }

    // Adapter Class
    class AdminSubmissionAdapter extends RecyclerView.Adapter<AdminSubmissionAdapter.ViewHolder> {
        List<PriceSubmission> list;

        public AdminSubmissionAdapter(List<PriceSubmission> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Corrected: Use the new admin-specific layout
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_submission, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            PriceSubmission sub = list.get(position);
            UserProfile user = new UserProfile();

            holder.tvItemName.setText(sub.getProductName());
            holder.tvShopName.setText("By " + (user.getShopName() != null ? user.getShopName() : "Unknown") + " in " + (sub.getMarketName() != null ? sub.getMarketName() : "Unknown"));
            holder.tvPrice.setText("ETB " + sub.getPrice());

            // Corrected: Logic to show/hide buttons vs. status chip
            if (!sub.getIsApproved() && !"Rejected".equals(sub.getStatus())) { // This is a pending item
                holder.adminActionsContainer.setVisibility(View.VISIBLE);
                holder.chipStatus.setVisibility(View.GONE);

                holder.btnApprove.setOnClickListener(v -> updateSubmissionStatus(sub.getPriceId(), true));
                holder.btnReject.setOnClickListener(v -> updateSubmissionStatus(sub.getPriceId(), false));
            } else { // This is an approved or rejected item
                holder.adminActionsContainer.setVisibility(View.GONE);
                holder.chipStatus.setVisibility(View.VISIBLE);
                holder.chipStatus.setText(sub.getStatus());

                int color = sub.getIsApproved() ? Color.parseColor("#66BB6A") : Color.parseColor("#EF5350");
                holder.chipStatus.setChipBackgroundColor(ColorStateList.valueOf(color));
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvItemName, tvPrice, tvShopName;
            Chip chipStatus;
            MaterialButton btnApprove, btnReject;
            LinearLayout adminActionsContainer;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvItemName = itemView.findViewById(R.id.tvItemName);
                tvPrice = itemView.findViewById(R.id.tvPrice);
                tvShopName = itemView.findViewById(R.id.tvShopName);
                chipStatus = itemView.findViewById(R.id.chipStatus);
                btnApprove = itemView.findViewById(R.id.btnApprove);
                btnReject = itemView.findViewById(R.id.btnReject);
                adminActionsContainer = itemView.findViewById(R.id.admin_actions_container);
            }
        }
    }
}

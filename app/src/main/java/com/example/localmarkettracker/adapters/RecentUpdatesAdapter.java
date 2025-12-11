package com.example.localmarkettracker.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localmarkettracker.R;
import com.example.localmarkettracker.activities.AdminDashboardActivity;

import java.util.List;

public class RecentUpdatesAdapter extends RecyclerView.Adapter<RecentUpdatesAdapter.ViewHolder> {

    private List<AdminDashboardActivity.PriceUpdate> priceUpdateList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(AdminDashboardActivity.PriceUpdate priceUpdate);
        void onApproveClick(AdminDashboardActivity.PriceUpdate priceUpdate);
        void onDeleteClick(AdminDashboardActivity.PriceUpdate priceUpdate);
    }

    public RecentUpdatesAdapter(List<AdminDashboardActivity.PriceUpdate> priceUpdateList, OnItemClickListener listener) {
        this.priceUpdateList = priceUpdateList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recent_update, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdminDashboardActivity.PriceUpdate update = priceUpdateList.get(position);

        holder.tvItemName.setText(update.getItemName());
        holder.tvMarketName.setText(update.getMarketName());
        holder.tvPrice.setText(update.getPrice());
        holder.tvTimeAgo.setText(update.getTimeAgo());
        holder.tvStatus.setText(update.getStatus().toUpperCase());

        // Set status color
        if (update.getStatus().equals("verified")) {
            holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.teal_700));
        } else {
            holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_orange_dark));
        }

        // Set click listeners
        holder.cardItem.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(update);
            }
        });

        holder.btnApprove.setOnClickListener(v -> {
            if (listener != null) {
                listener.onApproveClick(update);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(update);
            }
        });

        // Show/hide approve button based on status
        if (update.getStatus().equals("verified")) {
            holder.btnApprove.setVisibility(View.GONE);
        } else {
            holder.btnApprove.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return priceUpdateList.size();
    }

    public void updateList(List<AdminDashboardActivity.PriceUpdate> newList) {
        priceUpdateList.clear();
        priceUpdateList.addAll(newList);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardItem;
        TextView tvItemName;
        TextView tvMarketName;
        TextView tvPrice;
        TextView tvTimeAgo;
        TextView tvStatus;
        ImageButton btnApprove;
        ImageButton btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            // TODO: Initialize all views from item_recent_update.xml
            // These IDs should match your item layout XML
            cardItem = itemView.findViewById(R.id.card_item);
            tvItemName = itemView.findViewById(R.id.tv_item_name);
            tvMarketName = itemView.findViewById(R.id.tv_market_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvTimeAgo = itemView.findViewById(R.id.tv_time_ago);
            tvStatus = itemView.findViewById(R.id.tv_status);
            btnApprove = itemView.findViewById(R.id.btn_approve);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
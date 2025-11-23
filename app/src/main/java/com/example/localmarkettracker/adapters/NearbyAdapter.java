package com.example.localmarkettracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull; // Added for correct annotations
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localmarkettracker.R;
import com.example.localmarkettracker.models.MarketModel;

import java.util.List;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.NearbyViewHolder> {

    private final Context context;
    private final List<MarketModel> nearbyList;
    // CRITICAL ADDITION: Field to hold the click listener
    private final OnItemClickListener clickListener;

    // ✅ CORRECTED CONSTRUCTOR: Now accepts the click listener
    public NearbyAdapter(Context context, List<MarketModel> nearbyList, OnItemClickListener listener) {
        this.context = context;
        this.nearbyList = nearbyList;
        this.clickListener = listener; // Assign the listener
    }

    @NonNull
    @Override
    public NearbyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Using `parent.getContext()` for LayoutInflater is slightly safer
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_market, parent, false);
        return new NearbyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NearbyViewHolder holder, int position) {
        final MarketModel market = nearbyList.get(position); // Made final for use in lambda

        // 1. Bind Data
        holder.marketName.setText(market.getName());
        holder.marketDescription.setText(market.getDescription());
        holder.marketStatus.setText(market.getStatus());
        
        // Use the string resource defined in the previous step
        holder.marketCategory.setText(context.getString(R.string.category_label, market.getCategory())); 
        
        holder.marketDistance.setText(market.getDistance());
        holder.marketIcon.setImageResource(market.getIconResId());

        // Status color logic (Assuming R.color.success and R.color.error are defined)
        int colorResId = market.getStatus().equalsIgnoreCase("Open") ?
                R.color.success : R.color.error;
        holder.marketStatus.setTextColor(ContextCompat.getColor(context, colorResId));

        // 2. Add Click Handler (Resolves the click issue in MainActivity)
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                // When item is clicked, call the method passed from MainActivity
                clickListener.onItemClick(market);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nearbyList.size();
    }
    
    // ✅ REQUIRED Interface for the click handler
    public interface OnItemClickListener {
        void onItemClick(MarketModel market);
    }


    static class NearbyViewHolder extends RecyclerView.ViewHolder {
        TextView marketName, marketDescription, marketStatus, marketCategory, marketDistance;
        ImageView marketIcon;

        NearbyViewHolder(View itemView) {
            super(itemView);
            // These IDs should be resolved from your item_market.xml layout
            marketName = itemView.findViewById(R.id.marketName);
            marketDescription = itemView.findViewById(R.id.marketDescription);
            marketStatus = itemView.findViewById(R.id.marketStatus);
            marketCategory = itemView.findViewById(R.id.marketCategory);
            marketDistance = itemView.findViewById(R.id.marketDistance);
            marketIcon = itemView.findViewById(R.id.marketIcon);
        }
    }
}
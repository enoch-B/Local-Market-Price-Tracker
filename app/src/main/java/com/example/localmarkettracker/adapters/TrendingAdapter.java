package com.example.localmarkettracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localmarkettracker.R;
import com.example.localmarkettracker.models.MarketModel;

import java.util.List;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.TrendingViewHolder> {

    private final Context context;
    private final List<MarketModel> trendingList;
    private final OnItemClickListener clickListener;

    public TrendingAdapter(Context context, List<MarketModel> trendingList, OnItemClickListener listener) {
        this.context = context;
        this.trendingList = trendingList;
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public TrendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the correct layout file
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_market, parent, false);
        return new TrendingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingViewHolder holder, int position) {
        MarketModel market = trendingList.get(position);

        // --- Data binding (Null checks are removed as views are guaranteed to be initialized) ---
        holder.marketName.setText(market.getName());
        holder.marketDescription.setText(market.getDescription());
        holder.marketStatus.setText(market.getStatus());
        holder.marketPrice.setText(market.getPrice());
        holder.marketRegion.setText(market.getRegion());
        // Note: Assumes R.string.category_label exists in your string resources
        holder.marketCategory.setText(context.getString(R.string.category_label, market.getCategory()));
        holder.marketDistance.setText(market.getDistance());
        holder.marketIcon.setImageResource(market.getIconResId());

        // --- Status color logic ---
        int colorResId = market.getStatus().equalsIgnoreCase("Open") ?
                R.color.success : R.color.error; // Assumes R.color.success and R.color.error exist
        holder.marketStatus.setTextColor(ContextCompat.getColor(context, colorResId));

        // --- Click Handler ---
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemClick(market);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trendingList.size();
    }

    // --- Interface for Click Events ---
    public interface OnItemClickListener {
        void onItemClick(MarketModel market);
    }

    // --- ViewHolder Class ---
    static class TrendingViewHolder extends RecyclerView.ViewHolder {
        // All views declared as non-final fields
        final TextView marketName, marketDescription, marketStatus, marketCategory, marketDistance, marketPrice, marketRegion;
        final ImageView marketIcon;

        TrendingViewHolder(View itemView) {
            super(itemView);
            // --- Initialize all views safely using findViewById ---
            marketName = itemView.findViewById(R.id.marketName);
            marketDescription = itemView.findViewById(R.id.marketDescription);
            marketStatus = itemView.findViewById(R.id.marketStatus);
            marketCategory = itemView.findViewById(R.id.marketCategory);
            marketDistance = itemView.findViewById(R.id.marketDistance);
            marketIcon = itemView.findViewById(R.id.marketIcon);
            marketPrice = itemView.findViewById(R.id.tvPriceValue);
            marketRegion = itemView.findViewById(R.id.tvMarketRegion);

            // Note: Making them 'final' is a good practice as they won't change after initialization.
        }
    }
}

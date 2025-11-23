package com.example.localmarkettracker.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.localmarkettracker.R;
import com.example.localmarkettracker.activities.ItemDetailsActivity;
import com.example.localmarkettracker.models.ItemPrice;
import java.util.List;
import java.util.Locale;

// Adapter for displaying latest prices
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<ItemPrice> latestPrices;
    private OnItemClickListener clickListener;

    public ItemAdapter(List<ItemPrice> latestPrices, OnItemClickListener listener) {
        this.latestPrices = latestPrices;
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_market, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemPrice priceEntry = latestPrices.get(position);

        // Bind the data correctly with IDs from your XML
        holder.tvMarketName.setText("Item ID: " + priceEntry.getItemId());
        holder.tvPriceValue.setText(String.format(Locale.getDefault(), "%.2f %s", priceEntry.getPrice(), priceEntry.getCurrency()));
        holder.tvMarketRegion.setText("Market: " + priceEntry.getMarketId());

        // Click handler
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemClick(priceEntry);
            } else {
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, ItemDetailsActivity.class);
                intent.putExtra("ITEM_ID", priceEntry.getItemId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return latestPrices.size();
    }

    // --- ViewHolder ---
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvMarketName;   // maps to R.id.marketName
        TextView tvPriceValue;   // maps to R.id.tvPriceValue
        TextView tvMarketRegion; // maps to R.id.tvMarketRegion

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMarketName = itemView.findViewById(R.id.marketName);
            tvPriceValue = itemView.findViewById(R.id.tvPriceValue);
            tvMarketRegion = itemView.findViewById(R.id.tvMarketRegion);
        }
    }

    // Listener interface
    public interface OnItemClickListener {
        void onItemClick(ItemPrice item);
    }
}

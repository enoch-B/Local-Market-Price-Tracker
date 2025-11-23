package com.example.localmarkettracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localmarkettracker.R;
import com.example.localmarkettracker.models.ItemPrice;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.PriceViewHolder> {

    private final List<ItemPrice> prices;

    public PriceAdapter(List<ItemPrice> prices) {
        this.prices = prices;
    }

    @NonNull
    @Override
    public PriceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_market, parent, false);
        return new PriceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PriceViewHolder holder, int position) {
        ItemPrice priceEntry = prices.get(position);

        // Correct binding with existing XML IDs
        holder.tvTitle.setText("Market: " + priceEntry.getMarketName());
        holder.tvPrice.setText(String.format(Locale.getDefault(),
                "%.2f %s", priceEntry.getPrice(), priceEntry.getCurrency()));

        Timestamp ts = priceEntry.getTimestamp();
        if (ts != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());
            holder.tvMeta.setText("Reported: " + sdf.format(ts.toDate()));
        } else {
            holder.tvMeta.setText("Reported: N/A");
        }
    }

    @Override
    public int getItemCount() {
        return prices != null ? prices.size() : 0;
    }

    public static class PriceViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle; // corresponds to marketName
        TextView tvPrice; // corresponds to tvPriceValue
        TextView tvMeta;  // corresponds to tvMarketRegion

        public PriceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.marketName);       // changed from tvMarketName
            tvPrice = itemView.findViewById(R.id.tvPriceValue);     // already correct
            tvMeta = itemView.findViewById(R.id.tvMarketRegion);    // already correct
        }
    }
}

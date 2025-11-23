package com.example.localmarkettracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localmarkettracker.R;
import com.example.localmarkettracker.models.MarketModel;

import java.util.List;

public class MarketGridAdapter extends RecyclerView.Adapter<MarketGridAdapter.ViewHolder> {

    List<MarketModel> list;
    Context context;

    public MarketGridAdapter(List<MarketModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_market_grid, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MarketModel model = list.get(position);

        holder.name.setText(model.getName());
        holder.price.setText(model.getPrice());
        holder.category.setText(model.getCategory());

        // ✅ Removed Glide, using local icon resource instead
        holder.image.setImageResource(model.getIconResId());

        holder.itemView.setOnClickListener(v -> {
            // Open detail page
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, price, category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.marketImage);
            name = itemView.findViewById(R.id.marketName);
            price = itemView.findViewById(R.id.marketPrice);
            category = itemView.findViewById(R.id.marketCategory);
        }
    }
}

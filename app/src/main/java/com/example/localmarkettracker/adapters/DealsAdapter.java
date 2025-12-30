package com.example.localmarkettracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.localmarkettracker.R;
import com.example.localmarkettracker.models.PriceSubmission;

import java.util.List;

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.ViewHolder> {

    private final Context context;
    private final List<PriceSubmission> dealList;

    public DealsAdapter(Context context, List<PriceSubmission> dealList) {
        this.context = context;
        this.dealList = dealList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_deal_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PriceSubmission deal = dealList.get(position);
        holder.tvProductName.setText(deal.getProductName());
        holder.tvPrice.setText("ETB " + String.format("%.2f", deal.getPrice()));

        // In a real app, you'd calculate this. For now, it's a placeholder.
        holder.tvPriceChange.setText("-5%");

        String imageUrl = deal.getProductImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background) // A default image
                    .into(holder.ivProductImage);
        }
    }

    @Override
    public int getItemCount() {
        return dealList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName, tvPrice, tvPriceChange;

        ViewHolder(View view) {
            super(view);
            ivProductImage = view.findViewById(R.id.ivProductImage);
            tvProductName = view.findViewById(R.id.tvProductName);
            tvPrice = view.findViewById(R.id.tvPrice);
           // tvPriceChange = view.findViewById(R.id.tvPriceChange);
        }
    }
}

package com.example.localmarkettracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide; // You need to add Glide dependency
import com.example.localmarkettracker.R;
import com.example.localmarkettracker.models.PriceSubmission;

import java.util.List;

public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.ViewHolder> {

    public interface PriceItemListener {
        void onFavoriteClicked(PriceSubmission price, boolean isFavorite);
        void onItemClicked(PriceSubmission price);
    }

    private final Context context;
    private final List<PriceSubmission> priceList;
    private final PriceItemListener listener;

    public PriceAdapter(Context context, List<PriceSubmission> priceList, PriceItemListener listener) {
        this.context = context;
        this.priceList = priceList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_customer_price, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PriceSubmission price = priceList.get(position);

        holder.tvProductName.setText(price.getProductName());
        holder.tvMarketName.setText("from " + price.getMarketName());
        holder.tvPrice.setText("ETB " + String.format("%.2f", price.getPrice()));

        String imageUrl = price.getProductImageUrl(); // Assuming method exists
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.ivProductImage);
        }
        // Here you would load the actual product image using its URL
        // For now, we use a placeholder.
        // Glide.with(context).load(price.getProductImageUrl()).into(holder.ivProductImage);

        // You would also check if the item is in the user's favorites list
        // and set the favorite button state accordingly.

        holder.itemView.setOnClickListener(v -> listener.onItemClicked(price));
        holder.btnFavorite.setOnClickListener(v -> {
            // Toggle favorite state (this is a simplified example)
            listener.onFavoriteClicked(price, true);
        });
    }

    @Override
    public int getItemCount() {
        return priceList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName, tvMarketName, tvPrice;
        ImageButton btnFavorite;

        ViewHolder(View view) {
            super(view);
            ivProductImage = view.findViewById(R.id.ivProductImage);
            tvProductName = view.findViewById(R.id.tvProductName);
            tvMarketName = view.findViewById(R.id.tvMarketName);
            tvPrice = view.findViewById(R.id.tvPrice);
            btnFavorite = view.findViewById(R.id.btnFavorite);
        }
    }
}

package com.example.localmarkettracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localmarkettracker.R;
import com.example.localmarkettracker.models.MarketModel;

import java.util.List;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.MarketViewHolder> {

    private Context context;
    private List<MarketModel> marketList;

    public MarketAdapter(Context context, List<MarketModel> marketList) {
        this.context = context;
        this.marketList = marketList;
    }

    @Override
    public MarketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_market, parent, false);
        return new MarketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MarketViewHolder holder, int position) {
        MarketModel market = marketList.get(position);

        holder.marketName.setText(market.getName());
        holder.marketDescription.setText(market.getDescription());
        holder.marketStatus.setText(market.getStatus());
        holder.marketCategory.setText("Category: " + market.getCategory());
        holder.marketDistance.setText(market.getDistance());
        holder.marketIcon.setImageResource(market.getIconResId());

        // Color status text
        if (market.getStatus().equalsIgnoreCase("Open")) {
            holder.marketStatus.setTextColor(ContextCompat.getColor(context, R.color.success));
        } else {
            holder.marketStatus.setTextColor(ContextCompat.getColor(context, R.color.error));
        }
    }

    @Override
    public int getItemCount() {
        return marketList.size();
    }

    public static class MarketViewHolder extends RecyclerView.ViewHolder {
        TextView marketName, marketDescription, marketStatus, marketCategory, marketDistance;
        ImageView marketIcon;

        public MarketViewHolder(View itemView) {
            super(itemView);
            marketName = itemView.findViewById(R.id.marketName);
            marketDescription = itemView.findViewById(R.id.marketDescription);
            marketStatus = itemView.findViewById(R.id.marketStatus);
            marketCategory = itemView.findViewById(R.id.marketCategory);
            marketDistance = itemView.findViewById(R.id.marketDistance);
            marketIcon = itemView.findViewById(R.id.marketIcon);
        }
    }
}

package com.example.localmarkettracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localmarkettracker.R;

import java.util.List;

public class ManagementAdapter extends RecyclerView.Adapter<ManagementAdapter.ViewHolder> {

    public interface ManagementItemListener {
        void onEdit(int position);
        void onDelete(int position);
    }

    public static class ManagementItem {
        public String primaryText;
        public String secondaryText;
        public int iconResId;

        public ManagementItem(String primary, String secondary, int icon) {
            this.primaryText = primary;
            this.secondaryText = secondary;
            this.iconResId = icon;
        }
    }

    private final List<ManagementItem> items;
    private final ManagementItemListener listener;

    public ManagementAdapter(List<ManagementItem> items, ManagementItemListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_management_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ManagementItem item = items.get(position);
        holder.tvPrimary.setText(item.primaryText);
        holder.tvSecondary.setText(item.secondaryText);
        holder.ivIcon.setImageResource(item.iconResId);

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(position));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvPrimary, tvSecondary;
        ImageButton btnEdit, btnDelete;

        ViewHolder(View view) {
            super(view);
            ivIcon = view.findViewById(R.id.ivIcon);
            tvPrimary = view.findViewById(R.id.tvPrimaryText);
            tvSecondary = view.findViewById(R.id.tvSecondaryText);
            btnEdit = view.findViewById(R.id.btnEdit);
            btnDelete = view.findViewById(R.id.btnDelete);
        }
    }
}

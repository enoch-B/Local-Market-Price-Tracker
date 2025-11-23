package com.example.localmarkettracker.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localmarkettracker.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> implements Filterable {

    private Context context;
    private List<String> categories;           // original list
    private List<String> filteredCategories;   // filtered list
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(String categoryName);
    }

    public CategoryAdapter(Context context, List<String> categories, OnCategoryClickListener listener) {
        this.context = context;
        this.categories = categories;
        this.filteredCategories = new ArrayList<>(categories); // initialize filtered list
        this.listener = listener;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        String category = filteredCategories.get(position); // use filtered list
        holder.categoryName.setText(category);

        String normalizedCategory = category.toLowerCase().trim();

        switch (normalizedCategory) {
            case "vegetables":
                holder.categoryIcon.setImageResource(R.drawable.ic_vegetables);
                holder.categoryIcon.setColorFilter(Color.GREEN);
                break;
            case "fruits":
                holder.categoryIcon.setImageResource(R.drawable.ic_fruits);
                holder.categoryIcon.setColorFilter(Color.RED);
                break;
            case "meat":
                holder.categoryIcon.setImageResource(R.drawable.ic_meat);
                holder.categoryIcon.setColorFilter(Color.parseColor("#B00020"));
                break;
            case "dairy":
                holder.categoryIcon.setImageResource(R.drawable.ic_dairy);
                holder.categoryIcon.setColorFilter(Color.WHITE);
                break;
            case "store items":
                holder.categoryIcon.setImageResource(R.drawable.ic_store_items);
                holder.categoryIcon.setColorFilter(Color.parseColor("#FFA500"));
                break;
            case "building materials":
                holder.categoryIcon.setImageResource(R.drawable.ic_building_materials);
                holder.categoryIcon.setColorFilter(Color.GRAY);
                break;
            case "grains & cereals":
                holder.categoryIcon.setImageResource(R.drawable.ic_grains_cereals);
                holder.categoryIcon.setColorFilter(Color.YELLOW);
                break;
            case "spices & herbs":
                holder.categoryIcon.setImageResource(R.drawable.ic_spices_herbs);
                holder.categoryIcon.setColorFilter(Color.parseColor("#FF5722"));
                break;
            default:
                holder.categoryIcon.setImageResource(R.drawable.ic_stor);
                holder.categoryIcon.setColorFilter(Color.LTGRAY);
                break;
        }

        holder.categoryName.setTextColor(Color.WHITE);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCategoryClick(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredCategories.size(); // return filtered list size
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase().trim();
                List<String> result = new ArrayList<>();
                if (query.isEmpty()) {
                    result.addAll(categories);
                } else {
                    for (String category : categories) {
                        if (category.toLowerCase().contains(query)) {
                            result.add(category);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = result;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredCategories.clear();
                filteredCategories.addAll((List<String>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryIcon;
        CardView card;

        CategoryViewHolder(View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryIcon = itemView.findViewById(R.id.categoryIcon);
            card = itemView.findViewById(R.id.categoryCard);
        }
    }
}

